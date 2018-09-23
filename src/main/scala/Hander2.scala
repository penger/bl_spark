import java.io._

/**
  * Created by GP39 on 2016/11/3.
  */
object Hander2 {
  def main(args: Array[String]): Unit = {
    val f = new File("e:/history2")
    test(f,func =>{
      func.write("helllllllllllll")
    })
    test(f,_.write("helllllllllllll"))
  }

  def test(f:File, func : BufferedWriter =>Unit): Unit ={
    val bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(f)))
    val s="kdfs,dfsaf"
    s.conta
    println(s.split(",")(0))
    func(bw)
    bw.close()

  }

}
