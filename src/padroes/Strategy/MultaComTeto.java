import java.math.BigDecimal;

public class MultaComTeto implements ICalculoMultaStrategy {
	private BigDecimal precoDiario;
	private BigDecimal valorLimite;
	
	public MultaComTeto (BigDecimal precoDiario, BigDecimal valorLimite) {
		this.precoDiario = precoDiario;
		this.valorLimite = valorLimite;
	}
	@Override
	public BigDecimal calcular(long diasAtraso) { 
		BigDecimal total = this.precoDiario.multiply(BigDecimal.valueOf(diasAtraso));
		
		if (total.compareTo(valorLimite)>0) {
			total = this.valorLimite;
		}
		return total;
	}
}
