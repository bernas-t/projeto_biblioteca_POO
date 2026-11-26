package model;
import exception.MaterialIndisponivelException;

public class Periodico extends MaterialBibliografico{
    private int NumeroEdicao;

    public Periodico(int NumeroEdicao,int id, String titulo, int AnoPublicacao)throws MaterialIndisponivelException{
        super(id,titulo,AnoPublicacao );
        this.NumeroEdicao = NumeroEdicao;

    }
    @Override 
    public void ExibirDetalhes(){
        System.out.println("Id: "+ super.getID() + " ,Titulo: " + super.getTitulo() + " ,Ano de publicação; " + getAnoPublicacao() + " ,Numero de edição: " + NumeroEdicao);
    }
}
