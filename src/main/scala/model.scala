package com.dhs.xgboost
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{
  DoubleType,
  StringType,
  StructField,
  StructType
}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.StringIndexer
import ml.dmlc.xgboost4j.scala.spark.XGBoostClassifier
import org.apache.spark.ml.{Pipeline, PipelineModel}
import ml.combust.mleap.spark.SparkSupport._
import resource._
import ml.combust.bundle.BundleFile
import org.apache.spark.ml.bundle.SparkBundleContext
object XGB {
  def main(args: Array[String]): Unit = {
    println("started program")
    val spark = SparkSession.builder().getOrCreate()
    val schema = new StructType(
      Array(
        StructField("sepal length", DoubleType, true),
        StructField("sepal width", DoubleType, true),
        StructField("petal length", DoubleType, true),
        StructField("petal width", DoubleType, true),
        StructField("class", StringType, true)
      )
    )
    println("spark instantiated")
    val rawInput =
      spark.read
        .schema(schema)
        .option("header", "true")
        .csv("/data/iris.csv")
    println("data read")
    val stringIndexer = new StringIndexer()
      .setInputCol("class")
      .setOutputCol("classIndex")

    val vectorAssembler = new VectorAssembler()
      .setInputCols(
        Array("sepal length", "sepal width", "petal length", "petal width")
      )
      .setOutputCol("features")

    val xgbParam = Map(
      "eta" -> 0.1f,
      "missing" -> -999,
      "objective" -> "multi:softprob",
      "num_class" -> 3,
      "num_round" -> 100,
      "num_workers" -> 2
    )
    val xgbClassifier = new XGBoostClassifier(xgbParam)
      .setFeaturesCol("features")
      .setLabelCol("classIndex")

    xgbClassifier.setMaxDepth(2)

    val scorePipeline = new Pipeline()
      .setStages(Array(vectorAssembler, xgbClassifier))

    val trainPipeline = new Pipeline()
      .setStages(Array(stringIndexer, scorePipeline))

    val xgbClassificationModel = trainPipeline.fit(rawInput)
    println("model fit")
    xgbClassificationModel.write.overwrite().save("/data/xgboost")

    val sbc = SparkBundleContext().withDataset(
      xgbClassificationModel.transform(rawInput)
    )
    for (bf <- managed(BundleFile("jar:file:/data/xgboosttest.zip"))) {
      xgbClassificationModel.writeBundle.save(bf)(sbc).get
    }

    spark.stop()

  }
}
