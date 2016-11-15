import java.io.File
import java.util.Scanner

/**
  * Created by GP39 on 2016/11/3.
  */
object HanderPattern {
  def main(args: Array[String]): Unit = {
    withHander(new File("e://history"),hander => {
      while (hander.hasNext()){
        println(hander.next())
      }
    })
  }

  def withHander(f:File,hander:Scanner => Unit): Unit ={
    val scanner = new Scanner(f)
    hander(scanner)
    scanner.close()
  }

}
