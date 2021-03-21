package com.example.kuaiyijia.ui.scanFaChe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.kuaiyijia.R;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/*
Author by: xy
Coding On 2021/3/18;
*/
public class ScanFaChe extends Fragment {
    private static final int REQUEST_CODE_SCAN = 0;
    private static final String TAG = "scanCarGo";
    private Button mBtnScan;
    private int mResultScan;//车辆ID
    private int mYear;
    private int mMonth;
    private int mDay;
    private Handler mHandler = new Handler() {

        private int mUpdateResult;
        private int mColumnNum;

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1 :
                    mColumnNum = msg.getData().getInt("columnNum");//接受msg传递过来的参数
                    Log.i("lgq","id: "+mColumnNum);
                    if (mColumnNum == 1) {
                        Log.d(TAG, "run: 返回值为1");
                        Toast.makeText( getContext(), "绑定成功..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText( getContext(), "绑定失败..", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2 :
                    //接受msg传递过来的参数
                    mUpdateResult = msg.getData().getInt("updateResult");
                    Log.i("lgq","id: "+ mUpdateResult);
                    if (mUpdateResult == 1) {
                        Log.d(TAG, "run: 返回值为1");
                        Toast.makeText( getContext(), "更改状态成功..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText( getContext(), "更改状态失败..", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private Button mBtnTrafficJam;
    private Button mBtnFault;
    private Button mBtnAccident;
    private Button mBtnFaultDischarged;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scancargo,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH)+1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        String ti_rq = mYear + "-" + mMonth + "-" + mDay;
        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date.getTime());

        initView();
        mBtnTrafficJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( getContext());
                builder.setTitle("提醒！")
                        .setMessage("是否遇到堵车？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //修改数据库代码
                                String TabName = "XS_TRAN_INFO";
                                //按理来说应该用订单编号来识别，在这里暂且使用车辆id来判断，后续需更改！
                                String ID_name = "V_ID";
                                int ID_value = 2;

                                String[] columns = {"RESULTMSG"};
                                String[] values = {"Traffic_Jam"};
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int result = database.updateForData(TabName, ID_name, ID_value, columns, values);
                                        Message msg = new Message();
                                        msg.what = 2;
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("updateResult", result);
                                        msg.setData(bundle);
                                        mHandler.sendMessage(msg);

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
        });

        mBtnFault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( getContext());
                builder.setTitle("提醒！")
                        .setMessage("是否遇到故障？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //修改数据库代码
                                String TabName = "XS_TRAN_INFO";
                                //按理来说应该用订单编号来识别，在这里暂且使用车辆id来判断，后续需更改！
                                String ID_name = "V_ID";
                                int ID_value = 2;

                                String[] columns = {"RESULTMSG"};
                                String[] values = {"Fault"};
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int result = database.updateForData(TabName, ID_name, ID_value, columns, values);
                                        Message msg = new Message();
                                        msg.what = 2;
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("updateResult", result);
                                        msg.setData(bundle);
                                        mHandler.sendMessage(msg);
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
        });

        mBtnAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( getContext());
                builder.setTitle("提醒！")
                        .setMessage("是否遇到交通事故？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //修改数据库代码
                                String TabName = "XS_TRAN_INFO";
                                //按理来说应该用订单编号来识别，在这里暂且使用车辆id来判断，后续需更改！
                                String ID_name = "V_ID";
                                int ID_value = 2;

                                String[] columns = {"RESULTMSG"};
                                String[] values = {"Accident"};
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int result = database.updateForData(TabName, ID_name, ID_value, columns, values);
                                        Message msg = new Message();
                                        msg.what = 2;
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("updateResult", result);
                                        msg.setData(bundle);
                                        mHandler.sendMessage(msg);
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
        });
        mBtnFaultDischarged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( getContext());
                builder.setTitle("提醒！")
                        .setMessage("是否遇到解除异常情况？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //修改数据库代码
                                String TabName = "XS_TRAN_INFO";
                                //按理来说应该用订单编号来识别，在这里暂且使用车辆id来判断，后续需更改！
                                String ID_name = "V_ID";
                                int ID_value = 2;

                                String[] columns = {"RESULTMSG"};
                                String[] values = {"ok"};
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int result = database.updateForData(TabName, ID_name, ID_value, columns, values);
                                        Message msg = new Message();
                                        msg.what = 2;
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("updateResult", result);
                                        msg.setData(bundle);
                                        mHandler.sendMessage(msg);
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
        });
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scan(v);
                //绑定司机和车辆, 添加数据库表项
//                String tabName = "XS_TRAN_INFO";
//                String[] tabTopName = {"TI_ID", "TI_RQ", "V_ID", "EM_ID", "STIME", "STATUS"};
                String sql = "insert into XS_TRAN_INFO(TI_ID, C_ID, HYBID, TI_RQ, L_ID, B_ID, V_ID, EM_ID, STIME, ETIME, STATUS, RESULT, RESULTMSG) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        Log.d(TAG, "run: 进入线程");
                        Connection conn = database.getSQLConnection();
                        try {
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setInt(1,4);
                            ps.setInt(2,1);
                            ps.setInt(3,1);
                            ps.setString(4,ti_rq);
                            ps.setInt(5,1);
                            ps.setInt(6,1);
                            //车辆id
                            ps.setInt(7,1);
                            //司机id
                            ps.setInt(8,1);
                            ps.setTimestamp(9,timeStamp);
                            ps.setTimestamp(10,timeStamp);
                            ps.setInt(11,0);
                            ps.setInt(12,0);
                            ps.setString(13, "ok");
                            bundle.putInt("columnNum", ps.executeUpdate());
                            msg.setData(bundle);
                            Log.d(TAG, "run: sql执行完毕");
                            conn.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        mHandler.sendMessage(msg);
                    }
                }).start();
//                if (mColumnNum == 1) {
//                    Log.d(TAG, "run: 返回值为1");
//                    Toast.makeText(scanCarGo.this, "绑定成功..", Toast.LENGTH_SHORT).show();
//                }
                //返回首页

            }
        });
    }
    private void initView() {
        mBtnScan = getActivity().findViewById(R.id.bt_scanCarGo);
        mBtnTrafficJam = getActivity().findViewById(R.id.bt_traffic_jam);
        mBtnFault = getActivity().findViewById(R.id.bt_fault);
        mBtnAccident = getActivity().findViewById(R.id.bt_accident);
        mBtnFaultDischarged = getActivity().findViewById(R.id.bt_fault_discharged);
    }



    public void scan(View view) {
        Intent intent = new Intent(new Intent(getContext(), CaptureActivity.class));
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                mResultScan = Integer.parseInt(data.getStringExtra(Constant.CODED_CONTENT));
            }
        }
    }
}
