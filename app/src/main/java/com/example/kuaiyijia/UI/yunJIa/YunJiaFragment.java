package com.example.kuaiyijia.UI.yunJIa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;
import com.google.android.material.tabs.TabLayout;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Author by: xy
Coding On 2021/3/18;
*/
public class YunJiaFragment extends Fragment {
    private static final String TAG = "Yunjia";
    private TextView marea;
    private Spinner mspinner1;
    private TextView mfreight;
    private  Spinner msprinner2;
    private Button msearch;
    private Button  minfoshuru;
    private TextView minfoarea;
    private TextView minfofreight;
    private TextView minfomoney;
    private String area;
    private String things;
    private String area2;
    private String mV_money;
    private String mV_sprinner1;
    private String mV_sprinner2;
    private String mV_sprinner3;
    private String amendValue;

    private Spinner mspinner3;
    private TextView minfoarea2;



//    private android.os.Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch(msg.what){
//                case 1 :
//                    mV_sprinner1 = msg.getData().getString("mV_area");//接受msg传递过来的参数
//                    mV_sprinner2 = msg.getData().getString("mV_things");//接受msg传递过来的参数
//                    mV_money = msg.getData().getString("mV_money");//接受msg传递过来的参数
//                    mV_sprinner3 = msg.getData().getString("mV_area2");//接受msg传递过来的参数
//                    minfoarea.setText("发货地:" + mV_sprinner1);
//                    minfofreight.setText("配件:" +mV_sprinner2 );
//                    minfomoney.setText("金额:" +mV_money );
//                    minfoarea2.setText("收货地:"+mV_sprinner3);
//                    break;
//                case 2:
//                    String str = msg.getData().getString("mtv_array");
//                    minfomoney.setText("金额:"+str);
//            }
//        }
//    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_yunjia,container,false);
        View view = inflater.inflate(R.layout.yunjia_fragment,container,false);
        final List<String> msubTitle = new ArrayList<>();
        msubTitle.add("进行中");
        msubTitle.add("已完成");
        msubTitle.add("已取消");
        msubTitle.add("全部");
        msubTitle.add("筛选");

        final yunjiaFragmentAdapter secondFragmentAdapter = new yunjiaFragmentAdapter(getActivity().getSupportFragmentManager());
        secondFragmentAdapter.SetSubFragments(msubTitle);
        final ViewPager viewPager = view.findViewById(R.id.yunjia_viewpaper);
        viewPager.setAdapter(secondFragmentAdapter);
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initView();
//        //地区查询
//        msearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    handlerSearch(v);
//                }catch (SQLException throwables){
//                    throwables.printStackTrace();
//                }
//            }
//        });
//
//        //修改金额
//        minfomoney.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater layoutInflater = LayoutInflater.from(getContext()); // 创建视图容器并设置上下文
//                final View view = layoutInflater.inflate(R.layout.yunjia_xiugai_layout,null); // 获取list_item布局文件的视图
//                new AlertDialog.Builder(getContext()).setTitle("修改金额").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        EditText editText = (EditText) view.findViewById(R.id.shuru);
//                        amendValue  =  editText.getText().toString();
//                        amendDate(v);
//                        Toast.makeText(getContext(), "你修改的金额为"+editText.getText(), Toast.LENGTH_SHORT).show();
//                    }
//                }).setNegativeButton("取消", null).show();
//
//            }
//            public void amendDate(View view){
//                String tabName = "PUB_HAHA";
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int rs = Database.amendFromData(tabName,"V_ID","V_NO","HC_MONEY",
//                                "V_ID2", area,things,amendValue,area2);
//                        Message msg = new Message();
//                        msg.what = 2;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("mtv_array",amendValue);
//                        msg.setData(bundle);
//                        mHandler.sendMessage(msg);
//
//
//                    }
//                });
//                thread.start();
//
//            }
//        });
//    }
//    private void initView() {
//        marea = getActivity().findViewById(R.id.area);
//        mspinner1 = getActivity().findViewById(R.id.spinner1);
//        mfreight = getActivity().findViewById(R.id.freight);
//        msprinner2 = getActivity().findViewById(R.id.spinner2);
//        msearch = getActivity().findViewById(R.id.search);
//        minfoarea = getActivity().findViewById(R.id.info_area);
//        minfofreight = getActivity().findViewById(R.id.info_freight);
//        minfomoney = getActivity().findViewById(R.id.info_money);
//        minfoshuru = getActivity().findViewById(R.id.shuru);
//
//        mspinner3 = getActivity().findViewById(R.id.spinner3);
//        minfoarea2 = getActivity().findViewById(R.id.info_area2);
//        mspinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                area = getResources().getStringArray(R.array.area)[position];
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        msprinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                things = getResources().getStringArray(R.array.sad)[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        mspinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                area2 = getResources().getStringArray(R.array.choose)[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        msearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    handlerSearch(v);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private void handlerSearch(View v) throws  SQLException{
//
//        if(TextUtils.isEmpty(area)){
//            Toast.makeText(getContext(),"发货地不能为空",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(things)){
//            Toast.makeText(getContext(),"配件不能为空",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(area2)){
//            Toast.makeText(getContext(),"收货地不能为空",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String tabName = "PUB_HAHA";
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ResultSet rs = Database.SelectFromData_2("*",tabName,"V_ID","V_NO","V_ID2",area,things,area2);
//                try {
//                    Message msg = new Message();
//                    msg.what = 1;
//                    Bundle bundle = new Bundle();
//                    while (rs.next()){
//                        bundle.putString("mV_area",rs.getString("V_ID"));
//                        bundle.putString("mV_things",rs.getString("V_NO"));
//                        bundle.putString("mV_money",rs.getString("HC_MONEY"));
//                        bundle.putString("mV_area2",rs.getString("V_ID2"));
//                        msg.setData(bundle);
//                        mHandler.sendMessage(msg);
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//        Database.closeConnect();
//    }
}
