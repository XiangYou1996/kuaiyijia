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
public class GoingOrderFragment extends Fragment {
//    private Button going_order_button;
    private ListView going_order_list;
    private List<OrderItem> orderItemList = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1011:
                    orderItemList.clear();
                    ListItems<OrderItem> listItems = (ListItems<OrderItem>) msg.getData().getSerializable("all_orders");
                    for (int i=0;i<listItems.size();i++){
                        orderItemList.add(listItems.get(i));
                    }
                    going_order_list = getActivity().findViewById(R.id.going_order_list);
                    if (going_order_list!=null){
                        OrderListAdapter adapter = new OrderListAdapter(getContext(),orderItemList,0);
                        going_order_list.setAdapter(adapter);
                        going_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //
                                Intent mIntent = new Intent(getContext(),GoingOrderDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("orderItem",orderItemList.get(position));
                                mIntent.putExtras(bundle);
                                startActivity(mIntent);
                            }
                        });
                    }
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ordergoing, container, false);;
        return root;
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                DataBaseForMultilFragment database2 = new DataBaseForMultilFragment();
                ResultSet rs_going = database2.SelectFromData("*", "orders", "order_status", "1");
                try {
                    while (rs_going.next()) {
                        OrderItem orderItem = new OrderItem(rs_going.getString("id"), rs_going.getString("order_number"), rs_going.getString("huowu_status"),
                                rs_going.getString("send_address"), rs_going.getString("receive_address"), rs_going.getString("order_status"),
                                rs_going.getString("daishou_money"), rs_going.getString("dianfu_money"), rs_going.getString("receive_station"),
                                rs_going.getString("receive_person"), rs_going.getString("receive_tel"), rs_going.getString("send_person"), rs_going.getString("send_tel"),
                                rs_going.getString("service_way"), rs_going.getString("addition"), rs_going.getString("item_nums"), rs_going.getString("mail_fess"));
                        alist.add(orderItem);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 1011;
                Bundle bundle = new Bundle();
                bundle.putSerializable("all_orders", alist);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }

    private void initView() {
        Button going_order_button = getActivity().findViewById(R.id.going_order_button);

        going_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getContext(),AddOrderActivity.class);
                startActivity(mIntent);
            }
        });
    }
}
