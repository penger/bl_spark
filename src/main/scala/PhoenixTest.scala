import java.sql.{Connection, DriverManager, ResultSet, Statement}


/**
  * Created by hadoob on 14/11/2016.
  */
object PhoenixTest {
  def main(args: Array[String]): Unit = {
    //10.199.5.180:2181
    println("==============================")
    println("arg0 -> zk, arg1 -> tablename, arg2 -> field")
    println("==============================")
    val zk = args(0)
    println(s"zk url is ${zk }")
    val tableName = args(1)
    val fieldName = args(2)

    var conn: Connection = null
    var stat: Statement = null
    var rs: ResultSet = null
    //    var prop = null
    try {
      Class.forName("org.apache.phoenix.jdbc.PhoenixDriver")
      //      prop = new Properties()
      //      prop.load(new FileInputStream(this.getClass."hbase-site.xml") )

      //      conn = DriverManager.getConnection("jdbc:phoenix:192.168.191.64")
      //
      //      stat = conn.createStatement()
      //      rs = stat.executeQuery("SELECT * FROM \"WEB_STAT\"")


      conn = DriverManager.getConnection(s"jdbc:phoenix:${zk }")

      stat = conn.createStatement()
      val sql: String = s"SELECT ${fieldName } FROM ${tableName }"
      println(sql)
      rs = stat.executeQuery(sql)

      while (rs.next()) {
        System.out.println(fieldName + " = " + rs.getString(fieldName))
        Thread.sleep(500)
      }
    } catch {
      case ex: Exception => {
        println(ex)
      }
    } finally {
      if(stat != null) {
        stat.close()
      }
      if(conn != null) {
        conn.close()
      }
    }

  }
}
