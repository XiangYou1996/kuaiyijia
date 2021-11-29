package com.example.kuaiyijia.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.kuaiyijia.Adapter.VpAdapter;
import com.example.kuaiyijia.R;

public class HorizontalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);

        ViewPager2 viewPager2 = findViewById(R.id.vp_h);
        VpAdapter adapter = new VpAdapter(this);
        viewPager2.setAdapter(adapter);
    }
}