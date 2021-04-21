package com.example.kuaiyijia.UI.orderManage;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.OrderItem;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.CustomDialog;

public class AddOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText add_re_station;
    private EditText add_re_address;
    private EditText add_re_person;
    private EditText add_re_phone;
    private EditText add_orderNum;
    private EditText add_orderfreight;
    private EditText add_dianfukuan;
    private EditText add_agencyMoney;
    private EditText add_to_address;
    private EditText add_to_person;
    private EditText add_to_phone;
    private EditText add_remark;
    private RadioButton a_post;
    private RadioButton a_selfTake;
    private Button finished_confirm;
    private Button back_bt;
    private OrderItem orderItem;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.arg1 == 0){
                Toast.makeText(AddOrderActivity.this,"添加失败！",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AddOrderActivity.this,"添加成功！",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.order_addition);
        initView();
    }

    private void initView() {
        add_re_station = (EditText) findViewById(R.id.add_re_station);
        add_re_address = (EditText) findViewById(R.id.add_re_address);
        add_re_person = (EditText) findViewById(R.id.add_re_person);
        add_re_phone = (EditText) findViewById(R.id.add_re_phone);
        add_orderNum = (EditText) findViewById(R.id.add_orderNum);
        add_orderfreight = (EditText) findViewById(R.id.add_orderfreight);
        add_dianfukuan = (EditText) findViewById(R.id.add_dianfukuan);
        add_agencyMoney = (EditText) findViewById(R.id.add_agencyMoney);
        add_to_address = (EditText) findViewById(R.id.add_to_address);
        add_to_person = (EditText) findViewById(R.id.add_to_person);
        add_to_phone = (EditText) findViewById(R.id.add_to_phone);
        add_remark = (EditText) findViewById(R.id.add_remark);
        back_bt = (Button) findViewById(R.id.backtolast);
        // ratioButton
        a_post = (RadioButton) findViewById(R.id.a_post);
        a_selfTake = (RadioButton) findViewById(R.id.a_selfTake);
        finished_confirm = findViewById(R.id.finished_confirm);
        finished_confirm.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finished_confirm:
                //  ，弹框询问 ，在获取数据， 再添加数据
                CustomDialog dialog = new CustomDialog(AddOrderActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("您是否确认添加该订单？");
                dialog.setConfirm("确认", new CustomDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(CustomDialog dialog) {
                        /*
                        orderItem = new OrderItem("","","1",add_to_address.getText().toString(),add_re_address.getText().toString(),
                                "1",add_dianfukuan.getText().toString(),add_dianfukuan.getText().toString(),add_re_station.getText().toString(),
                                add_re_person.getText().toString(),add_re_phone.getText().toString(),add_to_person.getText().toString(),
                                add_to_phone.getText().toString(),"",add_remark.getText().toString(),add_orderNum.getText().toString(),
                                add_orderfreight.getText().toString());
                        if (a_post.isChecked()){
                            orderItem.setService_way("0");
                        }*/
                        // 写入数据库
                        writeToDataBase();
                    }
                });
                dialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
                    @Override
                    public void onCancel(CustomDialog dialog) {
                        Log.i("TAG", "onCancel: ");
                        Toast.makeText(AddOrderActivity.this,"取消成功~",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case R.id.backtolast:
                finish();
                break;
        }
    }

    private void writeToDataBase() {
        // 生成该订单的唯一订单号
//        orderItem.setOrder_number("5");
        String [] names = {"order_number","huowu_status","send_address","receive_address","order_status","daishou_money","dianfu_money",
        "receive_station","receive_person","receive_tel","send_person","send_tel","service_way","addition","item_nums","mail_fess"};
        // 获取数据
        String service_way ;
        if (a_post.isChecked()){
            service_way = "0";
        }
        else {
            service_way = "1";
        }
        String [] values = {"5","1",add_to_address.getText().toString(),add_re_address.getText().toString(),
                "1",add_dianfukuan.getText().toString(),add_dianfukuan.getText().toString(),add_re_station.getText().toString(),
                add_re_person.getText().toString(),add_re_phone.getText().toString(),add_to_person.getText().toString(),
                add_to_phone.getText().toString(),service_way,add_remark.getText().toString(),add_orderNum.getText().toString(),
                add_orderfreight.getText().toString()};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = Database.insertIntoDataForColumn("orders",names,values);
                Message message = new Message();
                message.arg1 = result;
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }
}
