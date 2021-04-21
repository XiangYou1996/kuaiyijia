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

import com.example.kuaiyijia.Adapter.OrderListAdapter;
import com.example.kuaiyijia.Database.DataBaseForMultilFragment;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.OrderItem;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Author by: xy
Coding On 2021/3/16;
*/
public class FinisedOrderFragment  extends Fragment implements View.OnClickListener {
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
                    ListView finished_list = getActivity().findViewById(R.id.finished_list);

                    OrderListAdapter adapter = new OrderListAdapter(getContext(),orderItemsList,0);
                    if (finished_list != null) {
                        finished_list.setAdapter(adapter);
                        finished_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent mIntent = new Intent(getContext(), FinishedOrderDetailedActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("orderItem", orderItemsList.get(position));
                                mIntent.putExtras(bundle);
                                startActivity(mIntent);
                            }
                        });
                    }
                    break;
            }
        }
    };

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
        Button finished_order_button = getActivity().findViewById(R.id.finished_order_button);
        finished_order_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finished_order_button:
                Intent mIntent = new Intent(getContext(),AddOrderActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}
