package com.example.kuaiyijia.ui.personManage;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.PersonListItem;
import com.example.kuaiyijia.ui.MainActivity;
import com.example.kuaiyijia.R;


public class PersonModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText pm_modify_name;
    private EditText pm_modify_phone;
    private TextView pm_modify_webbranch;
    private Spinner  pm_modify_station_bt;
    private Button    pm_person_manage_confirm;
    private Button back_bt;
    private PersonListItem person;
    // 岗位在array中的位置
    private  int array_position = 0;
    // 定义handler 来处理数据操作结果
    private Handler mHandler  = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1041:
                    //  获取更新的状态
                    int result = msg.getData().getInt("updateResult");
                    if (result  == 0){
                        Toast.makeText(PersonModifyActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(PersonModifyActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        // 跳转回到 person manage fragment 此处直接finish当前的activity即可  但是由于要刷新数据还是intent
                        Intent mIntent = new Intent(PersonModifyActivity.this, MainActivity.class);
                        mIntent.putExtra("id",2);
                        startActivity(mIntent);
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
        setContentView(R.layout.activity_person_modify);
        initView();
        initData();
        setSpinnerStyle();
    }
    public void initView(){
        pm_modify_name = findViewById(R.id.pm_modify_name);
        pm_modify_phone = findViewById(R.id.pm_modify_phone);
        pm_modify_webbranch = findViewById(R.id.pm_modify_webbranch);
        pm_modify_station_bt = findViewById(R.id.pm_modify_station_bt);
        pm_person_manage_confirm  = findViewById(R.id.pm_person_manage_confirm);
        back_bt = (Button) findViewById(R.id.backtolast);
        pm_person_manage_confirm.setOnClickListener(this);
        back_bt.setOnClickListener(this);
        String [] spinnerItems = getResources().getStringArray(R.array.StationType);
        // spinner 样式
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.myspinnerstyle,spinnerItems);
        adapter.setDropDownViewResource(R.layout.myspinnerdropoutstyle);
        pm_modify_station_bt.setAdapter(adapter);
        pm_modify_station_bt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                array_position = position + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void initData(){
        // 岗位对应的number
        //  获取 上一个activity传过来的数据
        Intent lastIntent = getIntent();
        Bundle bundle = lastIntent.getExtras();
        person = bundle.getParcelable("person");

        // 设置数据
        pm_modify_name.setText(person.getPerson_name());
        pm_modify_phone.setText(person.getPerson_tel());
        pm_modify_webbranch.setText(person.getPerson_webBranch());
        // 在array 找到对应岗位的数值
//        for (int i = 0 ; i< getResources().getTextArray(R.array.StationType).length;i++){
//            if(person.getPerson_sation().equals(getResources().getTextArray(R.array.StationType)[i])){
//                array_position = i ;
//                break;
//            }
//        }
        array_position = Integer.valueOf(person.getPerson_sation_ID())-2000-1;
        pm_modify_station_bt.setSelection(array_position);

    }
    public void setSpinnerStyle(){


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pm_person_manage_confirm:
                // 传输数据到数据库
                updateDataToDatabase();
                break;
            case R.id.backtolast:
                // 跳转回到 person manage fragment 此处直接finish当前的activity即可  但是由于要刷新数据还是intent
                Intent mIntent = new Intent(PersonModifyActivity.this, MainActivity.class);
                mIntent.putExtra("id",2);
                startActivity(mIntent);
                break;
        }
    }

    public void updateDataToDatabase(){
        // 更新数据  这里需要写回更新两个表
        // 第一个表
        String TabName = "PUB_EMPLOYEE";
        String ID_name = "EM_ID";
        int ID_value = Integer.valueOf(person.getPerson_ID());
        String EM_NAME = pm_modify_name.getText().toString();
        String MTEL = pm_modify_phone.getText().toString();
        String HYBID = person.getPerson_webBranch();
        String [] columns = {"EM_NAME","MTEL"};
        String [] values = {EM_NAME ,MTEL};


        // 更新第二个表
        String  ET_ID = String.valueOf(array_position+2000);
        String TabName_2 = "PUB_EMETYPE";
        String ID_name_2 = "EM_ID";
        int  ID_value_2 = Integer.valueOf(person.getPerson_ID());

        String [] columns_2 = {"ET_ID"};
        String [] values_2 = {ET_ID};
        // 开启线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 第一个
                int result = Database.updateForData(TabName,ID_name,ID_value, columns,values);
                // 第二个
                int result_2 = Database.updateForData(TabName_2, ID_name_2, ID_value_2,columns_2,values_2);
                Message msg = new Message();
                msg.what = 1041;
                Bundle bundle = new Bundle();
                bundle.putInt("updateResult", result&result_2);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        });
        thread.start();
        thread.interrupt();

    }
}