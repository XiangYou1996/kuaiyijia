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
import com.example.kuaiyijia.Entity.StationListItem;
import com.example.kuaiyijia.R;

import java.util.List;

public class StationModifyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText station_modify_name;
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
    private StationListItem station;
    private Button station_modify_cf_bt;
    private Button back_bt;
    private List<CheckBox> checkBoxes = null;
    private  StringBuilder authority_code = new StringBuilder("0000000000");
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
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1045:
                    int result = msg.getData().getInt("result");
                    if (result == 0){
                        Toast.makeText(StationModifyActivity.this, "岗位修改失败！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent mIntent = new Intent(StationModifyActivity.this, StationManageActivity.class);
                        startActivity(mIntent);
                        Toast.makeText(StationModifyActivity.this, "修改成功！",Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.activity_station_modify);
        initView();
        initData();
    }
    public void initView (){
        station_modify_name = (EditText) findViewById(R.id.station_modify_name);

        checkBox1 = (CheckBox) findViewById(R.id.mcheck1);
        checkBox2 = (CheckBox) findViewById(R.id.mcheck2);
        checkBox3 = (CheckBox) findViewById(R.id.mcheck3);
        checkBox4 = (CheckBox) findViewById(R.id.mcheck4);
        checkBox5 = (CheckBox) findViewById(R.id.mcheck5);
        checkBox6 = (CheckBox) findViewById(R.id.mcheck6);
        checkBox7 = (CheckBox) findViewById(R.id.mcheck7);
        checkBox8 = (CheckBox) findViewById(R.id.mcheck8);
        checkBox9 = (CheckBox) findViewById(R.id.mcheck9);
        checkBox10 = (CheckBox) findViewById(R.id.mcheck10);
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

        station_modify_cf_bt = (Button) findViewById(R.id.station_modify_cf_bt);
        station_modify_cf_bt.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }
    public void initData (){
        Intent mIntent = getIntent();
        station =  mIntent.getParcelableExtra("station");
        station_modify_name.setText(station.getET_NAME());
        StringBuilder stringBuilder = new StringBuilder(station.getET_CODE());
        if (stringBuilder.charAt(0) == '1'){
            checkBox1.setChecked(true);
        }
        if (stringBuilder.charAt(1) == '1'){
            checkBox2.setChecked(true);
        }
        if (stringBuilder.charAt(2) == '1'){
            checkBox3.setChecked(true);
        }
        if (stringBuilder.charAt(3) == '1'){
            checkBox4.setChecked(true);
        }
        if (stringBuilder.charAt(4) == '1'){
            checkBox5.setChecked(true);
        }
        if (stringBuilder.charAt(5) == '1'){
            checkBox6.setChecked(true);
        }
        if (stringBuilder.charAt(6) == '1'){
            checkBox7.setChecked(true);
        }
        if (stringBuilder.charAt(7) == '1'){
            checkBox8.setChecked(true);
        }
        if (stringBuilder.charAt(8) == '1'){
            checkBox9.setChecked(true);
        }
        if (stringBuilder.charAt(9) == '1'){
            checkBox10.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.station_modify_cf_bt:
                updateStationtoDatabase();
                break;
            case R.id.backtolast:
                finish();
                break;
        }
    }

    private void updateStationtoDatabase() {
        // 获取岗位名字
        String station_name = "'"+station_modify_name.getText().toString()+"'";
        // 权限编码
        String code = String.valueOf(authority_code);
        // 开启线程插入数据
        String ID = "ET_ID";
        int ID_value = Integer.valueOf(station.getET_ID());
        String [] names = {"ET_CODE","ET_NAME"};
        String [] values = {code, station_name};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = Database.updateForData("PUB_TMSETYPE",ID,ID_value,names,values);
                Message message = new Message();
                message.what = 1045;
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