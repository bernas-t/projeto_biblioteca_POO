package model;

import java.io.Serializable;

import exception.NomeInvalidoException;
import padroes.Observadores.ObservadorIF;
public abstract class Usuario implements Serializable, ObservadorIF {

    private int id;
    private String nome;
    private String login;
    private String senhaHash;
    private String cargo;

    public Usuario(int id, String nome, String login, String senhaHash, String cargo) throws NomeInvalidoException{
        if (this.nome == null){
            throw new NomeInvalidoException();
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
            throw new SenhaInvalidaException();
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

    // @Override 
    // public void atualizar(String m){
    //     System.out.println();
    // }
}
