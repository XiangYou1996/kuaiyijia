package com.example.kuaiyijia.UI.orderManage;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.kuaiyijia.Adapter.OrderListAdapter;
import com.example.kuaiyijia.Database.DataBaseForMultilFragment;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.OrderItem;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.BaseFragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/*
Author by: xy
Coding On 2021/3/16;
*/
public class ScanLackFragment extends BaseFragment {
    private ReentrantLock lock = new ReentrantLock();
    private RecyclerView sl_list;
    private List<OrderItem> sl_orderList = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1014:
                    sl_orderList.clear();
                    ListItems<OrderItem> listItems =(ListItems<OrderItem>) msg.getData().getSerializable("orders");
                    for (int i=0;i<listItems.size();i++){
                        sl_orderList.add(listItems.get(i));
                    }
//                    OrderListAdapter adapter = new OrderListAdapter(getContext(),sl_orderList,2);
//                    sl_list.setAdapter(adapter);
                    ScanLackOrderAdapter adapter = new ScanLackOrderAdapter(sl_orderList);
                    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            switch(view.getId()){
                                case R.id.btn_scan:
                                    // Todo
                                    break;
                                case R.id.order_ll:
                                    // todo
                                    break;
                            }
                        }
                    });
                    sl_list.setAdapter(adapter);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanlack,container,false);
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
        // ????????????????????????????????????
        String sql = "SELECT * from orders where huowu_status < item_nums";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    DataBaseForMultilFragment database4 = new DataBaseForMultilFragment();
                    ResultSet rs = database4.SelectFromDataCustomSql(sql);
                    try {
                        while (rs.next()){
                            OrderItem orderItem = new OrderItem(rs.getString("id"),rs.getString("order_number"),rs.getString("huowu_status"),
                                    rs.getString("send_address"),rs.getString("receive_address"),rs.getString("order_status"),
                                    rs.getString("daishou_money"),rs.getString("dianfu_money"),rs.getString("receive_station"),
                                    rs.getString("receive_person"),rs.getString("receive_tel"),rs.getString("send_person"),rs.getString("send_tel"),
                                    rs.getString("service_way"),rs.getString("addition"),rs.getString("item_nums"),rs.getString("mail_fess"));
                            alist.add(orderItem);
                        }

                    }catch (SQLException e){
                        e.printStackTrace();
                    }

                }finally {
                    lock.unlock();
                }
                Message message = new Message();
                message.what = 1014;
                Bundle bundle = new Bundle();
                bundle.putSerializable("orders",alist);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }

    private void initView() {
        sl_list = getActivity().findViewById(R.id.finished_rv);
        sl_list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    static class ScanLackOrderAdapter extends BaseQuickAdapter<OrderItem, BaseViewHolder> {

        public ScanLackOrderAdapter(@Nullable List<OrderItem> data) {
            super(R.layout.order_item_scanlack,data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, OrderItem item) {
            helper.addOnClickListener(R.id.order_ll);
            helper.setText(R.id.tv_order_num,item.getID());
            helper.setText(R.id.tv_start_address,item.getSend_address());
            helper.setText(R.id.tv_end_address,item.getReceive_address());
            helper.setText(R.id.tv_num,"No.Ps"+item.getItem_nums());
            helper.setText(R.id.tv_money,item.getDaishou_money());
            helper.addOnClickListener(R.id.btn_scan);
            int num = Integer.valueOf(item.getItem_nums()) - Integer.valueOf(item.getHuowu_status());
            helper.setText(R.id.tv_lack_num,String.valueOf(num));
        }
    }

}
