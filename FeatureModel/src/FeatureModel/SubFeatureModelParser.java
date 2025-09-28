package FeatureModel;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Parse un fichier XML contenant un sous-Feature Model (SubFeatureModel).
 */
public class SubFeatureModelParser {

    /**
     * Charge un sous-Feature Model depuis un fichier XML.
     * 
     * @param filePath Chemin du fichier XML
     * @return Un objet SubFeatureModel contenant les fonctionnalités requises
     * @throws Exception Si une erreur de parsing survient
     */
    public static SubFeatureModel parseSubFeatureModel(String filePath) throws Exception {
        File file = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        // Récupérer la liste des fonctionnalités requises
        Set<String> requiredFeatures = new HashSet<>();
        NodeList requiredNodes = document.getElementsByTagName("requiredFeature");

        for (int i = 0; i < requiredNodes.getLength(); i++) {
            Node node = requiredNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                requiredFeatures.add(element.getTextContent().trim());
            }
        }

        return new SubFeatureModel(requiredFeatures);
    }
}//FinClass
