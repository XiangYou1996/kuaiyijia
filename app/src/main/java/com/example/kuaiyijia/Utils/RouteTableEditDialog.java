package com.example.kuaiyijia.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.LineEntity;
import com.example.kuaiyijia.R;

import java.util.Date;
import java.util.UUID;

/*
Author by: xy
Coding On 2021/4/14;
*/
public class RouteTableEditDialog {
    private Context context;
    public RouteTableEditDialog(Context context){
        this.context = context ;
    }
    public void  showDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(context); // 创建视图容器并设置上下文
        final View view = layoutInflater.inflate(R.layout.dialog_route_edit,null); // 获取list_item布局文件的视图
        new AlertDialog.Builder(context).setTitle("添加线路").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // get start station and end station
                EditText et_start = view.findViewById(R.id.et_start);
                EditText et_end = view.findViewById(R.id.et_end);
                LineEntity line = new LineEntity("",et_start.getText().toString(),et_end.getText().toString(),"","");
                // insert a line
                intsert(line);

            }
        }).setNegativeButton("取消", null).show();
    }

    private void intsert(LineEntity line) {
        // new a thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String [] names = {"C_ID","L_START","L_END"};
                String [] values = {"1","'"+line.getStart_station()+"'","'"+line.getEnd_station()+"'"};
                Database.insertIntoDataForColumn("PUB_LINE",names,values);
            }
        });
        thread.start();
        thread.interrupt();
    }
}
