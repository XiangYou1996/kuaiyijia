package com.example.kuaiyijia.UI.LoginRegister;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseMethods;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.UserEntity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.FreightBinding.BindingFreightActivity;
import com.example.kuaiyijia.Utils.BaseActivity;

import java.sql.ResultSet;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_phone;
    private EditText et_pwd;
    private TextView to_login;
    private TextView tv_register;
    private EditText et_pwd_again;
    private SharedPreferences sp;
    private UserEntity User =new UserEntity();
    private String Password;
    private String Account;
    private Boolean isExist;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    if (msg.arg1 == 0){
                        User.setAccount(et_phone.getText().toString());
                        User.setPassword(et_pwd.getText().toString());
                        // 将信息先存到数据库
                        String [] names = {DataBaseConfig.UserAccount,DataBaseConfig.UserPassword};
                        String [] values = {"'"+User.getAccount()+"'","'"+User.getPassword()+"'"};
                        DataBaseMethods.insert(DataBaseConfig.UserTableName,names,values);

                    }else {
                        showToast("该用户已经被注册！");
                    }

/*                    UserEntity user = User;
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
                    }*/
                    // 带上用户名和密码   回到登录界面
                    UserEntity user = User;
                    if(user != null) {
                        //  使用sp来存储
                        sp = getSharedPreferences("userinfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("IsRegister",true);
                        editor.putString("register_account",user.getAccount());
                        editor.putString("register_password",user.getPassword());
                        editor.commit();
                        navigateTo(LoginActivity.class);
                        finish();
                    }
                    break;
            }
        }
    };

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
                register();
                break;
        }
    }

    private void register() {
        // 首先检测 电话号是否为空且正确，密码是否为空且输入正确
        String phone = et_phone.getText().toString();
        String password = et_pwd.getText().toString();
        String password_agian = et_pwd_again.getText().toString();
        if (phone.length() == 0) {
            showToast("请输入电话号码~");
            return;

        }else if (phone.length()!= 11){
            showToast("请输入正确的电话号码!");
            return;

        }
        if (password.length() == 0 || password_agian.length() == 0){
            showToast("请输入密码并确认~");
            return;
        }
        if (!password.equals(password_agian)) {
            showToast("两次密码输入不一致！");
            return;
        }
        userExist(phone);
/*        //  创建一个用户
        UserEntity user = new UserEntity();
        user.setAccount(phone);
        user.setPassword(password);
        // 将信息先存到数据库
        String [] names = {DataBaseConfig.UserAccount,DataBaseConfig.UserPassword};
        String [] values = {"'"+user.getAccount()+"'","'"+user.getPassword()+"'"};
        DataBaseMethods.insert(DataBaseConfig.UserTableName,names,values);*/

    }

    private void userExist(String account) {

        // 查询是否存在该用户
        Thread thread=  new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg= new Message();
                msg.what = 1;
               ResultSet rs = Database.SelectFromData("*",DataBaseConfig.UserTableName,DataBaseConfig.UserAccount,account);
               try {
                   while (rs.next()){
                       if (rs.getString(DataBaseConfig.UserPriID) ==null) {
                           msg.arg1 = 0;
                       }else {
                           msg.arg1 = 1;
                       }
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
               mHandler.sendMessage(msg);
            }
        });
        thread.start();
        thread.interrupt();
    }

}