package FeatureModel;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLParserExample {
    public static void main(String[] args) {
        try {
            // Charger le fichier XML
            File file = new File("test.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            // Récupérer le nœud parent
            Element rootElement = doc.getDocumentElement(); // Exemple : <root>

            // Récupérer uniquement les enfants directs de typeA
            NodeList directTypeAChildren = getDirectChildrenByTagName(rootElement, "typeA");

            // Afficher les résultats
            System.out.println("Enfants directs de <" + rootElement.getNodeName() + "> de type <typeA> :");
            for (int i = 0; i < directTypeAChildren.getLength(); i++) {
                Element typeAElement = (Element) directTypeAChildren.item(i);
                System.out.println("- " + typeAElement.getTagName() + " : " + typeAElement.getTextContent().trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    }
}
