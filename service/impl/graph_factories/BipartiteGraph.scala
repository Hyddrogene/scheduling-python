package cb_ctt.features.service.impl.graph_factories

import cb_ctt.Formulation
import cb_ctt.aslib.Conf
import cb_ctt.dto._
import cb_ctt.features.service.impl.list_stat_features.ListStatsFeatureFactory
import cb_ctt.features.service.{Feature, FeatureFactory, FeatureGroup}
import cb_ctt.utils.DefaultInstances

import scala.collection.JavaConverters._

// TODO:
// check if equivalent are included (from master thesis)
// (# courses that fit in all rooms) / #courses        -> ~= mean of left degrees of course_capacity graph
// (# courses that fit in all periods) / #courses      -> ~= mean of right degrees of period_course_unavailable graph
//the fraction of exams
// – that fit only one room over all exams;
// – that fit only one period over all exam
class BipartiteGraph[L, R](
                            val left: Seq[L],
                            val right: Seq[R],
                            val edge: (L, R) => Boolean
                            //                            val l_edges: Map[L, Set[R]],
                            //                            val r_edges: Map[R, Set[L]],
                          ) {
//  def this(left: Seq[L], right: Seq[R], l_edges: Map[L, Set[R]], r_edges: Map[R, Set[L]]) = {
//    this(left, right,
//      edge = left
////      l_edges = left.map(l => (l, right.filter(r => edge(l, r)).toSet)).toMap,
////      r_edges = right.map(r => (r, left.filter(l => edge(l, r)).toSet)).toMap,
//    )
//  }

  def leftDegree(l: L): Int = right.count(r => edge(l, r))

  def rightDegree(r: R): Int = left.count(l => edge(l, r))

  def leftDegrees: Seq[Int] = left.map(l => leftDegree(l))

  def rightDegrees: Seq[Int] = right.map(r => rightDegree(r))
}

class WeightedBipartiteGraph[L, R](
                                    override val left: Seq[L],
                                    override val right: Seq[R],
                                    //                                    val weights: Map[(L, R), Double],
                                    val weight: (L, R) => Option[Double],
                                  ) extends BipartiteGraph[L, R](left, right, edge = (l, r) => weight(l, r).isDefined) {


  def rightWeightedDegrees: Seq[Double] = right.map(r => rightWeightedDegree(r))

  def leftWeightedDegrees: Seq[Double] = left.map(l => leftWeightedDegree(l))

  def rightWeightedDegree(r: R): Double = left.flatMap(l => weight(l, r)).sum

  def leftWeightedDegree(l: L): Double = right.flatMap(r => weight(l, r)).sum

  def weights: Seq[Double] = for {
    l <- left
    r <- right
    w <- weight(l, r)
  } yield w
}

class BipartiteGraphFeatureFactory[G <: BipartiteGraph[_, _]](
                                                               override val name: String,
                                                               graph: (CbCttInstance, Formulation) => G)
  extends FeatureFactory {

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup = {
    val g = graph(instance, formulation)

    val feats = calcFeatures(g)

    new FeatureGroup(
      this,
      feats.asJava
    )
  }

  def calcFeatures(graph: G): Seq[Feature] = {
    val left_feat = ListStatsFeatureFactory.features(
      distribution = graph.leftDegrees.map(_.toDouble).to,
      prefix = Some("left.deg"),
      value_properties = Seq(
        ("fully_connected", _ == graph.right.size),
        ("almost_fully_connected", _ == graph.right.size - 1),
        ("not_connected", _ == 0),
        ("one_connection", _ == 1),
      ))

    val right_feat = ListStatsFeatureFactory.features(
      distribution = graph.rightDegrees.map(_.toDouble).to,
      prefix = Some("right.deg"),
      value_properties = Seq(
        ("fully_connected", _ == graph.left.size),
        ("almost_fully_connected", _ == graph.left.size - 1),
        ("not_connected", _ == 0),
        ("one_connection", _ == 1),
      ))

    assert(!left_feat.exists(_.getName.contains("capacity")))
    val relations = (left_feat zip right_feat).flatMap {
      case (l, r) => {
        val name = l.getName
        Seq(
          new Feature(s"l_per_r.deg.$name", l.getValue / r.getValue),
          new Feature(s"r_per_l.deg.$name", r.getValue / l.getValue),
        )
      }
    }

    val e = graph.leftDegrees.sum
    val l = graph.left.size
    val r = graph.right.size

    val out = (relations
      ++ left_feat
      ++ right_feat
      ++ Seq(
      new Feature("edge_cnt", e.toDouble),
      new Feature("edge_density", e.toDouble / (l * r))
    ))

    assert(!out.exists(_.getName.contains("capacity")))
    out
  }
}

