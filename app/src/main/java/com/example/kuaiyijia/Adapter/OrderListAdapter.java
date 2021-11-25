package com.example.kuaiyijia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuaiyijia.Entity.OrderItem;
import com.example.kuaiyijia.Entity.ProfitDetailItem;
import com.example.kuaiyijia.R;

import java.util.ArrayList;
import java.util.List;

/*
Author by: xy
Coding On 2021/3/16;
*/
public class OrderListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<OrderItem> adpterList = new ArrayList<>();
    private int adpterType;

    public OrderListAdapter(Context context, List<OrderItem> adpterList,int adpterType) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.adpterList = adpterList;
        this.adpterType = adpterType;
    }
    // 0 是完成和进行的订单页面，1 是代收款页面  2 ...
    @Override
    public int getCount() {
        return adpterList.size();
    }

    @Override
    public Object getItem(int position) {
        return adpterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        public TextView order_num;
        public TextView to_address;
        public TextView re_address;
        public TextView num_item;
        public TextView agency_money;
        public TextView lack_items;
        public Button retention_infomation;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderListAdapter.ViewHolder holder = null;
        OrderItem orderItem = (OrderItem) getItem(position);
        if (convertView == null){
            holder = new OrderListAdapter.ViewHolder();

            if (adpterType ==0){
            convertView = inflater.inflate(R.layout.order_item, null);
            holder.num_item = (TextView) convertView.findViewById(R.id.num_item); }
            if (adpterType == 1){
                convertView = inflater.inflate(R.layout.order_daishukuan_item, null);
            }
            if (adpterType == 2){
                convertView = inflater.inflate(R.layout.order_item_scanlack, null);
                holder.num_item = (TextView) convertView.findViewById(R.id.num_item);
/*                holder.lack_items = (TextView) convertView.findViewById(R.id.sl_num);*/
            }
            if (adpterType == 3){
                convertView = inflater.inflate(R.layout.order_item_retention, null);
                holder.num_item = (TextView) convertView.findViewById(R.id.num_item);
            }
            holder.order_num = (TextView) convertView.findViewById(R.id.order_num);
            holder.to_address = (TextView) convertView.findViewById(R.id.to_address);
            holder.re_address = (TextView) convertView.findViewById(R.id.re_address);
            holder.agency_money = (TextView) convertView.findViewById(R.id.agency_money);
            convertView.setTag(holder);
        }
        else {
            holder = (OrderListAdapter.ViewHolder) convertView.getTag();
        }

        holder.order_num.setText(orderItem.getOrder_number());
        holder.to_address.setText(orderItem.getSend_address());
        holder.re_address.setText(orderItem.getReceive_address());
        holder.agency_money.setText(orderItem.getDaishou_money());
        if (adpterType == 0 ||adpterType == 2 || adpterType == 3){
            holder.num_item.setText(orderItem.getItem_nums());}
        if (adpterType ==2){
            int lack_nums = Integer.valueOf(orderItem.getItem_nums()) - Integer.valueOf(orderItem.getHuowu_status());
            holder.lack_items.setText(String.valueOf(lack_nums));
        }
        return convertView;
    }
}
