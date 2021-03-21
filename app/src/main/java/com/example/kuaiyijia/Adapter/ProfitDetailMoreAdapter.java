package com.example.kuaiyijia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuaiyijia.Entity.ProfitDetailItem;
import com.example.kuaiyijia.Entity.ProfitDetailMoreItem;
import com.example.kuaiyijia.R;

import java.util.ArrayList;
import java.util.List;

/*
Author by: xy
Coding On 2021/3/15;
*/
public class ProfitDetailMoreAdapter  extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ProfitDetailMoreItem> adapterList = new ArrayList<>();

    public ProfitDetailMoreAdapter(Context context, List<ProfitDetailMoreItem> adapterList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.adapterList = adapterList;
    }

    @Override
    public int getCount() {
        return adapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        public TextView station;
        public TextView profit;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProfitDetailMoreAdapter.ViewHolder holder = null;
        ProfitDetailMoreItem moreItem = (ProfitDetailMoreItem) getItem(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.profit_detailmore_list_item, null);
            holder = new ProfitDetailMoreAdapter.ViewHolder();
            holder.station = (TextView) convertView.findViewById(R.id.pd_station);
            holder.profit = (TextView) convertView.findViewById(R.id.pd_profit);
            convertView.setTag(holder);
        }
        else {
            holder = (ProfitDetailMoreAdapter.ViewHolder) convertView.getTag();
        }
        holder.station.setText(moreItem.getStation());
        holder.profit.setText(moreItem.getProfit());
        return convertView;
    }
}
