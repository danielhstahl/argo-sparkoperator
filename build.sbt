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
val mleapVersion = "0.16.0"
val sparkVersion = "2.4.5"
libraryDependencies += "ml.dmlc" %% "xgboost-jvm" % "1.0.0"
//libraryDependencies += "ml.dmlc" %% "xgboost4j-spark" % "1.0.0"
libraryDependencies += "ml.combust.mleap" %% "mleap-spark" % mleapVersion
libraryDependencies += "ml.combust.mleap" %% "mleap-spark-extension" % mleapVersion
libraryDependencies += "ml.combust.mleap" %% "mleap-xgboost-spark" % mleapVersion //includes xgboost4j-spark:0.90
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided"

assemblyMergeStrategy in assembly := {
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case PathList("META-INF", xs @ _*)       => MergeStrategy.discard
  case x                                   => MergeStrategy.first
}
