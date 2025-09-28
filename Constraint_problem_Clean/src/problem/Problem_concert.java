package problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/*Publication : Dell Logic Puzzles
Auteur : Eliot George
Numéro : Avril 1998
Page : 10
Niveau de difficulté : ★★☆☆☆ (2 étoiles)

Contexte :
Tim et Keri ont une journée bien remplie devant eux : ils veulent absolument tout voir et tout entendre au Tunapalooza ‘98,
 le concert annuel organisé au profit des thons dans leur ville natale.
Pour en profiter au maximum, ils décident de se séparer, mais ont prévu de se retrouver pendant quatre concerts de groupes de rock,
 À chacun de ces concerts, ils se retrouveront à un point de rendez-vous parmi,
 Chaque groupe joue un genre musical unique parmi :

Objectif :
Associer à chaque groupe son style musical et le lieu de rendez-vous correspondant.
Indices :

    1. Korrupt ne joue ni de la country ni du grunge.

    2. Tim et Keri ne se retrouvent pas aux jeux de foire pendant le concert de Ellyfish.

    3. Ils ne se retrouvent pas au vendeur de T-shirts pendant le concert du groupe reggae.

    4. Exactement deux des trois affirmations suivantes sont vraies :
    	a) Ellyfish joue du grunge.
    	b) Ils ne se retrouvent pas au stand d’information pendant le concert de Retread Ed and the Flat Tires.
    	c) Ils ne se retrouvent pas au vendeur de T-shirts pendant que Yellow Reef joue.

    5. Les concerts de country et de speed metal sont, dans un ordre ou l’autre :

        a) soit celui de Retread Ed and the Flat Tires

        b) soit celui où le rendez-vous est à la fosse.

    6. Le groupe de reggae n’est ni Korrupt ni celui associé au stand d’information.
    
   Solution : 
	groupes : Ellyfish, rdv : T-shirt vendor, type : grunge
	groupes : Korrupt, rdv : mosh pit, type : speed metal
	groupes : Retread Ed and the Flat Tires, rdv : information booth, type : country
	groupes : Yellow Reef, rdv : carnival games, type : reggae
*/


/**
 * Modélise et résout le puzzle logique "Tunapalooza" à l'aide de Choco Solver.
 * 
 * Cette classe instancie les variables, applique les contraintes logiques
 * extraites des indices du problème, et explore toutes les solutions possibles.
 */
public class Problem_concert {

	
	// Name
	protected String[] groupes = new String[] {"Ellyfish" , "Korrupt" , "Retread Ed and the Flat Tires" , "Yellow Reef"};
	protected String[] rdv = new String[] {"carnival games" , "information booth" , "mosh pit" , "T-shirt vendor"};
	protected String[] type = new String[] {"country" , "grunge" , "reggae" , "speed metal"};

	
    // Maps inversés
    protected HashMap<String, Integer> groupesMap;
    protected HashMap<String, Integer> rdvMap;
    protected HashMap<String, Integer> typeMap;
    
    //Model choco
	protected Model model;

    //Variables
	protected IntVar[] x_type;
	protected IntVar[] x_rdv;
	
	// Variables maps
	protected HashMap<String,IntVar> x_name_rdv;
	protected HashMap<String,IntVar> x_name_type;
	

	/**
	 * Constructeur principal du problème "Tunapalooza".
	 * 
	 * Initialise le modèle ChocoSolver, les mappings symboliques (groupes, lieux, types),
	 * les variables de décision et les contraintes du problème logique.
	 */
	public Problem_concert() {
		model = new Model("Tunalalooza puzzle (Dell Logic Puzzles)");
		tunapaloozaHelper();
		variables();
		constraints();
	}//FinMethod
	
	
    /**
     * Initialise les tables associant chaque nom (groupe, lieu de RDV, type de musique)
     * à un entier unique (entre 1 et 4).  
     * Ces mappings sont utilisés pour simplifier la modélisation des contraintes dans ChocoSolver.
     */
    public void tunapaloozaHelper() {
        groupesMap = new HashMap<>();
        rdvMap = new HashMap<>();
        typeMap = new HashMap<>();

        // Associe chaque groupe à un entier (1 à 4)
        IntStream.range(0, groupes.length).forEach(i -> groupesMap.put(groupes[i], i + 1));
        
        // Associe chaque lieu de RDV à un entier (1 à 4)
        IntStream.range(0, rdv.length).forEach(i -> rdvMap.put(rdv[i], i + 1));
        
        // Associe chaque type de musique à un entier (1 à 4)
        IntStream.range(0, type.length).forEach(i -> typeMap.put(type[i], i + 1));
    }//FinMethod

	
    
