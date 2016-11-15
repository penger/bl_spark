package localtest

/**
  * Created by GP39 on 2016/9/6.
  */
class R(n:Int, d:Int){
  val number :Int =n
  val denom :Int = d
  require(d!=0)
  implicit def xxxxxxx(x:Int) = new R(x)
  println("create :"+n+"/"+d)
  //从构造器
  def this(n:Int) = this(n,1)
  override def toString: String = number+"/"+denom
  def +(that:R):R =
    new R(number * that.denom +that.number * denom , denom * that.denom)
  def lessThan(that:R):Boolean ={
    val x = this.number*that.denom < that.number*this.denom
    println(this.number* that.denom  + "       <     " + that.number* this.denom + "       "+ x)
    x
  }
  def max(that:R)=
    if(this.lessThan(that)) that else this
  def min(that:R)=
    if(lessThan(that)) this else that


}
