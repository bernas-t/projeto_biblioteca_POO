package exception;

public class DadoInvalidoException extends BibliotecaException {
    private static final long serialVersionUID = 1L;

    public DadoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
