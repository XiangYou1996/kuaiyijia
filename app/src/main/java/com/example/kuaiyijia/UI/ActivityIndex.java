package com.example.kuaiyijia.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.kuaiyijia.Adapter.VpAdapter;
import com.example.kuaiyijia.R;

import java.util.ArrayList;

public class ActivityIndex extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        ViewPager2 viewPager2 = findViewById(R.id.bannerVp);
        VpAdapter adapter = new VpAdapter(this);
        viewPager2.setAdapter(adapter);
    }
}