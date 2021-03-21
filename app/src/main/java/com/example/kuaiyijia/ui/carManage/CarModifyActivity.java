package com.example.kuaiyijia.ui.carManage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.CarListItem;
import com.example.kuaiyijia.MainActivity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.ui.personManage.StationManageActivity;


public class CarModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText car_modify_plate_number;
    private EditText car_modify_id;
    private EditText car_modify_license;
    private Spinner car_modify_type;
    private EditText car_modify_car_lenth;
    private EditText car_modify_car_height;
    private EditText car_modify_car_width;
    private EditText car_modify_car_load;
    private Button car_modify_confirm_bt;
    private CarListItem car ;
    private String car_type;
    private int tag ;
    private Toast toast ;
    private  int ModifyCarNum = 1051;

    private Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1051:
                    tag = msg.getData().getInt("updateResult");
                    // 处理返回结果
                    if (tag  != 0){
                        toast = Toast.makeText(CarModifyActivity.this,"修改成功！",Toast.LENGTH_LONG);
                        toast.show();
                        // 跳转到管理界面
                        Intent mIntent= new Intent(CarModifyActivity.this, MainActivity.class);
                        mIntent.putExtra("id",4);
                        startActivity(mIntent);
                    }
                    else {
                        toast = Toast.makeText(CarModifyActivity.this,"修改失败！",Toast.LENGTH_LONG);
                        toast.show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_modify);
        initView();
        initData();
    }


    public void initView(){
        car_modify_plate_number =(EditText) findViewById(R.id.car_modify_plate_number);
        car_modify_id = (EditText) findViewById(R.id.car_modify_id);
        car_modify_license = (EditText) findViewById(R.id.car_modify_license);
        car_modify_type = (Spinner) findViewById(R.id.car_modify_type);
        car_modify_car_lenth = (EditText) findViewById(R.id.car_modify_car_lenth);
        car_modify_car_height =  (EditText) findViewById(R.id.car_modify_car_height);
        car_modify_car_width = (EditText) findViewById(R.id.car_modify_car_width);
        car_modify_car_load = (EditText) findViewById(R.id.car_modify_car_load);
        car_modify_confirm_bt = (Button) findViewById(R.id.car_modify_confirm_bt);

        car_modify_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                car_type = String.valueOf(position) ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        car_modify_confirm_bt.setOnClickListener(this);
    }
    public void initData(){
        // 获取传过来的数据  使用parcelable方式
        Intent lastIntent = getIntent();
        Bundle bundle = lastIntent.getExtras();
        car = bundle.getParcelable("modify");
        if(car != null) {
            car_modify_plate_number.setText(car.getCarPlateNum());
            car_modify_id.setText(car.getCarIdentityId());
            car_modify_license.setText(car.getCarLicenseId());
            //car_modify_type
            car_modify_car_lenth.setText(car.getLenth());
            car_modify_car_width.setText(car.getWidth());
            car_modify_car_height.setText(car.getHeight());
            car_modify_car_load.setText(car.getLoad());
            car_modify_type.setSelection(Integer.valueOf(car.getCarType()));
        }
    }
    @Override
    public void onClick(View v) {
        // 更新数据项
        switch (v.getId()){
            case R.id.car_modify_confirm_bt:
                // 传输数据到数据库
                updateDataToDatabase();
                break;
        }

    }

    public void updateDataToDatabase(){
        // 更新数据
        String TabName = "PUB_VEHICLE";
        String ID_name = "V_ID";
        int ID_value = Integer.valueOf(car.getID());
        String V_NO = car_modify_plate_number.getText().toString();
        String V_CNO = car_modify_id.getText().toString();
        String V_TNO = car_modify_license.getText().toString();
        String VT_ID = car_type;
        String HC_LENGHT = car_modify_car_lenth.getText().toString();
        String HC_WIDTH =  car_modify_car_height.getText().toString();
        String HC_HEIGHT = car_modify_car_width.getText().toString();
        String V_LOAD = car_modify_car_load.getText().toString();
        String [] columns = {"V_NO", "V_CNO", "V_TNO","VT_ID" ,"HC_LENGHT" , "HC_WIDTH" ,"HC_HEIGHT","V_LOAD"};
        String [] values = {V_NO,V_CNO,V_TNO,VT_ID,HC_LENGHT,HC_WIDTH,HC_HEIGHT,V_LOAD};
        // 开启线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = Database.updateForData(TabName,ID_name,ID_value, columns,values);
                Message msg = new Message();
                msg.what = 1051;
                Bundle bundle = new Bundle();
                bundle.putInt("updateResult", result);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        });
        thread.start();
        thread.interrupt();
    }

}