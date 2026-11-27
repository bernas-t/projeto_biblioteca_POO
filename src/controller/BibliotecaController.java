package controller;

import exception.BibliotecaException;
import exception.DadoInvalidoException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import model.Cargo;
import model.Emprestimo;
import model.Livro;
import model.MaterialBibliografico;
import model.Periodico;
import model.Usuario;
import controller.EmprestimoService;
import controller.MaterialService;
import controller.UsuarioService;

public class BibliotecaController {

    private final UsuarioService usuarioService;
    private final MaterialService materialService;
    private final EmprestimoService emprestimoService;

    public BibliotecaController(UsuarioService usuarioService, MaterialService materialService,
            EmprestimoService emprestimoService) {
        this.usuarioService = Objects.requireNonNull(usuarioService);
        this.materialService = Objects.requireNonNull(materialService);
        this.emprestimoService = Objects.requireNonNull(emprestimoService);
    }

    public void login(String login, String senha) throws BibliotecaException {
        usuarioService.logar(login, senha);
    }

    public void logout() {
        usuarioService.deslogar();
    }

    public Usuario getUsuarioLogado() {
        return usuarioService.getUsuarioLogado();
    }

    public boolean nenhumUsuarioCadastrado() {
        return usuarioService.listar().isEmpty();
    }

    public Usuario registrarSe(String nome, String login, String senha) throws BibliotecaException {
        Cargo cargo = nenhumUsuarioCadastrado() ? Cargo.BIBLIOTECARIO : Cargo.LEITOR;
        return usuarioService.cadastrar(nome, login, senha, cargo);
    }

    public Usuario cadastrarUsuario(String nome, String login, String senha, Cargo cargo)
            throws BibliotecaException {
        exigirBibliotecario();
        return usuarioService.cadastrar(nome, login, senha, cargo);
    }

    public List<Usuario> listarUsuarios() throws BibliotecaException {
        exigirBibliotecario();
        return usuarioService.listar();
    }

    public void removerUsuario(int id) throws BibliotecaException {
        exigirBibliotecario();
        if (emprestimoService.contarAtivosDoUsuario(id) > 0) {
            throw new BibliotecaException("O usuário possui empréstimos ativos e não pode ser removido.");
        }
        usuarioService.remover(id);
    }

    public void alterarMinhaSenha(String novaSenha) throws BibliotecaException {
        Usuario logado = exigirLogin();
        usuarioService.atualizarSenha(logado.getId(), novaSenha);
    }

    public MaterialBibliografico cadastrarMaterial(String[] dados) throws BibliotecaException {
        exigirBibliotecario();
        if (dados == null || dados.length == 0 || dados[0] == null) {
            throw new DadoInvalidoException("Dados do material não informados.");
        }
        int id = materialService.proximoId();
        MaterialBibliografico material;
        switch (dados[0].trim().toUpperCase()) {
            case "LIVRO":
                exigirCampos(dados, 5);
                material = new Livro(id, dados[1], parseInteiro(dados[2], "ano"), dados[3], dados[4]);
                break;
            case "PERIODICO":
                exigirCampos(dados, 4);
                material = new Periodico(id, dados[1], parseInteiro(dados[2], "ano"),
                        parseInteiro(dados[3], "edição"));
                break;
            default:
                throw new DadoInvalidoException("Tipo de material desconhecido: '" + dados[0] + "'.");
        }
        materialService.cadastrar(material);
        return material;
    }

