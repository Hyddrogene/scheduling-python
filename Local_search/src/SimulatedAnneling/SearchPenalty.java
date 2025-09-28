package SimulatedAnneling;

public class SearchPenalty {
	private Constants constants = new Constants();
	public SearchPenalty() {
		
	}//FinMethod
	
	public double search(SolutionHelperPenalty s) {
		if(s.penaltyHard > 0) {
			return (constants.C1 * s.penaltyHard) + (constants.C2  + (s.penaltySoft * (1/1000)) );
		}
		else{
			return  (constants.C2  + (s.penaltySoft * (1/1000)));
		}
	}//FinMethod
	
	public double modifiedPenalty(SolutionHelperPenalty s) {
		//AJOUTE les poids !!!
		//TODO
		return search(s) + s.penalty;
	}//FinMethod
	
	public double fstun(double f, double f0, double gamma) {

			return   1.0 - Math.exp((-1*gamma) * (f - f0));
	}//FinMethod
	
}//FinMethod
