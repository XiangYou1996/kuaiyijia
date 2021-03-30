package com.example.kuaiyijia.ui.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
Author by: xy
Coding On 2021/3/18;
*/
public class MessageNotificationSendFragment extends Fragment {
    private static final String TAG = "tuisong";
    public static final String EXTRA_MESSAGE1="com.example.tuisong.MESSAGE";
    public static final String EXTRA_MESSAGE2="com.example.tuisong.MESSAGE";
    private static final String CHANNEL_ID = "chat" ;
    //public static final String NOTIFICATION_SERVICE = "notification";

    NotificationManager manager;
    NotificationChannel channel;
    String strChannelID = "strChannelID1";
    EditText editText;
    EditText editText1;
    Button button;


    private Button submit;
    private LinearLayout linearLayout;
    private TextView mdingdan;
    private TextView mfahuo;
    private TextView mshouhuo;
    private TextView mfukuan;
    private Button mchaxun;
    private EditText mshuru1;
    private TextView mshuru2;


    private String mV_ID;
    private String mV_FA;
    private String mV_SH;
    private String mV_FU;

    private Button mchakan1;




    private android.os.Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1 :
//                    System.out.println("3333333");
                    mV_ID = msg.getData().getString("mV_ID");//接受msg传递过来的参数
                    mV_FA = msg.getData().getString("mV_FA");//接受msg传递过来的参数
                    mV_SH = msg.getData().getString("mV_SH");//接受msg传递过来的参数
                    mV_FU = msg.getData().getString("mV_FU");//接受msg传递过来的参数

                    Log.i("lgq","area: "+mV_ID);

                    mdingdan.setText(mV_ID+"(订单号)");
                    mfahuo.setText(mV_FA +"(发货单位)");
                    mshouhuo.setText(mV_SH+"(收获单位)");
                    mfukuan.setText(mV_FU+"(代收款)");

                    break;

            }
        }
    };
    private MessageNotificationSendFragment notificationManager;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_send,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        //查询
        mchaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "onClick: 12345");
                    handlerSearch(v);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        //查看详情

        mchakan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(),SearchInformation.class);

                EditText editText1 = (EditText) getActivity().findViewById(R.id.shuru_1);
                //EditText editText2 = (EditText) findViewById(R.id.shuru_2);

                String message1 = editText1.getText().toString();
                // String message2 = editText2.getText().toString();

                intent.putExtra(EXTRA_MESSAGE1, message1);
                //intent.putExtra(EXTRA_MESSAGE2, message2);
                startActivity(intent);
            }
        });


        //推送消息
        editText = getActivity().findViewById(R.id.shuru_1);
        editText1 = getActivity().findViewById(R.id.shuru_2);
        button = getActivity().findViewById(R.id.submit);
        manager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        channel = new NotificationChannel(strChannelID, "不知道这个是啥", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent intent=new Intent(getContext(),NotificationDetail.class);
                    EditText editText2 = (EditText) getActivity().findViewById(R.id.shuru_1);
                    String message2 = editText2.getText().toString();
                    intent.putExtra(EXTRA_MESSAGE2, message2);

                    PendingIntent pendingIntent=PendingIntent.getActivity(getContext(),0,intent, 0);

                    Notification.Builder builder = new Notification.Builder(getContext(), strChannelID);
                    builder.setSmallIcon(R.drawable.icon_notification_send);
                    builder.setTicker("这个Ticker是通知的标题吗？");
                    builder.setContentTitle("有新订单到货啦！" );
                    builder.setContentText("订单号:" + editText.getText().toString()+",收获单位："+editText1.getText().toString());
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    manager.notify(0x01, builder.build());
                } else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
                    builder.setSmallIcon(R.drawable.icon_notification_send);
                    builder.setContentTitle(editText.getText().toString());
                    builder.setContentText(editText1.getText().toString());
                    builder.setOngoing(true);
                    manager.notify(0x02, builder.build());
                }
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                getActivity().NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }








    private void initView() {
        mshuru1 = getActivity().findViewById(R.id.shuru_1);
        mshuru2 = getActivity().findViewById(R.id.shuru_2);


        mdingdan = getActivity().findViewById(R.id.dingdan);
        mfahuo = getActivity().findViewById(R.id.fahuo);
        mshouhuo = getActivity().findViewById(R.id.shouhuo);
        mfukuan = getActivity().findViewById(R.id.fukuan);
        mchaxun = getActivity().findViewById(R.id.chaxun);


        mchakan1 = getActivity().findViewById(R.id.chakan_1);
    }

    private void handlerSearch(View v) throws SQLException {
        String post = mshuru1.getText().toString();
        String name = mshuru2.getText().toString();
        if(TextUtils.isEmpty(post)){
            Toast.makeText(getContext(),"订单号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getContext(),"收获单位不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String tabName = "PUB_TUISONG";
//        String tabTopName = "V_ID";
//        String value = "沙坪坝";
//        String tabTopName = "V_ID";
//        String value = "沙坪坝";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = Database.SelectFromData_2("*",tabName,"V_ID","V_SH",post,name);

                try {
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    while (rs.next()){
                        Log.e(TAG, "run: budnle111");
                        bundle.putString("mV_ID",rs.getString("V_ID"));
                        bundle.putString("mV_FA",rs.getString("V_FA"));
                        bundle.putString("mV_SH",rs.getString("V_SH"));
                        bundle.putString("mV_FU",rs.getString("V_FU"));

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
