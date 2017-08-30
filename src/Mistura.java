import java.util.LinkedList;
import java.util.Random;
import java.lang.Math;

public class Mistura implements MisturaInt{

	/* A classe mistura corresponde a uma lista de thetaj's , onde cada thetaj e uma mistura de gaussianas */ 

	int M;
	LinkedList<thetaj> Theta;


	/* -> Metodo construtor que recebe um inteiro que corresponde ao numero de misturas de gaussianas, 
	 * e uma lista de parametros thetaj, que correspondem a cada curva de gaussianas com parametros wj,sigmaj,
	 * a1j, a2j, b1j e b2j */

	public Mistura(int m, LinkedList<thetaj> Theta) {
		super();
		M = m;
		this.Theta = Theta;
	}

	@Override
	public String toString() {
		return "" + M + "" + Theta + "";
	}


	/* -> A funcao probj recebe uma lista de vetores de tempos e valores de um indivíduo e devolve
	 * a densidade de probabilidade dessa amostra numa mistura j */

	public exp probj(LinkedList<double []> timevali, thetaj t) { 

		double sum = 0;

		for(int i=0; i<timevali.size(); i++) {
			// sum1 é a parte da funcao da densidade de probabilidade p(yi|thetaj) equivalente a somatorio -[yi(tl) - f(thetaj,tl)]^2 ;
			sum+= Math.pow(timevali.get(i)[1]-t.a1*Math.exp(-t.b1*timevali.get(i)[0])-t.a2*Math.exp(-t.b2*timevali.get(i)[0]),2);
		}

		double expo = -sum/(2*t.sigma);
		exp10 a = new exp10(Math.pow(2*Math.PI*t.sigma, -timevali.size()/2.0), 0);
		exp x = new exp(a, expo);
		return x;
	}


	/* -> A funcao prob calcula a probabilidade da amostra ser vista por uma mistura thetaj */

	public exp prob (Amostra amt, int j) {
		
		exp10 pa = new exp10(1, 0);
		exp p = new exp(pa, 0);

		for(int i=0; i<amt.numind(); i++) {
			exp x = probj(amt.indice(i), Theta.get(j));
			p.prod(x);
		}

		return p;
	}


	/* -> A funcao update recebe como argumento uma lista de thetaj (mistura nova) e substitui a mistura atual pela 
	 * que recebeu */

	public void update(LinkedList<thetaj> t) {
		Theta = t;
	}


	/* -> A funcao Xij calcula a probabilidade ponderada de um individuo ser visto por uma mistura thetaj */

	public exp Xij (LinkedList<double []> timevali, thetaj t) {

		exp Num = probj(timevali, t);
		Num.a.x*= t.w;

		exp10 Dena = new exp10(0, 0);
		exp Den = new exp(Dena, 0);
		for(int u=0; u<M; u++) {
			exp x = probj(timevali, Theta.get(u));
			x.a.x*= Theta.get(u).w;
			Den.soma(x);
		}

		Num.div(Den);
		return Num;
	}


	/* -> A funcao wjupdate faz o update do w de um thetaj (calcula wj(k+1))*/

	public exp wjupdate (Amostra amt, thetaj t) {
		exp10 suma = new exp10(0, 0);
		exp sum = new exp(suma, 0);
		for(int i=0; i<amt.numind(); i++) {
			exp x = Xij(amt.indice(i), t);
			sum.soma(x);
		}

		sum.a.x/= amt.numind();
		return sum;
	}


	/* -> A funcao ajupdate faz o update do a de um theta j (calcula aj(k+1)) */

	public exp ajupdate (Amostra amt, thetaj t) {

		// Calculo do numerador da expressao;
		exp10 Numa = new exp10(0, 0);
		exp Num = new exp(Numa, 0);
		
		for(int i=0; i<amt.numind(); i++) {
			exp10 suma = new exp10(0, 0);
			exp sum = new exp(suma, 0);
			for(int l=0; l<amt.indice(i).size(); l++) {
				exp x = Xij(amt.indice(i), t);
				x.a.x*=(amt.indice(i).get(l)[1])*(Math.exp(-t.b1*amt.indice(i).get(l)[0])-Math.exp(-t.b2*amt.indice(i).get(l)[0]));
				sum.soma(x);
			}
			Num.soma(sum);
		}

		// Calculo do denominador da expressao;
		exp10 Dena = new exp10(0, 0);
		exp Den = new exp(Dena, 0);
		
		for(int i=0; i<amt.numind(); i++) {
			exp10 suma = new exp10(0, 0);
			exp sum = new exp(suma, 0);
			for(int l=0; l<amt.indice(i).size(); l++) {
				exp x = Xij(amt.indice(i), t);
				x.a.x*=Math.pow((Math.exp(-t.b1*amt.indice(i).get(l)[0])-Math.exp(-t.b2*amt.indice(i).get(l)[0])), 2);
				sum.soma(x);	
			}
			Den.soma(sum);
		}
		Num.div(Den);
		return Num;
	}


