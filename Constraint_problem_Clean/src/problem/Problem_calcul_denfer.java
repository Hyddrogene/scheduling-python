package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.chocosolver.solver.Cause;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;

import org.chocosolver.solver.variables.IntVar;

public class Problem_calcul_denfer {
	protected Model model;
	protected Model model_useless;
	protected IntVar[] vars;
	protected IntVar[] vars_useless;
	protected HashMap<String,IntVar> varMap;
	protected HashMap<String,IntVar> varMap_useless;
	
	protected List<Solution> solutions;
	protected IntVar varObj;
	protected String[] numbersEn = {
		    "zero", "one", "two", "three", "four", "five",
		    "six", "seven", "eight", "nine", "ten", "eleven", "twelf"
		};
	
	protected HashMap<String,Integer> varMap2;
	
	public Problem_calcul_denfer() {
		model = new Model("Calcul d'enfer");
		model_useless = new Model("C-F-U");
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
		 List<IntVar> tmp = new ArrayList<>();
		 varMap = new HashMap<>();
		 varMap_useless = new HashMap<>();

		 for (int i = 0; i < distinctChars.length; i++) {
		        char letter = (char) distinctChars[i];
		        IntVar var = model.intVar(String.valueOf(letter), -100, 100);
		        IntVar var2 = model_useless.intVar(String.valueOf(letter), -100, 100);
		        vars[i] = var;
		        tmp.add(var2);
		        varMap.put(""+letter, var);
		        varMap_useless.put(""+letter, var2);
		  }
		 	Set<Character> usedLetters = Arrays.stream(distinctChars)
				    .collect(Collectors.toSet());
		 	
				List<IntVar> tmp2 = new ArrayList<>();
		 	
				for (char c = 'a'; c <= 'z'; c++) {
				    if (!usedLetters.contains(c)) {
				        IntVar var = model_useless.intVar(String.valueOf(c), -100, 100);
				        tmp.add(var);
				        tmp2.add(var);
				        System.out.println("Character c : "+c);
				        varMap_useless.put(String.valueOf(c), var);
				    }
				}
				vars_useless = tmp.toArray(new IntVar[0]);
				var_useless2 = tmp2.toArray(new IntVar[0]);

		 
		 
		 
		 varObj = model.intVar(0,distinctChars.length*100);
		 varObj2 = model_useless.intVar(0,vars_useless.length*100);

	}//FinMethod
	protected IntVar[] var_useless2;
	int nr_values ;
	public void constraints() {


		//model.allDifferent(allVars).post();
		model.allDifferent(vars).post();
		//model.increasing(vars, 0).post();
		model_useless.allDifferent(vars_useless).post();
		//model_useless.decreasing(var_useless2, 0).post();
		//z = -3
		//e = -2
		//varMap.get("z").eq(-3).post();;
		//varMap.get("e").eq(-2).post();;
		
		for(int i = 0 ; i < numbersEn.length ;i++) {
			IntVar[] varsTmp = new IntVar[numbersEn[i].length()];
			char[] numbersEnTmp = numbersEn[i].toCharArray();
			for(int j = 0; j < numbersEnTmp.length ;j++) {
				//System.out.println(""+numbersEnTmp[j]);
				varsTmp[j] = varMap.get(""+numbersEnTmp[j]);
			}
			model.sum(varsTmp, "=",i).post();
		}
		
		//#model.absolute(, varObj)
		IntVar[] absolutes = IntStream.range(0, this.nr_values)
			    .mapToObj(i -> {
			        IntVar abs = model.intVar("abs_" + i, 0, 100); // borne haute à adapter si besoin
			        model.absolute(abs, vars[i]).post();
			        return abs;
			    })
			    .toArray(IntVar[]::new);
		
		//model.sum(absolutes,"=", varObj).post();;
		model.max(varObj, absolutes).post();
	
		
		IntVar[] absolutes2 = IntStream.range(0, this.vars_useless.length)
			    .mapToObj(i -> {
			        IntVar abs = model_useless.intVar("abs_" + (i+vars_useless.length), 0, 100); // borne haute à adapter si besoin
			        model_useless.absolute(abs, vars_useless[i]
			        		).post();
			        return abs;
			    })
			    .toArray(IntVar[]::new);
		
		//vars_useless

        //varObj1 = model.intVar(0,vars.length*100);
        //varObj2 = model_useless.intVar(0,vars_useless.length*100);
        

		//model_useless.sum(absolutes2,"=", varObj2).post();;
		model_useless.max(varObj2, absolutes2).post();
		//model.sum(new IntVar[] {varObj1,varObj2},"=", varObj).post();;
		
	}//FinMethod
	
