package Run;

import GA.GeneticAlgorithm;
import GA.ParametersGA;
import ParserUTP.ConverterJsonChoco;
import ParserUTP.InstanceUTPArray;
import SimulatedAnneling.Solver;

public class Exec {

	public static void main(String[] args) {
		if (args.length < 1) {
	        System.out.println("Usage : Argument 1 json file");
	        System.exit(1);
	    }
	    String filenameData = args[0];
	    filenameData = "/home/etud/timetabling/tools/tools_php/tmp/experiment_07-12-23/instanceUSP_generated_071223111836_realistic_example1.json";
		filenameData = "/home/etud/minizincPython/example_extension_v2.json";
		//filenameData = "/home/etud/minizincPython/to_example_1.json";
		
	    ConverterJsonChoco g = new ConverterJsonChoco(filenameData);
		g.CreateInstance();
		InstanceUTPArray utp  = g.getInstanceUTPArray();
		String filenameSolution = filenameData.substring(0,(filenameData.length()-5))+"_solution.txt";
		
		//ParametersGA parameters = new ParametersGA(1000,10,0.5,0.1);
		//GeneticAlgorithm ga = new GeneticAlgorithm(parameters,utp);
		
		Solver solver = new Solver(utp);
		solver.solve();
		System.out.println("Finish");
		//System.out.println("Solution : "+ga.solve());
	}//FinMethod

}//FinClass
