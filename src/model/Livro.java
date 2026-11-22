package model;

import exception.MaterialIndisponivelException;

public class Livro extends MaterialBibliografico{
    private String isbn;
    private String autor;
    private String editora;

    public Livro(int id, String titulo, int AnoPublicacao, String isbn,String autor, String editora) throws MaterialIndisponivelException{
        super(id, titulo, AnoPublicacao);
        this.isbn = isbn;
        this.autor = autor;
        this.editora = editora;
    }
    @Override 
    public void ExibirDetalhes(){
        System.out.println("Id: "+ super.getID() + " ,Titulo: "+super.getTitulo() + " ,Autor: "+autor + " ,Editora" + editora + " ,Isbn: "+ isbn + " ,disponivel: " + PodeSerEmprestado());
    }

    public boolean PodeSerEmprestado(){
        return super.PodeSerEmprestado();
    }
}
