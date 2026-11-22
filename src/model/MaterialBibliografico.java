package model;
import java.io.Serializable;
import exception.MaterialIndisponivelException;

public abstract class MaterialBibliografico implements Serializable {

    private int id;
    private String titulo;
    private int AnoPublicacao;
    private boolean Disponivel;
//cadastrar novos livros
    public MaterialBibliografico(int id, String titulo, int AnoPublicacao, boolean Disponivel) throws MaterialIndisponivelException{
        if(titulo == null){
            throw new MaterialIndisponivelException();
        }
    this.id = id;
    this.titulo = titulo;
    this.AnoPublicacao = AnoPublicacao;
    this.Disponivel = true;        
    }

    public abstract void ExibirDados();

    public boolean PodeSerEmprestado(){
        return this.Disponivel;
    }

    public void AlterarDisponibilidade(boolean tf){
        this.Disponivel = tf;
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
}