    //IntVar varObj1;
    IntVar varObj2; 
	
	public void setObjectif() {
		model.setObjective(false, this.varObj);
		model_useless.setObjective(false, this.varObj2);
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
			        vars
			    )/*,
			    Search.intVarSearch(
			    		ffl,
			        new IntDomainMin(),
			        vars_useless
			    )*/

			   
			);
		
		
		VariableSelector<IntVar> ffl2 = new FirstFail(model_useless);
		

		this.model_useless.getSolver().setSearch(
			    Search.intVarSearch(
			    		ffl2,
			        new IntDomainMin(),
			        new IntVar[] {varObj2}
			    ),
			    Search.intVarSearch(
			    		ffl2,
			        new IntDomainMin(),
			        vars_useless
			    )/*,
			    Search.intVarSearch(
			    		ffl,
			        new IntDomainMin(),
			        vars_useless
			    )*/

			   
			);

	}//FinMethod
	
	
	public void constraints_assign(HashMap<String, Integer> map){
		for(String key : map.keySet()) {
			//System.out.println("key : "+key+" val : " +map.get(key.toString()));
	        IntVar var = varMap_useless.get(key);
	        int value = map.get(key);
	        try {
				var.instantiateTo(value, Cause.Null);
			} catch (ContradictionException e) {
				e.printStackTrace();
			} 
		}
	}//FinMethod
	
	public void solve() {
	
		Solver solver = model.getSolver();
		List<Solution> solutions = new ArrayList<>();
		int maxSolutions = 100;
		
		IntVar[] allVars = Stream.concat(
			    Stream.of(varObj),
			    Arrays.stream(vars)
			).toArray(IntVar[]::new);
		
		for(int i = 0; i < allVars.length ;i++) {
			System.out.println("allVars[i].getName() "+allVars[i].getName());
		}

		while (solver.solve() && solutions.size() < maxSolutions) {
		    solutions.add(new Solution(model, allVars).record());
		}

		   varMap2 = new HashMap<>();
		   System.out.println("=== Solutions trouvées ===");
		   int count = 1;
		   for (Solution sol : solutions) {
			   System.out.printf("> Solution %d score %d :%n", count++,sol.getIntVal(varObj));
			   //int var_count = 0;
			   for (IntVar var : vars) {
				   int val = sol.getIntVal(var);
				   	if(val != 1000) {
				        System.out.printf("   %s = %d%n", var.getName(), val);
				        //char letter = (char) ('a' + var_count);
				        varMap2.put(var.getName(), val);
				        //varMap2.put(var.getName(), val);
				     }
				   	//var_count++;
			   }
			   System.out.println();

			   
			}
		   
		   System.out.println("=== Statistiques ===");
		   model.getSolver().printStatistics();
		   
		   //=======================================

		    System.out.println("Model_useless :"+" vars_useless.length : "+this.vars_useless.length);
		    constraints_assign(varMap2);
			Solver solver2 = model_useless.getSolver();
			//solver2.showContradiction();
			List<Solution> solutions2 = new ArrayList<>();
		   
			
			IntVar[] allVars2 = Stream.concat(
				    Stream.of(varObj2),
				    Arrays.stream(vars_useless)
				).toArray(IntVar[]::new);

			while (solver2.solve()) {
				solutions2.add(new Solution(model_useless,allVars2).record());
			}
			
			count = 1;
			   for (Solution sol : solutions2) {
				   System.out.printf("> Solution %d score %d :%n", count++,sol.getIntVal(varObj2));
				   
				   for (IntVar var : vars_useless) {
					   int val = sol.getIntVal(var);
					   System.out.printf("   %s = %d%n", var.getName(), val);
				   }
				   System.out.println();
				}
		   
		   //=======================================
		   
		   System.out.println("LALA"+varMap2);
		   if(solutions.size() != 0) {
			   for (int i = 0; i < numbersEn.length; i++) {
				    String word = numbersEn[i];
				    StringBuilder equation = new StringBuilder();
				    int sum = 0;

				    for (int j = 0; j < word.length(); j++) {
				        char c = word.charAt(j);
				        int val = varMap2.get(""+c); 
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
		   model_useless.getSolver().printStatistics();
		   //model.getSolver().limitTime("40s");
	}//FinMethod

}//FinMethod