    /**
     * Initialise les variables de décision pour le modèle Choco.
     * 
     * Pour chaque groupe musical, crée deux variables :
     * <ul>
     *   <li>Une variable représentant le type de musique joué (de 1 à 4)</li>
     *   <li>Une variable représentant le lieu de rendez-vous (de 1 à 4)</li>
     * </ul>
     * 
     * Les variables sont stockées :
     * <ul>
     *   <li>Dans les tableaux {@code x_type} et {@code x_rdv} pour un accès indexé</li>
     *   <li>Dans les maps {@code x_name_type} et {@code x_name_rdv} pour un accès par nom de groupe</li>
     * </ul>
    */
    public void variables() {
        
        // Listes temporaires pour stocker les variables de type et de lieu de rendez-vous
        ArrayList<IntVar> tmp_x_type = new ArrayList<>();
        ArrayList<IntVar> tmp_x_rdv = new ArrayList<>(); 

        // Dictionnaires permettant d’associer un nom de groupe à sa variable de type et de rendez-vous
        x_name_rdv = new HashMap<>();
        x_name_type = new HashMap<>(); 
        
        for (int i = 0; i < this.groupes.length; i++) {
            // Création d'une variable représentant le lieu de rendez-vous (entre 1 et 4)
            IntVar var1 = model.intVar("rdv_" + this.groupes[i], 1, 4);

            // Création d'une variable représentant le type de musique (entre 1 et 4)
            IntVar var2 = model.intVar("type_" + this.groupes[i], 1, 4);

            // Ajout des variables aux listes temporaires
            tmp_x_type.add(var2);
            tmp_x_rdv.add(var1);
            
            // Association du nom du groupe à ses variables respectives
            x_name_rdv.put(this.groupes[i], var1);
            x_name_type.put(this.groupes[i], var2);
        }

        // Conversion des listes temporaires en tableaux
        x_type = tmp_x_type.toArray(IntVar[]::new);
        x_rdv = tmp_x_rdv.toArray(IntVar[]::new);

    }//FinMethod
    
    
    /**
     * Définit toutes les contraintes logiques du puzzle Tunapalooza.
     *
     * Implémente les 6 indices fournis dans l'énigme :
     * exclusions de genres et de lieux, contraintes conditionnelles,
     * logique booléenne (exactement 2 affirmations vraies), et contraintes globales.
     */
	public void constraints() {
		
	    //1. Korrupt ne joue ni de la country ni du grunge.
		
		model.arithm(this.x_name_type.get("Korrupt"),"!=",this.typeMap.get("country")).post();
		model.arithm(this.x_name_type.get("Korrupt"),"!=",this.typeMap.get("grunge")).post();

	    //2. Tim et Keri ne se retrouvent pas aux jeux de foire pendant le concert de Ellyfish.
		
		this.x_name_rdv.get("Ellyfish").ne(this.rdvMap.get("carnival games")).post();;

	    //3. Ils ne se retrouvent pas au vendeur de T-shirts pendant le concert du groupe reggae.
		model.ifThen(model.intEqView(x_type[0], this.typeMap.get("reggae")), model.arithm(x_rdv[0], "!=", this.rdvMap.get("T-shirt vendor") ));
		model.ifThen(model.intEqView(x_type[1], this.typeMap.get("reggae")), model.arithm(x_rdv[1], "!=", this.rdvMap.get("T-shirt vendor") ));
		model.ifThen(model.intEqView(x_type[2], this.typeMap.get("reggae")), model.arithm(x_rdv[2], "!=", this.rdvMap.get("T-shirt vendor") ));
		model.ifThen(model.intEqView(x_type[3], this.typeMap.get("reggae")), model.arithm(x_rdv[3], "!=", this.rdvMap.get("T-shirt vendor") ));
		
		
	    //4. Exactement deux des trois affirmations suivantes sont vraies :
	    //	a) Ellyfish joue du grunge.
	    //	b) Ils ne se retrouvent pas au stand d’information pendant le concert de Retread Ed and the Flat Tires.
	    //	c) Ils ne se retrouvent pas au vendeur de T-shirts pendant que Yellow Reef joue.
		

		// 3 booléens pour les 3 affirmations
		BoolVar[] truth = new BoolVar[3];
		for (int i = 0; i < 3; i++) {
		    truth[i] = model.boolVar("truth_" + i);
		}
		// (a) Ellyfish joue du grunge
		model.reifyXeqY(this.x_name_type.get("Ellyfish"), model.intVar(this.typeMap.get("grunge")),truth[0].boolVar());

		// (b) Ils ne se retrouvent pas au stand d'information pendant Retread Ed
		model.reifyXneY(this.x_name_rdv.get("Retread Ed and the Flat Tires"), model.intVar(this.rdvMap.get("information booth")),truth[1].boolVar());

		// (c) Ils ne se retrouvent pas au vendeur de T-shirts pendant Yellow Reef
		model.reifyXneY(this.x_name_rdv.get("Yellow Reef"), model.intVar(this.rdvMap.get("T-shirt vendor")),truth[2].boolVar() );

		// Contraindre à exactement 2 affirmations vraies
		//model.sum(truth, "=", 2).post();
		model.globalCardinality(truth,new int[]{0,1}  ,new IntVar[] {model.intVar(1),model.intVar(2)} , true).post();


	    //5. Les concerts de country et de speed metal sont, dans un ordre ou l’autre :
	    //	(a) soit celui de Retread Ed and the Flat Tire.
	    //	(b) soit celui où le rendez-vous est à la fosse.
		
		model.member(this.x_name_type.get("Retread Ed and the Flat Tires"), 
				new int[] {this.typeMap.get("country"),this.typeMap.get("speed metal")}).post();;
				
				
		model.ifThen(model.intEqView(x_rdv[0], this.rdvMap.get("mosh pit")), model.member(x_type[0], new int[] {this.typeMap.get("country"),this.typeMap.get("speed metal")}) );
		model.ifThen(model.intEqView(x_rdv[1], this.rdvMap.get("mosh pit")), model.member(x_type[1], new int[] {this.typeMap.get("country"),this.typeMap.get("speed metal")}) );
		model.ifThen(model.intEqView(x_rdv[2], this.rdvMap.get("mosh pit")), model.member(x_type[2], new int[] {this.typeMap.get("country"),this.typeMap.get("speed metal")}) );
		model.ifThen(model.intEqView(x_rdv[3], this.rdvMap.get("mosh pit")), model.member(x_type[3], new int[] {this.typeMap.get("country"),this.typeMap.get("speed metal")}) );
		


	    //6. Le groupe de reggae n’est ni Korrupt ni celui associé au stand d’information.
		
		this.x_name_type.get("Korrupt").ne(this.typeMap.get("reggae")).post();;

		model.ifThen(model.intEqView(x_type[0], this.typeMap.get("reggae")), model.arithm(x_rdv[0], "!=", this.rdvMap.get("information booth")) );
		model.ifThen(model.intEqView(x_type[1], this.typeMap.get("reggae")), model.arithm(x_rdv[1], "!=", this.rdvMap.get("information booth")) );
		model.ifThen(model.intEqView(x_type[2], this.typeMap.get("reggae")), model.arithm(x_rdv[2], "!=", this.rdvMap.get("information booth")) );
		model.ifThen(model.intEqView(x_type[3], this.typeMap.get("reggae")), model.arithm(x_rdv[3], "!=", this.rdvMap.get("information booth")) );

		// Contraintes d’unicité (chaque groupe a un genre et un lieu unique)
		model.allDifferent(x_rdv).post();
		model.allDifferent(x_type).post();
		
	}//FinMethod
	
