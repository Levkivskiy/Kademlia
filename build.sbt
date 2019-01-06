name := "Kadem"

version := "0.1"

scalaVersion := "2.12.8"

val akka = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-persistence" % "2.5.19",
)

val testDep = Seq(
  "com.typesafe.akka" %% "akka-testkit" % "2.5.19" % Test,
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test",
)

libraryDependencies ++= akka ++ testDep
 