package com.example.kuaiyijia.ui.orderManage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Entity.OrderItem;
import com.example.kuaiyijia.ui.MainActivity;
import com.example.kuaiyijia.R;

public class GoingOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button confirm;
    private OrderItem orderItem;
    private TextView going_re_station;
    private TextView going_re_address;
    private TextView going_re_person;
    private TextView going_re_phone;
    private TextView going_itemnums;
    private TextView going_dianfu;
    private TextView going_daishoukuan;
    private TextView going_to_address;
    private TextView going_to_person;
    private TextView going_to_phone;
    private TextView going_to_service_way;
    private EditText going_mail_fess;
    private TextView going_addition;
    private Button print;
    private Button back_bt;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.order_going_detail);
        initUI();
        initData();
    }

    private void initData() {
        orderItem = (OrderItem) getIntent().getExtras().getParcelable("orderItem");
        going_re_station.setText("收货单位："+orderItem.getReceive_station());
        going_re_address.setText("收货地址：" +orderItem.getReceive_address());
        going_re_person.setText("收货人："+orderItem.getReceive_person());
        going_re_phone.setText("收货人电话："+orderItem.getReceive_tel());
        going_itemnums.setText("件数："+orderItem.getItem_nums()+"件");
        going_dianfu.setText("垫付金额："+orderItem.getDianfu_money()+"元");
        going_daishoukuan.setText("代收款金额："+orderItem.getDaishou_money()+"元");
        going_to_address.setText("发货地址："+orderItem.getSend_address());
        going_to_person.setText("收货人电话："+orderItem.getSend_person());
        going_to_phone.setText(orderItem.getSend_tel());
        String service_way;
        if (orderItem.getService_way().equals("0")){
            service_way = "配送";
        }
        else {
            service_way  = "自取";
        }
        going_to_service_way.setText("服务方式："+service_way);
        going_addition.setText(orderItem.getAddition());
        going_mail_fess.setText(orderItem.getMail_fess());

    }

    private void initUI() {
        going_re_station = (TextView) findViewById(R.id.going_re_station);
        going_re_address = (TextView) findViewById(R.id.going_re_address);
        going_re_person = (TextView) findViewById(R.id.going_re_person);
        going_re_phone = (TextView) findViewById(R.id.going_re_phone);
        going_itemnums = (TextView) findViewById(R.id.going_itemnums);
        going_dianfu = (TextView) findViewById(R.id.going_dianfu);
        going_daishoukuan = (TextView) findViewById(R.id.going_daishoukuan);
        going_to_address = (TextView) findViewById(R.id.going_to_address);
        going_to_person = (TextView) findViewById(R.id.going_to_person);
        going_to_phone = (TextView) findViewById(R.id.going_to_phone);
        going_to_service_way = (TextView) findViewById(R.id.going_to_service_way);
        going_mail_fess = (EditText) findViewById(R.id.going_mail_fess);
        going_addition = (TextView) findViewById(R.id.going_addition);
        back_bt = (Button) findViewById(R.id.backtolast);
        print = (Button) findViewById(R.id.print);
        print.setOnClickListener(this);
        confirm = findViewById(R.id.going_confirm);
        confirm.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.print:
                break;
            case R.id.going_confirm:
                // 如果没有任何改变
            case R.id.backtolast:
                Intent mIntent = new Intent(GoingOrderDetailActivity.this, MainActivity.class);
                mIntent.putExtra("id",1);
                startActivity(mIntent);
                break;
        }
    }

}
