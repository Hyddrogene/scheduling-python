package cb_ctt.features.service.impl

import cb_ctt.Formulation
import cb_ctt.algorithms.FeatureCalcError
import cb_ctt.algorithms.impl.SimulatedAnnealing
import cb_ctt.dto.CbCttInstance
import cb_ctt.features.service.impl.list_stat_features.ListStatsFeatureFactory
import cb_ctt.features.service.{Feature, FeatureFactory, FeatureGroup}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success}

/**
  * Probing features using the simulated annealing algorithm of Andrea Shaerf.
  */
class SimulatedAnnealingFeatures(timeout_ms: Seq[Long]) extends FeatureFactory {
  def this() = this(timeout_ms = Seq(500, 1000, 1500, 2000))

  /**
    *
    * @return the name of the feature factory. Must be the same name as the one the FeatureGroup,
    *         { @code calcFeatures(CbCttInstance)} returns.
    */
  override def name(): String = "probe_simulated_annealing"

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup = {
    val feats = for {
      timeout <- timeout_ms
    } yield SimulatedAnnealing.calc_feats(instance, formulation, timeout, _ => null) match {
      case Success(feat) => feat
      case Failure(e) => throw new FeatureCalcError(e)
    }

    val (violations, costs, objectives) = feats
      .map(f => (f.cost.violations, f.cost.cost, f.objective))
      .unzip3

    def feat(seq: Seq[Int], prefix: String) =
      seq.map(_.toDouble)
      .zipWithIndex
        .map{case (v,i) => new Feature(s"run$i.$prefix", v)}
//      ListStatsFeatureFactory.features(seq.map(_.toDouble).toArray, prefix = Some(prefix))

    new FeatureGroup(this,
      Seq.concat(
        feat(violations, "violations"),
        feat(costs, "cost"),
        feat(objectives, "objective"),
      ).asJavaCollection)
  }
}
