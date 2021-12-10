package com.example.kuaiyijia.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kuaiyijia.Adapter.VpAdapter;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.orderManage.AddOrderActivity;
import com.example.kuaiyijia.Utils.GetJson;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityIndex extends AppCompatActivity {

    private static final String TAG = "banner：";
    private int currentPosition;
    private static final int REQUEST_CODE_SCAN = 1;
    private String mResultScan = null;
    private Button mBtnSecond;

    private EditText mYundanId;
    private ImageButton mIBtnSearch;
    private ImageButton mIBtnScan;
    private Button mBtnIndexNewOrder;
    private Button mBtnIndexSendCar;
    private Button mBtnIndexReceiveGoods;
    private Button mBtnIndexNewBackOrder;
    private Button mBtnIndexGetGoods;

    private TextView mTvOrderingNum;

    private final String mHost_url = "https://www.kegmall.cn";
    private final String mUrlString = "https://www.kegmall.cn/bin/iface/ikegmall.jsp";
    private String mMethod;
    private ArrayList<String> mNetPointName = new ArrayList<>();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String orderingNum = msg.getData().getString("OrderingNum");
                    Log.d(TAG, "handleMessage: OrderingNum" + orderingNum);
                    mTvOrderingNum.setText("有" + orderingNum + "张运单进行中...");
            }
        }
    };
    private Intent mIntentAddOrder;


    private void initView() {
        mBtnSecond = findViewById(R.id.bt_second);
        mIBtnSearch = findViewById(R.id.bt_search);
        mYundanId = findViewById(R.id.et_search);
        mIBtnScan = findViewById(R.id.bt_scan);
        mBtnIndexNewOrder = findViewById(R.id.bt_index_newOrder);
        mBtnIndexSendCar = findViewById(R.id.bt_index_sendCar);
        mBtnIndexReceiveGoods = findViewById(R.id.bt_index_receiveGoods);
        mBtnIndexNewBackOrder = findViewById(R.id.bt_index_newBackOrder);
        mBtnIndexGetGoods = findViewById(R.id.bt_index_getGoods);
        mTvOrderingNum = findViewById(R.id.tv_ordering_num);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
        mMethod = "POST";
        mIntentAddOrder = new Intent(this, AddOrderActivity.class);

        ViewPager2 viewPager2 = findViewById(R.id.bannerVp);
        viewPager2.setCurrentItem(1);
        VpAdapter adapter = new VpAdapter(this);

        //自动连播
        Thread mLoop = new Thread(new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager2.getCurrentItem();
                viewPager2.setCurrentItem(currentItem + 1);
                if (currentItem == 4) {
                    //Log.d(TAG, "thread: 到第4页了，设置page为1");
                    viewPager2.setCurrentItem(1, false);
                }
                viewPager2.postDelayed(this, 2500);
            }
        });
        viewPager2.postDelayed(mLoop, 2500);

        //banner滑动监听，设置无限连播
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                //Log.d("currentpage", Integer.toString(currentPosition));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (currentPosition == 0) {
//                    Log.d(TAG, "onPageScrollStateChanged: 到第0页了，设置page为3");
                    viewPager2.setCurrentItem(adapter.getItemCount() -2, false);

                } else if (currentPosition == adapter.getItemCount() - 1) {
                    //Log.d(TAG, "onPageScrollStateChanged: 到第4页了，设置page为1");
                    viewPager2.setCurrentItem(1, false);

                }
            }
        });
        viewPager2.setAdapter(adapter);

        getNetPointName();

        //按钮事件
        //1.更换当前网点
        mBtnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlertDialog();
            }
            private void setAlertDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityIndex.this);
                String[] mNetPointNameList = mNetPointName.toArray(new String[mNetPointName.size()]);
                builder.setTitle("更换当前网点")
                        .setItems(mNetPointNameList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                // 在这里根据所点击的序号，向后台发送数据选择新的网点名称
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d(TAG, "setAlertDialog:  ");
            }
        });
        //2.搜索
        mIBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerSearch(v);
            }
        });
        //3.扫码
        mIBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan(v);
            }
        });
        //4.fun1 新运单
        mBtnIndexNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 选择目标网点
                setAlertDialog();

            }
            private void setAlertDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityIndex.this);
                String[] mNetPointNameList = mNetPointName.toArray(new String[mNetPointName.size()]);
                builder.setTitle("选择目标网点")
                        .setItems(mNetPointNameList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                // 在这里根据所点击的序号，记下目标网点

                                // 2. 跳转填写新运单页面
                                startActivity(mIntentAddOrder);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d(TAG, "setAlertDialog:  ");
            }
        });
        //5.fun2 发运
        mBtnIndexSendCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //6.fun3 收货
        mBtnIndexReceiveGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //7.fun4 新退单
        mBtnIndexNewBackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //8.fun5 取货
        mBtnIndexGetGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //9.订单数量
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msgOrderingNum = new Message();
                    String mPostStr = "{\"method\": \"queryj\",\"sqlid\": \"Tms_GetNewBillCount\",\"hybid\": 10,\"onlyone\": 1}";
                    String jsonRaw = new GetJson(mUrlString, mPostStr, mMethod).result;
                    ArrayList<String> url = new ArrayList<>();
                    Log.d(TAG, "onClick: Json = "+jsonRaw);
                    //把从后端接口拿到的json字符串转为Java中的JSONObject对象
                    JSONObject jo1 = JSON.parseObject(jsonRaw);
                    //有几条数据就循环多少次
                    for (int i = 0; i < jo1.getInteger("recordcount"); i++) {
                        //从所有数据中取出data
                        JSONObject data = jo1.getJSONObject("data");
                        //取出data中的url
                        url.add(data.getString("ydcount"));
                    }
                    msgOrderingNum.what = 1;
                    Bundle bOrderingNum = new Bundle();
                    bOrderingNum.putString("OrderingNum", url.get(0));
                    msgOrderingNum.setData(bOrderingNum);
                    mHandler.sendMessage(msgOrderingNum);
                    System.out.println(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handlerSearch(View v) {
        String yundanId = mYundanId.getText().toString();
        if (TextUtils.isEmpty(yundanId)) {
            Toast.makeText(this, "运单号不可以为空..", Toast.LENGTH_SHORT).show();
            return;
        }

    }
    public void scan(View view) {
        Intent intent = new Intent(new Intent(this, CaptureActivity.class));

        ZxingConfig config = new ZxingConfig();
        config.setShowAlbum(true);
        config.setShowFlashLight(true);
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);

        startActivityForResult(intent, REQUEST_CODE_SCAN);
        if (mResultScan != null) {
            Log.d(TAG, "scan: " + mResultScan);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                mResultScan = data.getStringExtra(Constant.CODED_CONTENT);
                Log.d(TAG, "onActivityResult: " + mResultScan);

            }
        }
    }
    public void getNetPointName(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String mPostStr = "{\"method\": \"queryj\",\"sqlid\": \"Tms_GetHybSelect\",\"phybid\":10765,\"hybid\": 0,\"thybid\": 0,\"pagenumber\": 1,\"pagesize\": 10}";
                    String jsonRaw = new GetJson(mUrlString, mPostStr, mMethod).result;
                    Log.d(TAG, "onClick: Json = "+jsonRaw);
                    //把从后端接口拿到的json字符串转为Java中的JSONObject对象
                    JSONObject jo1 = JSON.parseObject(jsonRaw);
                    //有几条数据就循环多少次
                    for (int i = 0; i < jo1.getInteger("recordcount"); i++) {
                        //从所有数据中取出data
                        JSONArray data = jo1.getJSONArray("data");
                        //取出第i个data
                        JSONObject jo2 = (JSONObject) data.get(i);
                        //取出data中的url
                        mNetPointName.add(jo2.getString("sname"));
                    }
                    System.out.println(mNetPointName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}