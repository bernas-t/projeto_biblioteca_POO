package exception;

public class MaterialIndisponivelException extends BibliotecaException {
    private static final long serialVersionUID = 1L;

    public MaterialIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
