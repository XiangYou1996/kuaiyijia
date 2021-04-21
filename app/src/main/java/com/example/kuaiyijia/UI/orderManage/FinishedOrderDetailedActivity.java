package com.example.kuaiyijia.UI.orderManage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Entity.OrderItem;
import com.example.kuaiyijia.UI.MainActivity;
import com.example.kuaiyijia.R;


public class FinishedOrderDetailedActivity extends AppCompatActivity implements View.OnClickListener {

    private Button confirm;
    private OrderItem orderItem;
    private TextView finished_re_station;
    private TextView finished_re_address;
    private TextView finished_re_person;
    private TextView finished_re_phone;
    private TextView finished_itemnums;
    private TextView finished_mail_fess;
    private TextView finished_dianfu;
    private TextView finished_daishou;
    private TextView finished_to_address;
    private TextView finished_to_phone;
    private TextView finished_to_service_way;
    private TextView finished_addition;
    private TextView finished_to_person;
    private Button back_bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.order_finished_detailed);
        initUI();
        initData();
    }

    private void initData() {
        orderItem = getIntent().getExtras().getParcelable("orderItem");
        finished_re_station.setText("收货单位："+orderItem.getReceive_station());
        finished_re_address.setText("收货地址："+orderItem.getReceive_address());
        finished_re_person.setText("收货人："+orderItem.getReceive_person());
        finished_re_phone.setText("收货人电话："+orderItem.getReceive_tel());
        finished_itemnums.setText("件数："+orderItem.getItem_nums()+"件");
        finished_mail_fess.setText(orderItem.getMail_fess()+"元");
        finished_dianfu.setText("垫付金额："+orderItem.getDianfu_money()+"元");
        finished_daishou.setText("代收款金额："+orderItem.getDaishou_money()+"元");
        finished_to_address.setText("发货地址："+orderItem.getSend_address());
        finished_to_person.setText("发货人："+orderItem.getSend_person());
        finished_to_phone.setText("发货人："+orderItem.getSend_tel());
        String service_way;
        if (orderItem.getService_way().equals("0")){
            service_way = "送货";
        }
        else {
            service_way  = "自取";
        }
        finished_to_service_way.setText("服务方式："+service_way);
        finished_addition.setText(orderItem.getAddition());
    }

    private void initUI() {
        finished_re_station = findViewById(R.id.finished_re_station);
        finished_re_address = findViewById(R.id.finished_re_address);
        finished_re_person = findViewById(R.id.finished_re_person);
        finished_re_phone = findViewById(R.id.finished_re_phone);
        finished_itemnums = findViewById(R.id.finished_itemnums);
        finished_mail_fess = findViewById(R.id.finished_mail_fess);
        finished_dianfu = findViewById(R.id.finished_dianfu);
        finished_daishou = findViewById(R.id.finished_daishou);
        finished_to_address = findViewById(R.id.finished_to_address);
        finished_to_person = findViewById(R.id.finished_to_person);
        finished_to_phone = findViewById(R.id.finished_to_phone);
        finished_to_service_way = findViewById(R.id.finished_to_service_way);
        finished_addition = findViewById(R.id.finished_addition_context);
        back_bt = (Button) findViewById(R.id.backtolast);
        confirm = findViewById(R.id.finished_confirm);
        confirm.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finished_confirm:
            case R.id.backtolast:
                Intent mIntent = new Intent(FinishedOrderDetailedActivity.this,MainActivity.class);
                mIntent.putExtra("id",1);
                startActivity(mIntent);
                break;
        }
    }


}
