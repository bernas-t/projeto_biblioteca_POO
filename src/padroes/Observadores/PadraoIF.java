package padroes.Observadores;

public interface PadraoIF {
    void adicionarObservador(ObservadorIF observador);
    void RemoverObservador(ObservadorIF observador);
    void NotificarObservadores();
}
