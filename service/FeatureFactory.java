package cb_ctt.features.service;

import cb_ctt.Formulation;
import cb_ctt.algorithms.FeatureCalcError;
import cb_ctt.aslib.AslibMainConf;
import cb_ctt.dto.CbCttInstance;
import cb_ctt.utils.DefaultInstances;

public interface FeatureFactory {

	/**
	 * @return the name of the feature factory. Must be the same name as the one the FeatureGroup,
	 * {@code calcFeatures(CbCttInstance)} returns.
	 */
	String name();

	/**
	 * @param instance the problem instance to calculate the features of
	 * @return the feature values
	 */
	FeatureGroup calcFeatures(CbCttInstance instance, Formulation formulation) throws FeatureCalcError;
}
