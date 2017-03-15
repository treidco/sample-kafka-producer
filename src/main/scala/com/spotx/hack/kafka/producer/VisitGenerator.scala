package com.spotx.hack.kafka.producer

import java.time.ZonedDateTime

import akka.actor.{Actor, Props}
import akka.event.Logging
import com.spotx.hack.kafka.producer.domain.Visit
import org.scalacheck.Gen


object VisitGenerator {

  def offset(x: Int): Props = Props(new VisitGenerator(x))

}

class VisitGenerator(offset: Int = 1) extends Actor {


  val log = Logging(context.system, this)

  val urls = List(
    "www.spotxchange.com",
    "www.google.com",
    "news.ycombinator.com",
    "www.visitbelfast.com"
  )

  def receive = {

    case GenerateVisitors(visits) => {
      log.info(s"Generator received generate $visits visits")
      sender ! generate(visits)
    }

  }

  private def generate(q: Int): List[Visit] = {

    (for {
      id: Int <- offset until offset + q
      timestamp: ZonedDateTime <- generateZonedTimeStamp
      userId <- Gen.oneOf(10 to 20).sample
      url: String <- Gen.oneOf(urls).sample
    } yield Visit(id, timestamp, userId, url)).toList

  }

  private def generateZonedTimeStamp = {

    import java.time._

    import com.fortysevendeg.scalacheck.datetime.GenDateTime.genDateTimeWithinRange
    import com.fortysevendeg.scalacheck.datetime.instances.jdk8._
    import com.fortysevendeg.scalacheck.datetime.jdk8.granularity.seconds

    val from = ZonedDateTime.now().minusMinutes(5)
    //    val from = ZonedDateTime.of(2015, 3, 24, 0, 0, 0, 0, ZoneOffset.UTC)
    val to = Duration.ofMinutes(5)

    val generator: Gen[ZonedDateTime] = genDateTimeWithinRange(from, to)
    generator.sample.map(zdt => zdt.withZoneSameInstant(ZoneOffset.UTC))

  }


}
