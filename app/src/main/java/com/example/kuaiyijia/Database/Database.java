package com.example.kuaiyijia.Database;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String TAG = "Database";
    private static String user = "app";
    private static String password = "app123456";
    private static String DatabaseName = "HYBAPP";
    private static String IP = "121.41.7.176";
    /**
     * 连接字符串
     */
    private static String connectDB = "jdbc:jtds:sqlserver://" + IP + ":1433/" + DatabaseName + ";useunicode=true;characterEncoding=UTF-8";

    private static Connection conn = null;
    private static Statement stmt = null;

    /**
     * 链接数据库
     *
     * @return
     */
    public static Connection getSQLConnection() {
        Connection con = null;
        try {
            //加载驱动换成这个
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //连接数据库对象
            con = DriverManager.getConnection(connectDB, user,
                    password);
        } catch (Exception e) {
        }
        return con;
    }

    /**
     * 向服务器数据库插入数据
     * @tabName 要插入的表名
     * @tabTopName 要插入的字段名字符串，例如（name,password,age）
     * @values 与tabTopName 中 字段名一一对应的值。一次插入多跳数据，可以用逗号隔开。例如（"张三"，"zhangsan","24"），（"李四"，"lisi","26"）
     */
    public static int insertIntoData(String tabName, String tabTopName, String values) {
        int i = 0;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                Log.d(TAG, "insertIntoData: ");
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
                return i;
            }
            String sql = "insert into " + tabName + tabTopName + " values " + values;
            i = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "同步数据库表【" + tabName + "】失败。");
        }
        return i;
    }

    public static int amendFromData(String tabName, String tabTopName, String tabTopName_2, String tabTopName_3, String values, String values_2, String values_3) {
        int i = 0;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
                return i;
            }
            //String sql = "UPDATE" + tabName + "SET" + tabTopName + "WHERE" + tabTopName + "=" + values + "'";
            String sql = "UPDATE " + tabName + " SET " + tabTopName_3+" = "+ values_3+" WHERE " + tabTopName + " = '" + values + "'" +
                    " and " + tabTopName_2 + " = '" + values_2 + "'";
            Log.d(TAG, "amendFromData: " + sql);
            i = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "同步数据库【" + tabName + "】失败");
        }
        return i;
    }
    public static int amendFromData(String tabName, String tabTopName, String tabTopName_2, String tabTopName_3,String tabTopName_4,
                                    String values,String values_2,String values_3,String values_4) {
        int i = 0;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
                return i;
            }
            //String sql = "UPDATE" + tabName + "SET" + tabTopName + "WHERE" + tabTopName + "=" + values + "'";
            String sql = "UPDATE " + tabName + " SET " + tabTopName_3+" = "+ values_3+" WHERE " + tabTopName + " = '" + values + "'" +
                    " and " + tabTopName_2 + " = '" + values_2 + "'" +  " and " + tabTopName_4 + " = '" + values_4 + "'";
            Log.d(TAG, "amendFromData: " + sql);
            i = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "同步数据库【" + tabName + "】失败");
        }
        return i;
    }
    public static int insertIntoDataForColumn(String tabName, String[] tabTopName, String[] values) {
        int i = 0;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
                return i;
            }
            // 拼接对应的数据项
            String columns = " (";
            String value = " (";
            for (int j = 0 ; j<tabTopName.length-1 ; j++){
                columns += tabTopName[j]+ ", ";
                value += values[j]+ ", ";
            }
            columns += tabTopName[tabTopName.length-1]+ ") ";
            value += values[tabTopName.length-1]+ ") ";
            String sql = "insert into " + tabName + columns + " values " + value;
            i = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "同步数据库表【" + tabName + "】失败。");
        }
        return i;
    }
    public static ResultSet SelectFromDataAll(String first, String tabName) {
        //int i = 0;
        ResultSet rs = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
            }
            String sql = "SELECT " + first + " FROM " + tabName ;
            Log.d(TAG, "sql:" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + tabName + "】失败。");
        }
        return rs;
    }
    public static ResultSet SelectFromData(String first, String tabName, String tabTopName, String values) {
        //int i = 0;
        ResultSet rs = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
            }
            String sql = "SELECT " + first + " FROM " + tabName + " WHERE " + tabTopName + " = '" + values + "'";
            Log.d(TAG, "sql:" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + tabName + "】失败。");
        }
        return rs;
    }

    public static ResultSet SelectFromData_2(String first, String tabName, String tabTopName,
                                             String tabTopName_2,String tabTopName_3, String values, String values_2,String values_3) {
        //int i = 0;
        ResultSet rs = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
            }
            String sql = "SELECT " + first + " FROM " + tabName + " WHERE " + tabTopName + " = '" + values + "'" +
                    " and " + tabTopName_2 + " = '" + values_2 + "'" + " and " + tabTopName_3 + " = '" + values_3 + "'";
            Log.d(TAG, "sql:" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + tabName + "】失败。");
        }
        return rs;
    }
    public static ResultSet SelectFromData_2(String first, String tabName, String tabTopName,
            String tabTopName_2, String values, String values_2) {
        //int i = 0;
        ResultSet rs = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
            }
            String sql = "SELECT " + first + " FROM " + tabName + " WHERE " + tabTopName + " = '" + values + "'" +
                    " and " + tabTopName_2 + " = '" + values_2 + "'";
            Log.d(TAG, "sql:" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + tabName + "】失败。");
        }
        return rs;
    }
    public  static  ResultSet SelectFromDataCustomSql(String sql) {
        //int i = 0;
        ResultSet rs = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
            }
            Log.d(TAG, "sql:" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + "】失败。");
        }
        return rs;
    }
    public static ResultSet SelectFromDataBetweenDate(String first, String tabName, String tabTopName,
                                             String tabTopName_2, String values, String values_2) {
        //int i = 0;
        ResultSet rs = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
            }
            String sql = "SELECT " + first + " FROM " + tabName + " WHERE " + tabTopName + " > '" + values + "'" +
                    " and " + tabTopName_2 + " < '" + values_2 + "'";
            Log.d(TAG, "sql:" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + tabName + "】失败。");
        }
        return rs;
    }

    public static ResultSet SelectFromDataBetweenDate2(String first, String tabName, String tabTopName,
                                                      String tabTopName_2, String tabTopName_3,String values,
                                                       String values_2,String values_3) {
        //int i = 0;
        ResultSet rs = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
            }
            String sql = "SELECT " + first + " FROM " + tabName + " WHERE " + tabTopName + " > '" + values + "'" +
                    " and " + tabTopName_2 + " < '" + values_2 + "' " +"and "+tabTopName_3+ " = '"+values_3+"' ";
            Log.d(TAG, "sql:" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + tabName + "】失败。");
        }
        return rs;
    }
    public static int deleteFromData(String tabName, String tabTopName, String values) {
        int i = 0;
        Connection conn = null;
        Statement stmt = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
                return i;
            }
            String sql = "DELETE FROM " + tabName + " WHERE " + tabTopName + " = " + values ;
            Log.i(TAG, "deleteFromData: "+ sql);
            i = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "同步数据库表【" + tabName + "】失败。");
        }
        return i;
    }
    /*
       更新数据操作
     */
    public static int updateForData(String tabName, String ID_name, int ID_value,String [] tabTopName, String [] values){
        // 返回更新的结果
        int result = 0;
        // 拼接sql字符串

        String sql = "UPDATE " + tabName +" SET " ;
        for(int i = 0 ; i < tabTopName.length -1; i++ ){
            sql = sql + tabTopName[i] + " =" +" '"+values[i]+"', ";
        }
        sql = sql + tabTopName[tabTopName.length-1] + " =" +" '"+values[tabTopName.length-1]+"' " +" WHERE " + ID_name +" = " +
                " '"+ID_value+"' ";
        if (conn ==null) {
            conn = getSQLConnection();
            try {
                stmt = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn == null || stmt == null){
            return result;
        }
        try {
            Log.i("Database :sql ", sql);
            result = stmt.executeUpdate(sql);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.i("mtj", "同步数据库表【" + tabName + "】失败。");
        }
        return result;
    }
    /**
     * 关闭数据库链接
     */
    public static void closeConnect() {
        if (stmt != null) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}


