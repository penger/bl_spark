package spark.ml

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by gongp on 2018/9/28.
  * K值临近
  */
object KMeansExample {
  def main(args: Array[String]): Unit = {
    var dataPath = "data/mllib/kmeans_data.txt"
    val conf = new SparkConf().setAppName("KMeans example")
    conf.setMaster("local[1]")


    val sc = new SparkContext(conf)
    Logger.getRootLogger.setLevel(Level.WARN)

    val examples = sc.textFile(dataPath).map(line=>Vectors.dense(line.split(" ").map(_.toDouble))).cache()
    val examples_count = examples.count()
    println(examples_count)

    val k =2
    val numIterations = 10

    val model = new KMeans()
      .setInitializationMode(KMeans.RANDOM)
      .setK(k)
      .setMaxIterations(numIterations)
      .run(examples)


    val cost = model.computeCost(examples)

    println("cost is : " + cost)

    sc.stop()


  }
}
