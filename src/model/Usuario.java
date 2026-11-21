package model;

import java.io.Serializable;

import exception.NomeInvalidoException;

public class Usuario implements Serializable {

    private int id;
    private String nome;
    private String login;
    private String senha;

    public Usuario(int id, String nome, String login, String senha) throws NomeInvalidoException{
        if (this.nome == null){
            throw new NomeInvalidoException();
        }
        
    }
}
