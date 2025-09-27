package cb_ctt.features.service.impl.list_stat_features

import cb_ctt.Formulation
import cb_ctt.dto.CbCttInstance
import cb_ctt.features.service.{Feature, FeatureFactory, FeatureGroup}

import scala.collection.JavaConverters._

class SlackFeatures extends FeatureFactory {
  /**
    *
    * @return the name of the feature factory. Must be the same name as the one the FeatureGroup,
    *         { @code calcFeatures(CbCttInstance)} returns.
    */
  override def name(): String = "slack"

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup = {
    val n_seats = instance.getRooms.asScala.toSeq.map(_.getCapacity).sum
    val seats_needed = instance.getCourses.asScala.toSeq.map(_.getStudents).sum
    val n_rooms = instance.getRooms.size

    val n_periods = instance.getDays * instance.getPeriodsPerDay
    val n_seat_time = n_periods * n_seats
    val seat_time_needed = instance.getCourses.asScala.toSeq.map(c => c.getStudents * c.getLectures).sum

    val n_slots = n_periods * n_rooms
    val n_events = instance.getCourses.asScala.toSeq.map(_.getLectures).sum

    new FeatureGroup(this, Seq(
      new Feature("seat", n_seats - seats_needed),
      new Feature("seat_slots", n_seat_time - seat_time_needed),
      new Feature("slot", n_slots - n_events),
//      new Feature("room_time", n_time_room - slots_needed),
    ).asJava)
  }
}
