package FeatureModel;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse un fichier XML et construit un Feature Model en objets Java.
 */
public class FeatureModelParser {

    /**
     * Charge un Feature Model depuis un fichier XML.
     * 
     * @param filePath Chemin du fichier XML
     * @return Un objet FeatureModel
     * @throws Exception Si une erreur de parsing survient
     */
    public static FeatureModel parseFeatureModel(String filePath) throws Exception {
        File file = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        // Lire le nom du Feature Model
        String name = document.getDocumentElement().getAttribute("name");

        // Récupérer la liste des fonctionnalités principales
        NodeList featureNodes = document.getElementsByTagName("Feature");
        List<Feature> features = new ArrayList<>();

        // Parcours des fonctionnalités principales
         
        
        for (int i = 0; i < featureNodes.getLength(); i++) {
            Node node = featureNodes.item(i);
            
            Element element = (Element) node;
            String name2 = element.getAttribute("name");
            boolean mandatory = Boolean.parseBoolean(element.getAttribute("mandatory"));
            boolean activate = Boolean.parseBoolean(element.getAttribute("activate"));
            // Création de l'objet Feature
            Feature feature = new Feature(name2, mandatory, activate);
            
            if (node.getParentNode().getNodeName().equals("Features")) {
                features.add(parseFeature(node,feature,node.getParentNode()));
            }
        }
        //System.out.println("==== : "+" SIZE :"+features.size());
        return new FeatureModel(name, features);
    }//FinMethod

