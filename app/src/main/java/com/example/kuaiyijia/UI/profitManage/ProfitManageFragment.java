package com.example.kuaiyijia.UI.profitManage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.Adapter.ProfitAdapter;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.Profit;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ProfitManageFragment extends Fragment implements View.OnClickListener {

    private EditText profit_search_edit;
    private TextView profit_manage_sr_bt;
    private Button profit_detail_today_bt;
    private Button profit_detail_nmonth_bt;
    private Button profit_detail_lmonth_bt;
    private Button profit_detail_more_bt;
    private LinearLayout profit_date_button_line;
    private LinearLayout profit_date_search_line;
    private ImageButton profitbacktodate;
    private ImageButton profit_date_sr_bt;
    private DatePickerDialog dialog_pickDate;
    private final Calendar calendar = Calendar.getInstance();
    private TextView profit_date_begin;
    private TextView profit_date_end;
    private ListView profit_list;
    private List<Profit> profitList = new ArrayList<>();
    private ProfitAdapter profitAdapter;
    private int mYear = calendar.get(Calendar.YEAR);
    private int mMonth = calendar.get(Calendar.MONTH);
    private int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    private String begin_date;
    private String end_date;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1060:
                    profitList.clear();
                    Profit profit;
                    ListItems<Profit> listItems = (ListItems<Profit>) msg.getData().getSerializable("all_profit");
                    for (int i =0 ;i < listItems.size() ; i++){
                        profit = listItems.get(i);
                        profitList.add(profit);
                    }
                    profit_list = (ListView) getActivity().findViewById(R.id.profit_list);
                    profit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent mIntent = new Intent(getContext(),ProfitDetailActivity.class);
                            // 传送对应员工的信息
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("profit",profitList.get(position));
                            mIntent.putExtras(bundle);
                            startActivity(mIntent);
                        }
                    });
                    ProfitAdapter adapter = new ProfitAdapter(getContext(),profitList);
                    profit_list.setAdapter(adapter);
                    break;
                case 1061:
                    profitList.clear();
                    Profit today_profit;
                    ListItems<Profit> today_listItems = (ListItems<Profit>) msg.getData().getSerializable("today_profit");
                    for (int i =0 ;i < today_listItems.size() ; i++){
                        today_profit = today_listItems.get(i);
                        profitList.add(today_profit);
                    }
                    profit_list = (ListView) getActivity().findViewById(R.id.profit_list);
                    profit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent mIntent = new Intent(getContext(),ProfitDetailActivity.class);
                            // 传送对应员工的信息
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("profit",profitList.get(position));
                            mIntent.putExtras(bundle);
                            startActivity(mIntent);
                        }
                    });
                    ProfitAdapter today_adapter = new ProfitAdapter(getContext(),profitList);
                    profit_list.setAdapter(today_adapter);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.activity_profit_manage,container ,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        ListItems<Profit> alist = new ListItems<>();

        // 开启线程去读取数据库
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = Database.SelectFromDataAll("*","XS_PFD_TCB");
                try {
                    while (rs.next()) {
                        Profit profit = new Profit(rs.getString("IDKEY"), rs.getString("ORDER_NUMBER"),
                                rs.getString("EM_ID"), "", "", rs.getString("SR_AMOUNT"));
                        alist.add(profit);
                        }
                    // 查找员工名字
                    int i = 0;
                    for ( i=0;i <alist.size();i++) {
                        ResultSet rs1 = Database.SelectFromData("*", "PUB_EMPLOYEE", "EM_ID", alist.get(i).getPersonID());
                        while (rs1.next()) {
                            alist.get(i).setPersonName(rs1.getString("EM_NAME"));
                        }
                    }
                    // 查找员工岗位
                    for ( i=0;i <alist.size();i++) {
                        ResultSet rs2 = Database.SelectFromData("*", "PUB_EMETYPE", "EM_ID", alist.get(i).getPersonID());
                        if (rs2.next()) {
                            String ET_ID = rs2.getString("ET_ID");
                            ResultSet rs3 = Database.SelectFromData("*", "PUB_TMSETYPE", "ET_ID", ET_ID);
                            if (rs3.next()) {
                                alist.get(i).setStation(rs3.getString("ET_NAME"));
                            }

                        }
                    }
                    Message message = new Message();
                    message.what = 1060;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("all_profit",alist);
                    message.setData(bundle);
                    mHandler.sendMessage(message);

                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.interrupt();
    }

    public  void initView(){
        profit_search_edit = (EditText) getActivity().findViewById(R.id.profit_search_edit);
        profit_manage_sr_bt = (TextView) getActivity().findViewById(R.id.profit_manage_sr_bt);
        profit_detail_today_bt = (Button) getActivity().findViewById(R.id.profit_detail_today_bt);
        profit_detail_nmonth_bt = (Button) getActivity().findViewById(R.id.profit_detail_nmonth_bt);
        profit_detail_lmonth_bt = (Button) getActivity().findViewById(R.id.profit_detail_lmonth_bt);
        profit_detail_more_bt = (Button) getActivity().findViewById(R.id.profit_detail_more_bt);
        profit_date_button_line = (LinearLayout) getActivity().findViewById(R.id.profit_date_button_line);
        profit_date_search_line = (LinearLayout) getActivity().findViewById(R.id.profit_date_search_line);

        profit_manage_sr_bt.setOnClickListener(this);

        profit_detail_more_bt.setOnClickListener(this);
        profit_detail_today_bt.setOnClickListener(this);
        profit_detail_nmonth_bt.setOnClickListener(this);
        profit_detail_lmonth_bt.setOnClickListener(this);
        // more 里面的
        profitbacktodate = (ImageButton) getActivity().findViewById(R.id.profitbacktodate);
/*        profit_date_begin_bt = (ImageButton) getActivity().findViewById(R.id.profit_date_begin_bt);
        profit_date_end_bt = (ImageButton) getActivity().findViewById(R.id.profit_date_end_bt);*/
        profit_date_sr_bt = (ImageButton) getActivity().findViewById(R.id.profit_date_sr_bt);
        profit_date_begin = (TextView) getActivity().findViewById(R.id.profit_date_begin);
        profit_date_end = (TextView) getActivity().findViewById(R.id.profit_date_end);
        profitbacktodate.setOnClickListener(this);
/*        profit_date_begin_bt.setOnClickListener(this);
        profit_date_end_bt.setOnClickListener(this);*/
        profit_date_begin.setOnClickListener(this);
        profit_date_end.setOnClickListener(this);
        profit_date_sr_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.profit_detail_more_bt:
                profit_date_button_line.setVisibility(View.GONE);
                profit_date_search_line.setVisibility(View.VISIBLE);
                break;
            case R.id.profitbacktodate:
                profit_date_button_line.setVisibility(View.VISIBLE);
                profit_date_search_line.setVisibility(View.GONE);
                break;
            case R.id.profit_date_begin:
                setDate(0);
                break;
            case R.id.profit_date_end:
                setDate(1);
                break;
            case R.id.profit_detail_today_bt:
                begin_date = mYear+"-"+(mMonth+1)+"-"+mDay;
                end_date = mYear+"-"+(mMonth+1)+"-"+(mDay +1);
                findDataBetweenDate(begin_date,end_date);
                break;
            case R.id.profit_detail_nmonth_bt:
                begin_date = mYear+"-"+(mMonth+1)+"-"+"01";
                end_date = mYear+"-"+(mMonth+1)+"-"+getCurrentMonthDays(mYear,mMonth+1);
                findDataBetweenDate(begin_date,end_date);
                break;
            case R.id.profit_detail_lmonth_bt:
                begin_date = mYear+"-"+(mMonth)+"-"+"01";
                end_date = mYear+"-"+(mMonth)+"-"+getCurrentMonthDays(mYear,mMonth);
                findDataBetweenDate(begin_date,end_date);
                break;
            case R.id.profit_date_sr_bt:
                // 获取选择的日期并且判定其不为空
                begin_date = profit_date_begin.getText().toString();
                end_date = profit_date_end.getText().toString();
                if (begin_date.length()>0 && end_date.length()>0){
                    findDataBetweenDate(begin_date,end_date);
                }
                break;
            case R.id.profit_manage_sr_bt:
                searchPerson();
            default:
                break;
        }
    }

    private void searchPerson() {
        List<Profit> alist =new ArrayList<>();
        String name = profit_search_edit.getText().toString();
        for ( int i=0;i<profitList.size();i++){
            if (profitList.get(i).getPersonName().contains(name)){
                alist.add(profitList.get(i));
            }
        }
        if (alist.size()>0){
            Toast.makeText(getContext(),"查找成功！",Toast.LENGTH_SHORT).show();
            ProfitAdapter adapter = new ProfitAdapter(getContext(),alist);
            profit_list.setAdapter(adapter);
        }else {
            Toast.makeText(getContext(),"没有该员工！",Toast.LENGTH_SHORT).show();
        }
    }

    private void findDataBetweenDate(String begin_date, String end_date) {

        ListItems<Profit> alist = new ListItems<>();

        // 查找当日的数据项
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = Database.SelectFromDataBetweenDate("*","XS_PFD_TCB","DTIME","DTIME",begin_date,end_date);
                try {
                    while (rs.next()){
                        Profit profit = new Profit(rs.getString("IDKEY"), rs.getString("ORDER_NUMBER"),
                                rs.getString("EM_ID"), "", "", rs.getString("SR_AMOUNT"));
                        alist.add(profit);
                    }
                    // 查找员工名字
                    int i = 0;
                    for ( i=0;i <alist.size();i++) {
                        ResultSet rs1 = Database.SelectFromData("*", "PUB_EMPLOYEE", "EM_ID", alist.get(i).getPersonID());
                        while (rs1.next()) {
                            alist.get(i).setPersonName(rs1.getString("EM_NAME"));
                        }
                    }
                    // 查找员工岗位
                    for ( i=0;i <alist.size();i++) {
                        ResultSet rs2 = Database.SelectFromData("*", "PUB_EMETYPE", "EM_ID", alist.get(i).getPersonID());
                        if (rs2.next()) {
                            String ET_ID = rs2.getString("ET_ID");
                            ResultSet rs3 = Database.SelectFromData("*", "PUB_TMSETYPE", "ET_ID", ET_ID);
                            if (rs3.next()) {
                                alist.get(i).setStation(rs3.getString("ET_NAME"));
                            }

                        }
                    }
                    Message message = new Message();
                    message.what = 1061;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("today_profit",alist);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.interrupt();
    }

    private void setDate(int i) {
        // i = 0 则为开始时间  1 则为结束时间
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        dialog_pickDate = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (i == 0){
                    profit_date_begin.setText(year+"-"+(month +1)+"-"+dayOfMonth);
                }
                else {
                    profit_date_end.setText(year+"-"+(month +1)+"-"+dayOfMonth);
                }

            }
        },mYear,mMonth,mDay);
        dialog_pickDate.show();
    }
    public String getCurrentMonthDays(int year, int month){
        String days = "31";
        switch (month){
            case 2:        // 判定闰年
                if ((year % 4 ==0 && year%100!=0)|| year % 400==0){
                    days ="29";
                }
                else {
                    days ="28";
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = "30";
                break;
            default:
                days = "31";
        }
        return days;
    }
}