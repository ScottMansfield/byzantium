name := "byzantium"

version := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.assembla.scala-incubator" %% "graph-core" % "1.9.3",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
  "org.apache.spark" %% "spark-core" % "1.4.0"
)

mainClass in Compile := Some("io.byzantium.Main")