package com.example.kuaiyijia.ui.scanDownLoad;

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

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Tools.Constants;
import com.example.kuaiyijia.Tools.utility;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
Author by: xy
Coding On 2021/3/18;
*/
public class ScanDownLoad  extends Fragment {
    private static final int REQUEST_CODE_SCAN = 0;
    private static final String TAG = "scanUnload";
    private Button mBtScanUnload;
    private String mResultScan = null;
    private boolean bIsOrderNum;
    private Handler mHandler = new Handler() {
        private int mUpdateResult;
        @Override
        public void handleMessage(Message msg) {

            switch(msg.what){
                case 1 :
                    bIsOrderNum = msg.getData().getBoolean("isOrderNum");
                    if (!bIsOrderNum) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("提醒！")
                                .setMessage("无此订单码，请重新扫描..")
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
                    else {
                        //是订单码，开始更新数据库对应字段状态值
                        updateOrderStatus();
                    }
                    break;
                case 2 :
                    //接收修改订单状态后的结果
                    mUpdateResult = msg.getData().getInt("updateResult");
                    Log.i("lgq","id: "+ mUpdateResult);
                    if (mUpdateResult == 1) {
                        Log.d(TAG, "run: 返回值为1");
                        Toast.makeText(getContext(), "修改订单状态成功..", Toast.LENGTH_SHORT).show();
                        Message msgInfoOrderStatus = new Message();
                        mHandler.sendMessage(utility.getInfo(msgInfoOrderStatus,3, Constants.TABNAME_orders,Constants.TABTOPNAME_order_number,mResultScan,"mOrderStatus", Constants.TABTOPNAME_order_status));
                    }
                    else {
                        Toast.makeText(getContext(), "修改订单状态失败..", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3 :
                    mOrderNum = mResultScan;
                    mOrderStatus = msg.getData().getString("mOrderStatus");//接受msg传递过来的参数
                    Log.i("lgq","订单状态: "+ mOrderStatus);
                    mTvOrderNum.setText("订单号：" + mOrderNum);
                    if (mOrderStatus == "5") {
                        mTvOrderStatus.setText("订单状态：完成");
                    }
                    break;

            }
        }
    };
    private TextView mTvOrderStatus;
    private TextView mTvOrderNum;
    private String mOrderNum;
    private String mOrderStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scanunload,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtScanUnload = getActivity().findViewById(R.id.bt_scanUnload);
        mTvOrderNum = getActivity().findViewById(R.id.tv_order_num);
        mTvOrderStatus = getActivity().findViewById(R.id.tv_order_status);

        mBtScanUnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先扫码，判断是否是订单码
                scan(v);
            }
        });
    }
    //如果是订单码，就要修改订单状态
    public void updateOrderStatus() {
        String TabName = "orders";
        String ID_name = "order_number";
        int ID_value = Integer.parseInt(mResultScan);

        String[] columns = {"order_status"};
        String[] values = {"5"};
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = Database.updateForData(TabName, ID_name, ID_value, columns, values);
                Message msgResultOfUpdate = new Message();
                msgResultOfUpdate.what = 2;
                Bundle bundle = new Bundle();
                bundle.putInt("updateResult", result);
                msgResultOfUpdate.setData(bundle);
                mHandler.sendMessage(msgResultOfUpdate);
            }
        }).start();

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

                mResultScan = data.getStringExtra(Constant.CODED_CONTENT);

                //进入数据库查询是否是订单码
                //注意：此处表名等数据库相关数据名称为参考keg_logistic库中数据
                //     之后需做相应修改。

                String tabName = "orders";
                String tabTopName = "order_number";
                String value = mResultScan;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msgIsOrderNum = new Message();
                        ResultSet rs = Database.SelectFromData("*", tabName, tabTopName, value);
                        try {
                            msgIsOrderNum.what = 1;
                            Bundle bundleIsOrderNum = new Bundle();
                            bundleIsOrderNum.putBoolean("isOrderNum", rs.isBeforeFirst());
                            msgIsOrderNum.setData(bundleIsOrderNum);
                            mHandler.sendMessage(msgIsOrderNum);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }
    }
}
