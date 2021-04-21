package com.example.kuaiyijia.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuaiyijia.Entity.BanCiEntity;
import com.example.kuaiyijia.R;

import java.util.ArrayList;
import java.util.List;

/*
Author by: xy
Coding On 2021/4/18;
*/
public class BanciAdapter  extends BaseAdapter {
    private List<BanCiEntity> adpaterList =new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public BanciAdapter(Context context,List<BanCiEntity> adpaterList){
        this.context = context;
        this.adpaterList = adpaterList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setAdpaterList(List<BanCiEntity> adpaterList) {
        this.adpaterList = adpaterList;
    }

    public class ViewHolder{
        public TextView tv_time;
    }
    @Override
    public int getCount() {
        return adpaterList.size();
    }

    @Override
    public Object getItem(int position) {
        return adpaterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BanCiEntity bc = (BanCiEntity) getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_time_table,null);
            viewHolder= new ViewHolder();
            viewHolder.tv_time  = convertView.findViewById(R.id.bc_tv_time);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (bc.getStart_Time()!= null){
            viewHolder.tv_time.setText(bc.getStart_Time());
        }
        else {
            viewHolder.tv_time.setText("00:00");
        }
        return convertView;
    }
}
