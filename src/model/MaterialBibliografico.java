package model;
import exception.MaterialIndisponivelException;
import java.io.Serializable;

public abstract class MaterialBibliografico implements Serializable {

    private int id;
    private String titulo;
    private int AnoPublicacao;
    private boolean Disponivel;
//cadastrar novos livros
    public MaterialBibliografico(int id, String titulo, int AnoPublicacao) throws MaterialIndisponivelException{
        if(titulo == null){
            throw new MaterialIndisponivelException("Titulo não pode ser vazio");
        }
    this.id = id;
    this.titulo = titulo;
    this.AnoPublicacao = AnoPublicacao;
    this.Disponivel = true;        
    }

    public abstract String ExibirDetalhes();

    public void MarcarComoDisponivel(){
        this.Disponivel = true;
    }
    public void MarcarComoIndisponivel(){
        this.Disponivel = false;
    }

    public int getID(){
        return id;
    }
    public String getTitulo(){
        return titulo;
    }
    public int getAnoPublicacao(){
        return AnoPublicacao;
    }
    public boolean PodeSerEmprestado(){
        return this.Disponivel;
    }

}
