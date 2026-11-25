package padroes.Factory;
import model.Livro;
import exception.MaterialIndisponivelException;
import model.Jornal;
import model.Revista;
import model.MaterialBibliografico;
public class FabricaDeMateriais {
    
    // public static MaterialBibliografico CriarMaterial(String Tipo,int id, String Titulo, int AnoPublicacao, String param1,String param2, String param3) throws MaterialIndisponivelException{
        // if (Tipo == null){
        //     throw new MaterialIndisponivelException();
        // }else if (Tipo == "LIVRO"){return new Livro(id, Titulo, AnoPublicacao, param1, param2, param3);
        // // param1 = isbn, param2 = autor, param3 = editora
        // }else if(Tipo == "Revista"){ return new Revista(id, Titulo, AnoPublicacao, param1, int(param2), param3);
        // }else if (Tipo == "Jornal"){return new Jornal(id, Titulo, AnoPublicacao, param1, Integer.parseInt(param2), param3);}
        // // param1 = editora, param2 = numeroEdicao (convertido para int), param3 = dataPublicacao
        // caralhoooooooooooooooooooooooooooooo
        
        
    public static model.Livro CriarLivro(int id ,String titulo,int AnoPublicacao, String isbn, String autor,String editora)throws MaterialIndisponivelException{
        return new Livro(id, titulo, AnoPublicacao, isbn, autor, editora);
    }
    public static model.Jornal CriarJornal(int id, String titulo,int AnoPublicacao, String editora,String NumeroEdicao, int DataPublicacao)throws MaterialIndisponivelException{
        return new Jornal(id, titulo, AnoPublicacao, editora, NumeroEdicao, DataPublicacao);
    }
    public static model.Revista CriarRevista(int id, String titulo,int AnoPublicacao, String editora,String NumeroEdicao, int DataPublicacao)throws MaterialIndisponivelException{
        return new Revista(id, titulo, AnoPublicacao, editora, NumeroEdicao, DataPublicacao);
    }



}

