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

object XGB {
  def main(args: Array[String]): Unit = {
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
    val rawInput = spark.read.schema(schema).csv("iris.csv")

    val stringIndexer = new StringIndexer()
      .setInputCol("class")
      .setOutputCol("classIndex")
      .fit(rawInput)
    val labelTransformed = stringIndexer.transform(rawInput).drop("class")

    val vectorAssembler = new VectorAssembler()
      .setInputCols(
        Array("sepal length", "sepal width", "petal length", "petal width")
      )
      .setOutputCol("features")
    val xgbInput = vectorAssembler
      .transform(labelTransformed)
      .select("features", "classIndex")

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

    val xgbClassificationModel = xgbClassifier.fit(xgbInput)
    xgbClassificationModel.write.overwrite().save("xgboost")
    spark.stop()

  }
}
