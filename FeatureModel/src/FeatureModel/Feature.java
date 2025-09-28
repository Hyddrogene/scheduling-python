package FeatureModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une caractéristique d'un Feature Model.
 */
public class Feature {
    private String name;
    private boolean mandatory;
    private String relationType; // "AND" (par défaut), "XOR", "OR"
    private List<Feature> subFeatures;
    private boolean activate;
    private Feature parent;

    /**
     * Constructeur d'une caractéristique.
     * 
     * @param name Nom de la caractéristique
     * @param mandatory Indique si la caractéristique est obligatoire
     */
    public Feature(String name, boolean mandatory, boolean activate) {
        this.name = name;
        this.mandatory = mandatory;
        this.relationType = "AND"; // Par défaut, une sous-caractéristique suit une relation AND
        this.subFeatures = new ArrayList<>();
        this.activate = activate;
        this.parent = null;
    }//FinMethod
    
    public void setParent(Feature prt) {
    	this.parent = prt;
    }//FinMethod

    /**
     * Ajoute une sous-caractéristique à cette caractéristique.
     * 
     * @param feature La sous-caractéristique à ajouter
     */
    public void addSubFeature(Feature feature) {
        this.subFeatures.add(feature);
    }//FinMethod

    /**
     * Définit le type de relation entre les sous-caractéristiques (AND, OR, XOR).
     * 
     * @param relationType Type de relation logique
     */
    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }//FinMethod

    public String getName() { return name; }//FinMethod
    public boolean isMandatory() { return mandatory; }//FinMethod
    public String getRelationType() { return relationType; }//FinMethod
    public List<Feature> getSubFeatures() { return subFeatures; }//FinMethod
    public boolean isActivate() { return this.activate; }//FinMethod
    public void setActivate(boolean val) {this.activate = val; }//FinMethod
    
    /**
     * Active récursivement tous les parents d'une feature.
     * 
     * Si cette feature a un parent, celui-ci est marqué comme activé 
     * (via {@code setActivate(true)}), puis la méthode est appelée
     * récursivement sur ce parent.
     * 
     * Cette méthode permet de garantir que l'activation d'une feature
     * entraîne automatiquement l'activation de ses ancêtres dans la hiérarchie.
     */
    public void activateParent() {
    	//System.out.println("named "+this.name+" parent "+parent);
    	if(this.parent == null) {
    		return;
    	}
    	else {
    		this.parent.setActivate(true);
    		this.parent.activateParent();
    	}
    }//FinMethod
    
    /**
     * Pacejka
     */

    
    
    /**
     * Génère une représentation XML hiérarchique de la feature et de ses sous-features.
     *
     * @param depth le niveau d'indentation actuel, utilisé pour formater correctement le XML
     *              (chaque niveau ajoute 4 espaces).
     * @return une chaîne de caractères contenant le XML correspondant à cette feature.
     *
     * <p>
     * Le format XML varie selon le type de feature :
     * </p>
     * <ul>
     *   <li>Si la feature ne contient aucune sous-feature, elle est représentée par une balise auto-fermante {@code <Feature ... />}.</li>
     *   <li>Si la feature est de type logique ("OR" ou "XOR"), elle est encadrée dans une balise {@code <subFeature type="...">} contenant ses enfants.</li>
     *   <li>Sinon, elle est représentée par une balise {@code <Feature>} contenant un bloc {@code <subFeature>} pour ses enfants.</li>
     * </ul>
     *
     * <p>
     * La feature spéciale nommée {@code "END"} est ignorée et n'apparaît pas dans le XML.
     * </p>
     */
    public String toXML(int depth) {
        StringBuilder res = new StringBuilder();
        String indent = "    ".repeat(depth); // 4 espaces par niveau

        if (this.subFeatures.isEmpty()) {
            if (!name.equals("END")) {
                res.append(indent)
                   .append("<Feature name=\"")
                   .append(name)
                   .append("\" activate=\"")
                   .append(this.activate)
                   .append("\" />\n");
            }
        } else {
            if (name.equals("OR") || name.equals("XOR")) {
                res.append(indent)
                   .append("<subFeature type=\"")
                   .append(name)
                   .append("\">\n");
                for (Feature ftr : this.subFeatures) {
                    res.append(ftr.toXML(depth + 1));
                }
                res.append(indent).append("</subFeature>\n");
            } else {
                res.append(indent)
                   .append("<Feature name=\"")
                   .append(name)
                   .append("\" activate=\"")
                   .append(this.activate)
                   .append("\">\n");

                res.append(indent).append("    <subFeature>\n");
                for (Feature ftr : this.subFeatures) {
                    res.append(ftr.toXML(depth + 2));
                }
                res.append(indent).append("    </subFeature>\n");
                res.append(indent).append("</Feature>\n");
            }
        }

        return res.toString();
    }//FinMethod
    
    
    public boolean isItActivate(String featureName){
    	
    	boolean res = false;
    	
    	for(int i = 0 ; i < this.subFeatures.size() ;i++) {
    		if(this.subFeatures.get(i).getName().equals(featureName)) {
    			return this.subFeatures.get(i).isActivate();
    		}
    		else {
    			
    			if(this.subFeatures.get(i).getSubFeatures().size() > 0) {
    				res = res  || this.subFeatures.get(i).isItActivate(featureName);
    			}
    		}
    	}
    	
    	return res;
    }//FinMethod



    @Override
    public String toString() {
        return "Feature{name='" + name + "', mandatory=" + mandatory + 
               ", relationType='" + relationType + "', subFeatures=" + subFeatures + "}";
    }//FinMethod
}//FinClass
