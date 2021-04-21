package com.example.kuaiyijia.UI.LoginRegister;

import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseMethods;
import com.example.kuaiyijia.Entity.UserEntity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.BaseActivity;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_phone;
    private EditText et_pwd;
    private TextView to_login;
    private TextView tv_register;
    private EditText et_pwd_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        et_phone = findViewById(R.id.et_phone);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_again = findViewById(R.id.et_pwd_again);
        tv_register = findViewById(R.id.tv_register);
        to_login = findViewById(R.id.tv_to_login);
        to_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_to_login:
                finish();
                break;
            case R.id.tv_register:
                UserEntity user = register();
                if (user != null){

                    // 绑定货运部
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",user);
                    navigateToWithData(BindingFreightActivity.class,bundle);
                    finish();
                }
                break;
        }
    }

    private UserEntity register() {
        // 首先检测 电话号是否为空且正确，密码是否为空且输入正确
        String phone = et_phone.getText().toString();
        String password = et_pwd.getText().toString();
        String password_agian = et_pwd_again.getText().toString();
        if (phone.length() == 0) {
            showToast("请输入电话号码~");
            return null;
        }else if (phone.length()!= 11){
            showToast("请输入正确的电话号码!");
            return null;
        }
        if (password.length() == 0 || password_agian.length() == 0){
            showToast("请输入密码并确认~");
            return null;
        }
        if (!password.equals(password_agian)) {
            showToast("两次密码输入不一致！");
        }
        //  创建一个用户
        UserEntity user = new UserEntity();
        user.setAccount(phone);
        user.setPassword(password);
        // 将信息先存到数据库
        String [] names = {DataBaseConfig.UserAccount,DataBaseConfig.UserPassword};
        String [] values = {"'"+user.getAccount()+"'","'"+user.getPassword()+"'"};
        DataBaseMethods.insert(DataBaseConfig.UserTableName,names,values);
        return user;

    }

}