	/* -> A funcao sigmajupdate faz o update do sigma de um thetaj (calcula sigmaj(k+1)) */

	public exp sigmajupdate(Amostra amt, thetaj t1, thetaj t2) {
		//t1 e (k+1) t2 e (k)
		exp10 Numa = new exp10(0, 0);
		exp Num = new exp(Numa, 0);
		
		for(int i=0; i<amt.numind(); i++) {
			LinkedList<double[]> timevali = amt.indice(i);
			exp10 suma = new exp10(0, 0);
			exp sum = new exp (suma, 0);
			for(int l=0; l<amt.indice(i).size(); l++) {
				exp sum1 = Xij(timevali, t2);
				sum1.a.x*=Math.pow(timevali.get(l)[1]-t1.a1*Math.exp(-t1.b1*timevali.get(l)[0])-t1.a2*Math.exp(-t1.b2*timevali.get(l)[0]),2);
				sum.soma(sum1);
			}
			Num.soma(sum);
		}

		exp10 Dena = new exp10(0, 0);
		exp Den = new exp(Dena, 0);
		for(int j=0; j<amt.numind(); j++) {
			LinkedList<double[]> timevali2 = amt.indice(j);
			exp x = Xij(timevali2, t2);
			x.a.x*= timevali2.size();
			Den.soma(x);
		}
		Num.div(Den);
		return Num ;
	}


	/* -> getThetaAuxbd devolve uma lista de thetaj's em que o j-esimo theta tem b1 e b2 gerados aleatoriamente 
	 * mas proximos (vizinhos) dos b's do thetaj inicial */

	public LinkedList <thetaj> getThetaAuxbd (int j){
		thetaj t = new thetaj(Theta.get(j).w, Theta.get(j).sigma, Theta.get(j).a1, Theta.get(j).a2, Theta.get(j).b1, Theta.get(j).b2);
		Random generator = new Random();
		double dif1 = generator.nextDouble();
		double dif2 = generator.nextDouble();

		int odd1 = generator.nextInt(2)+1;
		int odd2 = generator.nextInt(2)+1;
		double b1l;
		double b2l;
		//cria um vizinho aleatoriamente para o b1
		if (odd1==1)
			b1l = t.b1+dif1/10;
		else 
			b1l = t.b1-dif1/10;
		//cria um vizinho aleatoriamente para o b2 
		if(odd2==1)
			b2l = t.b2+dif2/10;
		else
			b2l = t.b2-dif2/10;

		thetaj tt = new thetaj(t.w,t.sigma,t.a1,t.a2,b1l,b2l);

		LinkedList<thetaj> Thetat = new LinkedList<>();
		for(int i=0; i<Theta.size(); i++){
			if(i!=j)
				Thetat.add(Theta.get(i));
			if(i==j)
				Thetat.add(tt);
		}
		
		return Thetat;
	}


	/* -> a funcao bdj_arrefecimento faz o arrefecimento de um thetaj, onde a probabilidade deste e comparada
	 * sucessivamente por uma mistura auxiliar com o thetaj correspondente com b1 e b2 vizinhos (gerados a partir
	 * da funcao getThetaAuxbd) */
	
	public void bdj_arrefecimento (Amostra amt, int j) {
		
		LinkedList <thetaj> Thetat = getThetaAuxbd(j);
		Mistura mixt = new Mistura(M,Thetat);

		//Este ciclo while serva para o caso de o primeiro Thetat ser melhor que o Theta
		exp mixtprob = mixt.prob(amt, j);
		exp prob = prob(amt, j);
		
		while((Math.log(mixtprob.a.x)+mixtprob.a.y*Math.log(10)+mixtprob.b)>(Math.log(prob.a.x)+prob.a.y*Math.log(10)+prob.b)){
			update(mixt.Theta);
			Thetat = getThetaAuxbd(j);
			mixt = new Mistura (M, Thetat);

			mixtprob = mixt.prob(amt, j);
			prob = prob(amt, j);
		}

		// Ciclo para atualizar Theta de Thetat for melhor e para aceitar 

		while((Math.log(mixtprob.a.x)+mixtprob.a.y*Math.log(10)+mixtprob.b)<=(Math.log(prob.a.x)+prob.a.y*Math.log(10)+prob.b)){
			Random generator = new Random();
			int odd = generator.nextInt(100)+1;
			if(odd!=1){
				Thetat = getThetaAuxbd(j);
				mixt = new Mistura(M, Thetat);
				mixtprob = mixt.prob(amt, j);

				while((Math.log(mixtprob.a.x)+mixtprob.a.y*Math.log(10)+mixtprob.b)>(Math.log(prob.a.x)+prob.a.y*Math.log(10)+prob.b)){
					update(mixt.Theta);
					Thetat = getThetaAuxbd(j);
					mixt = new Mistura (M, Thetat);

					mixtprob = mixt.prob(amt, j);
					prob = prob(amt, j);
				}
			}

			if(odd==1){
				break;
			}
		}



	}

