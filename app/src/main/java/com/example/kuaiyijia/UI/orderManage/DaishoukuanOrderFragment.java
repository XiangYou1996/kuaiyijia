package com.example.kuaiyijia.UI.orderManage;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
public class DaishoukuanOrderFragment extends BaseFragment implements View.OnClickListener {
    private ReentrantLock lock = new ReentrantLock();
    private EditText am_search_et;
    private Button am_search_bt;
    private Button am_payed_bt;
    private Button am_unpayed_bt;
    private ListView am_list;
    private TextView am_totalmoney;
    private List<OrderItem> daishouList = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1013:
                    daishouList.clear();
                    ListItems<OrderItem> listItems = (ListItems<OrderItem>) msg.getData().getSerializable("orders");
                    for (int i =0; i<listItems.size();i++){
                        daishouList.add(listItems.get(i));
                    }
                    OrderListAdapter adapter = new OrderListAdapter(getContext(),daishouList,1);
                    am_list.setAdapter(adapter);
                    am_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("orderItem", daishouList.get(position));
                            navigateToWithData(GoingOrderDetailActivity.class,bundle);
                            return true;
                        }
                    });
                    setTotalMoney();
                    break;
            }
        }
    };

    private void setTotalMoney() {
        float total_money = 0;
        for ( int i =0 ;i< daishouList.size();i++){
            total_money+= Float.valueOf(daishouList.get(i).getDaishou_money());
        }
        am_totalmoney.setText("代收款金额共计："+ String.valueOf(total_money)+"元");
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashoukuanorder, container, false);;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData(0);
    }

    private void initData(int payed) {
        ListItems<OrderItem> alist = new ListItems<>();
        String sql;
        if (payed ==0){
             sql = "SELECT * from orders where daishou_money > 0 and order_status = 0";
        }
        else {
             sql = "SELECT * from orders where daishou_money > 0 and order_status = 1";
        }
        // 开启新线程
        Thread thread =  new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    DataBaseForMultilFragment database3= new DataBaseForMultilFragment();
                    ResultSet rs_daishoukuan = database3.SelectFromDataCustomSql(sql);
                    try {
                        while (rs_daishoukuan.next()){
                            OrderItem orderItem = new OrderItem(rs_daishoukuan.getString("id"),rs_daishoukuan.getString("order_number"),rs_daishoukuan.getString("huowu_status"),
                                    rs_daishoukuan.getString("send_address"),rs_daishoukuan.getString("receive_address"),rs_daishoukuan.getString("order_status"),
                                    rs_daishoukuan.getString("daishou_money"),rs_daishoukuan.getString("dianfu_money"),rs_daishoukuan.getString("receive_station"),
                                    rs_daishoukuan.getString("receive_person"),rs_daishoukuan.getString("receive_tel"),rs_daishoukuan.getString("send_person"),rs_daishoukuan.getString("send_tel"),
                                    rs_daishoukuan.getString("service_way"),rs_daishoukuan.getString("addition"),rs_daishoukuan.getString("item_nums"),rs_daishoukuan.getString("mail_fess"));
                            alist.add(orderItem);

                        }
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                } finally {
                    lock.unlock();
                }
                Message message = new Message();
                message.what = 1013;
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
        am_search_et = getActivity().findViewById(R.id.am_search_et);
        am_search_bt = getActivity().findViewById(R.id.am_search_bt);
        am_payed_bt = getActivity().findViewById(R.id.am_payed_bt);
        am_unpayed_bt = getActivity().findViewById(R.id.am_unpayed_bt);
        am_list = getActivity().findViewById(R.id.am_list);
        am_totalmoney = getActivity().findViewById(R.id.am_totalmoney);

        am_search_bt.setOnClickListener(this);
        am_payed_bt.setOnClickListener(this);
        am_unpayed_bt.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.am_search_bt:
                searchByAddress();
                break;
            case R.id.am_payed_bt:
                am_payed_bt.setBackgroundColor(Color.parseColor("#CC3366"));
                am_unpayed_bt.setBackgroundColor(Color.parseColor("#666666"));
                initData(0);
                setTotalMoney();
                break;
            case R.id.am_unpayed_bt:
                am_payed_bt.setBackgroundColor(Color.parseColor("#666666"));
                am_unpayed_bt.setBackgroundColor(Color.parseColor("#CC3366"));
                initData(1);
                setTotalMoney();
                break;
        }
    }

    private void searchByAddress() {
        List<OrderItem> alist = new ArrayList<>();
        String address = am_search_et.getText().toString();
        for (int i=0;i<daishouList.size();i++){
            if (daishouList.get(i).getSend_address().contains(address) || daishouList.get(i).getReceive_address().contains(address)){
                alist.add(daishouList.get(i));
            }
        }
        if (alist.size()>0){
            //
            OrderListAdapter adapter = new OrderListAdapter(getContext(),alist,1);
            am_list.setAdapter(adapter);
        }
        else {
            Toast.makeText(getContext(),"没有相关订单~",Toast.LENGTH_SHORT).show();
        }
    }
}
