package com.spotx.hack.kafka.producer

import akka.actor.{Actor, ActorSystem, Props}
import com.spotx.hack.kafka.producer.domain.Visit

sealed trait Action

case object Start extends Action

case class GenerateVisitors(visitors: Int)

class VisitPrinter extends Actor {

  import io.circe.generic.auto._
  import io.circe.syntax._

  def receive = {
    case v: Visit => println(s"Some data: ${v.asJson.noSpaces}")
  }

}

class VisitKafkaProducer extends Actor {

  def receive = {

    case GenerateVisitors(x) => {

    }

  }
}


object Producer extends App {


  val system = ActorSystem("Producer")
  val visitPrinter = system.actorOf(Props[VisitPrinter], "visitPrinter")
  val visitGenerator = system.actorOf(VisitGenerator.offset(10), "visitGenerator")
  val coordinator = system.actorOf(Coordinator.start(visitGenerator, visitPrinter), "coordinator")

  coordinator ! GenerateVisitors(10)

}
