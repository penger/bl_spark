package localtest

import java.io.PrintWriter
import java.net.ServerSocket

/**
  * Created by GP39 on 2016/9/6.
  * Streaming
  */
object Sender {
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
    if(args.length !=2 ){
      println("usage:<port> <millisecond>")
      System.exit(1)
    }

    val listener = new ServerSocket(args(0).toInt)
    while(true){
      val socket = listener.accept()
      new Thread(){
        override def run={
          println("Got client connected from: " + socket.getInetAddress)
          val out = new PrintWriter(socket.getOutputStream(),true)
          while(true){
            Thread.sleep(args(1).toLong)
            val content = generateContent(index)
            println(content)
            out.write(content+"\n")
            out.flush()
          }
        }
      }.start()
    }
  }

}
