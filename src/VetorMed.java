
public class VetorMed {
	int ind;
	double time;
	double val;
	
	public VetorMed(int ind, double time, double val) {
		super();
		this.ind = ind;
		this.time = time;
		this.val = val;
	}
	@Override
	public String toString() {
		return "{" + ind + "," + time + "," + val + "}";
	}
	
	
}
