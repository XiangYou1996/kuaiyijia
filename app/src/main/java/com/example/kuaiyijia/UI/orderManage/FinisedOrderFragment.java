package com.example.kuaiyijia.UI.orderManage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

/*
Author by: xy
Coding On 2021/3/16;
*/
public class FinisedOrderFragment  extends BaseFragment  {
    private List<OrderItem> orderItemsList = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1010:
                    orderItemsList.clear();
                    ListItems<OrderItem> listItems = (ListItems<OrderItem>) msg.getData().getSerializable("all_orders");
                    for (int i = 0;i<listItems.size();i++){
                        orderItemsList.add(listItems.get(i));
                    }
                    FinishedOrderAdapter adapter = new FinishedOrderAdapter(orderItemsList);
                    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            switch (view.getId()){
                                case R.id.order_ll:
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("orderItem", orderItemsList.get(position));
                                    navigateToWithData(FinishedOrderDetailedActivity.class,bundle);
                                    break;
                            }
                        }
                    });
                    finished_list.setAdapter(adapter);
                    break;
            }
        }
    };
    private RecyclerView finished_list;
    private Button finished_order_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_finishorder,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        ListItems<OrderItem> alist = new ListItems<>();

        //search data
        Thread thread =  new Thread(new Runnable() {
            @Override
            public void run() {
                    DataBaseForMultilFragment database1 = new DataBaseForMultilFragment();
                    ResultSet rs_finished = database1.SelectFromData("*","orders","order_status","0");
                    try {
                        while (rs_finished.next()){
                            OrderItem orderItem = new OrderItem(rs_finished.getString("id"),rs_finished.getString("order_number"),rs_finished.getString("huowu_status"),
                                    rs_finished.getString("send_address"),rs_finished.getString("receive_address"),rs_finished.getString("order_status"),
                                    rs_finished.getString("daishou_money"),rs_finished.getString("dianfu_money"),rs_finished.getString("receive_station"),
                                    rs_finished.getString("receive_person"),rs_finished.getString("receive_tel"),rs_finished.getString("send_person"),rs_finished.getString("send_tel"),
                                    rs_finished.getString("service_way"),rs_finished.getString("addition"),rs_finished.getString("item_nums"),rs_finished.getString("mail_fess"));
                            alist.add(orderItem);
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                Message message = new Message();
                message.what = 1010;
                Bundle bundle = new Bundle();
                bundle.putSerializable("all_orders",alist);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }

    private void initView() {
        finished_list = getActivity().findViewById(R.id.finished_rv);
        finished_list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        finished_order_button = getActivity().findViewById(R.id.finished_order_bt);
        finished_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.finished_order_bt:
                        Intent mIntent = new Intent(getContext(),AddOrderActivity.class);
                        startActivity(mIntent);
                        break;
            }
        }
        });
    }



    static class FinishedOrderAdapter extends BaseQuickAdapter<OrderItem, BaseViewHolder> {

        public FinishedOrderAdapter(@Nullable List<OrderItem> data) {
            super(R.layout.item_order,data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, OrderItem item) {
            helper.addOnClickListener(R.id.order_ll);
            helper.setText(R.id.tv_order_num,"No.Ps"+item.getID());
            helper.setText(R.id.tv_start_address,item.getSend_address());
            helper.setText(R.id.tv_end_address,item.getReceive_address());
            helper.setText(R.id.tv_num,item.getItem_nums());
            helper.setText(R.id.tv_money,item.getDaishou_money());
            helper.setVisible(R.id.btn_scan,false);
        }
    }

}
