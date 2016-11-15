package localtest

import java.io.File

import org.uncommons.maths.number.Rational

/**
  * Created by GP39 on 2016/8/12.
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {
    println(c(377,319))

    def c(x:Int,y:Int):Int ={
      println(x +"         "+ y)
      var z=0
      if(x>y){
        z=x%y
        if(z==0){
          y
        }else{
          c(y,z)
        }
      }else{
        z=y%x
        if(z==0){
          x
        }else{
          c(x,z)
        }
      }
    }


    val oneHalf = new Rational(1,3)
    println(oneHalf)

    val s = new R(3, 15)+(new R(4, 5))
    println(s)

    val ss = new R(4,5) + new R(5,7)
    println(ss)
    println(ss.number)
    println(ss.denom)

    val x = new R(4,5) max (new R(5,6))
    println(x)


    def m0(x:Int) = x*x
    val m1 =(x:Int )=>x*x
    val m2 ={x:Int => x*x}

    println(m0(12))
    println(m1(12))
    println(m2(14))

    def f():String ={return "hello world"}
    def f1():String ="hello world"
    println(f)
    println(f1)

    def f2:Int => Double = {
      case 1 =>0.1
      case 2 =>0.3
      case _ =>0.0
    }

    println(f2(1))
    def f3:(Int,Int) =>Int = _+_
    println(f3(3,54))

  val filesHere = new File(".").listFiles()
    for ( xx <- filesHere){
      val length = xx.isFile
      println(xx + " " + length)
    }

    def scalaFiles =
      for{
        file <- filesHere
        if file.getName.endsWith("xml")
      }yield {
        file
      }

    println(scalaFiles(0))


    def half(n:Int) =
      if(n%2 == 0)
        n/2
    else
        throw new RuntimeException("n must be even")

    half(12)


    val newHalf =(n:Int) => if(n%2==0) n/2 else throw new RuntimeException("hello")
    newHalf(12)

    //swith & match
    val name="gongpengx"
    val trueName=
      name match{
        case "gongpeng" =>"xxxxxxxxxx"
        case "gongpeng1" =>"xxxxxxxxxx1"
        case "gongpeng2" =>"xxxxxxxxxx2"
        case "gongpeng3" =>"xxxxxxxxxx3"
        case _ => "error"
      }
    println(trueName)

    val inc  = (x:Int ) => x+1
    println(inc(1000))


    val num= List(1,2,4,5,6,18)
    num.foreach((x:Int ) => println(x))
    val num2 = num.filter((x)=> x%2==0)
    num2.foreach((x) => println(x))
    num2.foreach(println)
    println("--------------")
    val num3 = num.filter(_%3==0)
    num3.foreach(println(_))
    val nm = (_:Int)+(_:Int)
    println(nm(5,6))


  }



}
