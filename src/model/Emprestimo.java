package model;
import java.io.Serializable;
import java.io.ObjectInputFilter.Status;
import java.time.LocalDate;

import padroes.Strategy.CalculoMultaStrategy;
public class Emprestimo {
    private int id;
    private Usuario usuario;
    private MaterialBibliografico material;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private StatusEmprestimo status;

    public Emprestimo(int id, Usuario usuario, MaterialBibliografico material) {
        this.id = id;
        this.usuario = usuario;
        this.material = material;
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevistaDevolucao = this.dataEmprestimo.plusDays(7);
        this.status = StatusEmprestimo.Ativo;
    }

    public void RegistrarDevolucao() {
        this.dataDevolucao = LocalDate.now();
        this.status = StatusEmprestimo.Devolvido;
        this.material.AlterarDisponibilidade(true);
        double DiasAtraso = double(dataPrevistaDevolucao) - (double.dataDevolucao);
        
        return CalculoMultaStrategy.CalcularMulta(DiasAtraso);
    }

    public boolean EstaAtivo() {
        return this.status == StatusEmprestimo.Ativo;
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
    public StatusEmprestimo getEstatus(){
        return status;
    }
}

