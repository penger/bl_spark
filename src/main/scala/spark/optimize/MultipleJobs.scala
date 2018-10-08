package spark.optimize

import java.util.concurrent.{Callable, Executors}

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by gongp on 2018/9/28.
  * 并发统计文件行数
  */
object MultipleJobs {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[1]")

    val sc = new SparkContext(conf)
    sc.hadoopConfiguration.set("mapreduce.input.fileinputformat.split.minsize","20000000")

    val executors = Executors.newFixedThreadPool(2)
    val future = executors.submit(new Callable[Long] {
      override def call(): Long = {
        val DATA_PATH = "/home/hadoop/input/data_wide_3.txt"
        val rdd = sc.textFile(DATA_PATH)
        return  rdd.count()
      }
    })
    println(future.get())

    sc.stop()

  }

}
