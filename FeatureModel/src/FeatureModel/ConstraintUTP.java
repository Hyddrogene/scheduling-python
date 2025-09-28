package FeatureModel;

import java.util.Arrays;
import java.util.Vector;

public class ConstraintUTP {
	//Attribute
	private int rule;
	private int cpt;
	private String constraint;
	private String hardness;
	private int arity;
	private String[] type;
	private int[] elements;
	private Vector<Vector<Integer>> sessions;
	private int[] parameters;
	private int isActivate;
	
	//Method
	public ConstraintUTP(int cpt,int rule,String constraint,String hardness,int arity,String[] type,int[] elements,Vector<Vector<Integer>> sessions,int[] parameters) {
		this.cpt = cpt;
		this.rule = rule;
		this.constraint = constraint;
		this.hardness = hardness;
		this.arity = arity;
		this.type = type;
		this.elements = elements;
		this.sessions = sessions;
		this.parameters = parameters;
		this.isActivate = 0;
	}//FinMethod

	public int getIsActivate() {
		return isActivate;
	}//FinMethod

	public void setIsActivate(int isActivate) {
		this.isActivate = isActivate;
	}//FinMethod

	public int getRule() {
		return rule;
	}//FinMethod

	public int getCpt() {
		return cpt;
	}//FinMethod

	public String getConstraint() {
		return constraint;
	}//FinMethod

	public String getHardness() {
		return hardness;
	}//FinMethod

	public int getArity() {
		return arity;
	}//FinMethod

	public String[] getType() {
		return type;
	}//FinMethod

	public int[] getElements() {
		return elements;
	}//FinMethod

	public Vector<Vector<Integer>> getSessions() {
		return sessions;
	}//FinMethod

	public int[] getParameters() {
		return parameters;
	}//FinMethod
	
	public String stringTab(String[] tab) {
		String out = "";
			for(int i = 0 ; i < tab.length; i++) {
				out += tab[i]+" ";
			}
			return out;
	}//FinMethod
	
	public String stringTab(int[] tab) {
		String out = "";
			for(int i = 0 ; i < tab.length; i++) {
				out += tab[i]+" ";
			}
			return out;
	}//FinMethod
	
	public String stringTab(Vector<Vector<Integer>> tab) {
		String out = "";
			for(int i = 0 ; i < tab.size(); i++) {
				out+="[";
				for(int j = 0 ; j < tab.get(i).size()  ;j++ ) {
					if(i < tab.get(i).size()-1) {
						out += tab.get(i).get(j)+" ";
					}
					else {
						out += tab.get(i).get(j)+"";
					}
				}
				if(i < tab.size()-1) {
					out += ".\n";
				}
				out+="]";
			}
			return out;
	}//FinMethod
	
	public boolean containSession(int session) {
		for(int i = 0; i < this.sessions.size() ;i++) {
			for(int j =0; j < this.sessions.get(i).size();j++) {
				if(this.sessions.get(i).get(j) == session+1) {
					return true;
				}
			}
		}
		return false;
	}//FinMethod
	
	public String ToString() {
		String out = "";
		out += "Constraint "+cpt+"\nrule "+rule+"\nconstraint "+constraint+"\nhardness "+hardness+"\narity "+arity+"\n";
		out += "type ["+stringTab(type)+"]\n";
		out += "elements ["+stringTab(elements)+"]\n";
		out += "sessions ["+stringTab(sessions)+"]\n";
		out += "parameters ["+stringTab(parameters)+"]\n";
		return out;
	}//FinMethod
	public String toString() {
		String out = "";
		out += "Constraint "+cpt+"\nrule "+rule+"\nconstraint "+constraint+"\nhardness "+hardness+"\narity "+arity+"\n";
		out += "type ["+stringTab(type)+"]\n";
		out += "elements ["+stringTab(elements)+"]\n";
		out += "sessions ["+stringTab(sessions)+"]\n";
		out += "parameters ["+stringTab(parameters)+"]\n";
		return out;
	}//FinMethod
 }//FinClass
