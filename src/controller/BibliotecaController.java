package controller;

import Singleton.sessao;
import exception.BibliotecaException;
import java.math.BigDecimal;
import java.util.List;
import model.Cargo;
import model.Emprestimo;
import model.Livro;
import model.MaterialBibliografico;
import model.Periodico;
import model.Usuario;

public class BibliotecaController {

    private final UsuarioService usuarioService;
    private final MaterialService materialService;
    private final EmprestimoService emprestimoService;

public BibliotecaController(UsuarioService usuarioService, MaterialService materialService, EmprestimoService emprestimoService) {
        this.usuarioService = usuarioService;
        this.materialService = materialService;
        this.emprestimoService = emprestimoService;
    }

    public void login(String login, String senha) throws BibliotecaException {
        usuarioService.logar(login, senha);
    }

    public Usuario cadastrarUsuario(String nome, String login, String senha, Cargo cargo) throws BibliotecaException {
        return usuarioService.cadastrar(nome, login, senha, cargo);
    }

    // dados: ["LIVRO", id, titulo, ano, autor, isbn] ou ["PERIODICO", id, titulo, ano, numeroEdicao]
    public void cadastrarMaterial(String[] dados) throws BibliotecaException {
        Usuario logado = sessao.getInstancia().getUsuarioLogado();
        if (logado == null || logado.getCargo() != String.valueOf(Cargo.Bibliotecario)) {
            throw new BibliotecaException("Apenas bibliotecários logados podem cadastrar materiais.");
        }

        String tipo = dados[0].toUpperCase();
        int id = Integer.parseInt(dados[1]);
        String titulo = dados[2];
        int ano = Integer.parseInt(dados[3]);

        MaterialBibliografico material;
        if (tipo.equals("LIVRO")) {
            material = new Livro(id, titulo, ano, dados[4], dados[5]);
        } else if (tipo.equals("PERIODICO")) {
            material = new Periodico(id, titulo, ano, Integer.parseInt(dados[4]));
        } else {
            throw new BibliotecaException("Tipo de material inválido: " + tipo);
        }

        materialService.cadastrar(material);
    }

    public void emprestar(int usuarioId, int materialId) throws BibliotecaException {
        Usuario usuario = usuarioService.listar().stream()
                .filter(u -> u.getId() == usuarioId)
                .findFirst()
                .orElse(null);
        emprestimoService.registrarEmprestimo(usuarioId, materialId, usuario);
    }

    public BigDecimal devolver(int emprestimoId) throws BibliotecaException {
        return emprestimoService.registrarDevolucao(emprestimoId);
    }

    public List<MaterialBibliografico> listarMateriais() {
        return materialService.listarTodos();
    }

    public List<MaterialBibliografico> buscarMateriais(String titulo) {
        return materialService.buscarPorTitulo(titulo);
    }

    public List<Emprestimo> listarEmprestimosUsuario(int usuarioId) {
        return emprestimoService.listarPorUsuario(usuarioId);
    }
}