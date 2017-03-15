package com.spotx.hack.kafka.producer

import akka.actor.Actor
import cakesolutions.kafka.KafkaProducer.Conf
import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import com.spotx.hack.kafka.producer.domain.Visit
import org.apache.kafka.common.serialization.StringSerializer

object KafkaVisitProducer {

  val producer = KafkaProducer(
    Conf(new StringSerializer(), new StringSerializer(), bootstrapServers = "localhost:9092")
  )

}

class KafkaVisitProducer extends Actor {
  override def receive: Receive = {
    case (v: Visit, j: JsonVisitor) => {
      KafkaVisitProducer.producer.send(KafkaProducerRecord("pageviews", Some(v.eventId), j.value))
    }
  }
}
