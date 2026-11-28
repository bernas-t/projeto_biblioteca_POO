package model;

import exception.NomeInvalidoException;
import exception.SenhaInvalidaException;
import java.io.Serializable;
public abstract class Usuario implements Serializable {

    private int id;
    private String nome;
    private String login;
    private String senhaHash;
    private String cargo;

    public Usuario(int id, String nome, String login, String senhaHash, String cargo) throws NomeInvalidoException{
        if (this.nome == null){
            throw new NomeInvalidoException("Nome não pode ser vazio");
        }
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senhaHash = senhaHash;
        this.cargo = cargo;
    }

    // falta criar essa e outras exceções mas to com preguiça
    public boolean ValidarSenha(String sen) throws SenhaInvalidaException{
        if (this.senhaHash == sen){
            return true;
        }else{
            throw new SenhaInvalidaException("Senha inválida.");
        }
    }
    // public abstract void ExibirDados();

    public int getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public String getLogin(){
        return login;
    }
    public String getCargo(){
        return cargo;
    }

}
