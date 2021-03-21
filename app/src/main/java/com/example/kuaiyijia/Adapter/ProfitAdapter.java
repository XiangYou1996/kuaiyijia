package com.example.kuaiyijia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.Profit;
import com.example.kuaiyijia.R;

import java.util.List;
import java.util.zip.Inflater;

/*
Author by: xy
Coding On 2021/3/12;
*/
public class ProfitAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Profit> adapterList;
    private  Profit profit;
    public ProfitAdapter(Context context, List<Profit> alist) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        adapterList = alist;
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
        TextView name;
        TextView station;
        TextView profit;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProfitAdapter.ViewHolder holder = null;
        Profit profit = (Profit) getItem(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.profit_list_item, null);
            holder = new ProfitAdapter.ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.plist_item_name);
            holder.station = (TextView) convertView.findViewById(R.id.plist_item_station);
            holder.profit = (TextView) convertView.findViewById(R.id.plist_item_profit);
            convertView.setTag(holder);
        }
        else {
            holder = (ProfitAdapter.ViewHolder) convertView.getTag();
        }

        holder.name.setText(profit.getPersonName());
        holder.station.setText(profit.getStation());
        holder.profit.setText(profit.getMoney());
        return convertView;
    }
}
