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

    private int currentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        ViewPager2 viewPager2 = findViewById(R.id.bannerVp);
        viewPager2.setCurrentItem(1);//从第一张图开始播放
        VpAdapter adapter = new VpAdapter(this);

        Log.d("111", Integer.toString(viewPager2.getCurrentItem()));
        Log.d("222", Integer.toString(viewPager2.getScrollState()));
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
                //只有在空闲状态，才让自动滚动
                /*
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    Log.d("getItemCount()=", Integer.toString(adapter.getItemCount()));
                    viewPager2.setCurrentItem(adapter.getItemCount() -2, false);
                } else if (currentPosition == adapter.getItemCount() - 1) {
                    Log.d("getItemCount2()=", Integer.toString(adapter.getItemCount()));
                    viewPager2.setCurrentItem(1, false);
                }
                 */
                if (currentPosition == 0) {
                    viewPager2.setCurrentItem(adapter.getItemCount() -2, false);
                } else if (currentPosition == adapter.getItemCount() - 1) {
                    viewPager2.setCurrentItem(1, false);
                }
            }
        });





        viewPager2.setAdapter(adapter);
    }
}