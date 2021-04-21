package com.example.kuaiyijia.UI.personManage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Adapter.StationListAdapter;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.StationListItem;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StationManageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button pm_station_add_bt;
    private List<StationListItem> stationListItems = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1042:
                    stationListItems.clear();
                    ListItems<StationListItem> listItems = (ListItems<StationListItem>) msg.getData().getSerializable("all_station");
                    Log.i("TAG", "handleMessage: "+listItems.size());
                    for (int i=0 ; i < listItems.size() ; i++){
                        String id = listItems.get(i).getET_ID();
                        String name = listItems.get(i).getET_NAME();
                        String code = listItems.get(i).getET_CODE();

                        StationListItem station = new StationListItem(id,code,name);
                        stationListItems.add(station);
                    }
                    // 设置adapter
                    StationListAdapter adapter  = new StationListAdapter(StationManageActivity.this,stationListItems);
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };
    private ListView listView;
    private Button back_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_station_manage);
        initView();
        initData();
    }

    public void initView(){
        listView = (ListView) findViewById(R.id.pm_station_list);
        pm_station_add_bt = (Button) findViewById(R.id.pm_station_add_bt);
        back_bt = (Button) findViewById(R.id.backtolast);
        pm_station_add_bt.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    public void initData(){
        ListItems<StationListItem> alist = new ListItems<>();
        final StationListItem[] station = {null};

        // 开启新的线程查询数据库
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                ResultSet rs = Database.SelectFromDataAll("*","PUB_TMSETYPE");
                try {
                    while (rs.next()){
                        station[0] = new StationListItem(rs.getString("ET_ID"),rs.getString("ET_CODE"),
                                rs.getString("ET_NAME"));
                        alist.add(station[0]);
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 1042;
                Bundle bundle = new Bundle();
                bundle.putSerializable("all_station",alist);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });

        thread.start();
        thread.interrupt();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pm_station_add_bt:
                Intent mIntent = new Intent(StationManageActivity.this, StationAddActivity.class);
                startActivity(mIntent);
                break;
            case R.id.backtolast:
                finish();
                break;
        }
    }


}