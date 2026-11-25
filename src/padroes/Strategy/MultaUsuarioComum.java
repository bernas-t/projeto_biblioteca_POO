package padroes.Strategy;

public class MultaUsuarioComum implements CalculoMultaStrategy {
    @Override 
    public double CalcularMulta(double DiasAtraso){
        if (DiasAtraso <= 0){
            return 0.0;
        }
        else{
            return DiasAtraso * 0.5;
        }
    }
}
