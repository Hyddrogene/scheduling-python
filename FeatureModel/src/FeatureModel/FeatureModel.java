package FeatureModel;

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Représente un modèle de features (Feature Model) hiérarchique.
 * 
 * Un Feature Model permet de structurer, organiser et activer dynamiquement
 * des fonctionnalités (features) selon leurs dépendances ou regroupements.
 * 
 * Ce modèle contient un nom (identifiant du modèle) et une liste de features
 * racines, qui peuvent elles-mêmes contenir des sous-features organisées de
 * manière arborescente (ex. : mamifère, oiseau, reptile).
 * 
 * Cette classe fournit également les méthodes d'accès aux données principales
 * du modèle.
 * 
 * @see Feature pour la définition individuelle d’une feature.
 */
public class FeatureModel {
    private String name;
    private List<Feature> features;

    /**
     * Constructeur d'un Feature Model.
     * 
     * @param name Nom du Feature Model
     * @param features Liste des caractéristiques principales
     */
    public FeatureModel(String name, List<Feature> features) {
        this.name = name;
        this.features = features;
    }

    public String getName() { return name; }
    public List<Feature> getFeatures() { return features; }
    

    
    /**
     * Vérifie si toutes les fonctionnalités requises d'un `SubFeatureModel` sont présentes.
     * 
     * @param subFM Sous-Feature Model contenant les fonctionnalités requises
     * @return true si le FeatureModel contient toutes les fonctionnalités demandées, false sinon
     */
    public boolean isValidConfiguration(SubFeatureModel subFM) {
        // Récupération de toutes les fonctionnalités disponibles dans le FeatureModel
        ArrayList<Feature> availableFeatures = new ArrayList<>();
        for (Feature feature : features) {
            collectFeatures(feature, availableFeatures);
        }
        
        
        return subFM.getRequiredFeatures().stream()
                .allMatch(ftrName -> availableFeatures.stream()
                        .anyMatch(feature -> feature.getName().equals(ftrName) && feature.isActivate()));
        
    }//FinMethod
    


    /**
     * Fonction récursive qui collecte toutes les fonctionnalités disponibles.
     * 
     * @param feature Fonctionnalité à explorer
     * @param featureSet Ensemble dans lequel on stocke toutes les fonctionnalités disponibles
     */
    private void collectFeatures(Feature feature, ArrayList<Feature> featureSet) {
        featureSet.add(feature); // Ajouter la fonctionnalité actuelle
        if(feature.getSubFeatures().isEmpty()) {
        	return;
        }
        for (Feature subFeature : feature.getSubFeatures()) {
            collectFeatures(subFeature, featureSet); // Explorer récursivement les sous-fonctionnalités
        }
    }//FinMethod

    @Override
    public String toString() {
    	if (features.isEmpty()) {
            return name + "{}"; // Gère le cas où il n'y a aucune feature
        }
        
        // Démarre l'affichage des features activées à partir de la racine
        String activeFeatures = getActiveFeatures(features.get(0));

        return name + "{" + activeFeatures + "}";
    }

    /**
     * Explore récursivement les features pour récupérer celles qui sont activées.
     * @param feature La feature à explorer
     * @return Une chaîne contenant les noms des features activées et de leurs sous-features activées
     */
    private String getActiveFeatures(Feature feature) {
    	
    	//System.out.println("Ftr : "+feature.getName());
        if (!feature.isActivate()) {
            return ""; // Ignore les features désactivées
        }

        // Vérifie si la feature n'a pas de sous-features activées
        List<Feature> activeSubFeatures = feature.getSubFeatures().stream()
                .filter(Feature::isActivate) // Ne garde que les activées
                .toList(); // Optimisation avec toList() pour éviter plusieurs parcours

        // Si aucun sous-feature activé, retourne juste le nom de la feature
       /* if (activeSubFeatures.isEmpty()) {
            return feature.getName();
        }*/

        
        ArrayList<String> results = new ArrayList<String>();
        for(Feature ftr : activeSubFeatures ){
        	//System.out.println("ftr parent"+feature.getName()+" ss-ftr "+ftr.getName()+" size "+activeSubFeatures.size());
        	if(ftr.getSubFeatures().size() > 0) {
        		results.add(this.getActiveFeatures(ftr));
        	}
        	else {results.add(ftr.getName());}
        }
        
        
        
        // Récupère récursivement les sous-features activées
        // Si la feature a des sous-features activées, applique la récursion
        return feature.getName() + " (" +
                results.stream()//.filter(f -> f.getSubFeatures().size() > 0)
                        .collect(Collectors.joining(", ")) +
                ")";

        // Retourne la feature avec ses sous-features activées
       // return feature.getName() + " (" + subFeatures + ")";
    }//FinMethod
    
   
    /**
     * Active une feature donnée dans le modèle à partir de son nom.
     * Si la feature est trouvée dans la liste racine, elle est activée ainsi que ses parents récursivement.
     * Sinon, la recherche se poursuit récursivement dans les sous-features.
     *
     * @param featureName le nom de la feature à activer.
     */
   private void findAndActivatePrvt(String featureName, List<Feature> featuresPvrt) {
   	boolean notFoundAtRoot = true;
   	
 // Recherche de la feature dans la racine du modèle
   	for(Feature ftr : featuresPvrt) {
   		if(ftr.getName().equals(featureName)) {
   			ftr.setActivate(true);
   			ftr.activateParent();// Activation récursive des parents
   			notFoundAtRoot = false;
   		}
   	}
    // Si la feature n'est pas trouvée à la racine, on cherche récursivement dans les sous-features
   	if(notFoundAtRoot) {
   		for(Feature ftr : featuresPvrt) {
   			findAndActivatePrvt(featureName,ftr.getSubFeatures());
   		}
   	}
   }//FinMethod
      
