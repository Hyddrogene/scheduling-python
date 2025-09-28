package problem;

import java.util.HashMap;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public abstract class Problem_abstract {
	protected Model model;
	protected IntVar[] vars;
	protected HashMap<String,IntVar> varMap;
	
	public abstract void variables();
	public abstract void constraints();
	public abstract void objectives();
	public abstract void strategies();
	public abstract void solve();
	
}//FinClass
