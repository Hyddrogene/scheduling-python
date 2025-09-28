package FeatureModel;


public class Exec {
	
	/***
	 * Le processus est simple, on construit ici des features model avec les 
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        try {
            // Charger le Feature Model depuis un fichier XML
        	
        	String test = "cours-1.xml";
        	
            FeatureModel fm = FeatureModelParser.parseFeatureModel(test);
            
            SubFeatureModel subFM = SubFeatureModelParser.parseSubFeatureModel("SubFeatureModel.xml");
            
            //System.exit(0);
            System.out.println("Feature Model chargé :");
            System.out.println(fm);
            
            
            boolean isValid = fm.isValidConfiguration(subFM);

            // Affichage du résultat
            System.out.println("Le Feature Model " + fm.getName() + 
                (isValid ? " satisfait " : " ne satisfait pas ") + "le sous-Feature Model.");

            // Affichage du résultat

            System.out.println("SubFeature Model chargé :");
            System.out.println(subFM);
            
            String filenameData = "/home/etud/timetabling/src/asp/ua_l1_p1-p2_l3-info_2023.json";
            filenameData = "/home/etud/timetabling/src/asp/ua_l1_p1-p2_l3-info_v2_2023_extension_v2.json";
            
            ConverterJsonChoco g = new ConverterJsonChoco(filenameData);
    		g.CreateInstance();
    		InstanceUTPArray utp  = g.getInstanceUTPArray();
    		
    		test = "/home/etud/eclipse-workspace/FeatureModel/formatModelFeature.xml";
    		
    		FMwithUTP fmutp = new FMwithUTP(test,utp);
    		fm = fmutp.getFeatureModel();
    		
            System.out.println("My Model chargé :");
            System.out.println(fm);
    		
            System.out.println(fm.toXML());
            
            String test2 = fm.toXMLFormat(test);
            

            FeatureModel fm2 = FeatureModelParser.parseFeatureModel(test2);
            
            
            //System.exit(0);
            System.out.println("Feature Model chargé :");
            System.out.println(fm2);
            
            
            String ftrname = "modular";
            String ftrname2 = "resourcing";
            System.out.println("isItActivate ? "+ fm2.isItActivate(ftrname) +" response "+ftrname);
            System.out.println("isItActivate ? "+ fm2.isItActivate(ftrname2) +" response "+ftrname2);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

	}

}
