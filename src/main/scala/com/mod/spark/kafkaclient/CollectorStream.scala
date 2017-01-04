package com.mod.spark.kafkaclient

import _root_.kafka.serializer.StringDecoder
import com.mod.spark.utils.ContextUtilities._
import com.mod.spark.utils.Models.LogWithLocation
import org.apache.spark.SparkContext
import org.apache.spark.sql.SaveMode
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

/**
  * Created by mehmetoguzdivilioglu on 02/01/2017.
  */
object CollectorStream {
  def main(args: Array[String]): Unit = {
    val sc = createSparkContext("GeoLogSpark")
    val sqlContext = sc.getSQLContext
    import sqlContext.implicits._
    val batchPeriod = Seconds(5)

    def streamingApp(sc: SparkContext, batchDuration: Duration) = {
      val ssc = new StreamingContext(sc, batchDuration)
      val geocodedLogGeneratorConfiguration = Configuration.GeocodedLogGeneratorConfiguration
      val topic = geocodedLogGeneratorConfiguration.kafkaTopic

      val kafkaDirectParams = Map(
        "metadata.broker.list" -> "localhost:9092",
        "group.id" -> "geo",
        "auto.offset.reset" -> "largest"
      )

      val hdfsPath = geocodedLogGeneratorConfiguration.hdfsPath

      val kafkaDirectStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaDirectParams, Set(topic))

      val geocodedLogStream = kafkaDirectStream.transform(input =>
        input.flatMap(keyValue => {
          val line = keyValue._2
          println(line)
          val columns = line.split("\\t")
          if (columns.length == 5) {
            Some(LogWithLocation(columns(0).toDouble, columns(1).toDouble))
          }
          else {
            None
          }
      }))
      geocodedLogStream.foreachRDD { rdd =>
        val geocodedLogDF = rdd
          .toDF()
          .selectExpr("longitude", "latitude")
        geocodedLogDF
          .write
          .mode(SaveMode.Append)
          .parquet(hdfsPath)
      }
      ssc
    }
    val creatingFunc = () => streamingApp(sc, batchPeriod)
    val ssc = StreamingContext.getActiveOrCreate(creatingFunc)
    ssc.start()
    ssc.awaitTermination()
  }
}
