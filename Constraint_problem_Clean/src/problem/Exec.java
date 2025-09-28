package problem;

public class Exec {

	public static void main(String[] args) {
		/*problem_poupee pp = new problem_poupee();
		pp.solve();
		
		Problem_poupee_bool pp2 = new Problem_poupee_bool();
		pp2.solve();
		
		//Problem_calcul_denfer pce = new Problem_calcul_denfer();
		//pce.solve();
		
		Problem_calcul_denfer2 pce2 = new Problem_calcul_denfer2();
		pce2.solve();
		
		Problem_Triangle pt = new Problem_Triangle();
		pt.solve();
		
		Problem_concert pc = new Problem_concert();
		pc.solve();
		*/
		/*
		 *  1--2  3  4--5
			|        |  |
			6  7  8--9 10
			            |
			11 12 13 14 15
			
			16 17 18 19 20
			
			21 22 23 24 25
		 * */
		String filename = "/home/etud/exemple-monorail4x4.txt";
		filename = "/home/etud/exemple-monorail.txt";
		filename = "/home/etud/exemple-monorail6x6.txt";
		ReadMonorailInstance rmi = new ReadMonorailInstance(filename);
		
	}//FinMethod

}//FinClass
