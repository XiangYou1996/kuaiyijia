package com.example.kuaiyijia.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseMethods;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.LineEntity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.routeManage.BanciActivity;

import java.util.List;

/*
Author by: xy
Coding On 2021/4/14;
*/
public class RouteListAdapter extends BaseAdapter {
    private static String routeTableName = DataBaseConfig.routeTableName;
    private Context context;
    private List<LineEntity> adapterLists;
    private LayoutInflater inflater;

    public RouteListAdapter (Context context, List<LineEntity> adapterLists){
        this.context = context;
        this.adapterLists = adapterLists;
        inflater = LayoutInflater.from(context);
    }
    public void setAdapterLists(List<LineEntity> adapterLists){
        this.adapterLists =  adapterLists;
    }
    @Override
    public int getCount() {
        return adapterLists.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        public TextView start_address;
        public TextView end_address;
        public TextView tv_route_mange;
        public TextView tv_edit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RouteListAdapter.ViewHolder holder = null;
        LineEntity lineItem = (LineEntity) getItem(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_route_table,null);
            holder = new RouteListAdapter.ViewHolder();
            holder.start_address = convertView.findViewById(R.id.tv_start_address);
            holder.end_address = convertView.findViewById(R.id.tv_end_address);
            holder.tv_route_mange =convertView.findViewById(R.id.tv_route_mange);
            holder.tv_edit = convertView.findViewById(R.id.tv_edit);
            convertView.setTag(holder);
        }
        else {
            holder = (RouteListAdapter.ViewHolder)convertView.getTag();
        }
        if (lineItem.getStart_station() != null && lineItem.getEnd_station()!=null){
            holder.start_address.setText(lineItem.getStart_station());
            holder.end_address.setText(lineItem.getEnd_station());
        }
        else {
            holder.start_address.setText("默认出发地");
            holder.end_address.setText("默认结束地");
        }
        //
        holder.tv_route_mange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, BanciActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("line",lineItem);
                mIntent.putExtras(bundle);
                context.startActivity(mIntent);
            }
        });
        //
        ViewHolder finalHolder = holder;
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(context); // 创建视图容器并设置上下文
                View view = layoutInflater.inflate(R.layout.dialog_route_edit,null); // 获取list_item布局文件的视图
                // 设置初始的发站和到站
                LineEntity line =adapterLists.get(position);
                EditText et_start = view.findViewById(R.id.et_start);
                EditText et_end = view.findViewById(R.id.et_end);
                et_start.setText(line.getStart_station());
                et_end.setText(line.getEnd_station());
                new AlertDialog.Builder(context).setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        line.setStart_station(et_start.getText().toString());
                        line.setEnd_station(et_end.getText().toString());
                        //  更新线路
                        String [] names= {DataBaseConfig.routeStartStation,DataBaseConfig.routeEndStation};
                        String [] values = {line.getStart_station(),line.getEnd_station()};
                        DataBaseMethods.updateById(routeTableName, DataBaseConfig.routePriID,line.getId(),names,values);
                        finalHolder.start_address.setText(line.getStart_station());
                        finalHolder.end_address.setText(line.getEnd_station());
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
        return convertView;
    }
}
