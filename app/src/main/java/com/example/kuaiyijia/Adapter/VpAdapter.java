package com.example.kuaiyijia.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.GetJson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VpAdapter extends RecyclerView.Adapter<VpAdapter.VpViewHolder> {
    private Context mContext;
    private String mImg_str_url;
    private ArrayList<Drawable> da;
    private String TAG = "VpAdapter";
    private final String mUrlString;
    private final String mPostStr;
    private final String mMethod;
    private Thread mT1 = null;

    public VpAdapter(Context context) {
        mContext = context;
        mImg_str_url = "https://www.kegmall.cn";
        mUrlString = "https://www.kegmall.cn/bin/iface/ikegmall.jsp";
        mPostStr = "{\"method\": \"queryj\",\"sqlid\": \"Tms_GetAdList\",\"type\": 2}";
        mMethod = "POST";
        if (da == null) {
            da = new ArrayList<>();
            mT1 = new Thread(new Runnable() {

                private int mRecordcount;

                @Override
                public void run() {
                    try {
                        String jsonRaw = new GetJson(mUrlString, mPostStr, mMethod).result;
                        ArrayList<String> url = new ArrayList<>();
                        Log.d(TAG, "onClick: Json = "+jsonRaw);
                        //把从后端接口拿到的json字符串转为Java中的JSONObject对象
                        JSONObject jo = JSON.parseObject(jsonRaw);
                        mRecordcount = jo.getInteger("recordcount");

                        //有几条数据就循环多少次
                        for (int i = 0; i < mRecordcount; i++) {
                            url.add(((JSONObject)jo.getJSONArray("data").get(i)).getString("url"));
                        }

                        //这里要加入额定数据量+2的数量，第一页放最后一张图，最后一页要放第一张图（为了自动轮播）
                        da.add(loadImageFromNetwork(mImg_str_url+url.get(mRecordcount-1)));
                        for (int i = 0; i < mRecordcount; i++) {
                            da.add(loadImageFromNetwork(mImg_str_url+url.get(i)));
                        }
                        da.add(loadImageFromNetwork(mImg_str_url+url.get(0)));

                        System.out.println(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mT1.start();
        }
    }
    @NonNull
    @Override
    public VpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VpViewHolder(LayoutInflater.from(mContext).inflate((R.layout.item_h_v), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VpViewHolder holder, int position) {
        //holder.mTextView.setText("第  " + (position + 1) + " 界面");
        //holder.mLinearLayout.setBackgroundResource(backgrounds.get(position));
        holder.mLinearLayout.setBackground(da.get(position));

    }

    @Override
    public int getItemCount() {
        try {
            mT1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (da == null) {
            return 0;
        }
        return da.size();
    }
    class VpViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        TextView mTextView;

        VpViewHolder(@NonNull View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.ll_h_v);
           mTextView = itemView.findViewById(R.id.tv_hv);
        }
    }
    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), null);
        } catch (IOException e) {
            Log.d("skythinking", e.getMessage());
        }
        if (drawable == null) {
            Log.d("skythinking", "null drawable");
        } else {
            Log.d("skythinking", "not null drawable");
        }
        return drawable;
    }

}
