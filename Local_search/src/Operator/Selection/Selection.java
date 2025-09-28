package Operator.Selection;

import java.util.ArrayList;

import GA.SolutionGA;

public abstract class Selection {

	protected abstract ArrayList<SolutionGA> doSelection(ArrayList<SolutionGA> parents,ArrayList<SolutionGA> childs, int poolSize);

}
