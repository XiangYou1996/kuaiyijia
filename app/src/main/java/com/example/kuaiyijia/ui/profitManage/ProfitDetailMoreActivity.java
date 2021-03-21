package com.example.kuaiyijia.ui.profitManage;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Adapter.ProfitDetailMoreAdapter;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.ProfitDetailMoreItem;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProfitDetailMoreActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView ordernum_title;
    private ListView ordermore_list;
    private Button profit_detail_more_confirm_bt;
    private List<ProfitDetailMoreItem> moreList = new ArrayList<>();
    private String order_id;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1064:
                    moreList.clear();
                    ListItems<ProfitDetailMoreItem> listItems = (ListItems<ProfitDetailMoreItem>) msg.getData().getSerializable("order_detail");
                    Log.i("TAG", "handleMessage: "+listItems.size());
                    for (int i = 0 ; i < listItems.size();i++){
                        moreList.add(listItems.get(i));
                    }
                    ProfitDetailMoreAdapter adapter = new ProfitDetailMoreAdapter(ProfitDetailMoreActivity.this,moreList);
                    ordermore_list.setAdapter(adapter);
                    ordernum_title.setText("订单号： "+order_id);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_order_detail);
        initView();
        initData();
    }
    public void initView(){
        ordernum_title = (TextView) findViewById(R.id.ordernum_title);
        ordermore_list = (ListView) findViewById(R.id.ordermore_list);
        profit_detail_more_confirm_bt = (Button) findViewById(R.id.profit_detail_more_confirm_bt);
        profit_detail_more_confirm_bt.setOnClickListener(this);

    }
    public void initData(){
        ListItems<ProfitDetailMoreItem> alist = new ListItems<>();
        order_id = getIntent().getStringExtra("order_id");
        Log.i("TAG", "initData: order_id is"+order_id);
        // 开启线程查找数据
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 装员工ID

                List<String> blist = new ArrayList<>();
                List<String> clist = new ArrayList<>();
                ResultSet rs = Database.SelectFromData("*","XS_PFD_TCB","ORDER_NUMBER",order_id);
                try {
                    while (rs.next()){
                        ProfitDetailMoreItem detailMore = new ProfitDetailMoreItem(rs.getString("ORDER_NUMBER"),
                                "",rs.getString("SR_AMOUNT"));
                        blist.add(rs.getString("EM_ID"));
                        alist.add(detailMore);
                    }
                    for (int i = 0;i<blist.size();i++){
                        ResultSet rs1 = Database.SelectFromData("*","PUB_EMETYPE","EM_ID",blist.get(i));
                        while (rs1.next()){
                            clist.add(rs1.getString("ET_ID"));
                        }
                    }
                    for (int j = 0 ;j < clist.size();j++){
                        ResultSet rs2 = Database.SelectFromData("*","PUB_TMSETYPE","ET_ID",clist.get(j));
                        while (rs2.next()){
                            alist.get(j).setStation(rs2.getString("ET_NAME"));
                        }

                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 1064;
                Bundle bundle = new Bundle();
                bundle.putSerializable("order_detail",alist);
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
            case R.id.profit_detail_more_confirm_bt:
                finish();
                break;
        }
    }
}