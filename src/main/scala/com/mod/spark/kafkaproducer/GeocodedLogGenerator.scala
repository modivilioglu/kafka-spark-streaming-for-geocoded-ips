package com.mod.spark.kafkaproducer

import java.util.Properties

import com.mod.spark.kafkaclient.Configuration
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

import scala.util.Random

/**
  * Created by mehmetoguzdivilioglu on 02/01/2017.
  */
object GeocodedLogGenerator extends App {
  val generatorConfiguration = Configuration.GeocodedLogGeneratorConfiguration

  val productNames = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/products")).getLines().toArray


  val random = new Random()

  val topic = generatorConfiguration.kafkaTopic
  val props = new Properties()

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "LogGenerator")

  val kafkaProducer: Producer[Nothing, String] = new KafkaProducer[Nothing, String](props)

  (1 to generatorConfiguration.numberOfFiles)
    .flatMap(x => (1 to generatorConfiguration.rows))
    .foreach(x => kafkaProducer.send(generateRow))

  kafkaProducer.close()

  def generateRow = {
    val timestamp = System.currentTimeMillis()
    val converted = timestamp + ((System.currentTimeMillis() - timestamp) * generatorConfiguration.timeDelta)

    val longitude = 50.0 + random.nextDouble() % 2
    val latitude = random.nextDouble() % 2
    val ip = "192.X.X.X"
    val product = productNames(random.nextInt(productNames.length - 1))
    val line = s"$longitude\t$latitude\t$converted\t$ip\t$product\n"

    println(line)
    val producerRecord = new ProducerRecord(topic, line)
    producerRecord
  }
}
