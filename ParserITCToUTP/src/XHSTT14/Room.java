package XHSTT14;

import java.util.ArrayList;

public class Room {
	public int capacity;
	public String id;
	public String name;
	public String type;
	public ArrayList<String> labels;
	
	public Room(String id,String name, String type,ArrayList<String> labels) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.labels = labels;
	}//FinMethod
	
	public Room(String id,String name, String type,int capacity,ArrayList<String> labels) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.labels = labels;
		this.capacity = capacity;
	}//FinMethod
	
}//FinClass
