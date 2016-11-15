package localtest

import org.apache.spark.rdd.RDD

/**
  * Created by GP39 on 2016/11/2.
  */
object A {



}

class SearchFunction(val query:String){
  def isMatch(s:String):Boolean={
    s.contains(query)
  }

  def getMatchs(rdd:RDD[String]):RDD[String]={
    rdd.filter(isMatch)
  }


}
