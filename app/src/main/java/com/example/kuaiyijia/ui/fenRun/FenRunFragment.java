package com.example.kuaiyijia.ui.fenRun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
Author by: xy
Coding On 2021/3/18;
*/
public class FenRunFragment extends Fragment {
    private static final String TAG = "chaxun";
    private TextView mshuru1;
    private TextView mshuru2;
    private Button mcahxun;
    private TextView mgangwei;
    private TextView mxingming;
    private Button mfemrui;
    private TextView mwangdian;

    private String mV_post;
    private String mV_name;
    private String mV_number;
    private String mV_vo;
    private String mV_array;
    private String amendValue;
    private android.os.Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1 :
//                    System.out.println("3333333");
                    mV_post = msg.getData().getString("mV_post");//接受msg传递过来的参数
                    mV_name = msg.getData().getString("mV_name");//接受msg传递过来的参数
                    mV_number = msg.getData().getString("mV_number");//接受msg传递过来的参数
                    mV_vo = msg.getData().getString("mV_vo");//接受msg传递过来的参数

                    Log.i("lgq","area: "+mV_post);

                    mgangwei.setText(mV_post+"(岗位)");
                    mxingming.setText(mV_name +"(姓名)");
                    mfemrui.setText(mV_number+"%(分润)");
                    mwangdian.setText(mV_vo+"(网点)");

                    break;
                case 2:
//                    String str = msg.getData().getString("mtv_array");
                    mV_array = msg.getData().getString("mtv_array");

                    mfemrui.setText(mV_array+"%(分润)");
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fenrun,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        //查询分润
        mcahxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "onClick: 12345");
                    handlerSearch(v);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        //修改分润
        mfemrui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(getContext()); // 创建视图容器并设置上下文
                final View view = layoutInflater.inflate(R.layout.fenrun_xiugai,null); // 获取list_item布局文件的视图
                new AlertDialog.Builder(getContext()).setTitle("修改分润").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        EditText editText = (EditText) view.findViewById(R.id.shuru_3);
                        amendValue  =  editText.getText().toString();
                        amendDate(v);
                        Toast.makeText(getContext(), "你修改的分润为"+editText.getText(), Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", null).show();

            }
            public void amendDate(View view){

                String post = mshuru1.getText().toString();
                String name = mshuru2.getText().toString();
                String tabName = "PUB_FENRUI";

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: "+amendValue);
                        int rs = database.amendFromData(tabName,"V_ID","V_NAME","V_NUMBER",
                                post ,name,amendValue);


                        Message msg = new Message();
                        msg.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("mtv_array",amendValue);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);


                    }
                });
                thread.start();

            }
        });
    }
    private void initView() {
        mshuru1 = getActivity().findViewById(R.id.shuru_1);
        mshuru2 = getActivity().findViewById(R.id.shuru_2);
        mcahxun =getActivity().findViewById(R.id.chaxun);

        mgangwei = getActivity().findViewById(R.id.gangwei);
        mxingming = getActivity().findViewById(R.id.xingming);
        mfemrui = getActivity().findViewById(R.id.fenrui);
        mwangdian = getActivity().findViewById(R.id.wangdian);
        Log.d(TAG, "initView: 1.5");



    }



    private void handlerSearch(View v) throws SQLException {
        String post = mshuru1.getText().toString();
        String name = mshuru2.getText().toString();
        if(TextUtils.isEmpty(post)){
            Toast.makeText(getContext(),"岗位不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getContext(),"姓名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String tabName = "PUB_FENRUI";
//        String tabTopName = "V_ID";
//        String value = "沙坪坝";
//        String tabTopName = "V_ID";
//        String value = "沙坪坝";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = database.SelectFromData_2("*",tabName,"V_ID","V_NAME",post,name);
                Log.d(TAG, "rs is ");
                try {
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    while (rs.next()){
                        Log.e(TAG, "run: budnle111");
                        bundle.putString("mV_post",rs.getString("V_ID"));
                        bundle.putString("mV_name",rs.getString("V_NAME"));
                        bundle.putString("mV_number",rs.getString("V_NUMBER"));
                        bundle.putString("mV_vo",rs.getString("V_VO"));

                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        database.closeConnect();
    }

}
