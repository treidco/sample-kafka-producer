name := "twitter-kafka-producer"

version := "1.0"

scalaVersion := "2.12.1"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "0.10.2.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.17"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4"
libraryDependencies += "com.fortysevendeg" %% "scalacheck-datetime" % "0.2.0"

val circeVersion = "0.7.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
