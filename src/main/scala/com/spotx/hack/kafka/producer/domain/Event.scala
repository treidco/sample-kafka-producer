package com.spotx.hack.kafka.producer.domain

import java.time.ZonedDateTime

trait Event {
  def eventId: String

  def timestamp: String
}

case class Visit(override val eventId: String,
                 override val timestamp: String,
                 userId: String,
                 url: String
                ) extends Event

object Visit {
  def apply(eventId: Int,
            timestamp: ZonedDateTime,
            userId: Int,
            url: String): Visit = {
    Visit(
      eventId.toString,
      timestamp.withNano(0).withFixedOffsetZone.toString,
      userId.toString,
      url)
  }
}