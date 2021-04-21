package com.example.kuaiyijia.UI.zhuangCheCode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.kuaiyijia.R;
import com.yzq.zxinglibrary.encode.CodeCreator;

public class getCarLoadCode extends Activity {

    private static final String TAG = "getCarLoadCode";
    private static final int REQUEST_CODE_SCAN = 0;
    private String mV_id;
    private Button mBtn_ok;
    private TextView mTv_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getcarloadcode);
        Intent intent = getIntent();
        mV_id = intent.getStringExtra("V_ID");
        Log.d(TAG, "车辆ID：" + mV_id);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //这里的mV_no是创建装车码的参数，此处使用车牌
        Bitmap bitmap = CodeCreator.createQRCode(mV_id, 400, 400, logo);
        ImageView iv = findViewById(R.id.iv_QR);
        iv.setImageBitmap(bitmap);

    }
}

