package model;
import exception.MaterialIndisponivelException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// import padroes.Strategy.CalculoMultaStrategy;
public class Emprestimo {
    private int id;
    private Usuario usuario;
    private MaterialBibliografico material;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private int usuarioId;
    private int materialId;


    public Emprestimo(int id, Usuario usuario, MaterialBibliografico material) {
        this.id = id;
        this.usuario = usuario;
        this.material = material;
        this.usuarioId = usuario.getId();
        this.materialId = material.getID();
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevistaDevolucao = this.dataEmprestimo.plusDays(7);
        this.dataDevolucao = null;
    }

    public void RegistrarDevolucao() throws MaterialIndisponivelException {
        this.dataDevolucao = LocalDate.now();
        if (this.material != null){
            this.material.MarcarComoDisponivel();
        }else{
            throw new MaterialIndisponivelException("Material não encontrado para devolução.");
        }
    }
    public double CalcularDiasAtraso(){
        long diasAtraso = ChronoUnit.DAYS.between(this.dataPrevistaDevolucao, this.dataDevolucao);
        return diasAtraso;
    }

    public boolean estaAtivo(){
        if(this.dataDevolucao == null){
            return true;
        } 
        else{
            return false;
        }
    }

    public int getID(){
        return id;
    }
    public Usuario getUsuario(){
        return usuario;
    }
    public MaterialBibliografico getMaterial(){
        return material;
    }
    public LocalDate getDataEmprestimo(){
        return dataEmprestimo;
    }
    public LocalDate getDataPrevistaDevolucao(){
        return dataPrevistaDevolucao;
    }
    public LocalDate getDataDevolucao(){
        return dataDevolucao;
    }
    public int getUsuarioId() { 
        return usuarioId; 
    }
    public int getMaterialId() { 
        return materialId; 
    }
}


