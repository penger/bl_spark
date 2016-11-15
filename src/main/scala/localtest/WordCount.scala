package localtest

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by GP39 on 2016/9/5.
  */
object WordCount {
  def main(args: Array[String]){
    System.setProperty("HADOOP_USER_NAME","hive")
    System.setProperty("hadoop.home.dir","E://BaiduYunDownload")
    val file = "hdfs://m78sit:8020/user/hdfs/a.q"
    val conf = new SparkConf().setAppName("test spark")
    val sc = new SparkContext(conf)
    val fileData = sc.textFile(file,2).cache()
    val numNots= fileData.filter(line => line.contains("not")).count()
    val numSelects = fileData.filter(line => line.contains("select")).count()
    println("numbers of not:"+ numNots +" number of select :"+numSelects)

  }

}
