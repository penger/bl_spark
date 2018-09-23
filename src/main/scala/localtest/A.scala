package localtest

import org.apache.spark.rdd.RDD

/**
  * Created by GP39 on 2016/11/2.
  */
object A {

  def main(args: Array[String]): Unit = {
    print("hello world !")
  }

}

class SearchFunction(val query:String){
  def isMatch(s:String):Boolean={
    s.contains(query)
  }

  def getMatchs(rdd:RDD[String]):RDD[String]={
    rdd.filter(isMatch)
  }


}
