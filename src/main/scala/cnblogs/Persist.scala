package cnblogs

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by GP39 on 2016/9/19.
  */
object Persist {
  def main(args: Array[String]): Unit = {
    val sc = new SparkConf()
    val scc = new SparkContext()
    val line = scc.parallelize(List("a ","b","c","d"))
    line.persist(StorageLevel.DISK_ONLY)
    line.count()
    val count = line.countByValue()
    println(count)

  }

}
