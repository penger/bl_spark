package localtest

/**
  * Created by GP39 on 2016/9/2.
  */
object SortUtil {
  def main(args: Array[String]): Unit = {
    val s = Array(1, 6, 4, 5, 3, 6, 7, 8, 9, 23, 76, 56, 5, 5, 34, 3)
    println("原数组")
    println(s.mkString(","))
    quicksort(s)
    println("快速排序")
    println(s.mkString(","))
    insertion_sort(s)
    println("插入排序")
    println(s.mkString(","))
    bubble_sort(s)
    println("冒泡排序:")
    println(s.mkString(","))
  }

  def bubble_sort(xs:Array[Int])={
    def swap (i:Int, j:Int) ={
      //println("交换值为:"+xs(i)+" <==> "+xs(j))
      val value  = xs(i)
      xs(i) = xs(j)
      xs(j) = value
    }
    // 1- 15
    for(i <- Range(1,xs.length)){
      //println("第"+i+"次循环")
      for(j <- Range(i, xs.length) reverse ){
          if(xs(j)<xs(j-1)) swap(j,j-1)
        //println(xs.mkString("|"))
      }
    }
  }

  def insertion_sort(xs:Array[Int])={
    def swap(i:Int,j:Int){
      val temp =xs(i)
      xs(i)=xs(j)
      xs(j)=temp
    }
    //println(xs.length+"长度为")
    for (index <- Range(1,xs.length)){
      //println(index + "    "+ xs(index))
      val value = xs(index)
      var i=index-1
      while (i >= 0 && xs(i) < value){
        swap(i,i+1)
        i-=1
      }
    }

  }

  def quicksort(xs: Array[Int]) = {
    def swap(i: Int, j: Int) {
      val t = xs(i)
      xs(i) = xs(j)
      xs(j) = t
    }
    def sort(l: Int, r: Int) {
      val pivot = xs((l + r) / 2)
      var i = l
      var j = r
      while (i <= j) {
        while (xs(i) < pivot) i += 1
        while (xs(j) > pivot) j -= 1
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (l < j) sort(l, j)
      if (j < r) sort(i, r)
    }
    sort(0, xs.length - 1)
  }
}
