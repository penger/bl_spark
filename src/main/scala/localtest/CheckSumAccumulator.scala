package localtest

/**
  * Created by GP39 on 2016/8/29.
  */
import scala.collection.mutable.Map

object CheckSumAccumulator {


  private val cache = Map[String, Int]()

  def calculate(s: String): Unit =
    if (cache.contains(s)) {
      cache(s)
    } else {
      val acc = new CheckSumAccumulator
      for (c <- s) {
        acc.add(c.toByte)
        val cs = acc.checkSum()
        cache += (s -> cs)
        cache(s)
      }
    }

}

  class CheckSumAccumulator {
    private var sum = 0

    def add(b: Int) {
      sum += b
    }

    def checkSum(): Int =  1
  }

