package localtest

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by GP39 on 2016/9/6.
  */
object SparkProgramGuide {
  def main(args: Array[String]): Unit = {

    System.setProperty("HADOOP_USER_NAME","hdfs")
    System.setProperty("hadoop.home.dir","E://BaiduYunDownload")

    val conf = new SparkConf().setAppName("spark guide").setMaster("local")
    val sc = new SparkContext(conf)

    val data = Array(1,23,4,5,6,7)
    val distData  = sc.parallelize(data)
    val answer = distData.reduce((a,b) => a+b)
    println(answer)


    val distFile = sc.textFile("a.q")
    val wholeCount = distFile.map(s=>s.length).reduce((a,b)=>a+b)
    println("file have "+ wholeCount+ "counts ")

  }

}
