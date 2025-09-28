package XHSTT14;

import java.util.ArrayList;

public class Constraint14 {

	public String id;
	public String name;
	public String hardness;
	public int weight;
	public String costFunction;
	
	public ArrayList<Event> events;
	public ArrayList<Class14> classes;
	public ArrayList<Teacher> teachers;
	public ArrayList<Room> rooms;
	public ArrayList<String> labels;
	
	public Constraint14(String id, String name, String hardness, int weight, String costFunct) {
		this.id = id;
		this.name = name;
		this.hardness = hardness;
		this.weight = weight;
		this.costFunction = costFunct;
	}//FinMethod

	public void setClasses(ArrayList<Class14> classes) {
		this.classes = classes;
	}//FinMethod

	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}//FinMethod

	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}//FinMethod

	public void setTeachers(ArrayList<Teacher> teachers) {
		this.teachers = teachers;
	}//FinMethod
	
	
	

}//FinClass
