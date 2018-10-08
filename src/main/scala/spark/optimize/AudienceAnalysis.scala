package spark.optimize

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
  * Created by gongp on 2018/9/27.
  */
object AudienceAnalysis {

  lazy val nameIndexMap={
    val nameIndexMap = mutable.HashMap.empty[String ,Int]
    val basicNames = Seq("first_name","last_name","email","company", "job", "street_address", "city",
      "state_abbr", "zipcode_plus4", "url", "phone_number", "user_agent", "user_name")
    nameIndexMap ++= basicNames zip (0 to 12)

    nameIndexMap.foreach(x=>println(x._1+ "   "+ x._2))

    for( i <- 0 to 328){
      nameIndexMap ++= Seq(("letter_" + i, i * 3 + 13), ("number_" + i,  i * 3 + 14), ("bool_" + i, i * 3 + 15))
    }
    nameIndexMap
  }


  def $(name:String):Int=nameIndexMap.getOrElse(name,0)

  /**
    *  select city,state_abbr,count(*) from ${tableName} where last_name not like 'w%'
    *  and email like '%com%' and letter_77 like 'r'
    *  gorup by city ,state_abbr
    * @param args
    */

  def main(args: Array[String]): Unit = {

    nameIndexMap.foreach(x=>println(x._1+ "   "+ x._2))


    val conf = new SparkConf()
    val sc = new SparkContext(conf)


    val DATA_PATH="data/input/text"
    val peopleTxtRdd = sc.textFile(DATA_PATH)

    val resultRdd2 = peopleTxtRdd.map(_.split("\\|")).filter{
      p=>p($("last_name")).matches("^w")
    }.map{
      p=>println()
        (((p($("city"))),p($("state_abbr"))),1)
    }.reduceByKey(_+_,2)


    resultRdd2.collect()
  }


}
