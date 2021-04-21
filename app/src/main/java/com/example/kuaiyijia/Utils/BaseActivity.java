package com.example.kuaiyijia.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*
Author by: xy
Coding On 2021/4/16;
*/
public abstract class BaseActivity extends AppCompatActivity {
    private Context mContext;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initLayout());
    }
    // SharedPreferences
    public void getSP(String name){
        sp = getSharedPreferences(name,MODE_PRIVATE);
    }

    protected abstract int initLayout();

    // 跳转到其他页面
    public void navigateTo( Class cls){
        Intent mIntent = new Intent(mContext,cls);
        startActivity(mIntent);
    }
    // 跳转到其他页面 携带数据
    public void navigateToWithData( Class cls ,Bundle bundle){
        Intent mIntent = new Intent(mContext,cls);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
    }

    // 跳转到其他页面并销毁之前的代码
    //  flag== Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
    public void navigateWithDestroyBefore(Class cls,  int flag){
        Intent mIntent = new Intent(mContext,cls);
        mIntent.setFlags(flag);
        startActivity(mIntent);
    }

    public void showToast(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();

    }

    public void showToastSync(String msg){
        Looper.prepare();
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }



}
