package com.example.kuaiyijia.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.kuaiyijia.Adapter.VpAdapter;
import com.example.kuaiyijia.R;

import java.util.ArrayList;

public class ActivityIndex extends AppCompatActivity {

    private static final String TAG = "banner：";
    private int currentPosition;


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
    }
}