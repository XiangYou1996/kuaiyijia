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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.kuaiyijia.Adapter.OrderListAdapter;
import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseForMultilFragment;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.OrderItem;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.BaseFragment;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Author by: xy
Coding On 2021/3/16;
*/
public class GoingOrderFragment extends BaseFragment {
    private static final int REQUEST_CODE_SCAN = 0;
//    private Button going_order_button;
/*    private ListView going_order_list;*/
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
                    OrderAdapter adapter =new OrderAdapter(orderItemList);
                    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                            switch (view.getId()){
                                case R.id.order_ll:
                                    // 跳转到详情页面
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("orderItem",orderItemList.get(position));
                                    navigateToWithData(GoingOrderDetailActivity.class,bundle);
                                    break;
                                case R.id.btn_scan:
                                    //  开启扫码功能
                                    Intent intent = new Intent(new Intent(getContext(), CaptureActivity.class));
                                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                                    break;
                            }
                        }
                    });
                    rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    rv.setAdapter(adapter);
                    break;
                case 1012:
                    if (msg.getData().getBoolean("isOrderNum") == true){

                    }
                    // 弹框询问其是否确认收货
                    // todo

                    //  操作数据库，将该订单的状态
                    break;
            }
        }
    };
    private RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_layout, container, false);;
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
        Button going_order_button = getActivity().findViewById(R.id.going_order_bt);
        rv = getActivity().findViewById(R.id.rv);
        going_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.going_order_bt:
                        Intent mIntent = new Intent(getContext(),AddOrderActivity.class);
                        startActivity(mIntent);
                        break;
                }

            }
        });


    }

    static class OrderAdapter extends BaseQuickAdapter<OrderItem, BaseViewHolder>{

        public OrderAdapter(@Nullable List<OrderItem> data) {
            super(R.layout.item_order,data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, OrderItem item) {
            helper.addOnClickListener(R.id.order_ll);
            helper.setText(R.id.tv_order_num,item.getID());
            helper.setText(R.id.tv_start_address,item.getSend_address());
            helper.setText(R.id.tv_end_address,item.getReceive_address());
            helper.setText(R.id.tv_num,item.getItem_nums());
            helper.setText(R.id.tv_money,item.getDaishou_money());
            helper.addOnClickListener(R.id.btn_scan);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == getActivity().RESULT_OK){
            if (data != null){
                String mScanResult = data.getStringExtra(Constant.CODED_CONTENT);
                //  在数据库中匹配是否有该订单
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msgIsOrderNum = new Message();
                        ResultSet rs = Database.SelectFromData("*", DataBaseConfig.OrderTableName, DataBaseConfig.OrderNumber, mScanResult);
                        try {
                            msgIsOrderNum.what = 1012;
                            Bundle bundleIsOrderNum = new Bundle();
//                            bundleIsOrderNum.putBoolean("isOrderNum", );

                            bundleIsOrderNum.putString("OrderNum",mScanResult);
                            msgIsOrderNum.setData(bundleIsOrderNum);
                            if (!rs.isBeforeFirst()){
                                mHandler.sendMessage(msgIsOrderNum);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }
        }
    }
}
