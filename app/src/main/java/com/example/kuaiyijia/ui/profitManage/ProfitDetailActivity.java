package com.example.kuaiyijia.ui.profitManage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Adapter.ProfitDetailAdapter;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.Profit;
import com.example.kuaiyijia.Entity.ProfitDetailItem;
import com.example.kuaiyijia.ui.MainActivity;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ProfitDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText profit_search_edit;
    private Button profit_manage_sr_bt;
    private Button profit_detail_today_bt;
    private Button profit_detail_nmonth_bt;
    private Button profit_detail_lmonth_bt;
    private Button profit_detail_more_bt;
    private LinearLayout profit_date_button_line;
    private LinearLayout profit_date_search_line;
    private ImageButton profitbacktodate;
    private ImageButton profit_date_begin_bt;
    private ImageButton profit_date_end_bt;
    private ImageButton profit_date_sr_bt;
    private DatePickerDialog dialog_pickDate;
    private final Calendar calendar = Calendar.getInstance();
    private TextView profit_date_begin;
    private TextView profit_date_end;
    private ListView profit_list;
    private TextView profit_detail_totalprofit;
    private TextView profit_detail_person;
    private List<ProfitDetailItem> profitDetailList = new ArrayList<>();
    private int mYear = calendar.get(Calendar.YEAR);
    private int mMonth = calendar.get(Calendar.MONTH);
    private int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    private String begin_date;
    private String end_date;
    private LinearLayout profit_detail_line2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            float total_money = 0;
            ProfitDetailAdapter adapter;
            switch (msg.what){
                case 1062:
                    profitDetailList.clear();
                    total_money = 0;
                    ListItems<ProfitDetailItem> listItems = (ListItems<ProfitDetailItem>) msg.getData().getSerializable("all_detail");
                    for (int i =0 ;i < listItems.size() ; i++){
                        profitDetailList.add(listItems.get(i));
                        total_money += Float.valueOf(listItems.get(i).getProfit());
                    }
                    profit_detail_totalprofit.setText(String.valueOf(total_money));
                    adapter = new ProfitDetailAdapter(ProfitDetailActivity.this, profitDetailList);
                    profit_list.setAdapter(adapter);
                    break;
                case 1063:
                    profitDetailList.clear();
                    total_money = 0;
                    ListItems<ProfitDetailItem> listItems_2 = (ListItems<ProfitDetailItem>) msg.getData().getSerializable("today_profitdetail");
                    for (int i =0 ;i < listItems_2.size() ; i++){
                        profitDetailList.add(listItems_2.get(i));
                        total_money += Float.valueOf(listItems_2.get(i).getProfit());
                    }

                    adapter = new ProfitDetailAdapter(ProfitDetailActivity.this, profitDetailList);
                    profit_list.setAdapter(adapter);
                    break;
            }
            profit_detail_totalprofit.setText(String.valueOf(total_money)+"元");
        }
    };
    private Profit lastPageProfit;
    private Button profit_detail_confirm_bt;
    private Button back_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_profit_detail);
        initView();
        initData();

    }

    private void initData() {
        // 获取上个页面传送过来的订单ID和 员工ID ;
        lastPageProfit = getIntent().getParcelableExtra("profit");
        String EM_ID = lastPageProfit.getPersonID();
        // 设置名字
        profit_detail_person.setText("员工："+ lastPageProfit.getPersonName()+"的收益详情");
        ListItems<ProfitDetailItem> alist = new ListItems<>();
        // 开启新线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                该员工的所有订单
                ResultSet rs  = Database.SelectFromData("*","XS_PFD_TCB","EM_ID",EM_ID);
                try {
                    while (rs.next()){
                        ProfitDetailItem profitDetail = new ProfitDetailItem(rs.getString("ORDER_NUMBER"),"",
                                "",rs.getString("SR_AMOUNT"));
                        alist.add(profitDetail);
                    }
                    for (int i=0;i< alist.size();i++){
                        ResultSet rs1 = Database.SelectFromData("*","orders","order_number",alist.get(i).getOrderID());
                        while (rs1.next()){
                            alist.get(i).setSendAddress(rs1.getString("send_address"));
                            alist.get(i).setReceiveAddress(rs1.getString("receive_address"));}
                    }
                    Message message = new Message();
                    message.what = 1062;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("all_detail",alist);
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

    void initView(){
        profit_search_edit = (EditText) findViewById(R.id.profit_search_edit);
        profit_manage_sr_bt = (Button) findViewById(R.id.profit_manage_sr_bt);
        profit_detail_today_bt = (Button) findViewById(R.id.profit_detail_today_bt);
        profit_detail_nmonth_bt = (Button) findViewById(R.id.profit_detail_nmonth_bt);
        profit_detail_lmonth_bt = (Button) findViewById(R.id.profit_detail_lmonth_bt);
        profit_detail_more_bt = (Button) findViewById(R.id.profit_detail_more_bt);
        profit_date_button_line = (LinearLayout) findViewById(R.id.profit_date_button_line);
        profit_date_search_line = (LinearLayout) findViewById(R.id.profit_date_search_line);
        profit_detail_line2 = (LinearLayout) findViewById(R.id.profit_detail_line2);
        profit_list = (ListView) findViewById(R.id.profit_detail_list);
        profit_detail_totalprofit = findViewById(R.id.profit_detail_totalprofit);
        profit_detail_person = findViewById(R.id.profit_detail_person);
        back_bt = (Button) findViewById(R.id.backtolast);
        back_bt.setOnClickListener(this);
        profit_detail_more_bt.setOnClickListener(this);
        profit_detail_today_bt.setOnClickListener(this);
        profit_detail_nmonth_bt.setOnClickListener(this);
        profit_detail_lmonth_bt.setOnClickListener(this);
        profit_detail_confirm_bt = findViewById(R.id.profit_detail_confirm_bt);
        profit_detail_confirm_bt.setOnClickListener(this);
        profit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 进入订单详情信息
                Intent mIntent = new Intent(ProfitDetailActivity.this, ProfitDetailMoreActivity.class);
                mIntent.putExtra("order_id",profitDetailList.get(position).getOrderID());
                startActivity(mIntent);
            }
        });
        // more 里面的
        profitbacktodate = (ImageButton) findViewById(R.id.profitbacktodate);
        profit_date_begin_bt = (ImageButton) findViewById(R.id.profit_date_begin_bt);
        profit_date_end_bt = (ImageButton) findViewById(R.id.profit_date_end_bt);
        profit_date_sr_bt = (ImageButton) findViewById(R.id.profit_date_sr_bt);
        profit_date_begin = (TextView) findViewById(R.id.profit_date_begin);
        profit_date_end = (TextView) findViewById(R.id.profit_date_end);
        profitbacktodate.setOnClickListener(this);
        profit_date_begin_bt.setOnClickListener(this);
        profit_date_end_bt.setOnClickListener(this);
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
            case R.id.profit_detail_today_bt:
                begin_date = mYear+"-"+(mMonth+1)+"-"+mDay;
                end_date = mYear+"-"+(mMonth+1)+"-"+(mDay +1);
                findDataBetweenDate(begin_date,end_date, lastPageProfit.getPersonID());
                break;
            case R.id.profit_detail_nmonth_bt:
                begin_date = mYear+"-"+(mMonth+1)+"-"+"01";
                end_date = mYear+"-"+(mMonth+1)+"-"+getCurrentMonthDays(mYear,mMonth+1);
                findDataBetweenDate(begin_date,end_date, lastPageProfit.getPersonID());
                break;
            case R.id.profit_detail_lmonth_bt:
                begin_date = mYear+"-"+(mMonth)+"-"+"01";
                end_date = mYear+"-"+(mMonth)+"-"+getCurrentMonthDays(mYear,mMonth);
                findDataBetweenDate(begin_date,end_date, lastPageProfit.getPersonID());
                break;
            case R.id.profit_date_sr_bt:
                // 获取选择的日期并且判定其不为空
                begin_date = profit_date_begin.getText().toString();
                end_date = profit_date_end.getText().toString();
                if (begin_date.length()>0 && end_date.length()>0){
                    findDataBetweenDate(begin_date,end_date, lastPageProfit.getPersonID());
                }
                break;
            case R.id.profit_date_begin_bt:
                setDate(0);
                break;
            case R.id.profit_date_end_bt:
                setDate(1);
                break;
            case R.id.profit_detail_confirm_bt:
            case R.id.backtolast:
                Intent mIntent = new Intent(ProfitDetailActivity.this, MainActivity.class);
                mIntent.putExtra("id",5);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
    private void findDataBetweenDate(String begin_date, String end_date,String EM_ID) {

        ListItems<ProfitDetailItem> alist = new ListItems<>();

        // 查找当日的数据项
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = Database.SelectFromDataBetweenDate2("*","XS_PFD_TCB","DTIME","DTIME","EM_ID",
                        begin_date,end_date,lastPageProfit.getPersonID());
                try {
                    while (rs.next()){
                        ProfitDetailItem profit_detail = new ProfitDetailItem( rs.getString("ORDER_NUMBER"),
                                 "", "", rs.getString("SR_AMOUNT"));
                        alist.add(profit_detail);
                    }
                    // 查找发货和收货地址
                    for (int i=0;i< alist.size();i++){
                        ResultSet rs1 = Database.SelectFromData("*","orders","order_number",alist.get(i).getOrderID());
                        while (rs1.next()){
                            alist.get(i).setSendAddress(rs1.getString("send_address"));
                            alist.get(i).setReceiveAddress(rs1.getString("receive_address"));}
                    }
                    Message message = new Message();
                    message.what = 1063;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("today_profitdetail",alist);
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
        dialog_pickDate = new DatePickerDialog(ProfitDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
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