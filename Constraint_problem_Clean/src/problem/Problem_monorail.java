package problem;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Problem_monorail {
	
	public ReadMonorailInstance rmi;
	protected Model model;
	protected ArrayList<ArrayList<IntVar>> vars;

	public Problem_monorail(String filename) {
		rmi = new ReadMonorailInstance(filename);
		variables();
		constraints();
	}//FinMethod
	
	public void variables() {
		vars = new ArrayList<>();
		for(int i = 0; i < rmi.graphes.size() ;i++) {
			IntVar var = model.intVar("var_"+(i+1),new int[] {});
		}
	}//FinMethod
	
	public void constraints() {
		
	}//FinMethod
	
	public void solve() {
		
	}//FinMethod
	
}//FinClass
