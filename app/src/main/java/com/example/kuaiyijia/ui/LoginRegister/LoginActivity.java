package com.example.kuaiyijia.ui.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.ui.MainActivity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_pwd;
    private TextView login_bt;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 100:
                    if (msg.arg1 == 0){
                        Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mIntent);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_phone = findViewById(R.id.et_phone);
        et_pwd = findViewById(R.id.et_pwd);
        login_bt = findViewById(R.id.tv_login);
        login_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                beginLogin();
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
}