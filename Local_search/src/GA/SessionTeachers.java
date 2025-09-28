package GA;

import java.util.ArrayList;

public class SessionTeachers {
	public int teacher;
	public int nr_teachers;
	public ArrayList<Integer> teachers;
	
	public SessionTeachers(int nr_teachers ){
		this.nr_teachers = nr_teachers;
		teacher = -1;
		teachers = new ArrayList<Integer>();
	}//FinMethod
	
	public SessionTeachers(){
		
	}//FinMethod

	
	public SessionTeachers(SessionTeachers sr){
		this.teacher = sr.teacher;
		this.nr_teachers = sr.nr_teachers;
		teachers = new ArrayList<Integer>();
		
		if(sr.teachers.size()>0) {
			for(int i =0; i < sr.teachers.size() ;i++) {
				teachers.add(sr.teachers.get(i).intValue());
			}
		}

	}//FinMethod
}//FinClass
