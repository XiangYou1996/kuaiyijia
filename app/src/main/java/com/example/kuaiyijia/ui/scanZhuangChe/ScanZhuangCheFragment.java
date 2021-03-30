package com.example.kuaiyijia.ui.scanZhuangChe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

/*
Author by: xy
Coding On 2021/3/18;
*/
public class ScanZhuangCheFragment  extends Fragment {
    private static final String TAG = "scanCarLoad";
    private Button mBtScan;
    private String mResultScan = null;
    private Timestamp mTimeStamp;
    private int mColumnNum;
    private boolean mFlagBindCarPorter = false;
    private int mUpdateResult;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1 :
                    //接受msg传递过来的参数
                    mColumnNum = msg.getData().getInt("columnNum");
                    Log.i("lgq","id: "+ mColumnNum);
                    if (mColumnNum == 1) {
                        Log.d(TAG, "run: 返回值为1");
                        //Toast.makeText(   getContext(),"绑定成功..", Toast.LENGTH_SHORT).show();
                        //成功后，5.依次扫描每个商品条形码，绑定搬运人员和货物
                        AlertDialog.Builder builder = new AlertDialog.Builder( getContext());
                        builder.setTitle("提醒！")
                                .setMessage("绑定成功，请依次扫描所有商品条码..")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        mFlagBindCarPorter = true;
                                    }
                                })
                                .show();
                    }
                    else {
                        Toast.makeText(   getContext(), "绑定失败..", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    boolean isNullResultSet = msg.getData().getBoolean("isNullResultSet");
                    Log.i(TAG, "handleMessage: isNullResultSet" + isNullResultSet);
                    //如果查询结果为空就要重新输入车牌
                    if (!isNullResultSet) {
                        AlertDialog.Builder builder = new AlertDialog.Builder( getContext());
                        builder.setTitle("提醒！")
                                .setMessage("无此装车码，请重新扫码..")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                })
                                .show();
                    }
                    //是的话，4.就去执行搬运人员和车辆的绑定操作
                    else {
                        bindCarPorter();
                    }
                    break;
                case 3 :
                    mColumnNum = msg.getData().getInt("columnNum");//接受msg传递过来的参数
                    Log.i("lgq","id: "+ mColumnNum);
                    if (mColumnNum == 1) {
                        Log.d(TAG, "run: 返回值为1");
                        Toast.makeText(   getContext(), "添加成功..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(   getContext(), "添加失败..", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4 :
                    //接受msg传递过来的参数
                    mUpdateResult = msg.getData().getInt("updateResult");
                    Log.i("lgq","id: "+ mUpdateResult);
                    if (mUpdateResult == 1) {
                        Log.d(TAG, "run: 返回值为1");
                        Toast.makeText(   getContext(), "更改状态成功..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(   getContext(), "更改状态失败..", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };
    private Button mBtScanHuowuNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scancarload,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date date = new Date();
        mTimeStamp = new Timestamp(date.getTime());

        mBtScan = getActivity().findViewById(R.id.bt_scanCarLoad);
        mBtScanHuowuNum = getActivity().findViewById(R.id.bt_scanHuowuNum);

        mBtScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan(v,1);//1. 扫码装车码
            }
        });
        mBtScanHuowuNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindPorterCargoInsertHuowuCode(v);
                bindPorterCargoUpdateHuowus(v);
            }
        });
    }
    public void scan(View view, int REQUEST_CODE_SCAN) {
        Intent intent = new Intent(new Intent(getContext(), CaptureActivity.class));
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            if (data != null) {

                mResultScan = data.getStringExtra(Constant.CODED_CONTENT);
//                if (mFlagBindCarPorter == false) {
//                    bindDialog();//2. 判断是否绑定当前车辆
//                }
                bindDialog();//2. 判断是否绑定当前车辆
                Log.d(TAG, "onActivityResult: code1");

            }
        }
        if (requestCode == 2 && resultCode == getActivity().RESULT_OK) {
            if (data != null) {

                mResultScan = data.getStringExtra(Constant.CODED_CONTENT);
                Log.d(TAG, "onActivityResult: code2");

            }
        }
    }

    public void bindDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提醒！")
                .setMessage("是否绑定当前车辆？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        //3. 去数据库查询是否是装车码
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msgCarInfo = new Message();
                                Message msgCarIsNull = new Message();
                                String tabName = "PUB_VEHICLE";
                                String tabTopName = "V_ID";
                                String value = mResultScan;
                                ResultSet rs =    Database.SelectFromData("*", tabName, tabTopName, value);
                                try {
                                    //对本次查询判空，传递消息出去
                                    msgCarIsNull.what = 2;
                                    Bundle bCarIsNull = new Bundle();
                                    bCarIsNull.putBoolean("isNullResultSet",rs.isBeforeFirst());
                                    msgCarIsNull.setData(bCarIsNull);
                                    mHandler.sendMessage(msgCarIsNull);

                                } catch (SQLException | java.sql.SQLException e) {
                                    e.printStackTrace();
                                }
                                mHandler.sendMessage(msgCarInfo);
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                })
                .show();
    }
    public void bindCarPorter() {
        String sql = "insert into PUB_CARPORTER(V_ID, PORTER_ID) values(?,?)";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                Log.d(TAG, "run: 进入线程");
                Connection conn =    Database.getSQLConnection();
                try {
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1,mResultScan);
                    ps.setInt(2,1);
                    bundle.putInt("columnNum", ps.executeUpdate());
                    msg.setData(bundle);
                    Log.d(TAG, "run: sql执行完毕");
                    conn.close();
                } catch (SQLException | java.sql.SQLException throwables) {
                    throwables.printStackTrace();
                }
                mHandler.sendMessage(msg);
            }
        }).start();

    }
    public void bindPorterCargoInsertHuowuCode(View v) {
        scan(v,2);
        //5.1 先把商品条码insert在`order_huowu_code`中
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "insert into order_huowu_code(code_path, create_date) values(?,?)";
                Message msg = new Message();
                msg.what = 3;
                Bundle bundle = new Bundle();
                Connection conn =    Database.getSQLConnection();
                try {
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1,mResultScan);
                    ps.setTimestamp(2,mTimeStamp);
                    bundle.putInt("columnNum", ps.executeUpdate());
                    msg.setData(bundle);
                    Log.d(TAG, "run: sql执行完毕");
                    conn.close();
                } catch (SQLException | java.sql.SQLException throwables) {
                    throwables.printStackTrace();
                }
                mHandler.sendMessage(msg);
            }
        }).start();
    }
    public void bindPorterCargoUpdateHuowus(View v) {
        //修改数据库代码
        String TabName = "order_huowus";
        String ID_name = "PORTER_ID";
        int ID_value = 1;

        String[] columns = {"code_path"};
        String[] values = {mResultScan};
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result =    Database.updateForData(TabName, ID_name, ID_value, columns, values);
                Message msg = new Message();
                msg.what = 4;
                Bundle bundle = new Bundle();
                bundle.putInt("updateResult", result);
                msg.setData(bundle);
                mHandler.sendMessage(msg);

            }
        }).start();

    }
}
