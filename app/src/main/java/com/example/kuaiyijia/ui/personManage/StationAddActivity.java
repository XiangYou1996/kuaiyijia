package com.example.kuaiyijia.ui.personManage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;

import java.util.List;

public class StationAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText station_add_name;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;
    private CheckBox checkBox7;
    private CheckBox checkBox8;
    private CheckBox checkBox9;
    private CheckBox checkBox10;
    private Button station_add_cf_bt;
    private  StringBuilder authority_code = new StringBuilder("0000000000");
    private Button back_bt;
    // checkBox 的监听对象
    private CompoundButton.OnCheckedChangeListener checkBoxes_listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.check1:
                    if (isChecked){
                        authority_code.setCharAt(0,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(0,'0');
                    }
                    break;
                case R.id.check2:
                    if (isChecked){
                        authority_code.setCharAt(1,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(1,'0');
                    }
                    break;
                case R.id.check3:
                    if (isChecked){
                        authority_code.setCharAt(2,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(2,'0');
                    }
                    break;
                case R.id.check4:
                    if (isChecked){
                        authority_code.setCharAt(3,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(3,'0');
                    }
                    break;
                case R.id.check5:
                    if (isChecked){
                        authority_code.setCharAt(4,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(4,'0');
                    }
                    break;
                case R.id.check6:
                    if (isChecked){
                        authority_code.setCharAt(5,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(5,'0');
                    }
                    break;
                case R.id.check7:
                    if (isChecked){
                        authority_code.setCharAt(6,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(6,'0');
                    }
                    break;
                case R.id.check8:
                    if (isChecked){
                        authority_code.setCharAt(7,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(7,'0');
                    }
                    break;
                case R.id.check9:
                    if (isChecked){
                        authority_code.setCharAt(8,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(8,'0');
                    }
                    break;
                case R.id.check10:
                    if (isChecked){
                        authority_code.setCharAt(9,'1');
                    }
                    if (!isChecked){
                        authority_code.setCharAt(9,'0');
                    }
                    break;
            }
        }
    };
    //  handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1044:
                    int result = msg.getData().getInt("result");
                    if (result == 0){
                        Toast.makeText(StationAddActivity.this,"修改岗位失败！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent mIntent = new Intent(StationAddActivity.this,StationManageActivity.class);
                        startActivity(mIntent);
                        Toast.makeText(StationAddActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_station_add);
        initView();
    }
    public  void initView(){

        station_add_name = (EditText) findViewById(R.id.station_add_name);

        checkBox1 = (CheckBox) findViewById(R.id.check1);
        checkBox2 = (CheckBox) findViewById(R.id.check2);
        checkBox3 = (CheckBox) findViewById(R.id.check3);
        checkBox4 = (CheckBox) findViewById(R.id.check4);
        checkBox5 = (CheckBox) findViewById(R.id.check5);
        checkBox6 = (CheckBox) findViewById(R.id.check6);
        checkBox7 = (CheckBox) findViewById(R.id.check7);
        checkBox8 = (CheckBox) findViewById(R.id.check8);
        checkBox9 = (CheckBox) findViewById(R.id.check9);
        checkBox10 = (CheckBox) findViewById(R.id.check10);
        checkBox1.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox2.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox3.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox4.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox5.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox6.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox7.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox8.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox9.setOnCheckedChangeListener(checkBoxes_listener);
        checkBox10.setOnCheckedChangeListener(checkBoxes_listener);
        back_bt = (Button) findViewById(R.id.backtolast);
        station_add_cf_bt = (Button) findViewById(R.id.station_add_cf_bt);
        station_add_cf_bt.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.station_add_cf_bt:
                addStationToDatabase();
                break;
            case R.id.backtolast:
                finish();
                break;
        }
    }

    public void addStationToDatabase(){
        // 获取岗位名字
        String station = "'"+station_add_name.getText().toString()+"'";
        // 权限编码
        String code = String.valueOf(authority_code);
        if (code.equals("0000000000")){
                Toast.makeText(StationAddActivity.this,"请给该职位赋予一定的权限~",Toast.LENGTH_SHORT).show();
        }else {
            // 开启线程插入数据
            String [] names = {"ET_ID","ET_CODE","ET_NAME"};
            String [] values = {"4",code, station};
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = Database.insertIntoDataForColumn("PUB_TMSETYPE",names,values);
                    Message message = new Message();
                    message.what = 1044;
                    Bundle bundle = new Bundle();
                    bundle.putInt("result",result);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            });
            thread.start();
            thread.interrupt();
        }

    }
}