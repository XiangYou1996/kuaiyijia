package com.example.kuaiyijia.UI.LoginRegister;

import androidx.annotation.NonNull;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.MainActivity;
import com.example.kuaiyijia.Utils.BaseActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_pwd;
    private TextView login_bt;
    private SharedPreferences sp;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 100:
                    if (msg.arg1 == 0){
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        // 查看记住密码和自动登录状态
                        if (ck_rem.isChecked()){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("IsRemember",ck_rem.isChecked());
                            editor.putBoolean("IsAuto",auto_login.isChecked());
                            editor.putString("Account",et_phone.getText().toString());
                            editor.putString("Password",et_pwd.getText().toString());
                            editor.commit();
                        }
                         navigateWithDestroyBefore(MainActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    else if (msg.arg1 ==1){
                        Toast.makeText(LoginActivity.this,"密码错误~",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this,"无此用户~请注册",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private CheckBox ck_rem;
    private CheckBox auto_login;
    private TextView to_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        checkAndRequestPermission();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    private void initData() {
        sp = getSharedPreferences("userinfo",MODE_PRIVATE);
        if (sp.getBoolean("IsRemember",false)){
            et_phone.setText(sp.getString("Account",""));
            et_pwd.setText(sp.getString("Password",""));
            ck_rem.setChecked(true);
            if (sp.getBoolean("IsAuto",false)){
                auto_login.setChecked(true);
                Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mIntent);
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        et_phone = findViewById(R.id.et_phone);
        et_pwd = findViewById(R.id.et_pwd);
        login_bt = findViewById(R.id.tv_login);
        ck_rem = findViewById(R.id.ck_rem);
        auto_login = findViewById(R.id.auto_login);
        to_register = findViewById(R.id.tv_to_register);
        sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        login_bt.setOnClickListener(this);
        to_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                beginLogin();
                break;
            case R.id.tv_to_register:
                navigateTo(RegisterActivity.class);
                break;
        }
    }

    private void beginLogin() {
        String phone = et_phone.getText().toString();
        String pwd = et_pwd.getText().toString();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 100;
                ResultSet rs = Database.SelectFromData("*","Users","account",phone);
                try {
                    if (rs.next()){
                        if (rs.getString("password").equals(pwd)){
                            message.arg1 = 0;
                        }
                        else {
                            message.arg1 = 1;
                        }
                    }
                    else {
                        message.arg1 = 2;
                    }
                } catch ( SQLException e){
                    e.printStackTrace();
                }
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission()
    {
        List<String> lackedPermission = new ArrayList<String>();
        // 例：要求有Manifest.permission.READ_PHONE_STATE权限
        if (!(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            lackedPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED))
        {
            lackedPermission.add(Manifest.permission.CAMERA);
        }

        if (lackedPermission.size() == 0)
        {
            // 已经有了权限
        }
        else
        {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用logAction，否则申请到权限之后再调用。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024)
        {
            // 获得用户授权
        }
        else
        {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(LoginActivity.this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

}