	/* -> Aquecimento de um thetaj (gera uma mistura Theta com o j-esimo thetaj com b1 e b2 aleatorios) */

	public LinkedList<thetaj> aquecimentoj(int j) {

		thetaj t = Theta.get(j);
		Random generator = new Random();

		//gerar b1 aleatorio entre 0 e 5 (sem contar com estes)
		double genb1 = generator.nextDouble()*5;
		while(genb1 == 5 || genb1 == 0) {
			genb1 = generator.nextDouble()*5;
		}

		//gerar b2 aleatorio entre b1 e 5
		double genb2 = genb1+(5-genb1)*generator.nextDouble();
		while(genb1 == genb2 || genb2 == 5) {
			genb2 = genb1+(5-genb1)*generator.nextDouble();
		}

		thetaj tt = new thetaj(t.w, t.sigma, t.a1, t.a2, genb1, genb2);

		LinkedList<thetaj> Thetat = new LinkedList<>();
		for(int i=0; i<Theta.size(); i++) {
			if(i!=j)
				Thetat.add(Theta.get(i));
			if(i==j)
				Thetat.add(tt);
		}
		
		return Thetat;
	}


	/* -> A funcao saj faz o simulated annealing de uma mistura thetaj (calcula os b's (k+1))*/

	public void saj(Amostra amt, int j) {
		int R = 0;

		while(R < 1000) {

			//guardar os valores de b1k e b2k (para o caso de Theta ser atualizado e os limites serem violados)
			double b1k = Theta.get(j).b1;
			double b2k = Theta.get(j).b2;

			//Criacao de uma mistura auxiliar mixk com b1 e b2 maximos

			LinkedList<thetaj> Thetat = aquecimentoj(j);
			while(Thetat.get(j).b1>Theta.get(j).b2)
				Thetat = aquecimentoj(j);

			Mistura mixk = new Mistura(M, Thetat);

			mixk.bdj_arrefecimento(amt, j);
			if(R==0) {
				bdj_arrefecimento(amt,j);
			}
			
			//Comparacao das misturas mixk e a atual

			if(mixk.Theta.get(j).b1>mixk.Theta.get(j).b2) {
				Thetat = aquecimentoj(j);
				mixk.Theta = Thetat;
				mixk.bdj_arrefecimento(amt, j);
				
				while(mixk.Theta.get(j).b1>mixk.Theta.get(j).b2) {
					Thetat = aquecimentoj(j);
					mixk.Theta = Thetat;
					mixk.bdj_arrefecimento(amt, j);
				}
			}
			
			exp mixkprob = mixk.prob(amt, j);
			exp prob = prob(amt, j);
			
			if((Math.log(mixkprob.a.x)+mixkprob.a.y*Math.log(10)+mixkprob.b)>(Math.log(prob.a.x)+prob.a.y*Math.log(10)+prob.b)){
				update(mixk.Theta);
			}

			//condicao para violacao dos limites
			if(Theta.get(j).b2>5) {
				double b2l = Theta.get(j).b2-0.2;
				thetaj t = new thetaj(Theta.get(j).w, Theta.get(j).sigma, Theta.get(j).a1, Theta.get(j).a2, Theta.get(j).b1, b2l);
				Theta.set(j, t);

				while(Theta.get(j).b2>5) {
					b2l = Theta.get(j).b2-0.2;
					thetaj tt = new thetaj(Theta.get(j).w, Theta.get(j).sigma, Theta.get(j).a1, Theta.get(j).a2, Theta.get(j).b1, b2l);
					Theta.set(j, tt);
				}

				while(Theta.get(j).b1>=Theta.get(j).b2) {
					double b1l = Thetat.get(j).b1-0.2;
					thetaj tt = new thetaj(Theta.get(j).w, Theta.get(j).sigma, Theta.get(j).a1, Theta.get(j).a2, b1l, Theta.get(j).b2);
					Theta.set(j, tt);
				}
				R-=1;
			}

			//condicao para b1j<0
			if(Theta.get(j).b1<0) {
				thetaj t = new thetaj(Theta.get(j).w, Theta.get(j).sigma, Theta.get(j).a1, Theta.get(j).a2, b1k, Theta.get(j).b2);
				Theta.set(j, t);
			}

			//condicao para b2j<0
			if(Theta.get(j).b2<0) {
				thetaj t = new thetaj(Theta.get(j).w, Theta.get(j).sigma, Theta.get(j).a1, Theta.get(j).a2, Theta.get(j).b1, b2k);
				Theta.set(j, t);
			}

			R+= 1;
		}
	}


