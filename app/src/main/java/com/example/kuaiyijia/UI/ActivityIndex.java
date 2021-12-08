package com.example.kuaiyijia.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kuaiyijia.Adapter.VpAdapter;
import com.example.kuaiyijia.R;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.sql.SQLException;
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

    private Button mBtnDialogNetPoint1;
    private Button mBtnDialogNetPoint2;
    private Button mBtnDialogNetPoint3;
    private Button mBtnDialogNetPoint4;


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

        mBtnDialogNetPoint1 = findViewById(R.id.btn_dialog_netpoint1);
        mBtnDialogNetPoint2 = findViewById(R.id.btn_dialog_netpoint2);
        mBtnDialogNetPoint3 = findViewById(R.id.btn_dialog_netpoint3);
        mBtnDialogNetPoint4 = findViewById(R.id.btn_dialog_netpoint4);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();

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
                    Log.d(TAG, "thread: 到第4页了，设置page为1");
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
                Log.d("currentpage", Integer.toString(currentPosition));
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

        //按钮事件
        //1.更换当前网点
        mBtnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                setDialog();
                switch (v.getId()) {
                    case R.id.btn_dialog_netpoint1:
                        Toast.makeText(ActivityIndex.this, "江北", Toast.LENGTH_SHORT).show();
                        mBtnSecond.setText("江北");
                        break;
                    case R.id.btn_dialog_netpoint2:
                        Toast.makeText(ActivityIndex.this, "渝北两路", Toast.LENGTH_SHORT).show();
                        mBtnSecond.setText("渝北两路");
                        break;
                    case R.id.btn_dialog_netpoint3:
                        Toast.makeText(ActivityIndex.this, "大石坝", Toast.LENGTH_SHORT).show();
                        mBtnSecond.setText("大石坝");
                        break;
                    case R.id.btn_dialog_netpoint4:
                        Toast.makeText(ActivityIndex.this, "老顶坡", Toast.LENGTH_SHORT).show();
                        mBtnSecond.setText("老顶坡");
                        break;
                }
            }
            private void setDialog() {
                Dialog mChangNetPointDialog = new Dialog(ActivityIndex.this, R.style.BottomDialog);
                mChangNetPointDialog.setTitle("更换当前网点");
                LinearLayout root = (LinearLayout) LayoutInflater.from(ActivityIndex.this).inflate(
                        R.layout.bottom_dialog, null);
                //初始化视图
                root.findViewById(R.id.btn_dialog_netpoint1).setOnClickListener(this);
                root.findViewById(R.id.btn_dialog_netpoint2).setOnClickListener(this);
                root.findViewById(R.id.btn_dialog_netpoint3).setOnClickListener(this);
                root.findViewById(R.id.btn_dialog_netpoint4).setOnClickListener(this);
                mChangNetPointDialog.setContentView(root);
                Window dialogWindow = mChangNetPointDialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                lp.x = 0; // 新位置X坐标
                lp.y = 0; // 新位置Y坐标
                lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
                root.measure(0, 0);
                lp.height = root.getMeasuredHeight();

                lp.alpha = 9f; // 透明度
                dialogWindow.setAttributes(lp);
                mChangNetPointDialog.show();
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
}