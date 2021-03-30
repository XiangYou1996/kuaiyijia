package com.example.kuaiyijia.ui.scanFaChe;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Tools.Constants;
import com.example.kuaiyijia.Tools.utility;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.R;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private String mResultScan = null;//车辆ID
    private String mV_no;//车牌
    private String mL_end;//目的地
    private int mL_id = 1;//线路id
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
                        //3.绑定成功后，需要更新界面上的数据。
                        Message msgInfoCarNO = new Message();
                        mHandler.sendMessage(utility.getInfo(msgInfoCarNO,4, Constants.TABNAME_PUB_VEHICLE,Constants.TABTOPNAME_V_ID,mResultScan,"mV_no", Constants.TABTOPNAME_V_NO));
                        Message msgInfoDest = new Message();
                        mHandler.sendMessage(utility.getInfo(msgInfoDest,5,Constants.TABNAME_PUB_LINES_D,Constants.TABTOPNAME_L_ID,Integer.toString(mL_id),"mL_end", Constants.TABTOPNAME_L_END));
                        Message msgInfoStatue = new Message();
                        mHandler.sendMessage(utility.getInfo(msgInfoStatue,6,Constants.TABNAME_XS_TRAN_INFO,Constants.TABTOPNAME_TI_ID,Integer.toString(mTI_id),"mResultMsg", Constants.TABTOPNAME_RESULTMSG));
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
                        Message msgInfoStatue = new Message();
                        mHandler.sendMessage(utility.getInfo(msgInfoStatue,6,Constants.TABNAME_XS_TRAN_INFO,Constants.TABTOPNAME_TI_ID,Integer.toString(mTI_id),"mResultMsg", Constants.TABTOPNAME_RESULTMSG));
                    }
                    else {
                        Toast.makeText( getContext(), "更改状态失败..", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
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
                    //2.是的话，就绑定
                    else {
                        bindDriverAndCar();
                    }

                    break;
                //更新车牌
                case 4 :
                    Log.d(TAG, "handleMessage: case4");
                    mV_no = msg.getData().getString("mV_no");//接受msg传递过来的参数
                    Log.i("lgq","mV_NO: "+mV_no);
                    mTvCarNo.setText("车牌号：" + mV_no);
                    break;
                case 5 :
                    Log.d(TAG, "handleMessage: case5");
                    mL_end = msg.getData().getString("mL_end");//接受msg传递过来的参数
                    Log.i("lgq","目的地: "+mL_end);
                    mTvDest.setText("目的地：" + mL_end);
                    break;
                case 6 :
                    Log.d(TAG, "handleMessage: case6");
                    mResultMsg = msg.getData().getString("mResultMsg");//接受msg传递过来的参数
                    Log.i("lgq","故障状态: "+mResultMsg);
                    mTvCarStatue.setText("故障状态：" + mResultMsg);
                    break;
            }
        }
    };


    private Button mBtnTrafficJam;
    private Button mBtnFault;
    private Button mBtnAccident;
    private Button mBtnFaultDischarged;
    private Timestamp mTimeStamp;
    private String mTi_rq;
    private TextView mTvCarNo;
    private TextView mTvDest;
    private TextView mTvCarStatue;
    private int mTI_id = 6;//运输记录id
    private String mResultMsg;

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
        mTi_rq = mYear + "-" + mMonth + "-" + mDay;
        Date date = new Date();
        mTimeStamp = new Timestamp(date.getTime());

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
                                updateResultMsg("Traffic_Jam");
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
                                updateResultMsg("Fault");
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
                                updateResultMsg("Accident");
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
                                updateResultMsg("ok");
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
                scan(v);

            }
        });
    }
    private void initView() {
        mBtnScan = getActivity().findViewById(R.id.bt_scanCarGo);
        mBtnTrafficJam = getActivity().findViewById(R.id.bt_traffic_jam);
        mBtnFault = getActivity().findViewById(R.id.bt_fault);
        mBtnAccident = getActivity().findViewById(R.id.bt_accident);
        mBtnFaultDischarged = getActivity().findViewById(R.id.bt_fault_discharged);
        mTvCarNo = getActivity().findViewById(R.id.tv_v_no);
        mTvDest = getActivity().findViewById(R.id.tv_dest);
        mTvCarStatue = getActivity().findViewById(R.id.tv_statue);
    }
    public void bindDriverAndCar(){
        //2.绑定司机和车辆, 添加数据库表项
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
                Connection conn = Database.getSQLConnection();
                try {
                    PreparedStatement ps = conn.prepareStatement(sql);
                    //注意这里第一个参数，是这张表的数据序号，需要每次增加1
                    ps.setInt(1,mTI_id);
                    ps.setInt(2,1);
                    ps.setInt(3,1);
                    ps.setString(4,mTi_rq);
                    //第五个参数L_ID 线路id，从何处获取？
                    ps.setInt(5,1);
                    ps.setInt(6,1);
                    //车辆id
                    ps.setString(7,mResultScan);
                    //司机id，之后需要单独获取
                    ps.setInt(8,1);
                    ps.setTimestamp(9,mTimeStamp);
                    ps.setTimestamp(10,mTimeStamp);
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

    }

    /**
     * 1.判断是否是装车码
     */
    public void isCode(){
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
                    msgCarIsNull.what = 3;
                    Bundle bCarIsNull = new Bundle();
                    bCarIsNull.putBoolean("isNullResultSet",rs.isBeforeFirst());
                    msgCarIsNull.setData(bCarIsNull);
                    mHandler.sendMessage(msgCarIsNull);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(msgCarInfo);
            }
        }).start();

    }

    /**
     * 获取车牌
     */
    public void getCarNo(){
        String tabName = "PUB_VEHICLE";
        String tabTopName = "V_ID";
        String value = mResultScan;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: getCarNO");
                Message msgCarInfo = new Message();
                ResultSet rs = Database.SelectFromData("*", tabName, tabTopName, value);
                try {
                    msgCarInfo.what = 4;
                    Bundle bundle = new Bundle();
                    while (rs.next()) {
                        bundle.putString("mV_no", rs.getString("V_NO"));
                        msgCarInfo.setData(bundle);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(msgCarInfo);
            }
        }).start();

    }

    /**
     * 更新故障状态
     * @param statue Traffic_Jam, Fault, Accident, ok
     */
    public void updateResultMsg(String statue){
        String TabName = "XS_TRAN_INFO";
        String ID_name = Constants.TABTOPNAME_TI_ID;
        String[] columns = {"RESULTMSG"};
        String[] values = {statue};
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = Database.updateForData(TabName, ID_name, mTI_id, columns, values);
                Message msg = new Message();
                msg.what = 2;
                Bundle bundle = new Bundle();
                bundle.putInt("updateResult", result);
                msg.setData(bundle);
                mHandler.sendMessage(msg);

            }
        }).start();

    }

    /**
     * 扫码模块
     * @param view
     */
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
                //mResultScan = Integer.parseInt(data.getStringExtra(Constant.CODED_CONTENT));
                mResultScan = data.getStringExtra(Constant.CODED_CONTENT);
                isCode();
            }
        }
    }
}