    public void atualizarMaterial(int id, String[] dados) throws BibliotecaException {
        exigirBibliotecario();
        MaterialBibliografico material = materialService.buscarPorId(id);
        String titulo = valorOuAtual(campo(dados, 0), material.getTitulo());
        int ano = vazio(campo(dados, 1))
                ? material.getAnoPublicacao()
                : parseInteiro(campo(dados, 1), "ano");

        if (material instanceof Livro) {
            Livro livro = (Livro) material;
            String autor = valorOuAtual(campo(dados, 2), livro.getAutor());
            String isbn = valorOuAtual(campo(dados, 3), livro.getIsbn());
            Livro validado = new Livro(id, titulo, ano, autor, isbn);
            livro.alterarTitulo(validado.getTitulo());
            livro.alterarAnoPublicacao(validado.getAnoPublicacao());
            livro.alterarAutor(validado.getAutor());
            livro.alterarIsbn(validado.getIsbn());
        } else if (material instanceof Periodico) {
            Periodico periodico = (Periodico) material;
            int edicao = vazio(campo(dados, 2))
                    ? periodico.getNumeroEdicao()
                    : parseInteiro(campo(dados, 2), "edição");
            Periodico validado = new Periodico(id, titulo, ano, edicao);
            periodico.alterarTitulo(validado.getTitulo());
            periodico.alterarAnoPublicacao(validado.getAnoPublicacao());
            periodico.alterarNumeroEdicao(validado.getNumeroEdicao());
        }
        materialService.atualizar(material);
    }

    public void removerMaterial(int id) throws BibliotecaException {
        exigirBibliotecario();
        materialService.remover(id);
    }

    public MaterialBibliografico buscarMaterial(int id) throws BibliotecaException {
        exigirLogin();
        return materialService.buscarPorId(id);
    }

    public List<MaterialBibliografico> listarMateriais() throws BibliotecaException {
        exigirLogin();
        return materialService.listarTodos();
    }

    public List<MaterialBibliografico> buscarMateriais(String trechoDoTitulo) throws BibliotecaException {
        exigirLogin();
        return materialService.buscarPorTitulo(trechoDoTitulo);
    }

    public Emprestimo emprestar(int usuarioId, int materialId) throws BibliotecaException {
        Usuario logado = exigirLogin();
        if (!logado.isBibliotecario() && logado.getId() != usuarioId) {
            throw new BibliotecaException("Você só pode registrar empréstimos em seu próprio nome.");
        }
        return emprestimoService.registrarEmprestimo(usuarioId, materialId);
    }

    public BigDecimal devolver(int emprestimoId) throws BibliotecaException {
        Usuario logado = exigirLogin();
        if (!logado.isBibliotecario() && !pertenceAo(logado, emprestimoId)) {
            throw new BibliotecaException("Empréstimo " + emprestimoId + " não encontrado entre os seus.");
        }
        return emprestimoService.registrarDevolucao(emprestimoId);
    }

    public List<Emprestimo> listarEmprestimos() throws BibliotecaException {
        Usuario logado = exigirLogin();
        return logado.isBibliotecario()
                ? emprestimoService.listarTodos()
                : emprestimoService.listarPorUsuario(logado.getId());
    }

    private boolean pertenceAo(Usuario usuario, int emprestimoId) {
        for (Emprestimo emprestimo : emprestimoService.listarPorUsuario(usuario.getId())) {
            if (emprestimo.getId() == emprestimoId) {
                return true;
            }
        }
        return false;
    }

    private Usuario exigirLogin() throws BibliotecaException {
        Usuario logado = usuarioService.getUsuarioLogado();
        if (logado == null) {
            throw new BibliotecaException("É necessário entrar no sistema.");
        }
        return logado;
    }

    private Usuario exigirBibliotecario() throws BibliotecaException {
        Usuario logado = exigirLogin();
        if (!logado.isBibliotecario()) {
            throw new BibliotecaException("Operação permitida apenas a bibliotecários.");
        }
        return logado;
    }

    private static void exigirCampos(String[] dados, int quantidade) throws DadoInvalidoException {
        if (dados.length < quantidade) {
            throw new DadoInvalidoException("Dados incompletos para cadastrar o material.");
        }
    }

    private static int parseInteiro(String valor, String campo) throws DadoInvalidoException {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new DadoInvalidoException("O campo '" + campo + "' deve ser um número inteiro.");
        }
    }

    private static String campo(String[] dados, int indice) {
        return (dados != null && indice < dados.length) ? dados[indice] : null;
    }

    private static boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }

    private static String valorOuAtual(String novo, String atual) {
        return vazio(novo) ? atual : novo;
    }
}
