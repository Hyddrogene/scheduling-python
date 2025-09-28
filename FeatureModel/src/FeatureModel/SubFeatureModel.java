package FeatureModel;

import java.util.Set;
import java.util.HashSet;

/**
 * Représente un sous-Feature Model contenant des exigences spécifiques.
 */
public class SubFeatureModel {
    private Set<String> requiredFeatures; // Liste des fonctionnalités requises

    /**
     * Constructeur du sous-Feature Model.
     * 
     * @param requiredFeatures Ensemble des fonctionnalités requises
     */
    public SubFeatureModel(Set<String> requiredFeatures) {
        this.requiredFeatures = requiredFeatures;
    }

    /**
     * Retourne la liste des fonctionnalités requises.
     * 
     * @return Un ensemble des fonctionnalités exigées
     */
    public Set<String> getRequiredFeatures() {
        return requiredFeatures;
    }


    @Override
    public String toString() {
        return "SubFeatureModel{requiredFeatures=" + requiredFeatures + "}";
    }
}//FinClass
