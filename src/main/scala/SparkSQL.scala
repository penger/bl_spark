import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
/**
  * Created by GP39 on 2016/9/20.
  */
object SparkSQL {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("spark sql").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val rowRdd = sc.textFile("m78sit:8022")
        .map(line=>{
          val data = line.split(",")
          val list = mutable.ArrayBuffer[Any]()
          list.append(data(0))
          list.append(data(3).toInt)
          Row.fromSeq(list)
        })
    val fields = new ArrayBuffer[StructField]()
    fields.append(StructField("name",StringType,true))
    fields.append(StructField("age",IntegerType,true))

    val schema = StructType(fields)

    val rdd = sqlContext.applySchema(rowRdd,schema)
    rdd.registerTempTable("people")

    sqlContext.sql("select * from people where age > 18").take(3).foreach(println)

    sc.stop();

  }

}
