package com.spotx.hack.kafka.producer

import akka.actor.{Actor, ActorSystem, Props}
import com.spotx.hack.kafka.producer.domain.Visit

case class GenerateVisitors(visitors: Int)

case class JsonVisitor(value: String)

class VisitPrinter extends Actor {

  import io.circe.generic.auto._
  import io.circe.syntax._

  def receive = {
    case v: Visit => sender ! (v, JsonVisitor(s"${v.asJson.noSpaces}"))
  }

}

object Producer extends App {

  val system = ActorSystem("Producer")
  val visitPrinter = system.actorOf(Props[VisitPrinter], "visitPrinter")
  val visitGenerator = system.actorOf(VisitGenerator.offset(1000), "visitGenerator")
  val visitKafkaProducer = system.actorOf(Props[KafkaVisitProducer], "kafkaProducer")
  val coordinator = system.
    actorOf(Coordinator.start(visitGenerator, visitPrinter, visitKafkaProducer), "coordinator")

  coordinator ! GenerateVisitors(10)

}
