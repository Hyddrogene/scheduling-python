package cb_ctt.features.service.impl

import java.io.IOException
import java.util.concurrent.TimeUnit

import scala.concurrent._
import ExecutionContext.Implicits.global
import cb_ctt.Formulation
import cb_ctt.algorithms.AlgorithmRunError.Reason
import cb_ctt.algorithms.FeatureCalcError
import cb_ctt.algorithms.impl.AspAlgorithm
import cb_ctt.dto.CbCttInstance
import cb_ctt.features.service.{Feature, FeatureFactory, FeatureGroup}
import cb_ctt.utils.OsUtils
import org.apache.commons.io.IOUtils
import org.json4s.JValue
import org.json4s.JsonAST._
import org.json4s.native.{compactJson, parseJson, prettyJson, renderJValue}

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, duration}
import scala.util.{Failure, Success, Try}

case class ClFeatGroup(features: List[ClFeat]) {
  def toFeatureGroup(fac: FeatureFactory): FeatureGroup = new FeatureGroup(fac, List(
    features.map { case ClFeat(name, value) => new Feature(name, value) },
  ).flatten.asJava)
}


object ClFeatGroup {
  def fromJson(value: JValue): ClFeatGroup = {
    value match {
      case JObject(obj) => {
        (
          obj.find(_._1 == "Static-Time"),
          obj.find(_._1 == "Static"),
          obj.find(_._1 == "Static_Optimization"),
          obj.find(_._1 == "Status"),
        ) match {
          case (
            Some(("Static-Time", JDouble(time))),
            Some(("Static", JArray(stat_feat))),
            Some(("Static_Optimization", JArray(stat_opt))),
            Some(("Status", JString(status)))
            ) => {

            val dyn_feats: List[ClFeat] = (for {
              (prefix, group_name) <- List(
                ("Dynamic-", "dynamic"),
                ("Optimization-", "dyn_opt")
              )
            } yield obj.filter(_._1.matches(s"$prefix\\d+"))
              .flatMap {
                case (str, JArray(list)) => {
                  val num = str.replaceAllLiterally(prefix, "")
                  list.map(ClFeat.fromJson)
                    .map(x => x.copy(name = s"$group_name.$num.${x.name}"))
                }
                case (_, x) => throw new Exception(s"${compactJson(renderJValue(x))} is not a valid $group_name")
              }).flatten

            //            log(s"static time: $time")
            //            obj.filter(_._1.matches("""Dynamic-Time-\d+"""))
            //              .sortBy(_._1)
            //              .foreach(log)

            status match {
              case "ok" =>
                ClFeatGroup(
                  features = stat_opt.map(ClFeat.fromJson).map(x => x.copy(name = s"stat_opt.${x.name}"))
                    ++ stat_feat.map(ClFeat.fromJson).map(x => x.copy(name = s"static.${x.name}")) ++ dyn_feats
                )
              case "OPTIMUM FOUND" => throw new FeatureCalcError(FeatureCalcError.Reason.PRESOLVED)
              case stat => throw new Exception(s"unexpected status: $stat")
            }
          }
          case _ => throw new Exception(s"${prettyJson(renderJValue(value))} is not a valid ClaspreFeatures object")
        }
      }
      case x => throw new Exception(s"${prettyJson(renderJValue(x))} is not a valid ClaspreFeatures object")
    }
  }
}

case class ClFeat(name: String, value: Double)

object ClFeat {
  def fromJson(x: JValue): ClFeat = x match {
    case JArray(List(JString(name), JDouble(value))) => ClFeat(name, value)
    case JArray(List(JString(name), JInt(value))) => ClFeat(name, value.toDouble)
    case JArray(List(JString(name), JDecimal(value))) => ClFeat(name, value.toDouble)
    case x => throw new Exception(s"${compactJson(renderJValue(x))} is not a valid feature")
  }
}

object ClaspreFeatureFactory {
  def newDefault() = new ClaspreFeatureFactory(timeout_sec = Some(10))
}

class ClaspreFeatureFactory(timeout_sec: Option[Int] = None, /* default ~= infinity*/
                            n_restarts: Option[Int] = None, /* default = 3 */
                            restart_after_n_conflicts: Option[Int] = Some(512), // default: ?= 1024
                            //                            restart_after_n_conflicts: Option[Int] = Some(256), // default: ?= 1024
                           ) extends FeatureFactory {

  /**
    *
    * @return the name of the feature factory. Must be the same name as the one the FeatureGroup,
    *         { @code calcFeatures(CbCttInstance)} returns.
    */
  override def name(): String = "claspre2"

  /**
    * @param instance the problem instance to calculate the features of
    * @return the feature values
    */
  override def calcFeatures(instance: CbCttInstance, formulation: Formulation): FeatureGroup = {
    import sys.process._

    val claspre = (List(OsUtils.getBin("claspre2").toString)
      ++ timeout_sec.map(timeout_sec => s"--time-limit=$timeout_sec")
      ++ restart_after_n_conflicts.map(n => s"--restart=$n")
      ++ n_restarts.map(n => s"--solve-limit=$n"))
    val gringo = List("gringo", "-o", "smodels")
    val cmd = Seq(
      "timeout",
      "--signal=6",
      s"--kill-after=1",
      (timeout_sec.getOrElse{println("NO TIMEOUT WAS SET."); 30} * 1.5).toString,
      "bash", "-c",
      gringo.mkString(" ") ++ " | " ++ claspre.mkString(" ")
      )

    //val claspre = (List(OsUtils.getBin("claspre2").toString)
      //++ timeout_sec.map(timeout_sec => s"--time-limit=$timeout_sec")
      //++ restart_after_n_conflicts.map(n => s"--restart=$n")
      //++ n_restarts.map(n => s"--solve-limit=$n"))
    //val gringo = List("gringo", "-o", "smodels")
    //val cmd = (gringo #| claspre)

    val src = AspAlgorithm.getAspSource(instance, formulation)

    def pipe(in: java.io.InputStream, out: java.io.OutputStream): Unit = IOUtils.copy(in, out)

    var features: Try[ClFeatGroup] = Failure(new IOException("No output."))
    val io = new ProcessIO(
      in => {
        pipe(src, in)
        in.close()
      }, 
      out => {
        features = Try(ClFeatGroup.fromJson(parseJson(out)))
      }, 
      err => {
        //      IOUtils.copy(err, System.err)
      },
      daemonizeThreads = false
    )
    val proc = cmd run io
    val f = Future{proc.exitValue()} (ExecutionContext.fromExecutor((command: Runnable) => command.run()))
    val code = try {
      log("waiting for claspre ... ")
      Await.result(f, Duration(timeout_sec.getOrElse(10) * 1.5, TimeUnit.SECONDS))
    } catch {
      case _: Throwable =>
        log("claspre -> timeout. ")
        proc.destroy()
        throw new FeatureCalcError(FeatureCalcError.Reason.TIMEOUT)
    }
    //log("finished. ")
    code match {
      case 124  => throw new FeatureCalcError(FeatureCalcError.Reason.TIMEOUT)
      case 0 | 10 | 1 | 20  => {
        // 20 presolved -> infeasible
        features match {
          case Failure(e: FeatureCalcError) => throw e
          case Failure(f) => {
            log("exception in claspre")
            throw new FeatureCalcError(f)
          }
          case Success(clasp_feat) => clasp_feat.toFeatureGroup(this)
        }
      }
          case exit => {
            log("2 exception in claspre")
            throw new FeatureCalcError(new java.io.IOException(s"clapre exited with code $exit"))
          }
    }
  }
  def log(msg: String) = {
    // System.err.println(msg)
  }
}
