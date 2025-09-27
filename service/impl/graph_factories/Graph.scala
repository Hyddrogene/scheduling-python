package cb_ctt.features.service.impl.graph_factories

import cb_ctt.Formulation
import cb_ctt.dto._
import cb_ctt.features.service.impl.list_stat_features.ListStatsFeatureFactory
import cb_ctt.features.service.{Feature, FeatureFactory, FeatureGroup}

import scala.collection.JavaConverters._
import scala.collection.immutable.TreeSet

/**
  * Undirected graph.
  */
class Graph[V](
                val vertices: Seq[V],
                val edges: Map[V, Set[V]],
              ) {

  def this(vertices: Seq[V], edge: (V, V) => Boolean) =
    this(vertices, edges = vertices.map(v => (v, vertices.filter(w => edge(v, w)).toSet)).toMap)

  def edge(v: V, w: V): Boolean = edges(v).contains(w)

  def degree(v: V): Int = edges(v).size

  def neighborhood(v: V): Seq[V] = edges(v).toSeq

  def edge_cnt: Int =
    vertices.tails.map(seq => seq.headOption match {
      case Some(v) => seq.tail.count(w => edge(v, w))
      case None => 0
    }).sum

  def local_clustering_coeffs: Seq[Double] = vertices flatMap (a => {
    val n = neighborhood(a)
    val deg = n.size
    if (deg < 2) None
    else Some({

      val triplets = (deg * (deg - 1)) / 2.0
      val triangles = n.tails
        .map(seq => seq.headOption match {
          case Some(b) => seq.tail.count(c => edge(b, c))
          case None => 0
        }).sum

      val coeff = triangles.toDouble / triplets
      //      println(s"coeff($a) = $coeff")
      coeff
    })
  })

  def local_edge_density: Seq[Double] = vertices flatMap (v => {
    val nhood = edges(v).toList
    if (nhood.isEmpty) {
      None
    } else {
      val V = nhood.size + 1
      val n_edges = (for {
        v <- nhood
        w <- nhood if edges(v) contains w
      } yield 1).sum
      val E = n_edges + nhood.size

      Some(E.toDouble / ((V - 1) * (V - 1)))
    }

  })

  def global_clustering_coeff: Double = {
    val triangles = vertices.tails.map(seq => {
      if (seq.isEmpty) 0
      else {
        val a = seq.head
        seq.tail.tails
          .map(seq => {
            if (seq.isEmpty) 0
            else {
              val b = seq.head
              if (edge(a, b)) {
                seq.tail.count(c => edge(b, c) && edge(c, a))
              } else 0
            }
          }).sum
      }
    }).sum

    //    println(s"triangles: $triangles")
    val n = vertices.size

    /* triangles / possible_triangles
     = triangles / ( n choose 3 )
     = triangles / ( n! / (3! * (n-3)!) )
     = triangles / ( (n * (n-1) * (n-2)) / 6 )
     = triangles * (  6 / (n * (n-1) * (n-2)))
     = (triangles * 6) / (n * (n-1) * (n-2))
     */

    triangles * 6.0 / (n * (n - 1) * (n - 2))
  }
}

/**
  * Undirected weighted graph.
  */
class WeightedGraph[V](vertices: Seq[V],
                       edges: Map[V, Set[V]],
                       val weight: Map[(V, V), Double]
                      ) extends Graph[V](vertices, edges) {

  def this(vertices: Seq[V], edge: (V, V) => Option[Double]) =
    this(vertices,
      edges = vertices.map(v => (v, vertices.filter(w => edge(v, w).isDefined).toSet)).toMap,
      weight = vertices.flatMap(v => vertices.flatMap(w => edge(v, w).map(wght => ((v, w), wght)))).toMap)

  def weightedDegree(v: V): Double = neighborhood(v).map(w => weight(v, w)).sum

  def weightedDegrees: Seq[Double] = vertices.map(v => weightedDegree(v))
}

/**
  * Features calculated:
  * - Vertex degree statitistics ( includes vertex cnt )
  * - Edge cnt
  * - global clusetering coefficient
  * - local clustering coefficient statistics
  * - weighted local clustering coefficient statistics
  * - count of connected components
  * - listStatFeatures over all graph features of connected components
  */
