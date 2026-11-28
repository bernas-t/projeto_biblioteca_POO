package View;

import Singleton.sessao;
import controller.BibliotecaController;
import exception.BibliotecaException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import model.Cargo;
import model.Emprestimo;
import model.MaterialBibliografico;
import model.Usuario;

public class BibliotecaView {
    private BibliotecaController controller;
    private Scanner scanner;

    public BibliotecaView(BibliotecaController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            Usuario logado = sessao.getInstancia().getUsuarioLogado();
            System.out.println("\n=== SISTEMA DE BIBLIOTECA ===");
            System.out.println("Sessão: " + (logado != null ? logado.getNome() + " (" + logado.getCargo() + ")" : "Não autenticado"));
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Fazer Login");
            System.out.println("3. Cadastrar Material (Requer Bibliotecário logado)");
            System.out.println("4. Listar Materiais");
            System.out.println("5. Buscar Material por Título");
            System.out.println("6. Realizar Empréstimo");
            System.out.println("7. Registrar Devolução");
            System.out.println("8. Listar Empréstimos por Usuário");
            System.out.println("9. Encerrar Sessão (Logout)");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        System.out.print("Nome: "); String nome = scanner.nextLine();
                        System.out.print("Login: "); String login = scanner.nextLine();
                        System.out.print("Senha: "); String senha = scanner.nextLine();
                        System.out.print("Cargo (BIBLIOTECARIO / LEITOR): ");
                        Cargo cargo = Cargo.valueOf(scanner.nextLine().trim().toUpperCase());
                        Usuario u = controller.cadastrarUsuario(nome, login, senha, cargo);
                        exibirMensagem("Usuário cadastrado com ID: " + u.getId());
                        break;

                    case 2:
                        System.out.print("Login: "); String l = scanner.nextLine();
                        System.out.print("Senha: "); String s = scanner.nextLine();
                        controller.login(l, s);
                        exibirMensagem("Login realizado com sucesso!");
                        break;

                    case 3:
                        System.out.print("Tipo (LIVRO / PERIODICO): "); String tipo = scanner.nextLine().trim().toUpperCase();
                        System.out.print("ID: "); String id = scanner.nextLine();
                        System.out.print("Título: "); String tit = scanner.nextLine();
                        System.out.print("Ano: "); String ano = scanner.nextLine();
                        if (tipo.equals("LIVRO")) {
                            System.out.print("Autor: "); String autor = scanner.nextLine();
                            System.out.print("ISBN: "); String isbn = scanner.nextLine();
                            controller.cadastrarMaterial(new String[]{tipo, id, tit, ano, autor, isbn});
                        } else {
                            System.out.print("Número da Edição: "); String ed = scanner.nextLine();
                            controller.cadastrarMaterial(new String[]{tipo, id, tit, ano, ed});
                        }
                        exibirMensagem("Material cadastrado!");
                        break;

                    case 4:
                        exibirMateriais(controller.listarMateriais());
                        break;

                    case 5:
                        System.out.print("Termo de busca: ");
                        exibirMateriais(controller.buscarMateriais(scanner.nextLine()));
                        break;

                    case 6:
                        System.out.print("ID do Usuário: "); int uId = Integer.parseInt(scanner.nextLine());
                        System.out.print("ID do Material: "); int mId = Integer.parseInt(scanner.nextLine());
                        controller.emprestar(uId, mId);
                        exibirMensagem("Empréstimo registrado!");
                        break;

                    case 7:
                        System.out.print("ID do Empréstimo: "); int empId = Integer.parseInt(scanner.nextLine());
                        BigDecimal multa = controller.devolver(empId);
                        exibirMensagem("Devolução concluída! Multa por atraso: R$ " + multa);
                        break;

                    case 8:
                        System.out.print("ID do Usuário: "); int idBusca = Integer.parseInt(scanner.nextLine());
                        exibirEmprestimos(controller.listarEmprestimosUsuario(idBusca));
                        break;

                    case 9:
                        sessao.getInstancia().encerrarSessao();
                        exibirMensagem("Sessão encerrada.");
                        break;

                    case 0:
                        exibirMensagem("Encerrando sistema...");
                        break;

                    default:
                        exibirMensagem("Opção inválida.");
                }
            } catch (BibliotecaException e) {
                exibirMensagem("[ERRO DE NEGÓCIO]: " + e.getMessage());
            } catch (Exception e) {
                exibirMensagem("[ERRO]: " + e.getMessage());
            }
        }
    }

    public void exibirMateriais(List<MaterialBibliografico> materiais) {
        if (materiais.isEmpty()) {
            exibirMensagem("Nenhum material encontrado.");
            return;
        }
        materiais.forEach(m -> System.out.println(m.ExibirDetalhes()));
    }

    public void exibirEmprestimos(List<Emprestimo> emprestimos) {
        if (emprestimos.isEmpty()) {
            exibirMensagem("Nenhum empréstimo encontrado.");
            return;
        }
        for (Emprestimo e : emprestimos) {
            System.out.println("ID: " + e.getID() + " | Material ID: " + e.getMaterialId() +
                               " | Empréstimo: " + e.getDataEmprestimo() +
                               " | Previsto: " + e.getDataPrevistaDevolucao() +
                               " | Status: " + (e.estaAtivo() ? "ATIVO" : "DEVOLVIDO em " + e.getDataDevolucao()));
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
