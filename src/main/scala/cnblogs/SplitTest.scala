package cnblogs

/**
  * Created by GP39 on 2016/11/1.
  */
object SplitTest {
  def main(args: Array[String]): Unit = {


    val model:Seq[(String,Int)] = ("name"->12)::("name"->12)::("name"->12)::("name"->12)::("name"->12)::Nil
    model.foreach{
      case (key,value )=>println(key +"   "+value)
      case _ =>println("empty")
    }



    val p="342"
    println(p.toInt)
    val pp = 435
    println(pp.toInt)



    val reg="^([^ ]*) +([^ ]*) +([^ ]*) +([^ ]*).*$".r
    val s="123 45 66 77 7 77 "
//    println(s.matches(reg))
    var reg( help1,help2,help3,help4) =s
    if(help1.equals("123")){
      help1="1234"
      println("error")
    }
    println(help1.toString.toInt ,help2.toInt,help3,help4)
  }

}
