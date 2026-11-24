package model;

import exception.MaterialIndisponivelException;

public class Jornal extends MaterialBibliografico {
    private String editora;
    private String NumeroEdicao;
    private int DataPublicacao;

    public Jornal(int id, String titulo,int AnoPublicacao, String editora,String NumeroEdicao, int DataPublicacao) throws MaterialIndisponivelException{
    super(id,titulo,AnoPublicacao);
    this.editora = editora;
    this.NumeroEdicao = NumeroEdicao;
    this.DataPublicacao = DataPublicacao;
    }
    @Override
    public void ExibirDetalhes(){
        System.out.println("Id: "+ super.getID() + " ,Titulo: "+super.getTitulo() + 
        " ,Editora: "+editora + " ,Numero de edição" + NumeroEdicao + " ,Data de publicação: "+ DataPublicacao + " ,disponivel: " + PodeSerEmprestado());
    }
    public boolean PodeSerEmprestado(){
        return super.PodeSerEmprestado();
    }
}

