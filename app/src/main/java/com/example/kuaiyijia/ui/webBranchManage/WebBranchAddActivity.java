package com.example.kuaiyijia.ui.webBranchManage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.Location;
import com.example.kuaiyijia.MainActivity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Tools.GaoDeLocation;


public class WebBranchAddActivity extends AppCompatActivity implements View.OnClickListener {

    private  Location location = null;
    private EditText wb_wb_name;
    private EditText wb_wb_abbreviation;
    private EditText wb_wb_tel;
    private EditText wb_wb_person_name;
    private EditText wb_wb_person_tel;
    private EditText wb_concretAddress;
    private ImageButton wb_wb_location_bt;
    private Button wb_add_confirm;

    private final static String TAG = "GaoDeLocation";
    // maplocationclientoption对象
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    //  声明一个地图定位客服端
    public AMapLocationClient mapLocationClient = null;
    //  回调定位监听器
    public AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            // 获取定位结果
            if (aMapLocation != null){
                if (aMapLocation.getErrorCode() == 0){
                    // 解析amaplocaiton
                    location = new Location(aMapLocation.getLatitude(),aMapLocation.getLongitude(),aMapLocation.getAddress(),
                            aMapLocation.getCountry(),aMapLocation.getProvince(),aMapLocation.getCity(),aMapLocation.getStreet(),
                            aMapLocation.getStreetNum(),aMapLocation.getCityCode(),aMapLocation.getAdCode());
                    wb_concretAddress.setText(aMapLocation.getAddress());
                    mapLocationClient.stopLocation();
                }else {
                    Log.e("MapError", "error code "+ aMapLocation.getErrorCode()+", error info:" +aMapLocation.getErrorInfo() );
                }
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1031:
                    int result = msg.getData().getInt("result");
                    if (result == 0){
                        Toast.makeText(WebBranchAddActivity.this, "添加网点失败！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent mIntent = new Intent(WebBranchAddActivity.this, MainActivity.class);
                        mIntent.putExtra("id",3);
                        startActivity(mIntent);
                        Toast.makeText(WebBranchAddActivity.this, "添加成功！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_branch_add);
        initView();
    }

    private void initView() {
        wb_wb_name = (EditText) findViewById(R.id.wb_wb_name);
        wb_wb_abbreviation = (EditText) findViewById(R.id.wb_wb_abbreviation);
        wb_wb_tel = (EditText) findViewById(R.id.wb_wb_tel);
        wb_wb_person_name = (EditText) findViewById(R.id.wb_wb_person_name);
        wb_wb_person_tel = (EditText) findViewById(R.id.wb_wb_person_tel);
        wb_concretAddress = (EditText) findViewById(R.id.wb_concretAddress);
        wb_wb_location_bt = (ImageButton) findViewById(R.id.wb_wb_location_bt);
        wb_add_confirm = (Button) findViewById(R.id.wb_add_confirm);

        wb_wb_location_bt.setOnClickListener(this);
        wb_add_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wb_wb_location_bt:
                Log.i("Location", "location: begin");
                Toast.makeText(getApplicationContext(),"请稍等，正在获取您的位置~",Toast.LENGTH_LONG).show();
                startClient(getApplicationContext());
                break;
            case R.id.  wb_add_confirm:
                // 添加 一个网点
                addWebBranch();
                break;
            default:
                break;
        }
    }

    private void addWebBranch() {
        // 获取该条记录的值
        String name = "'"+ wb_wb_name.getText().toString() +"'";
        String name_abbreviation = "'"+ wb_wb_abbreviation.getText().toString() +"'";
        String tel =  wb_wb_tel.getText().toString();
        String personName = "'"+  wb_wb_person_name.getText().toString() +"'";
        String personTel =  wb_wb_person_tel.getText().toString();
        String concretAddress = "'"+ wb_concretAddress.getText().toString() +"'";
        //这里默认所属物流Id 是1
        String [] names = {"HYBNAME","C_ID","DQ_CODE","HYBTEL","HYBLXR","HYBLXDH","HYBADDR"};
        String [] values = {name,"1",name_abbreviation,tel,personName,personTel,concretAddress};

        // 开启 线程访问数据库
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = Database.insertIntoDataForColumn("PUB_HYB",names,values);
                Message message = new Message();
                message.what = 1031;
                Bundle bundle = new Bundle();
                bundle.putInt("result",result);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }

    public void startClient(Context context){
        mapLocationClient = new AMapLocationClient(context);
        mapLocationClient.setLocationListener(mapLocationListener);
        // 设置定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setMockEnable(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        /*设置定位场景*/
//        mLocationOption.setLocationPurpose();  默认无场景
        // 给客户端对象设置定位参数
        mapLocationClient.setLocationOption(mLocationOption);
        mapLocationClient.startLocation();
    }
}