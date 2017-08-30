import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Amostra implements AmostraInt {

	/* -> A classe Amostra refere-se as medicoes obtidas da quantidade de farmaco nos pacientes. Esta representa 
	 * os dados que serao recebidos da base de dados. A amostra e uma lista de listas de vetores com tres elementos 
	 * cada (um indice, um tempo e um valor). */

	LinkedList<LinkedList<VetorMed>> amostra;

	public Amostra() {
		super();
		amostra = new LinkedList<LinkedList<VetorMed>>();
	}

	@Override
	public String toString() {
		return "" + amostra + "";
	}


	/* -> A funcao add recebe como argumento um vetor com 3 elementos (indice, tempo e valor) e adiciona o mesmo
	 * a amostra */

	public void add (VetorMed v) {
		LinkedList<VetorMed> newind = new LinkedList<>();

		if(v.ind>=amostra.size()) {
			newind.add(v);
			amostra.add(newind);
		}
		else
			amostra.get(v.ind).add(v);
	}


	/* -> A funcao length devolve o numero de vetores (medicoes) que existem numa amostra para um dado
	 * individuo com indice i */

	public int length(int i) {
		return amostra.get(i).size();
	}


	/* -> A funcao indice recebe como argumento um inteiro i e devolve todas as medicoes (tempos e valores, em pares)
	 * dos vetores com o indice i */

	public LinkedList<double []> indice (int i) {

		if(amostra.size() == 0) 
			return null;

		else {
			LinkedList<VetorMed> ind = amostra.get(i);
			LinkedList<double[]> timevali = new LinkedList<>();
			
			for(int j=0; j<ind.size(); j++) {
				double[] aux = new double[2];
				aux[0]=ind.get(j).time;
				aux[1]=ind.get(j).val;
				timevali.add(aux);
			}
			return timevali;
		}
	}


	/* -> A funcao numind devolve o numero de individuos que estao na amostra */

	public int numind () {
		return amostra.size();
	}


	/* -> A funcao read recebe um ficheiro de medicoes e adiciona os valores do ficheiro a amostra */
	
	public void read(String filename) {
		Scanner s;
		try {
			s = new Scanner(new File(filename));
			s.next(); //para ignorar a primeira linha
			while(s.hasNext()) {
				String data = s.next();
				String [] vetor = data.split(";");
				int ind = Integer.parseInt(vetor[0]);
				double time = Double.parseDouble(vetor[1]);
				double val = Double.parseDouble(vetor[2]);
				VetorMed v = new VetorMed(ind, time, val);

				add(v);
			}
			s.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}