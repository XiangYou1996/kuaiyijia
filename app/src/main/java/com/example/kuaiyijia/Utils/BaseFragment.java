package com.example.kuaiyijia.Utils;

import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/*
Author by: xy
Coding On 2021/4/16;
*/
public class BaseFragment extends Fragment {

    // 跳转到其他页面
    public void navigateTo( Class cls){
        Intent mIntent = new Intent(getActivity(),cls);
        startActivity(mIntent);
    }

    // 跳转到其他页面并销毁之前的代码
    //  flag== Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
    public void navigateWithDestroyBefore(Class cls,  int flag ){
        Intent mIntent = new Intent(getActivity(),cls);
        mIntent.setFlags(flag);
        startActivity(mIntent);
    }

    public void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

    }

    public void showToastSync(String msg){
        Looper.prepare();
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
