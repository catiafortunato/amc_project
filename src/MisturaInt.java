import java.util.LinkedList;

public interface MisturaInt {
	
	exp probj (LinkedList<double []> timevali, thetaj t);
	
	exp prob (Amostra amt, int j);
	
	void update (LinkedList<thetaj> t);
	
	exp Xij (LinkedList<double []> timvali, thetaj t);
	
	exp wjupdate (Amostra amt, thetaj t);
	
	exp ajupdate (Amostra amt, thetaj t);
	
	exp sigmajupdate(Amostra amt, thetaj t1, thetaj t2);
	
	LinkedList<thetaj> getThetaAuxbd(int j);
	
	void bdj_arrefecimento (Amostra amt, int j);
	
	LinkedList<thetaj> aquecimentoj(int j);
	
	void saj(Amostra amt, int j);
	
	boolean check(int j, Mistura mix);
	
	void EM(Amostra amt);
	
}
