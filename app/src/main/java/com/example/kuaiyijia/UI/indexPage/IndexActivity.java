package com.example.kuaiyijia.UI.indexPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.LoginRegister.LoginActivity;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_index);
        try {
            Thread.sleep(3000);
            Intent mIntent =new Intent(IndexActivity.this , LoginActivity.class);
            startActivity(mIntent);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}