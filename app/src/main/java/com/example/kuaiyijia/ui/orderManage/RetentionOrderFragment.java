package com.example.kuaiyijia.ui.orderManage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.Adapter.OrderListAdapter;
import com.example.kuaiyijia.Database.DataBaseForMultilFragment;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.OrderItem;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/*
Author by: xy
Coding On 2021/3/16;
*/
public class RetentionOrderFragment  extends Fragment {
    private ReentrantLock lock = new ReentrantLock();
    private ListView retention_list;
    private List<OrderItem> retentionList =new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1015:
                    retentionList.clear();
                    ListItems<OrderItem> listItems =(ListItems<OrderItem>) msg.getData().getSerializable("retention_orders");
                    for (int i=0;i<listItems.size();i++){
                        retentionList.add(listItems.get(i));
                    }
                    OrderListAdapter adapter = new OrderListAdapter(getContext(),retentionList,3);
                    retention_list.setAdapter(adapter);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retentionorder,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        ListItems<OrderItem> alist= new ListItems<>();
        // 2就是被标记延迟的订单
        String sql = "SELECT * from orders where order_status = 2";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    DataBaseForMultilFragment database5 =  new DataBaseForMultilFragment();
                    ResultSet rs_retention = database5.SelectFromDataCustomSql(sql);
                    try {
                        while (rs_retention.next()){
                            OrderItem orderItem = new OrderItem(rs_retention.getString("id"),rs_retention.getString("order_number"),rs_retention.getString("huowu_status"),
                                    rs_retention.getString("send_address"),rs_retention.getString("receive_address"),rs_retention.getString("order_status"),
                                    rs_retention.getString("daishou_money"),rs_retention.getString("dianfu_money"),rs_retention.getString("receive_station"),
                                    rs_retention.getString("receive_person"),rs_retention.getString("receive_tel"),rs_retention.getString("send_person"),rs_retention.getString("send_tel"),
                                    rs_retention.getString("service_way"),rs_retention.getString("addition"),rs_retention.getString("item_nums"),rs_retention.getString("mail_fess"));
                            alist.add(orderItem);
                        }

                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                } finally {
                    lock.unlock();
                }
                Message message = new Message();
                message.what = 1015;
                Bundle bundle = new Bundle();
                bundle.putSerializable("retention_orders",alist);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }

    private void initView() {
        retention_list = getActivity().findViewById(R.id.retention_list);
    }
}
