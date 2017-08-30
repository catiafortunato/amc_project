
public class exp {

	exp10 a;
	double b;

	/* -> A classe exp define uma exponencial de base e com um parametro a multiplicativo da base (definido como
	 * um numero do tipo exp10) e um parametro b (double) correspondente ao expoente*/
	
	public exp(exp10 a, double b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return "{"+ a + " "+"e^ " + b +"}";
	}

	
	/* -> A funcao prod calcula o produto de duas exponenciais exp */
	
	public void prod (exp y) {
		if(a.x<Math.pow(10, -300))
			a.getBetter();
		a.prod10(y.a);
		b+= y.b;
	}

	
	/* -> A funcao div calcula a divisao de duas exponenciais exp */
	
	public void div (exp y) {

		if(a.x<Math.pow(10, -300))
			a.getBetter();

		a.div10(y.a);
		b-= y.b;
	}
	
	
	/* -> A funcao soma calcula a soma entre duas exponenciais exp */

	public void soma(exp y) {
		//Se a exp que esta a somar a y for = 0:
		if(a.x == 0) {
			a=y.a;
			b=y.b;
		}
		else {
			//se o expoente da exp que esta a somar for maior que o da y:
			if(b>=y.b) {
				if(Math.abs(y.b-b)>700)
					getBetterexp();

				y.a.x*= Math.exp(y.b-b);
				a.soma10(y.a);
			}
			//caso contrario
			else {
				if(Math.abs(b-y.b) >700)
					y.getBetterexp();

				a.x*= Math.exp(b-y.b);
				y.a.soma10(a);
				a=y.a;
				b = y.b;
			}
		}
	}


	/* -> A funcao getBetterexp aumenta o fator multiplicativo a e diminui o expoente b*/
	
	public void getBetterexp() {
		a.x*=Math.exp(100);
		b-=100;
	}

	
	/* -> A funcao read exp converte um numero do tipo exp para um decimal */
	
	public double readexp() {
		return a.x*Math.pow(10, a.y)*Math.exp(b);
	}
	
}
