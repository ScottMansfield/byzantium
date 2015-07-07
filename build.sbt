name := "byzantium"

version := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.assembla.scala-incubator" % "graph-core_2.11" % "1.9.3",
  "org.scala-lang" % "scala-xml" % "2.11.0-M4"
)

mainClass in Compile := Some("io.byzantium.Main")