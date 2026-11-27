package exception;

public class LimiteEmprestimosException extends BibliotecaException {
    private static final long serialVersionUID = 1L;

    public LimiteEmprestimosException(String mensagem) {
        super(mensagem);
    }
}
