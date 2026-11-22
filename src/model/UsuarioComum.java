package model;

import exception.NomeInvalidoException;

public class UsuarioComum extends Usuario{
    public UsuarioComum(int id,String nome, String login, String senha) throws NomeInvalidoException{
        super(id,nome,login,senha);
    }

    @Override 
    public void ExibirDados(){
        System.out.println("id: " + getId() + " nome: " + getNome() + " login: " + getLogin());

    }
}
