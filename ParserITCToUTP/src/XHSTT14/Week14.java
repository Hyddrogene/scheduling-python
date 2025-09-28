package XHSTT14;

import java.util.ArrayList;

public class Week14 {

	public String name;
	public int count; 
	public String id;
	
	
	public ArrayList<Time14> times;
	public ArrayList<Day14> days;
	
	public Week14(String name, String id, int count) {
		this.name = name;
		this.id = id;
		this.count = count;
		
		this.times = new ArrayList<Time14>();
		this.days = new ArrayList<Day14>();
	}//finMethod
	
	public void addTime(Time14 t14 ) {
		this.times.add(t14);
		this.days.add(t14.refDay);
	}//FinMethod

	@Override
	public String toString() {
		return "Week14 [name=" + name + ", count=" + count + ", id=" + id + ", times=" + times + ", days=" + days + "]";
	}//FinMethod
	
	
}//FinClass
