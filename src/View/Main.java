package View;
import Strategy.ICalculoMultaStrategy;
import Strategy.MultaComTeto;
import controller.BibliotecaController;
import controller.EmprestimoService;
import controller.MaterialService;
import controller.UsuarioService;
import exception.PersistenciaException;
import java.math.BigDecimal;
import model.Emprestimo;
import model.MaterialBibliografico;
import model.Repositorio;
import model.RepositorioArquivo;
import model.Usuario;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Camada de Persistência
            Repositorio<Usuario> repoUsuarios = new RepositorioArquivo<>("usuarios.dat", Usuario::getId);
            Repositorio<MaterialBibliografico> repoMateriais = new RepositorioArquivo<>("materiais.dat", MaterialBibliografico::getID);
            Repositorio<Emprestimo> repoEmprestimos = new RepositorioArquivo<>("emprestimos.dat", Emprestimo::getID);

            // 2. Estratégia de Multa (Strategy)
            ICalculoMultaStrategy estrategiaMulta = new MultaComTeto(new BigDecimal("2.50"), new BigDecimal("50.00"));

            // 3. Camada de Serviços
            UsuarioService usuarioService = new UsuarioService(repoUsuarios);
            MaterialService materialService = new MaterialService(repoMateriais);
            EmprestimoService emprestimoService = new EmprestimoService(repoEmprestimos, materialService, estrategiaMulta);

            // 4. MVC (Controller e View)
            BibliotecaController controller = new BibliotecaController(usuarioService, materialService, emprestimoService);
            BibliotecaView view = new BibliotecaView(controller);

            view.exibirMenu();

        } catch (PersistenciaException e) {
            System.err.println("Erro crítico ao inicializar armazenamento: " + e.getMessage());
        }
    }

}
