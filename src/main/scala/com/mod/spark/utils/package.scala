package com.mod.spark.utils

/**
  * Created by mehmetoguzdivilioglu on 03/01/2017.
  */
package object Models {

  case class LogWithLocation(longitude: Double, latitude: Double)

}

package object GeoUtilities {
  import cats._
  import cats.implicits._
  import cats.{Id, Monad}

  import scala.language.higherKinds

  private def degreeToRadian(deg: Double) = (deg * Math.PI / 180.0);

  private def radianToDegree(rad: Double) = (rad * 180 / Math.PI);

  private def operation(d: Double) = d * 60.0000 * 1.1515;

  private def convert(distance: Double, unit: String) = if (unit == "K") distance * 1.609344; else if (unit == "N") distance * 0.8684;

  def distance(longitude1: Double, latitude1: Double, longitude2: Double, latitude2: Double, unit: String): Double = {
    val theta: Double = longitude1 - longitude2;
    var initialResult: Double = Math.sin(degreeToRadian(latitude1)) * Math.sin(degreeToRadian(latitude2)) + Math.cos(degreeToRadian(latitude1)) * Math.cos(degreeToRadian(latitude2)) * Math.cos(degreeToRadian(theta));

    val initialResultMonad = Monad[Id].pure(initialResult)

    val distResult = for {
      x <- initialResultMonad
      distanceResult1 <- Monad[Id].pure(Math.acos(x))
      distanceResult2 <- Monad[Id].pure(radianToDegree(distanceResult1))
      distanceResult3 <- Monad[Id].pure(operation(distanceResult2))
      distanceResult4 <- Monad[Id].pure(convert(distanceResult3, unit))
    } yield distanceResult1
    distResult
  }
}
