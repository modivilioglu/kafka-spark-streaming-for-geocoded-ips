package com.mod.spark.utils

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object ContextUtilities {

  def createSparkContext(appName: String) = {
    val conf = new SparkConf().setAppName(appName)
    val sc = SparkContext.getOrCreate(conf)
    sc
  }

  trait SparkContextBehaviour {
    def getSQLContext: SQLContext
  }

  implicit def toSparkContext(sc: SparkContext) = new SparkContextBehaviour {
    override def getSQLContext: SQLContext = SQLContext.getOrCreate(sc)
  }

}
