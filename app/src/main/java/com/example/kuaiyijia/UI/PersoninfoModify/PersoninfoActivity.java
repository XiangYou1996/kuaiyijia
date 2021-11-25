package com.example.kuaiyijia.UI.PersoninfoModify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.UserEntity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.FreightBinding.BindingFreightActivity;
import com.example.kuaiyijia.UI.MainActivity;
import com.example.kuaiyijia.Utils.BaseActivity;

import java.sql.ResultSet;


public class PersoninfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView pr_account;
    private TextView pr_name;
    private TextView pr_tel;
    private TextView pr_hyb;
    private TextView pr_wls;
    private TextView name_modify;
    private TextView tel_modify;
    private TextView hyb_modify;
    private TextView wls_modify;
    private UserEntity user;
    private Button backtolast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        user = (UserEntity) getIntent().getExtras().getSerializable("user");
        pr_account.setText(user.getAccount());
        pr_tel.setText(user.getAccount());
        final String[] hyb = {""};
        String wls = "";
        // 姓名 电话 待写
        if (user.getHYB_ID() != null) {
            // 查找货运部和物流商
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (user.getHYB_ID() != null) {
                        ResultSet rs1 = Database.SelectFromData("*", DataBaseConfig.WebBranchTableName, DataBaseConfig.WebBranchID, user.getHYB_ID());
                        try {
                            while (rs1.next()) {
                                hyb[0] = rs1.getString(DataBaseConfig.WebBranchName);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 物流商。。。
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (hyb[0] != null) {
                                    pr_hyb.setText(hyb[0]);
                                } else {
                                    pr_hyb.setText("无此货运部！");
                                }
                            }
                        });
                    }
                }
            });
            thread.start();
        }else {
            pr_hyb.setText("请绑定货运部！");
        }
    }

    private void initView() {
        pr_account = findViewById(R.id.pr_account);
        pr_name = findViewById(R.id.pr_name);
        pr_tel = findViewById(R.id.pr_tel);
        pr_hyb = findViewById(R.id.pr_hyb);
        pr_wls = findViewById(R.id.pr_wls);

        name_modify = findViewById(R.id.name_modify);
        tel_modify = findViewById(R.id.tel_modify);
        hyb_modify = findViewById(R.id.hyb_modify);
        wls_modify = findViewById(R.id.wls_modify);
        backtolast = findViewById(R.id.backtolast);
        name_modify.setOnClickListener(this);
        tel_modify.setOnClickListener(this);
        hyb_modify.setOnClickListener(this);
        wls_modify.setOnClickListener(this);
        backtolast.setOnClickListener(this);

    }

    @Override
    protected int initLayout() {
        return R.layout.activity_personinfo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.name_modify:
                break;
            case R.id.tel_modify:
                break;
            case R.id.hyb_modify:
                Bundle bundle = getIntent().getExtras();
                navigateToWithData(BindingFreightActivity.class,bundle);
                finish();
                break;
            case R.id.wls_modify:
                break;
            case R.id.backtolast:
                Bundle bundle1 = getIntent().getExtras();
                navigateToWithData(MainActivity.class,bundle1);
                finish();
                break;
        }
    }
}