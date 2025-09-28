package spheriqueProblem;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.RealVar;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;


public class taluman {
	private Model model;
	private int n;

	public taluman(int n) {
		this.model = new Model();
		this.n = n;
	}//FinMethod
	
	public RealVar factoriel(ArrayList<RealVar> minVars) {
		if(minVars.size()>1) {
			ArrayList<RealVar> vars = new ArrayList<RealVar>();
			for(int i = 1; i < minVars.size() ;i++) {
				vars.add(minVars.get(i-1).min(minVars.get(i)).realVar(0.0001));
			}
			return factoriel(vars);
		}
		else {
			return minVars.get(0);
		}

	}//FinMethod
	
	public void solve() {
		RealVar[] x = new RealVar[this.n];
		RealVar[] y = new RealVar[this.n];
		
		for(int i = 0; i < this.n ;i++) {
			x[i] = model.realVar(0, 2*Math.PI, 0.00001);
 			y[i] = model.realVar(0, Math.PI, 0.00001);
		}
		
		ArrayList<RealVar> vars = new ArrayList<RealVar>();
		ArrayList<RealVar> minVars = new ArrayList<RealVar>();
		
		int k = 1;
		for(int i = 0; i < this.n ;i++) {
			for(int j = (i+1); j < this.n ;j++) {
				RealVar tmp = model.realVar(0, 360, 0.00001);
				tmp = x[i].div(x[j]).add(y[i]).add(y[j]).realVar(0.001);//x[i].cos().mul(x[j].cos()).add(x[i].sin().mul(x[j].sin()).mul(y[j].sub(y[i]))).acos().realVar(0.0001);
				vars.add(tmp);
			}
			if(k > 1) {
				RealVar tmp = vars.get(k).min(vars.get(k-1)).realVar(0.0001);
				minVars.add(tmp);
			}
			k++;
		}
		RealVar out = factoriel(minVars);
		
		Solver solver = model.getSolver();
        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
        };
		//this.solution = this.solver.findSolution();

		solver.printStatistics();
		//model.min(vars.stream().map(u->u).toArray());
		
	}//FinMethod
}//FinClass
