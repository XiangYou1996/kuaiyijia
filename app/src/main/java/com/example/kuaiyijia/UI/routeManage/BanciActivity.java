package com.example.kuaiyijia.UI.routeManage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.example.kuaiyijia.Adapter.BanciAdapter;
import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseForMultilFragment;
import com.example.kuaiyijia.Database.DataBaseMethods;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.BanCiEntity;
import com.example.kuaiyijia.Entity.LineEntity;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.CustomDialog;
import com.example.kuaiyijia.Utils.DialogTools;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class BanciActivity extends AppCompatActivity {
    private List<BanCiEntity> adpterLists = new ArrayList<>();
    private ListView listView;
    private TimePickerView timePickerView = null;
    private Dialog dialog;
    private Context mContext;
    private LineEntity lineEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banci_layout);
        mContext = BanciActivity.this;
        initView();
        initData();
    }

    private void initData() {
        lineEntity = (LineEntity) getIntent().getExtras().getSerializable("line");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs= Database.SelectFromData("*", DataBaseConfig.BanciTableName,DataBaseConfig.BanciLineID, lineEntity.getId());
                try {
                    while (rs.next()){
                        BanCiEntity bc = new BanCiEntity(rs.getString(DataBaseConfig.BanciPriID),rs.getString(DataBaseConfig.BanciLineID),rs.getString(DataBaseConfig.BanciCID),
                                rs.getString(DataBaseConfig.BanciHYBID),rs.getString(DataBaseConfig.BanciSTIME));
                        adpterLists.add(bc);
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                Database.closeConnect();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BanciAdapter adapter = new BanciAdapter(BanciActivity.this,adpterLists);
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();
        thread.interrupt();
    }

    public void initView(){
        listView = findViewById(R.id.bancilist);
        TextView tvTip = findViewById(R.id.tv_tip);

        Button back = findViewById(R.id.backtolast);
        Button add_banci = findViewById(R.id.tv_add_time);
        add_banci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                startDate.set(1992, 0, 1);
                endDate.set(2100, 11, 31);

                timePickerView = new TimePickerBuilder(BanciActivity.this, (date, timeView) -> {
                    if (date != null) {
                        String time = getTime(date);
                        showDialog();
                        // 插入数据
                        String [] names ={DataBaseConfig.BanciLineID,DataBaseConfig.BanciCID,DataBaseConfig.BanciHYBID,DataBaseConfig.BanciSTIME};
                        String [] values = {lineEntity.getId(),lineEntity.getCID(),lineEntity.getHYBID(),"'"+time+"'"};
                        DataBaseMethods.insert(DataBaseConfig.BanciTableName,names,values);

                        dismissDialog();
                    }
                })
                        .setDate(selectedDate)
                        .setRangDate(startDate, endDate)
                        .setOutSideCancelable(true)
                        .setLayoutRes(R.layout.popup_time_select_layout, resV -> resV.findViewById(R.id.tv_confirm).setOnClickListener(v1 -> {
                            if (timePickerView != null) {
                                timePickerView.returnData();
                                timePickerView.dismiss();
                            }
                            //
                            //刷新
                            adpterLists.clear();
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DataBaseForMultilFragment data = new DataBaseForMultilFragment();
                                    ResultSet rs= data.SelectFromData("*", DataBaseConfig.BanciTableName,DataBaseConfig.BanciLineID, lineEntity.getId());
                                    try {
                                        while (rs.next()){
                                            BanCiEntity bc = new BanCiEntity(rs.getString(DataBaseConfig.BanciPriID),rs.getString(DataBaseConfig.BanciLineID),rs.getString(DataBaseConfig.BanciCID),
                                                    rs.getString(DataBaseConfig.BanciHYBID),rs.getString(DataBaseConfig.BanciSTIME));
                                            adpterLists.add(bc);
                                        }
                                    }catch (SQLException e){
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            BanciAdapter adapter = new BanciAdapter(BanciActivity.this,adpterLists);
                                            listView.setAdapter(adapter);
                                        }
                                    });
                                }
                            });
                            thread.start();
                            thread.interrupt();
                        }))
                        .setDividerType(WheelView.DividerType.WRAP)
                        .setContentTextSize(26)
                        .setTextColorCenter(0xFFFB684C)
                        .setTextColorOut(0xFFF5F5F5)
                        .setType(new boolean[]{false, false, false, true, true, false})
                        .setLabel("", "", "", "时", "分", "")
                        .setLineSpacingMultiplier(2f)
                        .setDividerColor(Color.RED)// 0xFFF9F9F9
                        .build();

                if (timePickerView != null) {
                    timePickerView.show();
                }
            }
        });
        //  返回上一页
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //  长按删除该条数据
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 删除数据
                deleteLine(adpterLists.get(position).getBID(),position);
                return true;
            }
        });
    }

    // 删除一条线路
    private void deleteLine(String id, int position) {
        // 先弹出确认对话框
        CustomDialog dialog = new CustomDialog(BanciActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("您确认要删除吗？");
        dialog.setConfirm("确认", new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                DataBaseMethods.deleteById(BanciActivity.this,DataBaseConfig.BanciTableName,DataBaseConfig.BanciPriID,id);
                // 主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adpterLists.remove(position);
                        BanciAdapter adapter = new BanciAdapter(BanciActivity.this,adpterLists);
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
        dialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
            @Override
            public void onCancel(CustomDialog dialog) {
            }
        });
        dialog.show();
    }
    public void showDialog() {
        dialog = DialogTools.createLoadingDialog(BanciActivity.this);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    @NotNull
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return format.format(date);
    }
}

