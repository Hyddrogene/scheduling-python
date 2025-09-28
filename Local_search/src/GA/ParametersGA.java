package GA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParametersGA {
	
	private JSONParser parser;
	private File file;
	private JSONObject jsonObject;
	
	public int nr_iterations;
	public int population_size;
	public double taux_mutations;
	public double bitflip_taux;
	
	
	public ParametersGA(String jf) {
		this.parser = new JSONParser();
		this.file = new File(jf);
		readJsonFile();
	}//FinMethod
	
	public ParametersGA(int nr_iterations, int population_size, double taux_mutations, double bitflip_taux) {
		this.nr_iterations = nr_iterations;
		this.population_size = population_size;
		this.taux_mutations = taux_mutations;
		this.bitflip_taux = bitflip_taux;
	}//FinMethod

	public void readJsonFile() {
		try {
			 this.jsonObject = (JSONObject) this.parser.parse(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}//FinMethod
	
}//FinClass
