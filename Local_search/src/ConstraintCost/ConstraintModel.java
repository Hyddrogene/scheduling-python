package ConstraintCost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.stream.Collectors;

import ParserUTP.ConstraintUTP;
import SimulatedAnneling.SolutionUTP;

public abstract class ConstraintModel {
	protected ConstraintUTP cutp;
		
	public ConstraintModel(ConstraintUTP cutp) {
		this.cutp = cutp;
	}//FinMethod
	
	public int countDifferenceToMax(ArrayList<Integer> lst){
		 // Compter l'occurrence de chaque valeur
		HashMap<Integer, Integer> occurrences = new HashMap<>();
        for (int valeur : lst) {
            occurrences.put(valeur, occurrences.getOrDefault(valeur, 0) + 1);
        }

        // Trouver la fréquence maximale
        int maxFrequence = Collections.max(occurrences.values());

        // Calculer le nombre de valeurs qui ne sont pas les plus fréquentes
        int nombreNonMaxFrequence = lst.size() - maxFrequence;

        return nombreNonMaxFrequence;
	}//FinMethod
	
	public int countDifferenceToMaxv2(ArrayList<Integer> lst){
		//System.out.println(lst);
		double maxFrequence = lst.stream().collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
	            .values().stream().mapToDouble(Long::longValue).max().orElse(0);
	    return (int) (lst.size() - maxFrequence);
	}//FinMethod
	
	public static ArrayList<Integer> fusionAllArray(ArrayList<ArrayList<Integer>> bigArrayList) {
		return bigArrayList.stream().flatMap(ArrayList::stream).collect(Collectors.toCollection(ArrayList::new));
	}//FinMethod
	
	public static ArrayList<Integer> fusionAllArray(Vector<Vector<Integer>> bigArrayList) {
		return bigArrayList.stream().flatMap(Vector::stream).collect(Collectors.toCollection(ArrayList::new));
	}//FinMethod
	
    public ArrayList<Integer> createSubArrayList(ArrayList<Integer> bigArrayList, Vector<Integer> indices) {
        return indices.stream()
                .filter(index -> index >= 0 && index < bigArrayList.size())
                .map(bigArrayList::get).collect(Collectors.toCollection(ArrayList<Integer>::new));
    }//FinMethod
	
	
    public ArrayList<Integer> createSubArrayList(ArrayList<Integer> bigArrayList, ArrayList<Integer> indices) {
        return indices.stream()
                .filter(index -> index >= 0 && index < bigArrayList.size())
                .map(bigArrayList::get).collect(Collectors.toCollection(ArrayList<Integer>::new));
    }//FinMethod
	
	public abstract int evaluate(SolutionUTP ga);
	//public abstract int evaluateAB(SolutionAB ga);

	public ConstraintUTP getUTP() {
		return this.cutp;
	}//FinMethod
	
	public int getHardness() {
		
		if(cutp.getHardness().equals("hard")) {
			return 0;
		}
		else if(cutp.getHardness().equals("soft")) {
			return 1;
		}
		else {
			return -1;
		}
	}//FinMethod

}//FinClass
