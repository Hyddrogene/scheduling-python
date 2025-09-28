package GA;

import java.util.ArrayList;

public class SessionRooms {
	
	public int room;
	public int nr_rooms;
	public ArrayList<Integer> rooms;
	
	public SessionRooms(int nr_rooms ){
		this.nr_rooms = nr_rooms;
		room = -1;
		rooms = new ArrayList<Integer>();
	}//FinMethod
	
	public SessionRooms(){
		
	}//FinMethod

	
	public SessionRooms(SessionRooms sr){
		this.room = sr.room;
		this.nr_rooms = sr.nr_rooms;
		rooms = new ArrayList<Integer>();
		
		if(sr.rooms.size()>0) {
			for(int i =0; i < sr.rooms.size() ;i++) {
				rooms.add(sr.rooms.get(i).intValue());
			}
		}

	}//FinMethod
	


}//FinClass