   /**
    * Active une feature donnée dans le modèle à partir de son nom.
    * Si la feature est trouvée dans la liste racine, elle est activée ainsi que ses parents récursivement.
    * Sinon, la recherche se poursuit récursivement dans les sous-features.
    *
    * @param featureName le nom de la feature à activer.
    */
    public void findAndActivate(String featureName) {
    	boolean nope = true;
    	for(Feature ftr : this.features) {
    		if(ftr.getName().equals(featureName)) {
    			ftr.setActivate(true);
    			ftr.activateParent();
    			nope = false;
    		}
    	}
    	if(nope) {
    		for(Feature ftr : this.features) {
    			findAndActivatePrvt(featureName,ftr.getSubFeatures());
    		}
    	}
    	
    }//FinMethod

    /**
     * Génère la représentation XML du modèle de features.
     *
     * @return une chaîne XML représentant la structure complète du modèle.
     */
    public String toXML() {
    	// Pour le moment on suppose qu'il n'y qu'une seule racine dans la liste
    	return this.getFeatures().get(0).toXML(0);
    }//FinMethod
    
    
    /**
     * Génère un nom de fichier en y ajoutant un timestamp, tout en conservant l'extension.
     *
     * @param dataFile le nom de base du fichier (avec ou sans extension).
     * @return un nom de fichier unique avec date et heure (format yy_MM_dd_HH_mmss).
     */
    private String namedFile(String dataFile) {

        // Séparation du nom et de l’extension
        String[] parts = dataFile.split("\\.");
        String baseName = parts[0];
        String extension = parts.length > 1 ? parts[1] : "xml";

        // Format de date : yy_MM_dd_HH_mmss
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy_MM_dd_HH_mmss");
        String timestamp = now.format(formatter);

        // Construction du nouveau nom
        String newFileName = baseName + "_" + timestamp + "." + extension;

        return newFileName;
    }//FinMethod
    
    /**
     * Génère un fichier XML conforme au schéma XSD du Feature Model,
     * en y intégrant la structure actuelle du modèle de features.
     *
     * @param dataFile le nom de fichier de base (ex. : "features.xml").
     * @return le nom du fichier généré (avec timestamp intégré).
     */
    public String toXMLFormat(String dataFile) {
        StringBuilder res = new StringBuilder();
        
        // En-tête XML + début de la balise <FeatureModel>
       res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
       .append("<FeatureModel name=\"feature model for UTP\" ")
       .append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
       .append("xsi:noNamespaceSchemaLocation=\"featureModel-schema.xsd\">\n")
       .append("<Features>\n");

	    // Ajout des features en XML
	    res.append(toXML());
	
	    // Fermeture des balises XML
	    res.append("</Features>\n</FeatureModel>");

       // Génération du nom de fichier avec timestamp
       String name = namedFile(dataFile);
       
       // Écriture dans le fichier       
       try (FileWriter writer = new FileWriter(name)) {
           writer.write(res.toString());
           System.out.println("==== Feature model généré ====");
           System.out.println("Filename : "+namedFile(dataFile));
       } catch (IOException e) {
           System.err.println("Erreur lors de l'écriture du feature model: " + e.getMessage());
       }
       return name; 
       
    }//FinMethod
    
    public boolean isItActivate(String featureName){
    	
    	boolean res = false;
    	
    	for(int i = 0 ; i < this.features.size() ;i++) {
    		if(this.features.get(i).getName().equals(featureName)) {
    			res = res || this.features.get(i).isActivate();
    		}
    		else {
    			
    			if(this.features.get(i).getSubFeatures().size() > 0) {
    				res = res  || this.features.get(i).isItActivate(featureName);
    			}
    		}
    	}
    	
    	return res;
    }//FinMethod

    


}//FinClass
