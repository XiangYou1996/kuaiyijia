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
import com.example.kuaiyijia.Entity.CarListItem;
import com.example.kuaiyijia.Entity.PersonListItem;
import com.example.kuaiyijia.UI.MainActivity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.CustomDialog;
import com.example.kuaiyijia.UI.personManage.PersonModifyActivity;


import java.util.List;

public class PersonListAdapter extends BaseAdapter {
    private static final  String TAG = "PersonAdapter";
    private List<PersonListItem> adapterLists;
    private LayoutInflater minflater;
    private Context context;
    private  CarListItem car;
    private  boolean isDelete =false;
    public PersonListAdapter(Context context, List<PersonListItem> adapterLists) {
        this.minflater = LayoutInflater.from(context);
        this.adapterLists = adapterLists;
        this.context = context;
        Log.i(TAG, "PersonListAdapter: "+adapterLists.size());
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1041:
                    int  result = msg.getData().getInt("result");
                    if (result != 0){
                        Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
                        // 然后跳转
                        Intent mIntent = new Intent(context, MainActivity.class);
                        mIntent.putExtra("id",2);
                        context.startActivity(mIntent);
                    }
                    else {
                        Toast.makeText(context,"删除失败！",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
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
        // 人员姓名  ，  人员岗位
        public TextView textView1;
        public TextView textView2;
        // 修改 删除
        public Button detail_modify_btn;
        public Button delete_btn;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PersonListAdapter.ViewHolder holder = null;
        PersonListItem personListItem = (PersonListItem) getItem(position);
        if (convertView == null){
            convertView = minflater.inflate(R.layout.pm_person_list_item, null);
            holder = new PersonListAdapter.ViewHolder();
            // 绑定对应组件的id
            holder.textView1 = (TextView) convertView.findViewById(R.id.pm_staff_name);
            holder.textView2 = (TextView) convertView.findViewById(R.id.pm_staff_station);
            holder.detail_modify_btn = (Button) convertView.findViewById(R.id.pm_staff_modify);
            holder.delete_btn = (Button) convertView.findViewById(R.id.pm_staff_delete);
            convertView.setTag(holder);
        } else {
            holder = (PersonListAdapter.ViewHolder) convertView.getTag();
        }
//        设置其内容
        if (personListItem.getPerson_name().length() > 0 ) {
            holder.textView1.setText(personListItem.getPerson_name());
        }
        else {
            holder.textView1.setText("读取失败");
        }
       if ( personListItem.getPerson_sation().length() > 0){
           holder.textView2.setText(personListItem.getPerson_sation());
        }
       else {
           holder.textView2.setText("读取失败");
       }
        holder.detail_modify_btn.setTag(position);
        holder.detail_modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转 人员修改
                Intent mIntent = new Intent(context, PersonModifyActivity.class);
                //   传递人员信息
                PersonListItem person = adapterLists.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("person", person);
                mIntent.putExtras(bundle);
                context.startActivity(mIntent);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   删除人员信息
                deletePersoninfo(position);

            }
        });
        return convertView;
    }

    public void deletePersoninfo(int position){
        // 弹出对话框询问是否真的删除
        CustomDialog dialog = new CustomDialog(context);

        dialog.setTitle("提示");
        dialog.setMessage("确定要删除该名员工吗？");
        dialog.setConfirm("确定",new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                PersonListItem person = adapterLists.get(position);
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        // 删除employ 数据
                        int result1 = Database.deleteFromData("PUB_EMPLOYEE","EM_ID",person.getPerson_ID());
                        // 删除第二个表
                        int result2 = Database.deleteFromData("PUB_EMETYPE", "EM_ID",person.getPerson_ID());
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        message.what = 1041;
                        bundle.putInt("result", result1&result2);
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
