package com.example.kuaiyijia.Database;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.UiThread;

import com.example.kuaiyijia.Database.Database;

/*
Author by: xy
Coding On 2021/4/16;
*/
public class DataBaseMethods {
    public  static void deleteById(Context context, String TableName, String ColumnName, String id){
        // 开始删除
        // 开个新线程
        final int[] result = {0};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                result[0] = Database.deleteFromData(TableName,ColumnName,id);
            }
        });
        thread.start();
        if (result[0] == 0) {
            Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"删除失败~",Toast.LENGTH_SHORT).show();
        }
        thread.interrupt();

    }
    public static void insert(String Table_name,String[] names, String[] values){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Database.insertIntoDataForColumn(Table_name,names,values);
            }
        });
        thread.start();
        thread.interrupt();
    }
    public static void updateById(String Table_name,String Id_name,String Id,String[] names, String[] values){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Database.updateForData(Table_name,Id_name,Integer.valueOf(Id),names,values);
            }
        });
        thread.start();
        thread.interrupt();
    }
}
