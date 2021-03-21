package com.example.kuaiyijia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.PersonListItem;
import com.example.kuaiyijia.Entity.StationListItem;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Tools.CustomDialog;
import com.example.kuaiyijia.ui.personManage.StationManageActivity;
import com.example.kuaiyijia.ui.personManage.StationModifyActivity;

import java.util.List;

public class StationListAdapter extends BaseAdapter {
    private Context context;
    private List<StationListItem> adapterlists;
    private LayoutInflater inflater;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1046:
                    int result = msg.getData().getInt("result");
                    if (result == 0){
                        Toast.makeText(context,"删除失败！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent mIntent = new Intent(context, StationManageActivity.class);
                        context.startActivity(mIntent);
                        Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public StationListAdapter(Context context, List<StationListItem> adapterlists) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.adapterlists = adapterlists;
    }

    @Override
    public int getCount() {
        return adapterlists.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        // 岗位名
        public TextView textView;
        public Button modify_btn;
        public Button delete_btn;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StationListAdapter.ViewHolder holder = null;
        StationListItem stationListItem = (StationListItem) getItem(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.pm_station_list_item,null);
            holder = new StationListAdapter.ViewHolder();
            // 绑定组件
            holder.textView = (TextView) convertView.findViewById(R.id.pm_station_name);
            holder.modify_btn = (Button) convertView.findViewById(R.id.pm_station_modify);
            holder.delete_btn = (Button) convertView.findViewById(R.id.pm_stationd_delete);
            convertView.setTag(holder);
        }
        else {
            holder = (StationListAdapter.ViewHolder) convertView.getTag();
        }
        if (stationListItem.getET_NAME().length()> 0){
            holder.textView.setText(stationListItem.getET_NAME());
        }
        else {
            holder.textView.setText("数据读取失败");
        }
        holder.modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///
                Intent mIntent = new Intent(context, StationModifyActivity.class);
                StationListItem station = adapterlists.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("station",station);
                mIntent.putExtras(bundle);
                context.startActivity(mIntent);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出提示框
                deletePersoninfo(position);
            }
        });
        return convertView;
    }

    public void deletePersoninfo(int position){
        // 弹出对话框询问是否真的删除
        CustomDialog dialog = new CustomDialog(context);

        dialog.setTitle("提示");
        dialog.setMessage("确定要删除该岗位吗？");
        dialog.setConfirm("确定",new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                StationListItem station = adapterlists.get(position);
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        // 删除station 数据
                        int result = Database.deleteFromData("PUB_TMSETYPE","ET_ID",station.getET_ID());
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        message.what = 1046;
                        bundle.putInt("result", result);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                };
                thread.start();
                thread.interrupt();
            }
        });
        dialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
            @Override
            public void onCancel(CustomDialog dialog) {
                Toast.makeText(context,"取消删除~",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
