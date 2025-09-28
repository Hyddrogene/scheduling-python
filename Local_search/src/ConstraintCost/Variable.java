package ConstraintCost;

public class Variable {
public String name;
public Integer value;
public String type;
public int[] range;

/*Constructor*/
public Variable(String name) {
	this.name = name;
	this.type = "";
	this.range = new int[0];
}//FinMethod

public Variable(String name,int[] range) {
	this.name = name;
	this.type = "";
	this.range = range;
}//FinMethod

public Variable(String name,String type) {
	this.name = name;
	this.type = "";
	this.range = new int[0];
}//FinMethod

public Variable(String name,int[] range,String type) {
	this.name = name;
	this.type = type;
}//FinMethod
/*End constructor*/

public void updateValue(int u) {
	value = u;
}//FinMethod

public void  updateValue() {
	System.out.println("Nothing");
}//FinMethod

}//FinClass
