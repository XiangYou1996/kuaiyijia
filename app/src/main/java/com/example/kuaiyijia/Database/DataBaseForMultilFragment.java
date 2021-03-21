package com.example.kuaiyijia.Database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
Author by: xy
Coding On 2021/3/18;
*/
public class DataBaseForMultilFragment {
    private  String user = "sa";
    private  String password = "root123";
    private  String DatabaseName = "CQU";
    private  String IP = "172.20.53.32";
    private  String connectDB = "jdbc:jtds:sqlserver://" + IP + ":1433/" + DatabaseName + ";useunicode=true;characterEncoding=UTF-8";

    private  Connection conn = null;
    private  Statement stmt = null;

    private  Connection getSQLConnection() {
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
    public ResultSet SelectFromData(String first, String tabName, String tabTopName, String values) {
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
            Log.d("TAG", "sql:" + sql);
            rs = stmt.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + tabName + "】失败。");
        }
        return rs;
    }
    public  ResultSet SelectFromDataCustomSql(String sql) {
        //int i = 0;
        ResultSet rs = null;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
            }
            Log.d("TAG", "sql:" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("mtj", "查询数据表【" + "】失败。");
        }
        return rs;
    }
}
