package com.mod.spark.main

import org.apache.spark.SparkContext
import com.mod.spark.utils.ContextUtilities._
import com.mod.spark.kafkaclient.Configuration._
import com.mod.spark.utils.Models._
import com.mod.spark.utils.GeoUtilities._

object DistanceCalculator extends java.io.Serializable {
  def main(args: Array[String]): Unit = {
    implicit val sc = createSparkContext("GeoLogSpark")
    implicit val path = GeocodedLogGeneratorConfiguration.hdfsPath
    val result = getSorted
    println(result)
  }

  private def getRDD(implicit sc: SparkContext, path: String) = {
    sc.getSQLContext.read.parquet(path).map(row => {
      LogWithLocation(row(0).toString.toDouble, row(1).toString.toDouble)
    })
  }

  case class Supplier(name: String, x: Double, y: Double)

  val supplierLocations = List[Supplier](Supplier("s1", 0.219319, 50.039481), Supplier("s2", 0.119319, 50.129481), Supplier("s3", 0.019319, 50.089481))

  def map(implicit sc: SparkContext, path: String) = {
    val result0 = getRDD.map(log => {
      val distanceTuplePerSupplier = supplierLocations.map(supplier => (supplier.name, (1, distance(supplier.x, supplier.y, log.longitude, log.latitude, "K"))))
      distanceTuplePerSupplier
    })
    val result1 = result0.flatMap(x => x)
    result1
  }

  def getWeights(implicit sc: SparkContext, path: String) = map.reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))

  def getSorted(implicit sc: SparkContext, path: String) = getWeights.sortBy(x => x._2._2 / x._2._1, false).collect().toList

}