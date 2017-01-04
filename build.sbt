name := "MyProject"
version := "1.0"
scalaVersion := "2.11.8"
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.last
  case PathList("javax", "activation", xs@_*) => MergeStrategy.last
  case PathList("org", "apache", xs@_*) => MergeStrategy.last
  case PathList("com", "google", xs@_*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs@_*) => MergeStrategy.last
  case PathList("com", "codahale", xs@_*) => MergeStrategy.last
  case PathList("com", "yammer", xs@_*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.0" % "provided"
//libraryDependencies += "com.databricks" % "spark-xml_2.11" % "0.4.0"
libraryDependencies += "com.typesafe" % "config" % "1.3.0"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.8.2.1"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.0" % "provided"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-assembly_2.11" % "1.6.0"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.6.0" % "provided"

libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "1.6.0" % "provided"

libraryDependencies += "joda-time" % "joda-time" % "2.8.1"

libraryDependencies += "org.joda" % "joda-convert" % "1.7"

libraryDependencies += "org.typelevel" %% "cats" % "0.7.2"