class WeightedBipartiteGraphFeatureFactory[T <: WeightedBipartiteGraph[_, _]](
                                                                               override val name: String,
                                                                               graph: (CbCttInstance, Formulation) => T,
                                                                               special_weight_values: (CbCttInstance, Formulation) => Seq[(String, Double => Boolean)]
                                                                             )
  extends BipartiteGraphFeatureFactory[T](name, graph) {

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup = {
    val g = graph(instance, formulation)
    val special_vals = special_weight_values(instance, formulation)

    val unweighted_feats = super.calcFeatures(g)

    val left_feat = ListStatsFeatureFactory.features(
      distribution = g.leftWeightedDegrees.to,
      prefix = Some("left.weighted_deg"),
      value_properties = special_vals) // ?
    val right_feat = ListStatsFeatureFactory.features(
      distribution = g.rightWeightedDegrees.to,
      prefix = Some("right.weighted_deg"),
      value_properties = special_vals) // ?
    val relations = (left_feat zip right_feat).flatMap {
      case (l, r) => {
        val name = l.getName
        Seq(
          new Feature(s"l_per_r.weighted_deg.$name", l.getValue / r.getValue),
          new Feature(s"r_per_l.weighted_deg.$name", r.getValue / l.getValue),
        )
      }
    }

    val weighted_feats = (relations
      ++ left_feat
      ++ right_feat
      ++ ListStatsFeatureFactory.features(
      distribution = g.weights.toArray,
      prefix = Some("edge_weights"),
      value_properties = special_vals
    ))

    new FeatureGroup(
      this,
      (unweighted_feats ++ weighted_feats).asJava
    )
  }
}

