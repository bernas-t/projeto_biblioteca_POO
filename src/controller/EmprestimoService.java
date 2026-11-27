package service;

import exception.BibliotecaException;
import exception.LimiteEmprestimosException;
import exception.MaterialIndisponivelException;
import exception.PersistenciaException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.Emprestimo;
import model.MaterialBibliografico;
import model.Repositorio;
import strategy.CalculoMultaStrategy;

/** PADRÃO STRATEGY (contexto): a política de multa é injetada pelo construtor. */
public class EmprestimoService {
    public static final int MAXIMO_EMPRESTIMOS_ATIVOS = 3;

    private final Repositorio<Emprestimo> repositorio;
    private final UsuarioService usuarioService;
    private final MaterialService materialService;
    private final CalculoMultaStrategy multaStrategy;

    public EmprestimoService(Repositorio<Emprestimo> repositorio, UsuarioService usuarioService,
            MaterialService materialService, CalculoMultaStrategy multaStrategy) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.usuarioService = Objects.requireNonNull(usuarioService);
        this.materialService = Objects.requireNonNull(materialService);
        this.multaStrategy = Objects.requireNonNull(multaStrategy);
    }

    public Emprestimo registrarEmprestimo(int usuarioId, int materialId) throws BibliotecaException {
        usuarioService.buscarPorId(usuarioId);
        MaterialBibliografico material = materialService.buscarPorId(materialId);

        if (!material.isDisponivel()) {
            throw new MaterialIndisponivelException("O material " + materialId + " já está emprestado.");
        }
        if (contarAtivosDoUsuario(usuarioId) >= MAXIMO_EMPRESTIMOS_ATIVOS) {
            throw new LimiteEmprestimosException("O usuário já possui "
                    + MAXIMO_EMPRESTIMOS_ATIVOS + " empréstimos ativos.");
        }

        Emprestimo emprestimo = new Emprestimo(repositorio.proximoId(), usuarioId, materialId);
        repositorio.adicionar(emprestimo);
        material.marcarComoIndisponivel();
        try {
            materialService.atualizar(material);
        } catch (BibliotecaException e) {
            material.marcarComoDisponivel();
            try {
                repositorio.remover(emprestimo.getId());
            } catch (PersistenciaException falhaAoDesfazer) {
                e.addSuppressed(falhaAoDesfazer);
            }
            throw e;
        }
        return emprestimo;
    }

    public BigDecimal registrarDevolucao(int emprestimoId) throws BibliotecaException {
        Emprestimo emprestimo = repositorio.buscarPorId(emprestimoId);
        if (emprestimo == null) {
            throw new BibliotecaException("Empréstimo " + emprestimoId + " não encontrado.");
        }
        MaterialBibliografico material = materialService.buscarPorId(emprestimo.getMaterialId());

        emprestimo.registrarDevolucao();
        BigDecimal multa = calcularMulta(emprestimo);
        material.marcarComoDisponivel();
        repositorio.atualizar(emprestimo);
        materialService.atualizar(material);
        return multa;
    }

    public BigDecimal calcularMulta(Emprestimo emprestimo) {
        return multaStrategy.calcular(emprestimo.calcularDiasAtraso());
    }

    public List<Emprestimo> listarTodos() {
        return repositorio.listarTodos();
    }

    public List<Emprestimo> listarPorUsuario(int usuarioId) {
        List<Emprestimo> doUsuario = new ArrayList<>();
        for (Emprestimo emprestimo : repositorio.listarTodos()) {
            if (emprestimo.getUsuarioId() == usuarioId) {
                doUsuario.add(emprestimo);
            }
        }
        return doUsuario;
    }

    public int contarAtivosDoUsuario(int usuarioId) {
        int ativos = 0;
        for (Emprestimo emprestimo : listarPorUsuario(usuarioId)) {
            if (emprestimo.estaAtivo()) {
                ativos++;
            }
        }
        return ativos;
    }
}
