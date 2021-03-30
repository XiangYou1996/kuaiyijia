package com.example.kuaiyijia.Tools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.kuaiyijia.Database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class utility {
    private static final String TAG = "utility";

    /**
     * 从数据库中获取单个数据，并且转换成message的方法
     * @param msgInfo 传入一个新的Message
     * @param msgNum 对应的Message编号
     * @param tabName 表名
     * @param tabTopName 所要查询的列名
     * @param value 列名下对应的值
     * @param key 取值时的key
     * @param columnLabel 需要取的数据所对应的列名
     * @return
     */
    public static Message getInfo(Message msgInfo, int msgNum, String tabName, String tabTopName, String value, String key, String columnLabel){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = Database.SelectFromData("*", tabName, tabTopName, value);
                try {
                    msgInfo.what = msgNum;
                    Bundle bundle = new Bundle();
                    while (rs.next()) {
                        bundle.putString(key, rs.getString(columnLabel));
                        msgInfo.setData(bundle);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getInfo: ");
        return msgInfo;
    }
}
