import java.util.LinkedList;

public class thetaj {
	
	/* -> A classe thetaj representa uma mistura de gaussianas com parametros w (peso), sigma (desvio padrao, igual
	 * para todas as gaussianas de thetaj), a1, a2, b1 e b2 (o valor medio das gaussianas da mistura e calculado a 
	 * parir destes ultimos) */
	
	LinkedList<Double> theta;
	double w;
	double sigma;
	double a1;
	double a2;
	double b1;
	double b2;
	
	public thetaj(double w, double sigma, double a1, double a2, double b1, double b2) {
		super();
		theta = new LinkedList<Double>();
		this.w = w;
		this.sigma = sigma;
		this.a1 = a1;
		this.a2 = a2;
		this.b1 = b1;
		this.b2 = b2;
		theta.add(w);
		theta.add(sigma);
		theta.add(a1);
		theta.add(a2);
		theta.add(b1);
		theta.add(b2);
	}

	@Override
	public String toString() {
		return "" + theta + "";
	}

}
