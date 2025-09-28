package ConstraintCost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

import ParserUTP.ConstraintUTP;
import ParserUTP.InstanceUTPArray;
import SimulatedAnneling.SolutionUTP;

public class Forbidden_Rooms extends ConstraintModel {
	ArrayList<Integer> variables;
	InstanceUTPArray utp;

	public Forbidden_Rooms(ConstraintUTP cutp,InstanceUTPArray utp) {
		super(cutp);
		this.utp = utp;
	}//FinMethod
	
	public int search_value(ConstraintUTP cons,String value) {
		for(int i = 0; i < cons.getParameters().length ;i++) {
			if(this.utp.parameter_name[cons.getParameters()[i]-1].equals(value)) {
				return cons.getParameters()[i]-1;
			}
		}
		return -1;
		
	}//FinMethod
	

	

	@Override
	public int evaluate(SolutionUTP ga) {
		
		int room_chain_num = search_value(cutp,"roomChain");
		Vector<String> rooms = this.utp.parameter_value.get(room_chain_num);
		int[] rooms_id = new int[rooms.size()];
		for(int i = 0; i < rooms_id.length ;i++) {
			rooms_id[i] = Integer.parseInt(rooms.get(i));
		}
		ArrayList<Integer> room = Arrays.stream(rooms_id).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
			int penalties = 0;
			if(this.cutp.getSessions().size() == 1) {
				for(int j = 0 ;j < this.cutp.getSessions().get(0).size()-1 ;j++) {
						if( room.contains(ga.x_slot.get(this.cutp.getSessions().get(0).get(j)-1))  ) {
							penalties += 1;
						}
				}
			}
			// TODO Auto-generated method stub
			return penalties;
		}
}//FinClass
