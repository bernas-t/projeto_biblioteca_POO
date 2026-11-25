package model;
import exception.NomeInvalidoException;

public class Administrador extends Usuario{
    public Administrador(int id,String nome, String login, String senha) throws NomeInvalidoException{
        super(id,nome,login,senha);
    }

    @Override 
    public void ExibirDados(){
        System.out.println("id: " + getId() + " nome: " + getNome() + " login: " + getLogin());

    }
    
    @Override 
    public void atualizar(String m){
        System.out.println();
    }
}