    //======================================================================
    
    
    public String ftrWord = "Feature";
    public String subFtrWord = "subFeature";
    
    
    

    
    /**
     * Récupère uniquement les enfants directs d'un nœud donné avec un tag spécifique.
     *
     * @param parentNode Nœud parent dans lequel chercher les enfants directs.
     * @param tagName    Nom du tag des enfants à récupérer.
     * @return NodeList contenant uniquement les enfants directs correspondants.
     */
    public static NodeList getDirectChildrenByTagName(Element parentNode, String tagName) {
        NodeList childNodes = parentNode.getChildNodes();
        Document doc = parentNode.getOwnerDocument();
        Element tempParent = doc.createElement("tempParent");

        // Ajouter uniquement les enfants directs de type `tagName`
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE
                    && tagName.equals(child.getNodeName())
                    && child.getParentNode().isSameNode(parentNode)) {
                tempParent.appendChild(child.cloneNode(true));
            }
        }

        return tempParent.getChildNodes(); // Retourne une liste des enfants directs filtrés
    }//Finmethod
    
    
    
    
    
    int op = 0;
    
    private static Feature parseFeature(Node node, Feature ftrParent,Node parentNode) {
    	
    	
    	Element element = (Element) node;
    
    	
    	//System.out.println("Node++ "+node.getNodeName());
    	//System.out.println("Node++ "+node.getChildNodes().getLength());
    	
    	
        String name = element.getAttribute("name");
        boolean mandatory = Boolean.parseBoolean(element.getAttribute("mandatory"));
        boolean activate = Boolean.parseBoolean(element.getAttribute("activate"));
        Feature feature = new Feature(name, mandatory, activate);
        
        if(element.getNodeName().equals("subFeature") && element.hasAttribute("type") ) {
        	feature = new Feature(element.getAttribute("type"), mandatory, ftrParent.isActivate());
        }
        
        
    	
    	if(element.getNodeName().equals("Feature") ||  element.hasAttribute("type")) {

        	NodeList childNodes = node.getChildNodes();
        	
        	/*if(childNodes.getLength() == 0) {
        		return feature;
        	}*/
    		
    		for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                
            	if(child.getNodeName().equals("Feature") || child.getNodeName().equals("subFeature")) {
            		if( element.hasAttribute("type")) {
            			
                		Feature ff = parseFeature(child,feature,node);
                		feature.addSubFeature(ff);
                		
            		}
            		else{
                		Feature ff = parseFeature(child,feature,node);
                		feature.addSubFeature(ff);
            		}
            		
            	}
                
            }
    		feature.setParent(ftrParent);
    		return feature;
    		
    	}
    	else { if(element.getNodeName().equals("subFeature") && !element.hasAttribute("type") ) {
    		//System.exit(0);
        	NodeList childNodes = node.getChildNodes();
        	
    		
    		for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                
            	if(child.getNodeName().equals("Feature") || child.getNodeName().equals("subFeature")) {
            		
            		Feature ff = parseFeature(child,ftrParent,parentNode);
            		ftrParent.addSubFeature(ff);
            		
            	}
                
            }
    		
    		}	

        Feature ftrtmp = new Feature("END", true, false);
    		return ftrtmp;
    	
    	}
    	
  
        

    }//FinMethod

    
    
    
    
    
    //%%%%%%%%%%%%%%%%%%%%%%
    
    
    public static FeatureModel parseFeatureModelFalse(String filePath) throws Exception {
        File file = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        // Lire le nom du Feature Model
        String name = document.getDocumentElement().getAttribute("name");

        // Récupérer la liste des fonctionnalités principales
        NodeList featureNodes = document.getElementsByTagName("Feature");
        List<Feature> features = new ArrayList<>();

        // Parcours des fonctionnalités principales
        //System.out.println("NAME : "+name+" SIZE :"+featureNodes.getLength());
         
        
        for (int i = 0; i < featureNodes.getLength(); i++) {
            Node node = featureNodes.item(i);
            
            Element element = (Element) node;
            String name2 = element.getAttribute("name");
            boolean mandatory = Boolean.parseBoolean(element.getAttribute("mandatory"));
            boolean activate = true;
            // Création de l'objet Feature
            Feature feature = new Feature(name2, mandatory, activate);
            
            if (node.getParentNode().getNodeName().equals("Features")) {
                features.add(parseFeatureFalse(node,feature,node.getParentNode()));
            }
        }
        //System.out.println("==== : "+" SIZE :"+features.size());
        return new FeatureModel(name, features);
    }//FinMethod
    
    
    
    
    
    
    private static Feature parseFeatureFalse(Node node, Feature ftrParent,Node parentNode) {
    	
    	
    	Element element = (Element) node;
    
    	
    	//System.out.println("Node++ "+node.getNodeName());
    	//System.out.println("Node++ "+node.getChildNodes().getLength());
    	
    	
        String name = element.getAttribute("name");
        boolean mandatory = Boolean.parseBoolean(element.getAttribute("mandatory"));
        boolean activate = false;
        Feature feature = new Feature(name, mandatory, activate);
        
        if(element.getNodeName().equals("subFeature") && element.hasAttribute("type") ) {
        	feature = new Feature(element.getAttribute("type"), mandatory, false);
        }
        
        
        //System.out.println("name "+name);
    	
    	if(element.getNodeName().equals("Feature") ||  element.hasAttribute("type")) {

        	NodeList childNodes = node.getChildNodes();
        	
        	/*if(childNodes.getLength() == 0) {
        		return feature;
        	}*/
    		
    		for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                //System.out.println("Node "+i +" cn "+child.getNodeName());
                
            	if(child.getNodeName().equals("Feature") || child.getNodeName().equals("subFeature")) {
            		if( element.hasAttribute("type")) {
            			
                		Feature ff = parseFeature(child,feature,node);
                		feature.addSubFeature(ff);
                		
            		}
            		else{
                		Feature ff = parseFeature(child,feature,node);
                		feature.addSubFeature(ff);
            		}
            		
            	}
                
            }
    		feature.setParent(ftrParent);
    		return feature;
    		
    	}
    	else { if(element.getNodeName().equals("subFeature") && !element.hasAttribute("type") ) {
    		//System.exit(0);
        	NodeList childNodes = node.getChildNodes();
        	
    		
    		for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                
            	if(child.getNodeName().equals("Feature") || child.getNodeName().equals("subFeature")) {
            		
            		Feature ff = parseFeature(child,ftrParent,parentNode);
            		ftrParent.addSubFeature(ff);
            		
            	}
                
            }
    		
    		}	

        Feature ftrtmp = new Feature("END", true, false);
    		return ftrtmp;
    	
    	}
    	
  
        

    }//FinMethod

    
}//FinClass
