package Strategy;
import java.math.BigDecimal;

public class MultaPorDia implements ICalculoMultaStrategy {
	private BigDecimal precoDiario;
	
	public MultaPorDia (BigDecimal precoDiario) {
		this.precoDiario = precoDiario;
	}
	@Override
	public BigDecimal calcular(long diasAtraso) { 
		return this.precoDiario.multiply(BigDecimal.valueOf(diasAtraso));
	}
}
