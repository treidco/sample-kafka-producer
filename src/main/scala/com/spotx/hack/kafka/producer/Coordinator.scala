package com.spotx.hack.kafka.producer

import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging
import com.spotx.hack.kafka.producer.domain.Visit

object Coordinator {
  def start(generator: ActorRef, printer: ActorRef, producer: ActorRef) =
    Props(new Coordinator(generator, printer, producer))
}

class Coordinator(generator: ActorRef, printer: ActorRef, kafkaProducer: ActorRef) extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case GenerateVisitors(x) => {
      log.info("Coordinator received GenerateVisitors")
      generator ! GenerateVisitors(x)
    }
    case v: Visit => {
      log.info(s"Coordinator received a message to print visit ${v.eventId}")
      printer ! v
    }
    case x: List[Visit] => {
      log.info(s"Coordinator received a list of visits of length: ${x.size}")
      x.foreach(self ! _)
    }
    case (v: Visit, j: JsonVisitor) => {
      log.info(s"Coordinator received a JsonVisitor: ${j.value}")
      kafkaProducer ! (v, j)
    }
  }
}