class GraphFeatureFactory[V <: AnyRef, G <: Graph[V]](
                                                       override val name: String,
                                                       graph: (CbCttInstance, Formulation) => G)
  extends FeatureFactory {

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup =
    new FeatureGroup(this, calcFeatures(this.graph(instance, formulation)).asJava)

  def connected_components(graph: G): Seq[Feature] = {
    val vs = graph.vertices
    val uf = new UnionFind[V](vs)
    graph.edges
      .foreach { case (v, ws) => ws.foreach(w => {
        val _ = uf.union(v, w)
      })
      }
    val components = vs.map(v => uf.find(v))
    val component_sizes = components.groupBy(x => x).values.map(_.length.toDouble).toArray

    val size_feats = ListStatsFeatureFactory.features(
      distribution = component_sizes,
      prefix = Some("connected_components.sizes"),
      value_properties = Seq(("one", _ == 1)))

    val entropy = ListStatsFeatureFactory.shannon_entropy(components, prefix = Some("connected_components"))
    size_feats ++ Seq(entropy)
  }

  def calcFeatures(graph: G): Seq[Feature] = {
    //    val degs = graph.degrees.map(_.toDouble).toArray
    val degs = graph.vertices.map(n => graph.degree(n).toDouble).toArray

    val loc_coeff = graph.local_clustering_coeffs.toArray
    val weighted_loc_coeff = (degs zip loc_coeff) map { case (a, b) => a * b }

    val con_comp_feats = connected_components(graph)

//    assert(loc_coeff.toSeq == graph.local_edge_density.toSeq,
//    s"${loc_coeff.toSeq} == ${graph.local_edge_density.toSeq}")
val v = graph.vertices.size
    val e = graph.edge_cnt
    Seq.concat(
      Seq(
        new Feature("edge_cnt", e),
        new Feature("cluster_coeff.global", graph.global_clustering_coeff),
        new Feature("edge_density", e / ( v * (v - 1) / 2.0))
      ),
      ListStatsFeatureFactory.features(degs,
        prefix = Some("degree"),
        value_properties = Seq(
          ("zero", _ == 0),
          ("one", _ == 1),
          ("eq_node_cnt", _ == graph.vertices.length),
          ("almost_eq_node_cnt", _ == graph.vertices.length - 1),
        )),
      ListStatsFeatureFactory.features(loc_coeff,
        prefix = Some("cluster_coeff.local"),
        value_properties = Seq(
          ("theoretical_min", _ == 0),
          ("theoretical_max", _ == 1),
        )),
      ListStatsFeatureFactory.features(weighted_loc_coeff,
        prefix = Some("cluster_coeff.local.weighted"),
        value_properties = Seq()),
      con_comp_feats,
    )
  }

}

/**
  * Features calculated:
  * - Weighted degree statitistics
  * - Edge weight statistics
  */
class WeightedGraphFeatureFactory[V <: AnyRef, T <: WeightedGraph[V]](
                                                                       override val name: String,
                                                                       graph: (CbCttInstance, Formulation) => T,
                                                                       edge_weight_properties: (CbCttInstance, Formulation) => Seq[(String, Double => Boolean)]
                                                                     )

  extends GraphFeatureFactory[V, T](name, graph) {

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup = {
    val g = this.graph(instance, formulation)
    val graph_feats = super.calcFeatures(g)

    val weighted_degree_feats = ListStatsFeatureFactory.features(g.weightedDegrees.to, prefix = Some("weighted_degree"), value_properties = Seq())
    val weight_feats = ListStatsFeatureFactory.features(g.weight.values.toArray,
      prefix = Some("edge_weights"),
      value_properties = edge_weight_properties(instance, formulation))

    new FeatureGroup(this, (graph_feats ++ weighted_degree_feats ++ weight_feats).asJava)
  }
}

object GraphFeatureFactory {
  implicit def period_ordering: Ordering[Period] = new Ordering[Period] {
    override def compare(x: Period, y: Period): Int = {
      x.getDay.compareTo(y.getDay) match {
        case 0 => x.getSlot.compareTo(y.getSlot)
        case v => v
      }
    }
  }

  implicit def curriculum_ordering: Ordering[Curriculum] = new Ordering[Curriculum] {
    override def compare(x: Curriculum, y: Curriculum): Int = {
      x.getId.compareTo(y.getId)
    }
  }

  implicit def course_ordering: Ordering[Course] = new Ordering[Course] {
    override def compare(x: Course, y: Course): Int = {
      x.getId.compareTo(y.getId)
    }
  }

