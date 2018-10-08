package spark.rddtest

import org.apache.spark._
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
//import org.apache.spark._

/**
  * Created by gongp on 2018/9/25.
  */
object SparkDataFrameAndDataSet {

  case class User(userID:String ,gender:String, age:String, occupation:String ,zipcode:String)

  def main(args: Array[String]): Unit = {
    var dataPath = "data/ml-1m/"
    val conf = new SparkConf()
    if (args.length > 2) {
      dataPath = args(0)
    } else {
      conf.setMaster("local[1]")
    }
//    conf.set("LOG_LEVEL","ERROR")
    conf.set("spark.sql.shuffle.partitions","2")

    val spark = SparkSession.builder().appName("spark sql example").config(conf).getOrCreate()
    val sc =spark.sparkContext

    val DATA_PATH=dataPath
    val userRDD=sc.textFile(DATA_PATH+"users.dat")
    //通过显示的为rdd注入schema，将其转换为dataframe
    import spark.implicits._

    val user2RDD = userRDD.map(_.split("::")).map(p=>User(p(0),p(1),p(2),p(3),p(4)))
    val userDataFrame=user2RDD.toDF()
    userDataFrame.take(10).foreach(println)
    println("打印前十个用户信息")
    println(userDataFrame.count())
    println("打印用户总量")

    //通过反射为RDD注入schema，将其变换为DataFrame
    val schemaString="userID gender age occupation zipcode"
//    val schema = StructType(schemaString.split(" ").map(fieldName => StructField(fieldName,StringType,true)))
//    val schema = StructType(schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true)))
    val schema = StructType(schemaString.split(" ").map(fieldName => StructField(fieldName,StringType,true)))
    val userRDD2 = userRDD.map(_.split("::")).map(p => Row(p(0), p(1).trim, p(2).trim, p(3).trim, p(4).trim))
    val userDataFrame2 = spark.createDataFrame(userRDD2,schema)
    userDataFrame2.take(10).foreach(println)
    println("打印前十个用户信息")
    println(userDataFrame2.count())
    println("打印用户总量")
    userDataFrame2.write.mode(SaveMode.Overwrite).json(DATA_PATH+"/user.json")
    userDataFrame2.write.mode(SaveMode.Overwrite).parquet(DATA_PATH+"/user.parqut")

    /**
      * 读取json格式数据
      */
    val userjsondf = spark.read.format("json").load(DATA_PATH+"/user.json")
    userjsondf.take(10).foreach(println)
    println("第一种方法读取json数据")
    /**
      * 读取json格式数据
      */
    val userjsondf2= spark.read.json(DATA_PATH+"/user.json")
    userjsondf2.take(10).foreach(println)
    println("第二种方法读取json数据")
    /**
      * 读取parqut格式数据
      */
    val userparqdf=spark.read.parquet(DATA_PATH+"/user.parqut")
    userparqdf.take(10).foreach(println)
    println("parquet格式读取数据")
    /**
      * 读取parqut格式文件
      */
    val userparqdf2=spark.read.format("parquet").load(DATA_PATH+"/user.parqut")
    userparqdf2.take(10).foreach(println)
    println("parquet格式读取数据")

    /**
      * 读取并用sql读取
      */
    val ratingRDD= sc.textFile(DATA_PATH+"ratings.dat")
    val ratingSchemaString = "userID movieID Rating Timestamp"
    val ratingSchema  = StructType(ratingSchemaString.split(" ").map(fieldName => StructField(fieldName,StringType,true)))
    val ratingRDD2= ratingRDD.map(_.split("::")).map(p=>Row(p(0),p(1).trim,p(2).trim,p(3).trim))
    val ratingDataFrame = spark.createDataFrame(ratingRDD2,ratingSchema)

    val mergedDataFrame = ratingDataFrame.filter("movieID = 2016")
      .join(userDataFrame,userDataFrame("userID")===ratingDataFrame("userID"),"inner")
      .select("gender","age")
      .groupBy("gender","age")
      .count()
    println("用dataframe 过滤后打印总数")
    mergedDataFrame.collect().foreach(println)
    println("用dtaframe 过滤后打印所有")
    userDataFrame.createTempView("users")
    val groupedUsers = spark.sql("select gender ,age , count(*) as n from users group by gender ,age")
    groupedUsers.show()
    println("spark sql 查询后展示")
    userDataFrame.map(u=>(u.getAs[String]("userID").toLong ,u.getAs[String]("age").toInt+1)).take(10).foreach(println)
    println("dataframe 新格式打印")
    sc.stop()

  }


}
