package com.example.kuaiyijia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuaiyijia.Entity.CarListItem;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.carManage.CarDetailActivity;


import java.util.List;

public class CarListAdapter extends BaseAdapter implements View.OnClickListener {
    private static final  String TAG = "ContentAdapter";
    private List<CarListItem> lcontentList;
    private LayoutInflater minflater;
    private Context context;
    private  CarListItem car;

    public CarListAdapter(Context context, List<CarListItem> lcontentList) {
        this.minflater = LayoutInflater.from(context);
        this.lcontentList = lcontentList;
        this.context = context;
    }

    @Override
    public int getCount() {
//        Log.i(TAG, "getCount: ");
        return lcontentList.size();
    }

    @Override
    public Object getItem(int position) {
//        Log.i(TAG, "getItem: ");
        return lcontentList.get(position);
    }

    @Override
    public long getItemId(int position) {
//        Log.i(TAG, "getItemId: ");
        return position;
    }
    public class ViewHolder{
        // 车牌号  车辆类型 货箱长宽高 载重量
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        // 查看详情  删除
        public Button detail_btn;
//        public Button delete_btn;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CarListItem carListItem = (CarListItem) getItem(position);

        if (convertView == null){
            convertView = minflater.inflate(R.layout.cars_list_item, null);
            holder = new ViewHolder();
            // 绑定对应组件的id
            holder.textView1 = (TextView) convertView.findViewById(R.id.car_manage_num);
            holder.textView2 = (TextView) convertView.findViewById(R.id.car_manage_type);
            holder.textView3 = (TextView) convertView.findViewById(R.id.car_manage_vol);
            holder.textView4 = (TextView) convertView.findViewById(R.id.car_manage_load);
            holder.detail_btn = (Button) convertView.findViewById(R.id.car_manage_detail_bt);
//            holder.delete_btn = (Button) convertView.findViewById(R.id.car_manage_delete_bt);
            convertView.setTag(holder);
            } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        设置其内容

        holder.textView1.setText("车牌号："+carListItem.getCarPlateNum());
        holder.textView2.setText("车辆类型："+
                context.getResources().getStringArray(R.array.carType)[Integer.valueOf(carListItem.getCarType())]);
        holder.textView3.setText("货箱长宽高："+ carListItem.getLenth()+"×"+carListItem.getWidth()
                +"×"+carListItem.getHeight()+"米");
        holder.textView4.setText("载重量："+carListItem.getLoad() + "Kg");
        holder.detail_btn.setTag(position);
        holder.detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到 车辆详情 并可以修改
                Intent detail_intent = new Intent(context, CarDetailActivity.class);
                if (lcontentList.size()>0){
                    car = lcontentList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("detail",car);
                    detail_intent.putExtras(bundle);
                    context.startActivity(detail_intent);
                }
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
    }


}
