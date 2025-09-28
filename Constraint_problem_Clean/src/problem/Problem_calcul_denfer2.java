package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;

import org.chocosolver.solver.variables.IntVar;

public class Problem_calcul_denfer2 {

		//Choco
		protected Model model;
		protected IntVar[] vars;
		protected IntVar[] allVars;
		protected IntVar[] vars_useless;
		protected IntVar varObj;
		protected HashMap<String,IntVar> varMap;
		protected HashMap<String,IntVar> varMap_useless;
		protected HashMap<String,Integer> varMapResultat;
		protected List<Solution> solutions;
		
		//Constant
		protected int minBorn = -100;
		protected int maxBorn = 100;
		protected String[] numbersEn = {
			    "zero", "one", "two", "three", "four", "five",
			    "six", "seven", "eight", "nine", "ten", "eleven", "twelf"//,"thirteen"
			};
		
		
		
		public Problem_calcul_denfer2() {
			model = new Model("Calcul d'enfer");
			variables();
			constraints();
			setObjectif();
			setSearch();
		}//FinMethod
		
		public void variables() {
			
			
			String full = Arrays.stream(numbersEn).reduce("", String::concat);
			Character[] distinctChars = full.chars()
				    .mapToObj(c -> (char) c)
				    .distinct()
				    .toArray(Character[]::new);
			
			
			
			nr_values = distinctChars.length;
			 vars = new IntVar[distinctChars.length];
			 
			 varMap = new HashMap<>();
			 varMap_useless = new HashMap<>();

			 for (int i = 0; i < distinctChars.length; i++) {
			        char letter = (char) distinctChars[i];
			        IntVar var = model.intVar(String.valueOf(letter), minBorn, maxBorn);
			        vars[i] = var;
			        varMap.put(""+letter, var);
			  }
			 	Set<Character> usedLetters = Arrays.stream(distinctChars)
					    .collect(Collectors.toSet());
			 	
					List<IntVar> tmp = new ArrayList<>();
			 	
					for (char c = 'a'; c <= 'z'; c++) {
					    if (!usedLetters.contains(c)) {
					        IntVar var = model.intVar(String.valueOf(c), minBorn, maxBorn);
					        tmp.add(var);
					        varMap_useless.put(String.valueOf(c), var);
					    }
					}
					vars_useless = tmp.toArray(new IntVar[0]);
			 
					allVars = Stream.concat(Arrays.stream(vars),Arrays.stream(vars_useless)).toArray(IntVar[]::new);
			 
					varObj = model.intVar(0,maxBorn);

		}//FinMethod
		
		int nr_values ;
		public void constraints() {
			
			model.allDifferent(allVars).post();
			
			IntStream.range(0, numbersEn.length).forEach(i -> model.sum(
		        numbersEn[i].chars().mapToObj(c -> varMap.get(String.valueOf((char) c))).toArray(IntVar[]::new),"=", i
		    ).post());
			
			IntVar[] absolutes = IntStream.range(0, this.allVars.length).mapToObj(i -> {
				        IntVar abs = model.intVar("abs_" + i, 0, maxBorn);
				        model.absolute(abs, allVars[i]).post();
				        return abs;
				    }).toArray(IntVar[]::new);
			
			model.max(varObj, absolutes).post();
	
		}//FinMethod
		

		public void setObjectif() {
			model.setObjective(false, this.varObj);
		}//FinMethod
		
		public void setSearch() {
			
			VariableSelector<IntVar> ffl = new FirstFail(model);
			this.model.getSolver().setSearch(
				    Search.intVarSearch(
				    		ffl,
				        new IntDomainMin(),
				        new IntVar[] {varObj}
				    ),
				    Search.intVarSearch(
				    		ffl,
				        new IntDomainMin(),
				        allVars
				    )
				);

		}//FinMethod
		
		
		public void solve() {
		
			Solver solver = model.getSolver();
			List<Solution> solutions = new ArrayList<>();
			int maxSolutions = 100;
			
			IntVar[] allVars_tmp = Stream.concat(
				    Stream.of(varObj),
				    Arrays.stream(allVars)
				).toArray(IntVar[]::new);

			while (solver.solve() && solutions.size() < maxSolutions) {
			    solutions.add(new Solution(model, allVars_tmp).record());
			}

			varMapResultat = new HashMap<>();
			   System.out.println("=== Solutions trouvées ===");
			   int count = 1;
			   for (Solution sol : solutions) {
				   System.out.printf("> Solution %d score %d :%n", count++,sol.getIntVal(varObj));
				   for (IntVar var : allVars) {
					   int val = sol.getIntVal(var);
					   System.out.printf("   %s = %d%n", var.getName(), val);
					   varMapResultat.put(var.getName(), val);
				   }
				   System.out.println();
				}
			  
			   //=======================================
			   
			   if(solutions.size() != 0) {
				   for (int i = 0; i < numbersEn.length; i++) {
					    String word = numbersEn[i];
					    StringBuilder equation = new StringBuilder();
					    int sum = 0;

					    for (int j = 0; j < word.length(); j++) {
					        char c = word.charAt(j);
					        int val = varMapResultat.get(""+c); 
					        equation.append(c).append("=").append(val);
					        sum += val;

					        if (j < word.length() - 1) {
					            equation.append(" + ");
					        }
					    }

					    equation.append(" = ").append(sum).append(" (attendu: ").append(i).append(")");
					    System.out.println(equation);
					} 
			   }

			   	//========================================
			   
			   System.out.println("=== Statistiques ===");
			   model.getSolver().printStatistics();
		}//FinMethod

}//FinClass