	/**
	 * Résout le problème de logique "Tunapalooza" et affiche toutes les solutions possibles.
	 * 
	 * Pour chaque solution, affiche les groupes, leur lieu de rendez-vous, et leur genre musical.
	 * Affiche également les statistiques de résolution (nombre de solutions, backtracks, etc.).
	 */
	public void solve() {
	    ArrayList<Solution> solutions = new ArrayList<>();
	    Solver solver = model.getSolver();
	    
	    // Recherche de toutes les solutions possibles
	    while (solver.solve()) {
	        solutions.add(new Solution(model).record());
	    }
	   
	    // Affichage formaté de chaque solution trouvée
	    int count = 1;
		System.out.println("========= Solution ===========");
	    for(Solution sol : solutions) {
	        System.out.printf("Solution %d \n", count);
	        for(int i = 0; i < groupes.length; i++) {
	            System.out.println(
	                "groupes : " + groupes[i]
	                + ", rdv : " + rdv[sol.getIntVal(x_name_rdv.get(groupes[i])) - 1]
	                + ", type : " + type[sol.getIntVal(x_name_type.get(groupes[i])) - 1]
	            );
	        }
	        count++;
	    }
	    // Affiche les statistiques de résolution (temps, backtracks, etc.)
		System.out.println("========= Statistics ===========");
	    solver.printStatistics();
	}//FinMethod

}//FinClass
