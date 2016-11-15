package cnblogs

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by GP39 on 2016/9/13.
  * 访问时间 用户id 查询词 url排名 点击的顺序号 用户点击的url
  * 20111230000011
  * 58e7d0caec23bcb4daa7bbcc4d37f008
  * 张国立的电视剧
  * 2
  * 1
  * http://tv.sogou.com/vertical/2xc3t6wbuk24jnphzlj35zy.html?p=40230600
  */
object Class3 {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("sogouResult")
    val sc =new SparkContext(sparkConf)
    //session 查询次数排行
    val rdd1 = sc.textFile("d:\\SougouQ1.txt").map(_.split("\t").filter(_.length==6))
    rdd1.filter(x => x.contains("abc")).count
    val rdd2 = rdd1.map(x=>(x(1),1)).reduceByKey(_+_).map(x=>(x._2,x._1)).sortByKey(false).map(x=>(x._2,x._1))
    rdd2.saveAsTextFile("nttt")
    sc.stop()

  }

}
