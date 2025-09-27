package cb_ctt.features.service.impl.list_stat_features

import cb_ctt.Formulation
import cb_ctt.dto.CbCttInstance
import cb_ctt.features.service.{Feature, FeatureFactory, FeatureGroup}

import scala.collection.JavaConverters._

class SimpleFeatures extends FeatureFactory {
  /**
    *
    * @return the name of the feature factory. Must be the same name as the one the FeatureGroup,
    *         { @code calcFeatures(CbCttInstance)} returns.
    */
  override def name(): String = "simple"

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup = {
    new FeatureGroup(this, Seq(
      new Feature("periods_per_day", instance.getPeriodsPerDay),
    ).asJava)
  }
}
