package XHSTT14;

import java.util.ArrayList;

public class Event {
	public String id;
	public String name;
	public int duration;
	public Course course;
	public ArrayList<Room> rooms;
	public ArrayList<Teacher> teachers;
	public ArrayList<Class14> classes;
	
	public ArrayList<String> labels;
	
	public Event(String id, String name, int duration, Course course) {
		this.id = id;
		this.duration = duration;
		this.name = name;
		this.course = course;
		
		
		this.rooms = new ArrayList<>();
		this.classes = new ArrayList<>();
		this.teachers = new ArrayList<>();
		
	}//FinMethod

	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}//FinMethod

	public void setTeachers(ArrayList<Teacher> teachers) {
		this.teachers = teachers;
	}//FinMethod

	public void setClasses(ArrayList<Class14> classes) {
		this.classes = classes;
	}//FinMethod
	
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}//FinMethod

	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", duration=" + duration + ", course=" + course.id + ", rooms="
				+ rooms.stream().map(u->u.id).toList() + ", teachers=" + teachers.stream().map(u->u.id).toList() + ", classes=" + classes.stream().map(u->u.id).toList() 
				+ "]\n";
	}
	
	

}//FinClass
