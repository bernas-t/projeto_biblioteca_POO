package model;

import java.io.Serializable;

import exception.NomeInvalidoException;
import padroes.Observadores.ObservadorIF;
public abstract class Usuario implements Serializable, ObservadorIF {

    private int id;
    private String nome;
    private String login;
    private String senha;

    public Usuario(int id, String nome, String login, String senha) throws NomeInvalidoException{
        if (this.nome == null){
            throw new NomeInvalidoException();
        }
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public abstract void ExibirDados();

    public int getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public String getLogin(){
        return login;
    }

    // @Override 
    // public void atualizar(String m){
    //     System.out.println();
    // }
}
