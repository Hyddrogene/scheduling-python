package XHSTT14;

import java.util.ArrayList;

public class Teacher {
	public String id;
	public String name;
	public String type;
	public ArrayList<String> labels;
	
	public Teacher(String id,String name, String type,ArrayList<String> labels) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.labels = labels;
	}//FinMethod
	
}//FinClass
