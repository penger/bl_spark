package localtest

/**
  * Created by GP39 on 2016/9/7.
  */
object NewDemo {

  def main(args: Array[String]): Unit = {
    def index={
      import java.util.Random
      val ran = new Random()
      ran.nextInt(7)
    }

    for(i <- 1 to 12){
      println(index)
    }
  }

}
