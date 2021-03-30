package com.example.kuaiyijia.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;


public class NotificationDetail extends AppCompatActivity {


    private static final String TAG = "tuisong";

    private EditText mshuru1;
    private EditText mshuru2;

    private TextView mshouhuo,mdianhua,mlianxi,mjianshu,myunfei,mdianfu,mdaishoukuan,mfahuo1,mdianhua2,mfuwu,mbeizhu;
    private Button mqueding;

    private String mV_FA,mV_SH,mV_FU,mV_NUMSHOU,mV_NAME,mV_JIANSHU,mV_YUNFEI,mV_DIANFU,mV_NUMFA,mV_FUWU,mV_BEIZHU,mV_ID;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1 :
//                    System.out.println("3333333");
                    mV_ID = msg.getData().getString("mV_ID");//接受msg传递过来的参数

                    mV_FA = msg.getData().getString("mV_FA");//接受msg传递过来的参数
                    mV_SH = msg.getData().getString("mV_SH");//接受msg传递过来的参数
                    mV_FU = msg.getData().getString("mV_FU");//接受msg传递过来的参数
                    mV_NUMSHOU = msg.getData().getString("mV_NUMSHOU");//接受msg传递过来的参数
                    mV_NAME = msg.getData().getString("mV_NAME");//接受msg传递过来的参数
                    mV_JIANSHU = msg.getData().getString("mV_JIANSHU");//接受msg传递过来的参数
                    mV_YUNFEI = msg.getData().getString("mV_YUNFEI");//接受msg传递过来的参数
                    mV_DIANFU = msg.getData().getString("mV_DIANFU");//接受msg传递过来的参数
                    mV_NUMFA = msg.getData().getString("mV_NUMFA");//接受msg传递过来的参数
                    mV_FUWU = msg.getData().getString("mV_FUWU");//接受msg传递过来的参数
                    mV_BEIZHU = msg.getData().getString("mV_BEIZHU");//接受msg传递过来的参数

                    // Log.i("lgq","area: "+mV_ID);

                    mshouhuo.setText("收货单位："+mV_SH);
                    //  mshuru1.setTextColor(Integer.parseInt("#FF0000"));
                    mdingdanhao.setText("订单号："+mV_ID);
                    mdianhua.setText("电话："+mV_NUMSHOU);
                    mlianxi.setText("联系人："+mV_NAME);
                    mjianshu.setText("件数："+mV_JIANSHU+"件");
                    myunfei.setText("运费："+mV_YUNFEI+"元");
                    mdianfu.setText("垫付："+mV_DIANFU+"元");
                    mdaishoukuan.setText("代收款："+mV_FU+"元");
                    mfahuo1.setText("发货单位："+mV_FA);
                    mdianhua2.setText("电话："+mV_NUMFA);
                    mfuwu.setText("服务："+mV_FUWU);
                    mbeizhu.setText("备注："+mV_BEIZHU);

                    break;

            }
        }
    };
    private TextView mdingdanhao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchinformation_layout);

        Intent intent = getIntent();
//        String message_1 = intent.getStringExtra(tuisong.EXTRA_MESSAGE1);
        String message_2 = intent.getStringExtra(MessageNotificationSendFragment.EXTRA_MESSAGE2);

        TextView textView_2 = findViewById(R.id.dingdanhao);
        //TextView textView_2 = findViewById(R.id.shouhuo1);
        textView_2.setText(message_2);
        //textView_2.setText("收获单位："+message_2);

        initView();
        start();


        mqueding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NotificationDetail.this, MessageNotificationSendFragment.class);
                startActivity(intent);
            }
        });

    }



    private void initView() {
        mshuru1 = this.findViewById(R.id.shuru_1);
        mshuru2 = this.findViewById(R.id.shuru_2);

        mdingdanhao = this.findViewById(R.id.dingdanhao);
        mshouhuo = this.findViewById(R.id.shouhuo1);
        mdianhua = this.findViewById(R.id.dianhua);
        mlianxi = this.findViewById(R.id.lianxi);
        mjianshu = this.findViewById(R.id.jianshu);
        myunfei = this.findViewById(R.id.yunfei);
        mdianfu = this.findViewById(R.id.dianfu);
        mdaishoukuan = this.findViewById(R.id.daishoukuan);
        mfahuo1 = this.findViewById(R.id.fahuo1);
        mdianhua2 = this.findViewById(R.id.dianhua2);
        mfuwu = this.findViewById(R.id.fuwu);
        mbeizhu = this.findViewById(R.id.beizhu);

        mqueding = this.findViewById(R.id.queding);
    }


    private void start() {
        String post = mdingdanhao.getText().toString();
        //String name = mshouhuo.getText().toString();

        String tabName = "PUB_TUISONG";
//        Connection con = null;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
             //读取数据：生成结果集ResultSet
                ResultSet rs = Database.SelectFromData("*",tabName,"V_ID",post);
                Log.d(TAG, "RLKH is ");
                try {
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    while (rs.next()){
                        Log.e(TAG, "run: budnle876");
                        // bundle.putString("mV_ID",rs.getString("V_ID"));

                        bundle.putString("mV_ID",rs.getString("V_ID"));
                        bundle.putString("mV_FA",rs.getString("V_FA"));
                        bundle.putString("mV_SH",rs.getString("V_SH"));
                        bundle.putString("mV_FU",rs.getString("V_FU"));
                        bundle.putString("mV_NUMSHOU",rs.getString("V_NUMSHOU"));
                        bundle.putString("mV_NAME",rs.getString("V_NAME"));
                        bundle.putString("mV_JIANSHU",rs.getString("V_JIANSHU"));
                        bundle.putString("mV_YUNFEI",rs.getString("V_YUNFEI"));
                        bundle.putString("mV_DIANFU",rs.getString("V_DIANFU"));
                        bundle.putString("mV_NUMFA",rs.getString("V_NUMFA"));
                        bundle.putString("mV_FUWU",rs.getString("V_FUWU"));
                        bundle.putString("mV_BEIZHU",rs.getString("V_BEIZHU"));

                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Database.closeConnect();
    }




}
