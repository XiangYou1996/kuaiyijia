package com.example.kuaiyijia.UI.carManage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.Adapter.CarListAdapter;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.CarListItem;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.BaseFragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarManageFragment extends BaseFragment implements View.OnClickListener {
    private static String TAG = "CarsManageActivity";
    private List<CarListItem> orderLists = new ArrayList<>();
    private List<CarListItem> orderLists_search = new ArrayList<>();
    private  CarListItem listItem;
    private Button car_search_bt;
    private Button car_add_bt;
    private ListView listView;
    private int allCarNum = 0;
    private int isFlush = 1;
    private CarListItem car;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1050:
                    orderLists.clear();
                    ListItems<CarListItem> listItems = (ListItems<CarListItem>) msg.getData().getSerializable("all_car");
                    for (int i= 0; i < listItems.size(); i++){
                        String car_id = listItems.get(i).getID();
                        String carPlateNum = listItems.get(i).getCarPlateNum();
                        String CarType = listItems.get(i).getCarType();
                        String CarLoad = listItems.get(i).getLoad();
                        String CarLenth = listItems.get(i).getLenth();
                        String CarWidth = listItems.get(i).getWidth();
                        String CarHeight = listItems.get(i).getHeight();
                        String carIdentityId =listItems.get(i).getCarIdentityId();
                        String carLicenseId = listItems.get(i).getCarLicenseId();
                        listItem = new CarListItem(car_id,carPlateNum, CarType,CarLoad, CarLenth, CarWidth,
                                CarHeight, carIdentityId, carLicenseId);
                        orderLists.add(listItem);
                    }
                    listView = getActivity().findViewById(R.id.cars_list);
                    CarListAdapter carListContentAdapter = new CarListAdapter(getContext(), orderLists);
                    listView.setAdapter(carListContentAdapter);
                    break;
            }
        }
    };
    private EditText car_search_edit;
    // onCreateView()方法，该方法返回视图文件，相当于Activity中onCreate方法中setContentView一样
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carmanage,container,false);
        return view;
    }

    // onViewCreated()方法，该方法当view创建完成之后的回调方法

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        //  先清空 list 方便刷新！
        orderLists.clear();
        initData(" ");
    }
    public void initView(){
        car_search_edit = (EditText) getActivity().findViewById(R.id.car_search_edit);
        car_search_bt = (Button) getActivity().findViewById(R.id.car_search_bt);
        car_add_bt = (Button) getActivity().findViewById(R.id.car_add_bt);
        car_add_bt.setOnClickListener(this);
        car_search_bt.setOnClickListener(this);
    }

    public void initData(String carPlateNum ){
        ListItems<CarListItem> alist = new ListItems<>();
        final CarListItem[] oneCar = new CarListItem[1];
        String tabName = "PUB_VEHICLE";
        String tabTopName = "C_ID";
        //value值之后从输入框中得到
        String value = "1050";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs;
                if (allCarNum == 0){
                    rs = Database.SelectFromDataAll("*",tabName);
                }
                else {
                    rs = Database.SelectFromData("*",tabName,"V_ID",carPlateNum);
                }
                try {
                    Message msg = new Message();
                    msg.what = 1050;
                    Bundle bundle = new Bundle();
                    while (rs.next()) {
                        oneCar[0] = new CarListItem(String.valueOf(rs.getInt("V_ID")),rs.getString("V_NO"),rs.getString("VT_ID"),rs.getString("V_LOAD"),
                                rs.getString("HC_LENGHT"),rs.getString("HC_WIDTH"),rs.getString("HC_HEIGHT")
                                ,rs.getString("V_CNO"),rs.getString("V_TNO"));
                        alist.add(oneCar[0]);
                    }
                    bundle.putSerializable("all_car",alist);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        thread.interrupt();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_add_bt:
                Intent intent_car_add = new Intent(getContext(), CarsAddActivity.class);
                startActivity(intent_car_add);
                orderLists.clear();
                break;
            case R.id.car_search_bt:
                getSearchCar();
                break;
        }
    }

    public void getSearchCar() {
        // 先清空 search list
        orderLists_search.clear();
        // 获取车牌号
        String carPlateNum = car_search_edit.getText().toString();
        int position = 0;
        // 先判断是否有该车辆，然后再跳转去detail页面
        for (int i= 0;i< orderLists.size(); i++){
            if (orderLists.get(i).getCarPlateNum().contains(carPlateNum) ){
                // 找到了就加入list
                orderLists_search.add(orderLists.get(i));
            }
        }
        if (orderLists_search.size() == 0){
            // 提示无该车辆
//            Toast.makeText(getContext(),"无该车辆，请重新输入！",Toast.LENGTH_LONG).show();
            showToast("无该车辆，请重新输入！");
        }
        else {
            // 收起软键盘
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
            // 有就 重新刷新listview
            CarListAdapter carListContentAdapter = new CarListAdapter(getContext(), orderLists_search);
            listView.setAdapter(carListContentAdapter);
        }
    }
}
