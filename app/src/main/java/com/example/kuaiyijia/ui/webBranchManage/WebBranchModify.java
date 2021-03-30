package com.example.kuaiyijia.ui.webBranchManage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.Location;
import com.example.kuaiyijia.Entity.WebBranchListItem;
import com.example.kuaiyijia.ui.MainActivity;
import com.example.kuaiyijia.R;


public class WebBranchModify extends AppCompatActivity implements View.OnClickListener {

    private EditText wb_modify_name;
    private EditText wb_modify_abbreviation;
    private EditText wb_modify_tel;
    private EditText wb_modify_person_name;
    private EditText wb_modify_person_tel;
    private EditText wb_modify_concretAddress;
    private TextView wb_modify_location_bt;
    private Button wb_modify_confirm;
    private WebBranchListItem webBranch;
    private Button back_bt;
    private Location location;
    private AMapLocationClient mapLocationClient = null;
    private AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    private AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null){
                location = new Location(aMapLocation.getLatitude(),aMapLocation.getLongitude(),aMapLocation.getAddress(),
                        aMapLocation.getCountry(),aMapLocation.getProvince(),aMapLocation.getCity(),aMapLocation.getStreet(),
                        aMapLocation.getStreetNum(),aMapLocation.getCityCode(),aMapLocation.getAdCode());
                wb_modify_concretAddress.setText(aMapLocation.getAddress());
                Log.i("TAG", "onLocationChanged: "+aMapLocation.getAddress());
                mapLocationClient.stopLocation();
            }
            else {
                Log.e("MapError", "error code "+ aMapLocation.getErrorCode()+", error info:" +aMapLocation.getErrorInfo() );
            }
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1037:
                    int result = msg.arg1;
                    if (result == 0){
                        Toast.makeText(getApplicationContext(),"修改失败！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent mIntent = new Intent(WebBranchModify.this, MainActivity.class);
                        mIntent.putExtra("id",3);
                        startActivity(mIntent);
                        Toast.makeText(getApplicationContext(),"修改成功！",Toast.LENGTH_SHORT).show();
//                        finish();
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
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_web_branch_modify);
        initView();
        initData();
    }

    private void initData() {
        webBranch = (WebBranchListItem) getIntent().getExtras().getParcelable("webBranch");

        wb_modify_name.setText(webBranch.getWebBranchName());
        wb_modify_abbreviation.setText(webBranch.getWebBranchJC());
        wb_modify_tel.setText(webBranch.getHuoYunTel());
        wb_modify_person_name.setText(webBranch.getCantact());
        wb_modify_person_tel.setText(webBranch.getCantactTel());
        wb_modify_concretAddress.setText(webBranch.getDetailAddress());
    }

    private void initView() {
        wb_modify_name = (EditText) findViewById(R.id.wb_modify_name);
        wb_modify_abbreviation = (EditText) findViewById(R.id.wb_modify_abbreviation);
        wb_modify_tel = (EditText) findViewById(R.id.wb_modify_tel);
        wb_modify_person_name = (EditText) findViewById(R.id.wb_modify_person_name);
        wb_modify_person_tel = (EditText) findViewById(R.id.wb_modify_person_tel);
        wb_modify_concretAddress = (EditText) findViewById(R.id.wb_modify_concretAddress);
        wb_modify_location_bt = (TextView) findViewById(R.id.wb_modify_location_bt);
        wb_modify_confirm = (Button) findViewById(R.id.wb_modify_confirm);
        back_bt = (Button) findViewById(R.id.backtolast);
        wb_modify_location_bt.setOnClickListener(this);
        wb_modify_confirm.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wb_modify_location_bt:
                Toast.makeText(WebBranchModify.this,"请稍等，正在获取您的位置~",Toast.LENGTH_LONG).show();
                startClient(WebBranchModify.this);
                break;
            case R.id.wb_modify_confirm:
                updateWebBranch();
                break;
            case R.id.backtolast:
                finish();
                break;
            default:
                break;
        }
    }

    private void startClient(Context context) {
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

    private void updateWebBranch() {
        //  需要更新的数据
        String name =  wb_modify_name.getText().toString();
        String name_abbreviation =  wb_modify_abbreviation.getText().toString() ;
        String tel =  wb_modify_tel.getText().toString();
        String personName =   wb_modify_person_name.getText().toString() ;
        String personTel =  wb_modify_person_tel.getText().toString();
        String concretAddress =  wb_modify_concretAddress.getText().toString() ;
        // c_id 默认为1
        String  ID_name = "HYBID";
        int ID_value =Integer.valueOf(webBranch.getWebBranchID()) ;
        String [] names = {"HYBNAME","C_ID","WDJC","HYBTEL","HYBLXR","HYBLXDH","HYBADDR"};
        String [] values = {name,"1",name_abbreviation,tel,personName,personTel,concretAddress};
        // 建立线程操作数据库
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = Database.updateForData("PUB_HYB",ID_name,ID_value,names,values);
                Message message = new Message();
                message.what = 1037;
                message.arg1 = result;
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }
}