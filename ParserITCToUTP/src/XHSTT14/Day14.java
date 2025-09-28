package XHSTT14;

import java.util.ArrayList;

public class Day14 {
	public String name;
	public int count; 
	public String id;
	
	public ArrayList<Time14> times = new ArrayList<Time14>(); 
	
	
	public Day14(String name, String id, int count) {
		this.name = name;
		this.id = id;
		this.count = count;
	}//finMethod

	@Override
	public String toString() {
		return "Day14 [name=" + name + ", count=" + count + ", id=" + id + ", times=" + times + "]";
	}
	
	public void addTimes(Time14 t14) {
		times.add(t14);
	}//FinMethod
	
}//FinClass