  def allFactories(): Array[FeatureFactory] = Array(

    new GraphFeatureFactory[Course, Graph[Course]](
      name = "course_teacher_conflicts",
      graph = (instance: CbCttInstance, f: Formulation) => new Graph[Course](
        vertices = instance.getCourses.asScala.to,
        edge = (v, w) => v != w && v.getTeacher.equals(w.getTeacher),
      )
    ),

    new GraphFeatureFactory[Course, Graph[Course]](
      name = "g_course_conflict",
      graph = (instance: CbCttInstance, f: Formulation) => {

        val curricula: Map[String, TreeSet[Curriculum]] = instance
          .getCourses.asScala.toSeq
          .map(c => (c.getId, TreeSet[Curriculum]() ++ instance.getCurricula.asScala.filter(_.getCourses.contains(c))))
          .toMap

        new Graph[Course](
          vertices = instance.getCourses.asScala.to,
          edge = (v, w) => v != w && (
            v.getTeacher.equals(w.getTeacher)
              || curricula(v.getId).intersect(curricula(w.getId)).nonEmpty
            ),
        )
      }
    ),

    /**
      * G = (V,E)
      * V = curricula
      * w(v,w) = number of courses v and w have in common
      */
    new WeightedGraphFeatureFactory[Curriculum, WeightedGraph[Curriculum]](
      name = "curriculum_course_conflicts",
      graph = (instance: CbCttInstance, f: Formulation) => {
        val courses: Map[String, TreeSet[Course]] = instance.getCurricula.asScala.map(c => (c.getId, TreeSet[Course]() ++ c.getCourses.asScala)).toMap
        new WeightedGraph[Curriculum](
          vertices = instance.getCurricula.asScala.to,
          edge = (v, w) => {
            if (v == w) {
              None
            } else {
              courses(v.getId).intersect(courses(w.getId)).size match {
                case 0 => None
                case cnt => Some(cnt.toDouble)
              }
            }
          },
        )
      },
      edge_weight_properties = (instance: CbCttInstance, f: Formulation) => Seq()
    ),

    /**
      * G = (V,E)
      * V = courses
      * w(v,w) = number of curricula in which both v and w are in
      */
    new WeightedGraphFeatureFactory[Course, WeightedGraph[Course]](
      name = "course_curriculum_conflicts",
      graph = (instance: CbCttInstance, f: Formulation) => {

        val curricula: Map[String, TreeSet[Curriculum]] = instance
          .getCourses.asScala.toSeq
          .map(c => (c.getId, TreeSet[Curriculum]() ++ instance.getCurricula.asScala.filter(_.getCourses.contains(c))))
          .toMap

        val courses = instance.getCourses.asScala
        new WeightedGraph[Course](
          vertices = courses.to,
          edge = (v, w) =>
            if (v.getId == w.getId) None
            else curricula(v.getId).intersect(curricula(w.getId)).size match {
              case 0 => None
              case cnt => Some(cnt.toDouble)
            }
        )
      },
      edge_weight_properties = (instance: CbCttInstance, f: Formulation) => Seq()
    ),

    /**
      * G = (V,E)
      * V = courses
      * w(v,w) = number of periods in which they both are unavailable
      */
    new WeightedGraphFeatureFactory[Course, WeightedGraph[Course]](
      name = "course_cnt_common_unavailable",
      graph = (instance: CbCttInstance, f: Formulation) => {
        val unavailable: Map[String, TreeSet[Period]] = instance.getCourses.asScala.map(c => (c.getId, TreeSet[Period]() ++ c.getUnavailable.asScala)).toMap
        new WeightedGraph[Course](
          vertices = instance.getCourses.asScala.to,
          edge = (v, w) =>
            if (v.getId == w.getId) None
            else unavailable(v.getId).intersect(unavailable(w.getId)).size match {
              case 0 => None
              case cnt => Some(cnt.toDouble)
            }
        )
      },
      edge_weight_properties = (instance: CbCttInstance, f: Formulation) => Seq()
    ),

  )
}

import scala.collection.mutable

private class UnionFind[T <: AnyRef](parent: mutable.HashMap[T, T]) {
  def this(elements: Seq[T]) = this(
    parent = {
      val map = new mutable.HashMap[T, T]()
      elements.foreach(e => map.put(e, e))
      map
    }
  )

  def union(a: T, b: T): Unit = {
    parent.put(a, find(b))
  }

  def find(a: T): T = {
    val p = parent(a)
    if (p eq a) {
      p
    } else {
      val new_p = find(p)
      parent.put(a, new_p)
      new_p
    }
  }
}

