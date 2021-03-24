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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.WebBranchListItem;
import com.example.kuaiyijia.ui.MainActivity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Tools.CustomDialog;
import com.example.kuaiyijia.ui.webBranchManage.WebBranchDetail;

import java.util.List;

/*
Author by: xy
Coding On 2021/3/10;
*/
public class WebBranchListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<WebBranchListItem> adapterLists;
    private WebBranchListItem webBranch;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1034:
                    int result = msg.getData().getInt("result");
                    if (result ==0){
                        Toast.makeText(context,"删除失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent mIntent = new Intent(context, MainActivity.class);
                        mIntent.putExtra("id",3);
                        context.startActivity(mIntent);
                        Toast.makeText(context,"删除成功！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public WebBranchListAdapter(Context context,  List<WebBranchListItem> adapterLists) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.adapterLists = adapterLists;
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
    public class ViewHolder {
        Button button;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        WebBranchListItem webBranchListItem = (WebBranchListItem) getItem(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.web_list_item,null);
            holder =  new ViewHolder();
            holder.button = (Button) convertView.findViewById(R.id.webBrachJC);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.button.setText(webBranchListItem.getWebBranchJC());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, WebBranchDetail.class);
                webBranch = adapterLists.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("webBranch",webBranch);
                mIntent.putExtras(bundle);
                context.startActivity(mIntent);
            }
        });
        holder.button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteWebBranch(position);
                return true;
            }
        });
        return convertView;
    }

    private void deleteWebBranch(int position) {
        Log.i("TAG", "deleteWebBranch: entered");
        WebBranchListItem webBranch  = adapterLists.get(position);
        // 先弹出确认对话框
        CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle("提示");
        dialog.setMessage("您确认要删除该网点吗？");
        dialog.setConfirm("确认", new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {

                // 开始删除
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result = Database.deleteFromData("PUB_HYB","HYBID",webBranch.getWebBranchID());
                        Message message =new Message();
                        message.what = 1034;
                        Bundle bundle = new Bundle();
                        bundle.putInt("result",result);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                });
                thread.start();
                thread.interrupt();
            }
        });
        dialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
            @Override
            public void onCancel(CustomDialog dialog) {
                Toast.makeText(context,"取消成功~",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
