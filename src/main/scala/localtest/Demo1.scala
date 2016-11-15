//package localtest
//
//import scala.io.Source
//
///**
//  * Created by GP39 on 2016/9/6.
//  */
//object Demo1 {
//
//  def formatArgs(strs: Array[String]) = strs.mkString(",")
//
//  def main(args: Array[String]): Unit = {
//    for (i <- 1 to 10 ) print(i)
//    println()
//    for (i <- 1 to 10 reverse) print(i)
//    println()
//    val a = new Array[Int](3)
//    a(0)=1
//    a(1)=2
//    for(i <- a){
//      print(i+ "xxxx  ")
//    }
//
//
//    println(a.length)
//    val b = Array(1,3,4,56,7,8,45)
//    println("------------")
//    for (i <- b){
//      print(i)
//    }
//    b.update(2,23)
//    println(b.mkString("[",",","]"))
//    println(b.mkString("|"))
//
//   println("123".toInt)
//
//    println("helloWorld".exists(_.isUpper))
//
//    println("helloWorld".hashCode)
//    println("helloWorld".hashCode)
//    println("helloWorld".hashCode)
//
//    print(max(12,45))
//    println("kldjfalsdfjX".exists(_.isUpper))
//
//    println("jdklfasfl".exists(_.isLower))
//
//
//    val movieSet = Set("Hitch","Poltergeist")
//    movieSet.add("shark")
//    movieSet += "56"
//    movieSet.foreach(x => print(x))
//
//
//    val pair = ("fdjalsf",23,"df")
//    print(pair._1)
//
//
//
//    //val r=formatArgs(Array("one","two","three"))
//    //assert(r == "one,two,one")
//
//
//    //读取文件
//    var maxx =0
//    for (i <- Source.fromFile("e://history").getLines()){
//      if(maxx < i.length){
//        maxx=i.length
//      }
//      println( i.length + "             "+ i)
//    }
//    println("max length is :"+ maxx)
//
//    val x = new HelloGp
//    x.data.foreach(x => println(x))
//
//
//  }
//
//  def max(x:Int,y:Int):Int = if(x>y) x else y
//
//
//
//
//
//}
