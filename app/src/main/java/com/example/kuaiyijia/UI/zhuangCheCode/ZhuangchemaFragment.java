package com.example.kuaiyijia.UI.zhuangCheCode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
Author by: xy
Coding On 2021/3/18;
*/
public class ZhuangchemaFragment extends Fragment {
    private static final String TAG = "zhuangchema";
    private EditText mCarId;
    private Button mBtnSearch;
    private TextView mInfoCarId;
    private TextView mInfoCarType;
    private TextView mInfoCarCapacity;
    private TextView mInfoCarDimension;
    private String mV_id;
    private String mV_no;
    private String mV_lenght;
    private String mV_width;
    private String mV_height;
    private Button mBtnCarLoadCode;
    private boolean isNullResultSet;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1 :
                    mV_id = msg.getData().getString("mV_id");//接受msg传递过来的参数
                    mV_no = msg.getData().getString("mV_no");//接受msg传递过来的参数
                    mV_lenght = msg.getData().getString("mV_lenght");//接受msg传递过来的参数
                    mV_width = msg.getData().getString("mV_width");//接受msg传递过来的参数
                    mV_height = msg.getData().getString("mV_height");//接受msg传递过来的参数
                    Log.i("lgq","id: "+mV_no);
                    mInfoCarId.setText("车牌号：" + mV_no);
                    mInfoCarDimension.setText("货箱长宽高:" + mV_lenght + "m × "+ mV_width + "m × "+ mV_height + "m");
                    break;
                case 2 :
                    isNullResultSet = msg.getData().getBoolean("isNullResultSet");
                    Log.i(TAG, "handleMessage: isNullResultSet" + isNullResultSet);
                    //如果查询结果为空就要重新输入车牌
                    if (!isNullResultSet) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("提醒！")
                                .setMessage("无所查询车牌，请重新输入..")
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
                        //Toast.makeText(searchCarNo.this, "无所查询车牌，请重新输入..", Toast.LENGTH_SHORT).show();
                        //return;
                    }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.zhuangchema,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent i = new Intent(getContext(), getCarLoadCode.class);
        initView();
        //搜索车牌号
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handlerSearch(v);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        //生成装车码
        mBtnCarLoadCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "准备生成装车码...");
                i.putExtra("V_ID",mV_id);
                startActivity(i);
                Log.d(TAG, "开始跳转页面...");
            }
        });
    }
    private void initView() {
        mCarId = getActivity().findViewById(R.id.carId);
        mBtnSearch =  getActivity().findViewById(R.id.search);
        mInfoCarId =  getActivity().findViewById(R.id.info_carId);
        mInfoCarType =  getActivity().findViewById(R.id.info_carType);
        mInfoCarCapacity =  getActivity().findViewById(R.id.info_carCapacity);
        mInfoCarDimension =  getActivity().findViewById(R.id.info_carDimension);
        mBtnCarLoadCode =  getActivity().findViewById(R.id.btn_carLoadCode);
    }

    private void handlerSearch(View v) throws SQLException {
        String carId = mCarId.getText().toString();
        if (TextUtils.isEmpty(carId)) {
            Toast.makeText(getContext(), "车牌号不可以为空..", Toast.LENGTH_SHORT).show();
            return;
        }
        String tabName = "PUB_VEHICLE";
        String tabTopName = "V_NO";
        //value值之后从输入框中得到
        String value = carId;
//        String value = "渝A66666";
        //测试判空操作
        //String value = "渝A66665";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msgCarInfo = new Message();
                Message msgCarIsNull = new Message();
                ResultSet rs = Database.SelectFromData("*", tabName, tabTopName, value);
                try {
                    //对本次查询判空，传递消息出去D
                    msgCarIsNull.what = 2;
                    Bundle bCarIsNull = new Bundle();
                    bCarIsNull.putBoolean("isNullResultSet",rs.isBeforeFirst());
                    msgCarIsNull.setData(bCarIsNull);
                    mHandler.sendMessage(msgCarIsNull);

                    msgCarInfo.what = 1;
                    Bundle bundle = new Bundle();
                    while (rs.next()) {
                        bundle.putString("mV_id", rs.getString("V_ID"));
                        bundle.putString("mV_no", rs.getString("V_NO"));
                        bundle.putString("mV_lenght", rs.getString("HC_LENGHT"));
                        bundle.putString("mV_width", rs.getString("HC_WIDTH"));
                        bundle.putString("mV_height", rs.getString("HC_HEIGHT"));
                        msgCarInfo.setData(bundle);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(msgCarInfo);
            }
        }).start();
        //database.closeConnect();
    }
}
