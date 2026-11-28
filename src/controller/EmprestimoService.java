package controller;

import Strategy.ICalculoMultaStrategy;
import exception.BibliotecaException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import model.Emprestimo;
import model.MaterialBibliografico;
import model.Repositorio;
import model.Usuario;

public class EmprestimoService {
    private Repositorio<Emprestimo> repositorio;
    private MaterialService materialService;
    private ICalculoMultaStrategy multaStrategy;
    public int MAXIMO_EMPRESTIMOS_ATIVOS = 3;

    public EmprestimoService(Repositorio<Emprestimo> repositorio, MaterialService materialService, ICalculoMultaStrategy multaStrategy) {
        this.repositorio = repositorio;
        this.materialService = materialService;
        this.multaStrategy = multaStrategy;
    }

    public Emprestimo registrarEmprestimo(int usuarioId, int materialId, Usuario usuario) throws BibliotecaException {
        if (usuario == null) {
            throw new BibliotecaException("Usuário não encontrado.");
        }

        MaterialBibliografico material = materialService.buscarPorId(materialId);
        if (material == null) {
            throw new BibliotecaException("Material não encontrado.");
        }

        if (!material.PodeSerEmprestado()) {
            throw new BibliotecaException("Material '" + material.getTitulo() + "' indisponível.");
        }

        long ativos = listarPorUsuario(usuarioId).stream().filter(Emprestimo::estaAtivo).count();
        if (ativos >= MAXIMO_EMPRESTIMOS_ATIVOS) {
            throw new BibliotecaException("Limite de " + MAXIMO_EMPRESTIMOS_ATIVOS + " empréstimos ativos atingido.");
        }

        int novoId = repositorio.listarTodos().stream().mapToInt(Emprestimo::getID).max().orElse(0) + 1;
        Emprestimo emp = new Emprestimo(novoId, usuario, material);
        
        material.MarcarComoIndisponivel();
        materialService.atualizar(material);
        repositorio.adicionar(emp);
        return emp;
    }

    public BigDecimal registrarDevolucao(int emprestimoId) throws BibliotecaException {
        Emprestimo emp = repositorio.buscarPorId(emprestimoId);
        if (emp == null || !emp.estaAtivo()) {
            throw new BibliotecaException("Empréstimo inválido ou já encerrado.");
        }

        emp.RegistrarDevolucao();
        
        // Atualiza também no repositório de materiais para persistir a disponibilidade
        MaterialBibliografico mat = materialService.buscarPorId(emp.getMaterialId());
        if (mat != null) {
            mat.MarcarComoDisponivel();
            materialService.atualizar(mat);
        }

        repositorio.atualizar(emp);
        return calcularMulta(emp);
    }

    public BigDecimal calcularMulta(Emprestimo emprestimo) {
        double diasAtraso = emprestimo.CalcularDiasAtraso();
        return multaStrategy.calcular((long) diasAtraso);
    }

    public List<Emprestimo> listarPorUsuario(int usuarioId) {
        return repositorio.listarTodos().stream()
                .filter(e -> e.getUsuarioId() == usuarioId)
                .collect(Collectors.toList());
    }

    public List<Emprestimo> listarTodos() {
        return repositorio.listarTodos();
    }
}