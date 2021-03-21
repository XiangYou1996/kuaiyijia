package com.example.kuaiyijia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuaiyijia.Entity.Profit;
import com.example.kuaiyijia.Entity.ProfitDetailItem;
import com.example.kuaiyijia.R;

import java.util.List;

/*
Author by: xy
Coding On 2021/3/14;
*/
public class ProfitDetailAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ProfitDetailItem> adaterlist;
    public ProfitDetailAdapter(Context context, List<ProfitDetailItem> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        adaterlist = list;
    }

    @Override
    public int getCount() {
        return adaterlist.size();
    }

    @Override
    public Object getItem(int position) {
        return adaterlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public  class  ViewHolder{
        TextView orderID;
        TextView sendAddress;
        TextView receiveAddress;
        TextView profit;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProfitDetailAdapter.ViewHolder holder = null;
        ProfitDetailItem detailItem = (ProfitDetailItem) getItem(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.profit_detail_list_item, null);
            holder = new ProfitDetailAdapter.ViewHolder();

            holder.orderID = (TextView) convertView.findViewById(R.id.profit_detail_orderid);
            holder.sendAddress = (TextView) convertView.findViewById(R.id.profit_detail_sendadrs);
            holder.receiveAddress = (TextView) convertView.findViewById(R.id.profit_detail_readrs);
            holder.profit = (TextView) convertView.findViewById(R.id.profit_detail_profit);
            convertView.setTag(holder);
        }
        else {
            holder = (ProfitDetailAdapter.ViewHolder) convertView.getTag();
        }

        holder.orderID.setText(detailItem.getOrderID());
        holder.sendAddress.setText(detailItem.getSendAddress());
        holder.receiveAddress.setText(detailItem.getReceiveAddress());
        holder.profit.setText(detailItem.getProfit());
        return convertView;
    }
}
