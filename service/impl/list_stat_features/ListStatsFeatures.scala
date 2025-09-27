package cb_ctt.features.service.impl.list_stat_features

import cb_ctt.dto.{CbCttInstance, Course, Room}
import cb_ctt.features.service.FeatureFactory

import scala.collection.JavaConverters._

object ListStatsFeatures {
  def all_factories(): Array[FeatureFactory] = {
    val per_course_features = {
      case class F(name: String,
                   distribution: Seq[Course] => Array[Double],
                   special_values: CbCttInstance => Seq[(String, Double => Boolean)],
                  )
      Seq[F](
        F(name = "n_lects",
          distribution = c => c.map(_.getLectures.toDouble).toArray,
          special_values = (i: CbCttInstance) => Seq()),
        F(name = "minDays",
          distribution = c => c.map(_.getMinDays.toDouble).toArray,
          special_values = (i: CbCttInstance) => Seq(("one", _ <= 1), ("all", _ == i.getDays))
        ),
        F(name = "n_unsuitableRooms",
          distribution = c => c.map(_.getNotSuitable.size().toDouble).toArray,
          special_values = (i: CbCttInstance) => Seq(("zero", _ == 0), ("all", _ == i.getRooms.size()), ("all_but_one", _ == i.getRooms.size() - 1))
        ),
        F(name = "n_students",
          distribution = c => c.map(_.getStudents.toDouble).toArray,
          special_values = (i: CbCttInstance) => Seq(("zero", _ == 0)),
        ),
        F(name = "n_unavailablePeriod",
          distribution = c => c.map(_.getUnavailable.size().toDouble).toArray,
          special_values = (i: CbCttInstance) => Seq(("zero", _ == 0)),
        ),
      ).map { case F(name, func, special) => new ListStatsFeatureFactory(s"course.$name", c => func(c.getCourses.asScala.toSeq), special) }
    }

    val room_features = {
      case class F(name: String,
                   distribution: Iterable[Room] => Array[Double],
                   special_values: CbCttInstance => Seq[(String, Double => Boolean)],
                  )
      Seq[F](
        F(name = "capacity",
          distribution = rooms => rooms.map(_.getCapacity.toDouble).toArray,
          special_values = _ => Seq()),
      ).map { case F(name, func, special) => new ListStatsFeatureFactory(s"room.$name", c => func(c.getRooms.asScala.toSeq), special) }
    }

    val location_features = {
      def locations(instance: CbCttInstance): Set[Int] = instance.getRooms.asScala.map(_.getLocation).toSet

      case class F(name: String,
                   distribution: CbCttInstance => Array[Double],
                   special_values: CbCttInstance => Seq[(String, Double => Boolean)],
                  )
      Seq[F](
        F(name = "n_rooms",
          distribution = c => locations(c).map(loc => c.getRooms.asScala.toSeq.count(_.getLocation == loc).toDouble).toArray,
          special_values = i => Seq(("one", _ == 1))
        ),
        F(name = "capacity.min",
          distribution = c => locations(c)
            .map(loc =>
              c.getRooms.asScala.toSeq
                .filter(_.getLocation == loc)
                .map(_.getCapacity)
                .min.toDouble)
            .toArray,
          special_values = _ => Seq()),
        F(name = "capacity.max",
          distribution = c => locations(c)
            .map(loc =>
              c.getRooms.asScala.toSeq
                .filter(_.getLocation == loc)
                .map(_.getCapacity)
                .max.toDouble)
            .toArray,
          special_values = _ => Seq()),
        F(name = "capacity.sum",
          distribution = c => locations(c)
            .map(loc =>
              c.getRooms.asScala.toSeq
                .filter(_.getLocation == loc)
                .map(_.getCapacity)
                .sum.toDouble)
            .toArray,
          special_values = _ => Seq()),
      ).map { case F(name, func, spec) => new ListStatsFeatureFactory(s"location.$name", func, spec) }
    }

    (/* per course */
      per_course_features
        ++ room_features
        ++ location_features
        ++ Seq(new SimpleFeatures)
      ).toArray
  }
}
