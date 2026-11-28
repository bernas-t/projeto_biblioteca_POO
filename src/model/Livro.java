package model;

import exception.MaterialIndisponivelException;

public class Livro extends MaterialBibliografico{
    private String isbn;
    private String autor;

    public Livro(int id, String titulo, int AnoPublicacao, String isbn,String autor) throws MaterialIndisponivelException{
        super(id, titulo, AnoPublicacao);
        this.isbn = isbn;
        this.autor = autor;
    }
    @Override 
    public String ExibirDetalhes(){
        return "Id: "+ super.getID() + " ,Titulo: "+super.getTitulo() + " ,Autor: "+autor +" ,Isbn: "+ isbn + " ,disponivel: " + super.PodeSerEmprestado();
    }

    public String getIsbn(){
        return isbn;
    }
    public String getAutor(){
        return autor;
    }
}
