package GA;

import ParserUTP.ConverterJsonChoco;
import ParserUTP.InstanceUTPArray;

public class ExecutionSolver {
	
	public int counter;
	
public ExecutionSolver() {
	this.counter = 1000;
}//FinMethod

public void solve() {
	Solution currentSolution;
	Solution bestSolution;
	String filenameData= "/home/etud/timetabling/src/asp/example4.json";
	ConverterJsonChoco g = new ConverterJsonChoco(filenameData);
	g.CreateInstance();
	InstanceUTPArray utp  = g.getInstanceUTPArray();
	String filenameSolution = filenameData.substring(0,(filenameData.length()-5))+"_solution.txt";
	
	currentSolution = new Solution(utp);
	
	for(int i = 0 ; i < this.counter ;i++) {
		
		
	}
}//FinMethod

}//FinClass

