package spark.ml

import org.apache.spark.mllib.optimization.SimpleUpdater
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by gongp on 2018/9/28.
  * 线性回归
  */
object LinearRegressionExample {
  def main(args: Array[String]): Unit = {
    val dataPath = "data/mllib/sample_linear_regression_data.txt"
    val conf = new SparkConf().setAppName("linear regression example").setMaster("local[1]")
    val sc  = new SparkContext(conf)


    val examples = MLUtils.loadLibSVMFile(sc,dataPath).cache()
    val splits = examples.randomSplit(Array(0.8 ,0.2))
    val training = splits(0).cache()
    val test = splits(1).cache()

    val numTraining = training.count()
    val numTest = test.count()

    println(s"Trainnig is $numTraining and Test is $test")

    examples.unpersist(blocking = false)

    val updater = new SimpleUpdater()
    val numIterations = 10
    val stepSize = 10
    val regParam = 0.1

    val algorithm = new LinearRegressionWithSGD()

    algorithm.optimizer
      .setNumIterations(numIterations)
      .setStepSize(stepSize)
      .setUpdater(updater)
      .setRegParam(regParam)

    val model = algorithm.run(training)

    val prediction = model.predict(test.map(_.features))
    val predictionAndLabel = prediction.zip(test.map(_.label))

    val loss = predictionAndLabel.map{
      case (p,1)=>
        val err= p-1
        err*err
    }.reduce(_+_)

    val rmse = math.sqrt(loss/numTest)

    sc.stop()





  }
}
