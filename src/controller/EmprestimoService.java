package controller;

import exception.BibliotecaException;
import exception.LimiteEmprestimosException;
import exception.MaterialIndisponivelException;
import exception.PersistenciaException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.Emprestimo;
import model.MaterialBibliografico;
import repository.Repositorio;
import strategy.CalculoMultaStrategy;

public class EmprestimoService {
  public static final int MAXIMO_EMPRESTIMOS_ATIVOS = 3;
   private final Repositorio<Emprestimo> repositorio;
   private final UsuarioService usuarioService;
   private final MaterialService materialService;
   private final CalculoMultaStrategy multaStrategy;

   public EmprestimoService(Repositorio<Emprestimo> repositorio, UsuarioService usuarioService, MaterialService materialService, CalculoMultaStrategy multaStrategy) {
      this.repositorio = (Repositorio)Objects.requireNonNull(repositorio);
      this.usuarioService = (UsuarioService)Objects.requireNonNull(usuarioService);
      this.materialService = (MaterialService)Objects.requireNonNull(materialService);
      this.multaStrategy = (CalculoMultaStrategy)Objects.requireNonNull(multaStrategy);
   }

   public Emprestimo registrarEmprestimo(int usuarioId, int materialId) throws BibliotecaException {
      this.usuarioService.buscarPorId(usuarioId);
      MaterialBibliografico material = this.materialService.buscarPorId(materialId);
      if (!material.isDisponivel()) {
         throw new MaterialIndisponivelException("O material " + materialId + " já está emprestado.");
      } else if (this.contarAtivosDoUsuario(usuarioId) >= 3) {
         throw new LimiteEmprestimosException("O usuário já possui 3 empréstimos ativos.");
      } else {
         Emprestimo emprestimo = new Emprestimo(this.repositorio.proximoId(), usuarioId, materialId);
         this.repositorio.adicionar(emprestimo);
         material.marcarComoIndisponivel();

         try {
            this.materialService.atualizar(material);
            return emprestimo;
         } catch (BibliotecaException var8) {
            material.marcarComoDisponivel();

            try {
               this.repositorio.remover(emprestimo.getId());
            } catch (PersistenciaException falhaAoDesfazer) {
               var8.addSuppressed(falhaAoDesfazer);
            }

            throw var8;
         }
      }
   }

   public BigDecimal registrarDevolucao(int emprestimoId) throws BibliotecaException {
      Emprestimo emprestimo = (Emprestimo)this.repositorio.buscarPorId(emprestimoId);
      if (emprestimo == null) {
         throw new BibliotecaException("Empréstimo " + emprestimoId + " não encontrado.");
      } else {
         MaterialBibliografico material = this.materialService.buscarPorId(emprestimo.getMaterialId());
         emprestimo.registrarDevolucao();
         BigDecimal multa = this.calcularMulta(emprestimo);
         material.marcarComoDisponivel();
         this.repositorio.atualizar(emprestimo);
         this.materialService.atualizar(material);
         return multa;
      }
   }

   public BigDecimal calcularMulta(Emprestimo emprestimo) {
      return this.multaStrategy.calcular(emprestimo.calcularDiasAtraso());
   }

   public List<Emprestimo> listarTodos() {
      return this.repositorio.listarTodos();
   }

   public List<Emprestimo> listarPorUsuario(int usuarioId) {
      List<Emprestimo> doUsuario = new ArrayList();

      for(Emprestimo emprestimo : this.repositorio.listarTodos()) {
         if (emprestimo.getUsuarioId() == usuarioId) {
            doUsuario.add(emprestimo);
         }
      }

      return doUsuario;
   }

   public int contarAtivosDoUsuario(int usuarioId) {
      int ativos = 0;

      for(Emprestimo emprestimo : this.listarPorUsuario(usuarioId)) {
         if (emprestimo.estaAtivo()) {
            ++ativos;
         }
      }

      return ativos;
   }

}
