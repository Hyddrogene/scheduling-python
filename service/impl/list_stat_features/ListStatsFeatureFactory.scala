package cb_ctt.features.service.impl.list_stat_features

import cb_ctt.Formulation
import cb_ctt.dto.CbCttInstance
import cb_ctt.features.service.{Feature, FeatureFactory, FeatureGroup}

import scala.collection.JavaConverters._

object ListStatsFeatureFactory {

  def feature_fun(prefix: Option[String]): (String, Double) => Feature = prefix match {
    case Some(p) => (n, v) => new Feature(s"$p.$n", v)
    case None => (n, v) => new Feature(n, v)
  }

  def shannon_entropy[A](distribution: Seq[A], prefix: Option[String] = None)(implicit feature: (String, Double) => Feature = feature_fun(prefix)): Feature = {

    val ps = distribution.foldLeft(Map[A, Int]())((map, next) => map.get(next) match {
      case None => map + ((next, 1))
      case Some(v) => map + ((next, v + 1))
    }).mapValues(cnt => cnt.toDouble / distribution.length)

    val entropy = ps.values.map(p => -p * math.log(p)).sum
    feature("shannon_entropy", entropy)
  }

  def features(distribution: Array[Double],
               value_properties: Seq[(String, Double => Boolean)] = Seq(),
               n_quantiles: Int = 5,
               n_moments: Int = 5,
               n_central_moments: Int = 4,
               prefix: Option[String] = None)(implicit feature: (String, Double) => Feature = feature_fun(prefix)): Seq[Feature] = {

    java.util.Arrays.sort(distribution)

    /* value of expecence */
    def E(f: Double => Double): Double = distribution.map(f).sum / distribution.length

    val entropy = shannon_entropy(distribution)(feature)


    val quantile_step = 1.0 / (n_quantiles - 1).toDouble
    val max_index = distribution.length - 1
    val quantiles = (0 until n_quantiles)
      .map(i => {
        val percent = i.toDouble * quantile_step
        val name = s"quantil_%1.4f".format(percent)
        val v = if (distribution.length == 0) {
          Double.NaN
        } else {
          distribution((percent * max_index).toInt)
        }
        feature(name, v)
      })

    val mean = E(x => x)
    val sum = distribution.sum
    val moments = (2 to n_moments)
      .map(m => {
        feature(
          s"moment_$m",
          E(x => math.pow(x.toDouble, m.toDouble))
        )
      })
    val central_moments = (1 to n_central_moments)
      .map(m => feature(s"central_moment_$m", E(x => math.pow(x - mean, m.toDouble))))
    val variance = distribution.map(x => math.pow(x - mean, 2)).sum / (distribution.length - 1).toDouble
    val std_deviation = math.sqrt(variance)
    val var_coefficient = std_deviation / mean
    val count = distribution.length
    val skewness = central_moments(2).getValue / math.pow(std_deviation, 3)
    val kurtosis = central_moments(3).getValue / math.pow(central_moments(1).getValue, 2) - 3


    val special_value_feats: Seq[Feature] = (for {
      (name, prop) <- value_properties
    } yield {
      val cnt = distribution.count(prop).toDouble
      val fraction = cnt / distribution.length
      Seq(
        feature(s"$name.cnt", cnt),
        feature(s"$name.fraction", fraction),
      )
    }).flatten

    (
      Seq(
        feature("sum", sum),
        feature("mean", mean),
        feature("variance", variance),
        feature("std_deviation", std_deviation),
        feature("var_coefficient", var_coefficient),
        feature("count", count.toDouble),
        feature("kurtosis", kurtosis),
        feature("skewness", skewness),
        entropy,
      )
        ++ special_value_feats
        ++ quantiles
        ++ moments
        ++ central_moments
      )
  }
}

/**
  * Creates a feature from a sample of random values. The feature consists of the statistical characteristics of the
  * sample.
  * Those values include:
  * - quantiles ( including: min, max, median )
  * - moments (including mean )
  * - central moments
  * - variance
  * - variation coefficient
  * - count
  * - skewness
  * - curtosis
  * - shannon entropy
  *
  * @param name             the name of the sampled value
  * @param sample           a function to extract the list of values from a CbCttInstance
  * @param value_properties each element (name, fn) is a function to detect some special values of the distribution
  *                         (e.g. 0 or the theoretical maximum). Name should be a descriptive name of this class of
  *                         values.
  */
class ListStatsFeatureFactory(override val name: String,
                              sample: CbCttInstance => Array[Double],
                              value_properties: CbCttInstance => Seq[(String, Double => Boolean)],
                             ) extends FeatureFactory {

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, _formulation: Formulation): FeatureGroup = {
    new FeatureGroup(
      this,
      ListStatsFeatureFactory.features(
        distribution = this.sample(instance),
        value_properties = this.value_properties(instance)
      ).asJava
    )
  }
}
