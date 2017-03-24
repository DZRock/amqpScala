name := "amqpScala"

val _version = "0.4.0-SNAPSHOT"
version := "1.0"

scalaVersion := "2.11.8"

resolvers += "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++=Seq (
  "org.scaldi" % "scaldi_2.11" % "0.5.8",
  "com.github.aselab" %% "scala-activerecord" % _version,
  "org.slf4j" % "slf4j-nop" % "1.7.24",
  "com.github.sstone" %% "amqp-client" % "1.5",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.5.0-RC1",
  "org.json4s" %% "json4s-native" % "3.5.1",
  "com.h2database" % "h2" % "1.4.193"
)