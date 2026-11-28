package controller;

import Singleton.sessao;
import exception.BibliotecaException;
import java.util.List;
import model.Cargo;
import model.Repositorio;
import model.Usuario;

public class UsuarioService {
    private Repositorio<Usuario> repositorio;

    public UsuarioService(Repositorio<Usuario> repositorio) {
        this.repositorio = repositorio;
    }

    public Usuario cadastrar(String nome, String login, String senha, Cargo cargo) throws BibliotecaException {
        for (Usuario u : repositorio.listarTodos()) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                throw new BibliotecaException("Login já cadastrado no sistema.");
            }
        }
        int novoId = repositorio.listarTodos().stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        Usuario novo = new Usuario(novoId, nome, login, senha, cargo.name());
        
        repositorio.adicionar(novo);
        return novo;
    }

    public void logar(String login, String senha) throws BibliotecaException {
        for (Usuario u : repositorio.listarTodos()) {
            if (u.getLogin().equalsIgnoreCase(login) && u.validarSenha(senha)) {
                sessao.getInstancia().iniciarSessao(u);
                return;
            }
        }
        throw new BibliotecaException("Login ou senha inválidos.");
    }

    public List<Usuario> listar() {
        return repositorio.listarTodos();
    }

    public void remover(int id) throws BibliotecaException {
        repositorio.remover(id);
    }
}