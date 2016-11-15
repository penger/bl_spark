import java.sql.*;

/**
 * Created by GP39 on 2016/9/14.
 *
 * m_da_globle_promo_goods
 */
public class GetOracleTableStructure {

    private static Connection connection=null;

    private static final String url_18="jdbc:oracle:thin:@10.201.48.18:1521:report";
    private static final String username_18="idmdata";
    private static final String password_18="bigdata915";
    private static final String schema_18="idmdata";




    //默认获取18的链接
    public static Connection getConnection(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url_18,username_18,password_18);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static Connection getConnection(String url,String username,String password){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String getSchema() throws Exception{
        String schema = getConnection().getMetaData().getUserName();
        System.out.println(schema);
        return schema.toUpperCase();
    }

    public static void getMetaData() throws SQLException {
        DatabaseMetaData metaData = getConnection().getMetaData();
        ResultSet tables = metaData.getTables(null, "IDMDATA", "%", new String[]{"TABLE"});
        while (tables.next()){
            String table_name = tables.getString("TABLE_NAME");
            System.out.println(table_name);
            if("M_UEC_POWER_DATA_POWER_B".equals(table_name)) {
                ResultSet columns =
                        getConnection().getMetaData().getColumns(null, "IDMDATA", "M_UEC_POWER_DATA_POWER_B", null);
                while (columns.next()){
                    String column_name = columns.getString("COLUMN_NAME");
                    String type_name = columns.getString("TYPE_NAME");
                    String column_size = columns.getString("COLUMN_SIZE");
                    System.out.println(column_name+"-----------------------------"+type_name+"             "+column_size);
                }
            }
        }
    }


    public static String getTableMetaDate2HiveStatement(Connection conn,String schema,String tableName) throws SQLException{
        StringBuffer sql_buff= new StringBuffer();
        sql_buff.append("create table `sourcedata.");
        sql_buff.append(tableName.toUpperCase());
        sql_buff.append("`(").append("\n");

        DatabaseMetaData metaData = getConnection().getMetaData();
        ResultSet columns = metaData.getColumns("null", schema.toUpperCase(), tableName.toUpperCase(), null);
        int count=0;
        while (columns.next()){
            String column_name = columns.getString("COLUMN_NAME");
            String type_name = columns.getString("TYPE_NAME");
            String data_type = columns.getString("DATA_TYPE");
            String column_size = columns.getString("COLUMN_SIZE");
            int decimal_digits = columns.getInt("DECIMAL_DIGITS");
            int num_prec_radix = columns.getInt("NUM_PREC_RADIX");
            String remark = columns.getString("REMARKS");

            System.out.println(column_name+" "+type_name+" "+data_type+" "+column_size+" "+remark+"         "+decimal_digits+","+num_prec_radix);
            if(count!=0){
                sql_buff.append(", \n");
            }else{
                sql_buff.append("\n");
            }
            count++;

            if(type_name.equalsIgnoreCase("varchar2") || type_name.equalsIgnoreCase("nvarchar2")){
                sql_buff.append("\t").append(column_name).append("\t").append("string");
            }else if(type_name.equalsIgnoreCase("number")){
                if(decimal_digits!=0){
                    sql_buff.append("\t").append(column_name).append("\t").append("decimal ");
                }else{
                    //如果有小数点,那么转换为double类型
                    sql_buff.append("\t").append(column_name).append("\t").append("double");
                }
            }else if(type_name.equalsIgnoreCase("long")){
                sql_buff.append("\t").append(column_name).append("\t").append("bigint ");
            }else if(type_name.equalsIgnoreCase("date")){
                sql_buff.append("\t").append(column_name).append("\t").append("date ");
            }else if(type_name.equalsIgnoreCase("timestamp")){
                sql_buff.append("\t").append(column_name).append("\t").append("timestamp ");
            }
            else{
                sql_buff.append("\t").append(column_name).append("\t").append("未识别类型");
                throw new RuntimeException("有需要特殊处理的数据类型");
            }
        }
        sql_buff.append(") partitioned by (`dt` string )");

        return sql_buff.toString();

    }


    public static void executeHiveSql(String sql){

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            Connection conn = DriverManager.getConnection("jdbc:hive2://10.201.48.26:10000/","hive","nF7=8H*%");
            Statement statement = conn.createStatement();
            boolean execute = statement.execute(sql);
//            ResultSet resultSet = statement.executeQuery(" select * from  sourcedata.s03_oms_order where dt='20160913'  limit 10");
//            while(resultSet.next()){
//                System.out.println(resultSet.getString(2)+"-----------");
//            }
//            resultSet.close();
            statement.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }









    }



    public static void main(String[] args ) throws Exception {

        if(args.length!=5){
            System.out.println("需要五个参数 url, username ,password ,schema ,table_name");
            System.exit(0);
        }
        String url=args[0];
        String username=args[1];
        String password=args[2];
        String schema=args[3];
        String table_name=args[4];

        System.out.println("-----start_----------");
        String hive_sql = getTableMetaDate2HiveStatement(getConnection(url,username,password), schema, table_name);
//        String hive_sql = getTableMetaDate2HiveStatement(getConnection(), "IDMDATA", "M_UEC_POWER_DATA_POWER_B");
        System.out.println("-----end_----------");

        System.out.println(hive_sql);

        //executeHiveSql(hive_sql);




    }


}
