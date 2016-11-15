package localtest

import java.io.File

/**
  * Created by GP39 on 2016/9/1.
  */
object FileUtil {

  def main(args: Array[String]): Unit = {
    val f= new File(".")
    val lists = subdirs2(f)
    var count =0
    for(l <- lists) {
      println(l.getName)
      count+=1
    }
    println(count)
    val add = (x:Int,y:Int) => print(x+y)
    add ( 3,54)

    def curriedSum(x:Int)(y:Int) = x+y
    println(curriedSum(4)(5))




  }




  def lists(file:File):Iterator[File]={
    val d =file.listFiles.filter(_.isDirectory)
    val f =file.listFiles.filter(_.isFile).toIterator
    f ++ d.toIterator.flatMap(lists _)


  }

  def subdirs2(dir: File): Iterator[File] = {
    println("---------------------------")
    val d = dir.listFiles.filter(_.isDirectory)
    val f = dir.listFiles.filter(_.isFile).toIterator
    f ++ d.toIterator.flatMap(subdirs2 _)
  }




  def subdirs3(dir: File): Iterator[File] = {
    val d = dir.listFiles.filter(_.isDirectory)
    val f = dir.listFiles.toIterator
    f ++ d.toIterator.flatMap(subdirs3 _)
  }


}
