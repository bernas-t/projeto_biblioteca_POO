package service;

import exception.BibliotecaException;
import exception.DadoInvalidoException;
import java.util.List;
import java.util.Objects;
import model.Cargo;
import model.Sessao;
import model.Usuario;
import model.Repositorio;

public class UsuarioService {
    private final Repositorio<Usuario> repositorio;

    public UsuarioService(Repositorio<Usuario> repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio);
    }

    public Usuario cadastrar(String nome, String login, String senha, Cargo cargo)
            throws BibliotecaException {
        if (login != null && buscarPorLogin(login) != null) {
            throw new DadoInvalidoException("O login '" + login.trim() + "' já está em uso.");
        }
        Usuario usuario = new Usuario(repositorio.proximoId(), nome, login, senha, cargo);
        repositorio.adicionar(usuario);
        return usuario;
    }

    public void logar(String login, String senha) throws BibliotecaException {
        Usuario usuario = (login == null) ? null : buscarPorLogin(login);
        if (usuario == null || !usuario.autenticar(senha)) {
            throw new BibliotecaException("Login ou senha inválidos.");
        }
        Sessao.getInstancia().iniciarSessao(usuario);
    }

    public Usuario getUsuarioLogado() {
        return Sessao.getInstancia().getUsuarioLogado();
    }

    public void deslogar() {
        Sessao.getInstancia().encerrarSessao();
    }

    public Usuario buscarPorId(int id) throws BibliotecaException {
        Usuario usuario = repositorio.buscarPorId(id);
        if (usuario == null) {
            throw new BibliotecaException("Usuário " + id + " não encontrado.");
        }
        return usuario;
    }

    public List<Usuario> listar() {
        return repositorio.listarTodos();
    }

    public void atualizarNome(int id, String novoNome) throws BibliotecaException {
        Usuario usuario = buscarPorId(id);
        usuario.alterarNome(novoNome);
        repositorio.atualizar(usuario);
    }

    public void atualizarSenha(int id, String novaSenha) throws BibliotecaException {
        Usuario usuario = buscarPorId(id);
        usuario.alterarSenha(novaSenha);
        repositorio.atualizar(usuario);
    }

    public void remover(int id) throws BibliotecaException {
        buscarPorId(id);
        Usuario logado = Sessao.getInstancia().getUsuarioLogado();
        if (logado != null && logado.getId() == id) {
            throw new BibliotecaException("Não é possível remover o usuário logado.");
        }
        repositorio.remover(id);
    }

    private Usuario buscarPorLogin(String login) {
        String procurado = login.trim();
        for (Usuario usuario : repositorio.listarTodos()) {
            if (usuario.getLogin().equalsIgnoreCase(procurado)) {
                return usuario;
            }
        }
        return null;
    }
}
