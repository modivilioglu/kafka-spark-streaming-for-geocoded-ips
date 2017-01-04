package com.mod.spark.kafkaclient

import com.typesafe.config.ConfigFactory
/**
  * Created by mehmetoguzdivilioglu on 02/01/2017.
  */
object Configuration {
  private val config = ConfigFactory.load()
  object GeocodedLogGeneratorConfiguration {
    private val weblogGenerator = config.getConfig("generator-config")
    lazy val timeDelta = weblogGenerator.getInt("time-delta")
    lazy val rows = weblogGenerator.getInt("rows")
    lazy val timeMultiplier = weblogGenerator.getInt("time-multiplier")
    lazy val inputPath = weblogGenerator.getString("input-path")
    lazy val numberOfFiles = weblogGenerator.getInt("number-of-files")
    lazy val kafkaTopic = weblogGenerator.getString("kafka-topic")
    lazy val hdfsPath = weblogGenerator.getString("hdfs-path")

  }
}
