package com.example.kuaiyijia.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kuaiyijia.Adapter.VpAdapter;
import com.example.kuaiyijia.R;

import java.util.ArrayList;

public class ActivityIndex extends AppCompatActivity {

    private static final String TAG = "banner：";
    private int currentPosition;
    private Button mBtnSecond;
    private ImageButton mIBtnSearch;
    private ImageButton mIBtnScan;
    private Button mBtnIndexNewOrder;
    private Button mBtnIndexSendCar;
    private Button mBtnIndexReceiveGoods;
    private Button mBtnIndexNewBackOrder;
    private Button mBtnIndexGetGoods;

    private void initView() {
        mBtnSecond = findViewById(R.id.bt_second);
        mIBtnSearch = findViewById(R.id.bt_search);
        mIBtnScan = findViewById(R.id.bt_scan);
        mBtnIndexNewOrder = findViewById(R.id.bt_index_newOrder);
        mBtnIndexSendCar = findViewById(R.id.bt_index_sendCar);
        mBtnIndexReceiveGoods = findViewById(R.id.bt_index_receiveGoods);
        mBtnIndexNewBackOrder = findViewById(R.id.bt_index_newBackOrder);
        mBtnIndexGetGoods = findViewById(R.id.bt_index_getGoods);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

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
                    Log.d(TAG, "onPageScrollStateChanged: 到第0页了，设置page为3");
                    viewPager2.setCurrentItem(adapter.getItemCount() -2, false);

                } else if (currentPosition == adapter.getItemCount() - 1) {
                    Log.d(TAG, "onPageScrollStateChanged: 到第4页了，设置page为1");
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

            }
        });
        //2.搜索
        mIBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //3.扫码
        mIBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}