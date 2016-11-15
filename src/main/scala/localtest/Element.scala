package localtest

/**
  * Created by GP39 on 2016/9/6.
  */
abstract class Element{
  def contents:Array[String]
  val height:Int = contents.length
  val width :Int = if(height ==0 ) 0 else contents(0).length

}
