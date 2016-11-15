package localtest

import scala.io.Source

/**
  * Created by GP39 on 2016/8/30.
  */
object BreakAndContinue {
  def main(args: Array[String]): Unit = {
    for(i <- 1 to 10 ; j <- 1 to i){
      if(j==1){
        println()
      }
      print("*")
    }


    processFile("e://history",40)
  }

  def processFile(fileName: String, width: Int, line: String) = {
    if(line.length>width){
      println(line.length+ "\t" + line)
    }
  }

  def processFile(fileName:String, width:Int): Unit = {
    val source = Source.fromFile(fileName)
    for(line <- source.getLines ){
      processFile(fileName,width,line)
    }
  }


}
