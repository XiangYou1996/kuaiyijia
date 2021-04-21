package com.example.kuaiyijia.UI.carManage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.CarListItem;
import com.example.kuaiyijia.UI.MainActivity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.CustomDialog;


public class CarDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView car_detail_plate_number;
    private TextView car_detail_id;
    private TextView car_detail_license;
    private TextView car_detail_type;
    private TextView car_detail_car_lenth;
    private TextView car_detail_car_height;
    private TextView car_detail_car_width;
    private TextView car_detail_car_load;
    private Button car_detail_modify_bt;
    private Button car_detail_delete_bt;
    private CarListItem car ;
    private int car_id;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1054:
                    int result = msg.getData().getInt("deleteState");
                    if (result == 0){
                        Toast.makeText(CarDetailActivity.this,"删除失败！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(CarDetailActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                        Intent mIntent = new Intent(CarDetailActivity.this, MainActivity.class);
                        mIntent.putExtra("id",4);
                        startActivity(mIntent);
                    }
                    break;
            }
        }
    };
    private Button back_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_car_detail);
        initView();
        initData();
    }

    public void initView(){
        car_detail_plate_number = (TextView) findViewById(R.id.car_detail_plate_number);
        car_detail_id =  (TextView) findViewById(R.id.car_detail_id);
        car_detail_license = (TextView) findViewById(R.id.car_detail_license);
        car_detail_type =  (TextView) findViewById(R.id.car_detail_type);
        car_detail_car_lenth = (TextView) findViewById(R.id.car_detail_car_lenth);
        car_detail_car_height = (TextView) findViewById(R.id.car_detail_car_height);
        car_detail_car_width = (TextView) findViewById(R.id.car_detail_car_width);
        car_detail_car_load = (TextView) findViewById(R.id.car_detail_car_load);
        car_detail_modify_bt = (Button) findViewById(R.id.car_detail_modify_bt);
        car_detail_delete_bt = (Button) findViewById(R.id.car_detail_delete_bt);
        back_bt = (Button) findViewById(R.id.backtolast);
        car_detail_modify_bt.setOnClickListener(this);
        car_detail_delete_bt.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }
    public void initData(){
        // 获取传过来的数据  使用parcelable方式
        Intent lastIntent = getIntent();
        Bundle bundle = lastIntent.getExtras();
        car = bundle.getParcelable("detail");
        if(car != null) {
            car_detail_plate_number.setText("车牌号: "+car.getCarPlateNum());
            car_detail_id.setText("车辆识别号: "+car.getCarIdentityId());
            car_detail_license.setText("行驶证号: "+car.getCarLicenseId());
            //car_detail_type
            car_detail_car_lenth.setText(car.getLenth());
            car_detail_car_width.setText(car.getWidth());
            car_detail_car_height.setText(car.getHeight());
            car_detail_car_load.setText(car.getLoad());
            car_detail_type.setText( getResources().getTextArray(R.array.carType)[Integer.valueOf(car.getCarType())]);
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            // 去 修改车辆信息界面
            case R.id.car_detail_modify_bt:
                Intent modify_intent = new Intent(this, CarModifyActivity.class);
                Bundle bundletoModify = new Bundle();
                bundletoModify.putParcelable("modify",car);
                modify_intent.putExtras(bundletoModify);
                startActivity(modify_intent);
                finish();
                break;
            // 删除该车辆信息 跳转回管理主页面
            case R.id.car_detail_delete_bt:
                //1 弹出是否删除对话框
                CustomDialog deleteDialog = new CustomDialog(CarDetailActivity.this);
                deleteDialog.setTitle("提示");
                deleteDialog.setMessage("是否确定删除该车辆？");
                deleteDialog.setConfirm("确定", new CustomDialog.IOnConfirmListener(){
                    @Override
                    public void onConfirm(CustomDialog dialog) {
                        // 2删除车辆数据
                        deleteCar();
                    }
                });
                deleteDialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
                    @Override
                    public void onCancel(CustomDialog dialog) {
                        Toast.makeText(CarDetailActivity.this, "取消删除！",Toast.LENGTH_SHORT).show();
                    }
                });
                deleteDialog.show();
                break;
            case R.id.backtolast:
                finish();
                break;
            }
        }

    // 删除数据
    public void deleteCar(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what= 1054;
                Bundle bundle = new Bundle();
                int result = Database.deleteFromData("PUB_VEHICLE","V_ID", car.getID());
                Log.e("delete", "delete: "+result);
                bundle.putInt("deleteState",result);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        });
        thread.start();
        thread.interrupt();
    }

}