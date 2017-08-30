
public class exp10 {

	double x;
	double y;

	/* -> A classe exp10 define uma exponencial de base 10 com um parametro a multiplicativo da base e um 
	 * parametro b correspondente ao expoente, ambos decimais*/
	
	public exp10(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "{" + x + " x10^ " + y + "}";
	}

	
	/* -> A funcao getBetter aumenta o fator multiplicativo a e diminui o expoente b*/

	public void getBetter() {
		x*=Math.pow(10, 100);
		y-=100;
	}


	/* -> A funcao prod10 calcula o produto de duas exponenciais exp10 */
	
	public void prod10(exp10 n) {
		x*=n.x;
		y+=n.y;
	}


	/* -> A funcao div calcula a divisao de duas exponenciais exp10 */
	
	public void div10(exp10 n) {
		x/=n.x;
		y-=n.y;
	}


	/* -> A funcao soma calcula a soma entre duas exponenciais exp10 */
	
	public void soma10(exp10 n) {
		//Se a exp10 que esta a somar a y for = 0:
		if(x==0) {
			x=n.x;
			y=n.y;
		}
		else {
			//se ambos os fatores multiplicativos forem diferentes:
			if(n.x!=0) {
				if(x!=0) {
					//se o expoente da exp10 que esta a somar for maior que o da n:
					if(y>=n.y) {
						while(x<Math.pow(10, -300)) {
							getBetter();
						}
						while(Math.abs(n.y-y)>300)
							getBetter();

						x+= n.x*Math.pow(10, n.y-y);
					}
					//caso contrario
					else {
						while(x<Math.pow(10, -300)) {
							getBetter();
						}
						while(Math.abs(y-n.y)>700) {
							n.getBetter();
						}

						x = n.x+x*Math.pow(10, y-n.y);
						y = n.y;
					}
				}
				//se o fator multiplicativo do que esta a somar for =0:
				else {
					if(y>=n.y) {
						while(Math.abs(n.y-y)>300)
							getBetter();

						x=n.x*Math.pow(10, n.y-y);
					}
					else {
						x = n.x;
						y = n.y;
					}
				}
			}
		}
	}
}
