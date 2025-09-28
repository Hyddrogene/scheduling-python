package XHSTT14;

public class Time14 {
	public String name;
	public int count; 
	public String id;
	public Day14 refDay;
	public Week14 refWeek;
	
	public Time14(String name, String id, int count) {
		this.name = name;
		this.id = id;
		this.count = count;
	}//finMethod
	
	public void setRefDay(Day14 refDay) {
		this.refDay = refDay;
	}//FinMethod
	
	public void setRefWeek(Week14 refWeek) {
		this.refWeek = refWeek;
	}//FinMethod

	@Override
	public String toString() {
		return "Time14 [name=" + name + ", count=" + count + ", id=" + id + ", refDay=" + refDay.id + ", refWeek=" + refWeek.id + "]";
	}
	
	
}//FinClass
