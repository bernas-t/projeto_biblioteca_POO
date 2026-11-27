package padroes.Singleton;

import model.Usuario;

public class sessao {
    private static sessao instancia;
    private Usuario usuarioLogado;

    private sessao(){
    }

    public static synchronized sessao getInstancia(){
        if (instancia == null){
            instancia = new sessao();
        }
        return instancia;
    }

    public void iniciarSessao(Usuario usuario){
        this.usuarioLogado = usuario;
    }

    public Usuario getUsuarioLogado(){
        return usuarioLogado;
    }

    public void encerrarSessao(){
        this.usuarioLogado = null;

    }

}