	/* -> Check verifica a condicao de convergencia do EM */

	public boolean check(int j, Mistura mixk) {
		boolean ok = Math.pow(Theta.get(j).b1-mixk.Theta.get(j).b1, 2)>0.0001 || Math.pow(Theta.get(j).b2-mixk.Theta.get(j).b2, 2)>0.0001;
		return ok;
	}


	/* -> EM é o algoritmo de expectation maximization, onde primeiro atualizam-se os w's, depois os a's, depois os
	 * b's e por fim os sigma's . O algoritmo para quando a convergencia do EM e verificada pela funcao check */

	public void EM(Amostra amt) {
		
		//criacao de uma mistura auxiliar equivalente a mistura k
		LinkedList<thetaj> Thetat = new LinkedList<>();
		for(int j=0; j<M; j++) {
			thetaj tj = new thetaj(Theta.get(j).w, Theta.get(j).sigma, Theta.get(j).a1, Theta.get(j).a2, Theta.get(j).b1, Theta.get(j).b2);
			Thetat.add(tj);
		}
		Mistura mix = new Mistura(M, Thetat);
		
		LinkedList<thetaj> Thetaaux = new LinkedList<>();
		
		//atualizacao dos pesos w's
		for(int j=0; j<M; j++) {
			exp wk = wjupdate(amt, mix.Theta.get(j));
			double wkj = wk.readexp();
	
			thetaj tk = new thetaj(wkj, mix.Theta.get(j).sigma, mix.Theta.get(j).a1, mix.Theta.get(j).a2, mix.Theta.get(j).b1, mix.Theta.get(j).b2);
			Thetaaux.add(tk);
		}
		
		//Mistura auxiliar com os pesos atualizados
		Mistura aux = new Mistura(M, Thetaaux);
		
		Thetaaux = new LinkedList<>();
		
		//atualizacao dos parametros a's
		for(int j=0; j<M; j++) {
			exp ak = ajupdate(amt, mix.Theta.get(j));
			double akj = ak.readexp();
			
			thetaj tk = new thetaj(aux.Theta.get(j).w, mix.Theta.get(j).sigma, akj, -akj, mix.Theta.get(j).b1, mix.Theta.get(j).b2);
			Thetaaux.add(tk);
		}
		
		//mistura com a's e w's atualizados
		aux = new Mistura(M, Thetaaux);
				
		//atualizacao dos parametros b's
		for(int j=0; j<M; j++) {
			Mistura auxj = new Mistura(M, aux.Theta);
			auxj.saj(amt, j);
	
			thetaj tk = new thetaj(aux.Theta.get(j).w, aux.Theta.get(j).sigma, aux.Theta.get(j).a1, aux.Theta.get(j).a2, auxj.Theta.get(j).b1, auxj.Theta.get(j).b2);
			Theta.set(j, tk);
		}
		//Theta ja tem todos os parametros atualizados menos sigma
		
		//atualizacao das variencias sigmas
		for(int j=0; j<M; j++) {
			exp sigmak = sigmajupdate(amt, Theta.get(j), mix.Theta.get(j));
			double sigmakj = sigmak.readexp();
			
			thetaj tk = new thetaj(Theta.get(j).w, sigmakj, Theta.get(j).a1, Theta.get(j).a2, Theta.get(j).b1, Theta.get(j).b2);
			Theta.set(j, tk);
		}
		//Theta ja tem todos os parametros atualizados
		
		//verificacao das condicoes de convergencia do EM
		boolean ok = false;
		for(int j=0; j<M; j++) {
			ok = ok || check(j, mix);	
		}
		if(ok) {
			EM(amt);
		}
	}
}