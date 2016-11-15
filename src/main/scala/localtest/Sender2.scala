package localtest

import java.io.PrintWriter
import java.net.ServerSocket

/**
  * Created by GP39 on 2016/9/6.
  * Streaming
  */
object Sender2 {
  def generateContent(index: Int) :String = {
    import scala.collection.mutable.ListBuffer
    val charList = ListBuffer[Char]()
    for(i <- 65 to 90){
      charList += i.toChar
    }
    val charArray = charList.toArray
    charArray(index).toString
  }

  def index={
    import java.util.Random
    val ran = new Random()
    ran.nextInt(7)
  }

  def main(args: Array[String]): Unit = {

    new Thread(){
      override def run={
        while(true){
          Thread.sleep(500)
          val content = generateContent(index)
          println(content)
        }
      }
    }.start()
  }

}
