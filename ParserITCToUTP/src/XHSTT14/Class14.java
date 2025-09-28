package XHSTT14;

import java.util.ArrayList;

public class Class14 {
	public int effectif;
	public String id;
	public String name;
	public String type;
	public ArrayList<String> labels;
	
	public Class14(String id,String name, String type,ArrayList<String> labels) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.labels = labels;
		this.effectif = 1;
	}//FinMethod
	
	public Class14(String id,String name, String type,int effectif,ArrayList<String> labels) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.labels = labels;
		this.effectif = effectif;
	}//FinMethod
}//FinClass