object BipartiteGraphFeatureFactory {
  def allFactories(): Array[FeatureFactory] = Array(
    new BipartiteGraphFeatureFactory(
      "course_curriculum_graph",
      (instance: CbCttInstance, _: Formulation) => {
        new BipartiteGraph[Course, Curriculum](
          left = instance.getCourses.asScala.to,
          right = instance.getCurricula.asScala.to,
          edge = (l: Course, r: Curriculum) => r.getCourses.contains(l)
        )
      }
    ).asInstanceOf[FeatureFactory],
    new BipartiteGraphFeatureFactory(
      "course_room_notSuitable_graph",
      (instance: CbCttInstance, _: Formulation) => {
        new BipartiteGraph[Course, Room](
          left = instance.getCourses.asScala.to,
          right = instance.getRooms.asScala.to,
          edge = (l: Course, r: Room) => l.getNotSuitable.contains(r)
        )
      }
    ).asInstanceOf[FeatureFactory],
    new BipartiteGraphFeatureFactory(
      "curriculum_teacher_graph",
      (instance: CbCttInstance, _: Formulation) => {
        new BipartiteGraph[Curriculum, String](
          left = instance.getCurricula.asScala.to,
          right = instance.getCourses.asScala.map(_.getTeacher).toSet.to,
          edge = (l: Curriculum, r: String) => l.getCourses.asScala.exists(_.getTeacher == r)
        )
      }
    ).asInstanceOf[FeatureFactory],

    new BipartiteGraphFeatureFactory(
      "period_course_unavailable",
      (instance: CbCttInstance, _: Formulation) => {
        new BipartiteGraph[Period, Course](
          left = for {
            d <- 0 until instance.getDays
            p <- 0 until instance.getPeriodsPerDay
          } yield new Period(d, p),
          right = instance.getCourses.asScala.to,
          edge = (l: Period, r: Course) => r.getUnavailable.contains(l),
        )
      }
    ).asInstanceOf[FeatureFactory],

    new WeightedBipartiteGraphFeatureFactory(
      "b_course_slot",
      (instance: CbCttInstance, _: Formulation) => {
        val slots = for {
          d <- 0 until instance.getDays
          p <- 0 until instance.getPeriodsPerDay
          r <- instance.getRooms.asScala.toSeq
        } yield Slot(r, new Period(d, p))
        new WeightedBipartiteGraph[Course, Slot](
          left = instance.getCourses.asScala.to,
          right = slots,
          weight = (l: Course, r: Slot) =>
            if (l.getUnavailable.contains(r.period)) None
            else Some((l.getStudents - r.room.getCapacity).toDouble)
        )
      },
      special_weight_values = (instance: CbCttInstance, _: Formulation) => Seq(
        ("big_enough", (x: Double) => x <= 0),
        ("capacity_exceeded", (x: Double) => x > 0.0)),
    ).asInstanceOf[FeatureFactory],

    new BipartiteGraphFeatureFactory(
      "courses_half_day_unavailable_graph",
      (instance: CbCttInstance, _: Formulation) => {
        new BipartiteGraph[Int, Course](
          left = 0 until instance.getDays,
          right = instance.getCourses.asScala.to,
          edge = (day: Int, r: Course) => r.getUnavailable.asScala.count(_.getDay == day) >= instance.getDays / 2
        )
      }
    ).asInstanceOf[FeatureFactory],

//        new WeightedBipartiteGraphFeatureFactory(
//          name = "b_course_dayslot",
//          special_weight_values = (instance: CbCttInstance, _: Formulation) => Seq(
//            ("stable", (x: Double) => x <= 0),
//            ("unstable", (x: Double) => x > 0.0)
//          ),
//          graph = (instance: CbCttInstance, _: Formulation) => {
//            val rooms = instance.getRooms.asScala.toSeq
//            val courses = instance.getCourses.asScala.toSeq
//            val periods = 0 until instance.getPeriodsPerDay
//            new WeightedBipartiteGraph[Course, Int](
//              left = courses,
//              right = periods,
//              weight = (c: Course, slot: Int) => Some(c.getUnavailable.asScala.count(_.getSlot == slot)),
//            )
//          }
//        ).asInstanceOf[FeatureFactory]

    /**
      * V= Courses u Rooms
      * E(c,r) iff c.students > r.capacity
      * w(c,r) = max(c.students − r.capacity, 0)
      */
    new WeightedBipartiteGraphFeatureFactory(
      name = "room_capacity",
      special_weight_values = (instance: CbCttInstance, _: Formulation) => Seq(),
      graph = (instance: CbCttInstance, _: Formulation) => {
        val rooms = instance.getRooms.asScala.toSeq
        val courses = instance.getCourses.asScala.toSeq
        new WeightedBipartiteGraph[Room, Course](
          left = rooms,
          right = courses,
          weight = (r: Room, c: Course) =>
            c.getStudents - r.getCapacity match {
              case x if x <= 0 => None
              case x if x > 0 => Some(x.toDouble)
            }
        )
      }
    ).asInstanceOf[FeatureFactory]

  )

  def main(args: Array[String]): Unit = {
    //    val Some(fac) = allFactories().find(_.name() == "room_capacity")
    //    val Some(fac) = allFactories().find(_.name() == "period_course_unavailable")
    val Some(inst) = DefaultInstances.random.find(_.getId == "Random_125Events_Occupancy50_1545252539518")
    for {
      //      fac <- allFactories()
      fac <- Conf.allFeatureFactories().asScala.values

    } {
      val start = System.currentTimeMillis()
      val feat = fac.calcFeatures(inst, Formulation.UD2)
      val end = System.currentTimeMillis()
      println(s"${fac.name()}\ttime: ${end - start}")
    }
  }

}

case class Slot(room: Room, period: Period)
