package model;
import exception.MaterialIndisponivelException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import padroes.Observadores.ObservadorIF;
import padroes.Observadores.PadraoIF;

public abstract class MaterialBibliografico implements Serializable, PadraoIF {

    private int id;
    private String titulo;
    private int AnoPublicacao;
    private boolean Disponivel;
    private transient List<ObservadorIF> observadores = new ArrayList<>();
//cadastrar novos livros
    public MaterialBibliografico(int id, String titulo, int AnoPublicacao) throws MaterialIndisponivelException{
        if(titulo == null){
            throw new MaterialIndisponivelException();
        }
    this.id = id;
    this.titulo = titulo;
    this.AnoPublicacao = AnoPublicacao;
    this.Disponivel = true;        
    }

    public abstract void ExibirDetalhes();

    public void MarcarComoDisponivel(){
        this.Disponivel = true;
    }
    public void MarcarComoIndisponivel(){
        this.Disponivel = false;
    }
    //antigo 

    // public boolean PodeSerEmprestado(){
    //     return this.Disponivel;
    // }

    // public void AlterarDisponibilidade(boolean tf){
    //     this.Disponivel = tf;
    //     if(this.Disponivel){
    //         NotificarObservadores();
    //     }
    // }
    
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

    // observadores padrão d+
//     @Override 
//     public void adicionarObservador(ObservadorIF ovbservador){
//         if(observadores == null){
//             observadores = new ArrayList<>();
//         }
//         if (!observadores.contains(ovbservador)){
//             observadores.add(ovbservador);
//         }
//     }
//     @Override 
//     public void RemoverObservador(ObservadorIF observador){
//         if(observador != null){
//             observadores.remove(observador);
//         }
//         // else{
//         //     throw new ObservadorException;
//         // }
//     }
//     @Override 
//     public void NotificarObservadores(){
//         if(observadores != null){
//             for(ObservadorIF obs : observadores){
//                 obs.atualizar(getTitulo() + "foi devolvido");
//             }
//             //
//         }
//     }

}
