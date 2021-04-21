package com.example.kuaiyijia.UI.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseMethods;
import com.example.kuaiyijia.Entity.UserEntity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.BaseActivity;
import com.example.kuaiyijia.Utils.CustomDialog;

public class AddFreightActivity extends BaseActivity {

    private EditText et_name;
    private EditText et_phone;
    private EditText et_user;
    private EditText et_user_phone;
    private EditText et_address;
    private TextView tv_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_user = findViewById(R.id.et_user);
        et_user_phone = findViewById(R.id.et_user_phone);
        et_address = findViewById(R.id.et_address);
        tv_btn = findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserAndsaveHYB();
            }
        });

    }

    private void addUserAndsaveHYB() {
        // 先弹出确认对话框
        CustomDialog dialog = new CustomDialog(AddFreightActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("您确认要添加该货运部吗？");
        dialog.setConfirm("确认", new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                // 新增货运部信息到数据库
                String [] names = {DataBaseConfig.WebBranchName,DataBaseConfig.HuoYunTel,DataBaseConfig.WebBranchCantact
                        ,DataBaseConfig.WebBranchCantactTel,DataBaseConfig.WebBranchDetailAddress};
                String [] values = {"'"+et_name.getText().toString()+"'",et_phone.getText().toString(),"'"+et_user.getText().toString()+"'",
                        "'"+et_user_phone.getText().toString()+"'","'"+et_address.getText().toString()+"'"};
                DataBaseMethods.insert(DataBaseConfig.WebBranchTableName,names,values);
                //
                UserEntity user = (UserEntity)  getIntent().getExtras().getSerializable("user");
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                navigateToWithData(BindingFreightActivity.class,bundle);
                finish();

            }
        });
        dialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
            @Override
            public void onCancel(CustomDialog dialog) {
            }
        });
        dialog.show();
    }

    private void initData() {

    }

    @Override
    protected int initLayout() {
        return R.layout.activity_add_freight;
    }
}