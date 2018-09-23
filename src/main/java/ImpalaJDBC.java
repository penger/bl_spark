import java.sql.*;

/**
 * Created by GP39 on 2016/9/14.
 * 直接用java连接impala
 */
public class ImpalaJDBC {

    private static final String SQL_STATEMENT= "select * from sourcedata.s03_oms_order";
    private static final String JDBC_URL= "jdbc:hive2://10.201.48.16:21050/";
    private static final String NEW_JDBC_URL= "jdbc:hive2://10.201.48.26:10000/";




    private static final String JDBC_DRIVER_NAME="org.apache.hive.jdbc.HiveDriver";
    public static  void  main(String[] args){
        try {
            Class.forName(JDBC_DRIVER_NAME);
            Connection conn = DriverManager.getConnection(JDBC_URL,"hive","nF7=8H*%");
            Statement statement = conn.createStatement();
            //ResultSet resultSet = statement.executeQuery(" select * from  sourcedata.s03_oms_order where dt='20160913' order by  limit 10 offset 20");
            ResultSet resultSet = statement.executeQuery(" select * from  sourcedata.s03_oms_order  limit 10");
            while(resultSet.next()){
                System.out.println(resultSet.getString(2)+"-----------");
            }
            statement.close();
            resultSet.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }
}
