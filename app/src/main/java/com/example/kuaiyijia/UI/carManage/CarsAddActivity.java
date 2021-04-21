package com.example.kuaiyijia.UI.carManage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.UI.MainActivity;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CarsAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText car_add_plate_number;
    private EditText car_add_id;
    private EditText car_add_license;
    private Spinner car_add_type;
    private EditText car_add_car_lenth;
    private EditText car_add_car_height;
    private EditText car_add_car_width;
    private EditText car_add_car_load;
    private Button car_add_confirm_bt;
    private String car_type;
    private int isExist = 0;
    private String[] columns;
    private String[] values;
    private Button back_bt;
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1052:
                    if(msg.getData().getInt("inserState") != 0){
                        Toast.makeText(CarsAddActivity.this,"添加成功！",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CarsAddActivity.this, MainActivity.class);
                        intent.putExtra("id",4);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(CarsAddActivity.this,"添加失败！",Toast.LENGTH_LONG).show();
                    }
                    break;
                case 1056:
                    isExist = msg.getData().getInt("exist");
                    if (isExist == 0){
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what= 1052;
                                Bundle bundle = new Bundle();
                                int result = Database.insertIntoDataForColumn("PUB_VEHICLE",columns,values);
                                Log.e("insert ", "run: "+result);
                                bundle.putInt("inserState",result);
                                msg.setData(bundle);
                                mhandler.sendMessage(msg);
                            }
                        });
                        thread.start();
                        thread.interrupt();
                    }
                    else {
                        Toast.makeText(CarsAddActivity.this,"该车辆已经存在！",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_cars_add);
        initView();
    }

    public void initView(){
        car_add_plate_number = (EditText) findViewById(R.id.car_add_plate_number);
        car_add_id =  (EditText) findViewById(R.id.car_add_id);
        car_add_license = (EditText) findViewById(R.id.car_add_license);
        car_add_car_lenth = (EditText) findViewById(R.id.car_add_car_lenth);
        car_add_car_height = (EditText) findViewById(R.id.car_add_car_height);
        car_add_car_width =  (EditText) findViewById(R.id.car_add_car_width);
        car_add_car_load =  (EditText) findViewById(R.id.car_add_car_load);
        car_add_confirm_bt = (Button) findViewById(R.id.car_add_confirm_bt);
        car_add_type = (Spinner) findViewById(R.id.car_add_type);
        back_bt = (Button) findViewById(R.id.backtolast);
        back_bt.setOnClickListener(this);
        car_add_confirm_bt.setOnClickListener(this);
        car_add_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                car_type = String.valueOf(position) ;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_add_confirm_bt:
                addCarInfo();
                Log.d("car add", "success ");
                break;
            case R.id.backtolast:
                finish();
                break;
        }
    }

    public void addCarInfo(){
        // 1 根据车牌号判定数据库中是否有该车辆
        String V_NO = car_add_plate_number.getText().toString();
        String V_CNO = car_add_id.getText().toString();
        String V_TNO = car_add_license.getText().toString();
        String VT_ID = car_type;
        String HC_LENGHT = car_add_car_lenth.getText().toString();
        String HC_WIDTH =  car_add_car_height.getText().toString();
        String HC_HEIGHT = car_add_car_width.getText().toString();
        String V_LOAD = car_add_car_load.getText().toString();
        columns = new String[]{"V_NO", "V_CNO", "V_TNO","VT_ID" ,"HC_LENGHT" , "HC_WIDTH" ,"HC_HEIGHT","V_LOAD"};
        values = new String[]{"'"+V_NO+"'",V_CNO,V_TNO,VT_ID,HC_LENGHT,HC_WIDTH,HC_HEIGHT,V_LOAD};
        // 进入数据库查找是否有该车辆
        Thread thread_checkCar = new Thread(new Runnable() {
            @Override
            public void run() {
                int exist = 0;
                Message msg = new Message();
                msg.what = 1056;
                Bundle bundle = new Bundle();
                ResultSet result = Database.SelectFromData(" * ","PUB_VEHICLE","V_NO",V_NO);
                try {
                    if(result.next()){
                        exist = 1;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                bundle.putInt("exist",exist);
                msg.setData(bundle);
                mhandler.sendMessage(msg);
            }
        });
        thread_checkCar.start();
        thread_checkCar.interrupt();

    }
}