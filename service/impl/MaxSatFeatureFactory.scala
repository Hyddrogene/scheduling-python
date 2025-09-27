package cb_ctt.features.service.impl

import java.util.concurrent.TimeUnit

import cb_ctt.Formulation
import cb_ctt.algorithms.{FeatureCalcError, InstanceToMaxsatConverter}
import cb_ctt.dto.CbCttInstance
import cb_ctt.features.service.{Feature, FeatureFactory, FeatureGroup}
import cb_ctt.utils.timeout

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration

class MaxSatFeatureFactory extends FeatureFactory {
  val time =  Duration(10, TimeUnit.SECONDS)
  /**
    *
    * @return the name of the feature factory. Must be the same name as the one the FeatureGroup,
    *         { @code calcFeatures(CbCttInstance)} returns.
    */
  override def name(): String = "maxsat_feats"

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup = {
    timeout(time) {
      val enc = new InstanceToMaxsatConverter()
      val clauses = enc.instanceToClauses(instance)
      val vars = enc.varCount().toDouble
      new FeatureGroup(this, Seq(
        new Feature("clauses_per_var", clauses.size() / vars),
        new Feature("soft_clauses_per_var", clauses.asScala.count(c => !c.isHard) / vars),
        new Feature("hard_clauses_per_var", clauses.asScala.count(_.isHard) / vars),
      ).asJava)
    } getOrElse {
      throw new FeatureCalcError(FeatureCalcError.Reason.TIMEOUT)
    }
  }
}
