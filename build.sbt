name := "xgboost"

version := "0.1"

organization := "com.dhs"

scalaVersion := "2.11.12"

sbtVersion := "0.13"

// The dependencies are in Maven format, with % separating the parts.
// Notice the extra bit "test" on the end of JUnit and ScalaTest, which will
// mean it is only a test dependency.
//
// The %% means that it will automatically add the specific Scala version to the dependency name.
// For instance, this will actually download scalatest_2.9.2

libraryDependencies += "ml.dmlc" %% "xgboost-jvm" % "1.0.0"
libraryDependencies += "ml.dmlc" %% "xgboost4j-spark" % "1.0.0"
libraryDependencies += "ml.combust.mleap" %% "mleap-spark" % "0.16.0"
libraryDependencies += "ml.combust.mleap" %% "mleap-spark-extension" % "0.16.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.5" % "provided"

assemblyMergeStrategy in assembly := {
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case PathList("META-INF", xs @ _*)       => MergeStrategy.discard
  case x                                   => MergeStrategy.first
}
