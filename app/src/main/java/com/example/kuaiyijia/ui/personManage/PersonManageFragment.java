package com.example.kuaiyijia.ui.personManage;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.Adapter.PersonListAdapter;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.PersonListItem;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PersonManageFragment extends Fragment implements View.OnClickListener {
    private final static  String TAG = "PersonManageFragment";
    private Button search_bt;
    private Button station_manage_bt;
    private EditText search_edit;
    private List<PersonListItem> personListItems =new ArrayList<>();
    private ListView listView;
    private PersonListItem person ;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1040:
                    personListItems.clear();
                    ListItems<PersonListItem> listItems = (ListItems<PersonListItem>) msg.getData().getSerializable("all_person");
                    for (int i=0; i< listItems.size(); i++){
                        String ID = listItems.get(i).getPerson_ID();
                        String Name = listItems.get(i).getPerson_name();
                        String Tel = listItems.get(i).getPerson_tel();
                        String  WebBranch = listItems.get(i).getPerson_webBranch();
                        String StationID = listItems.get(i).getPerson_sation_ID();
                        String Station = listItems.get(i).getPerson_sation();
                        person = new PersonListItem(ID,Name,Tel,WebBranch,StationID,Station);
                        personListItems.add(person);
                    }
                    //  adapter
                    PersonListAdapter adapter  = new PersonListAdapter(getContext(),personListItems);
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // 1 填充 view
        View view = inflater.inflate(R.layout.fragment_personmanage,container,false);
        return view;
    }

    // 2 初始化 view 的控件和数据
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }
    public  void initView(){
        search_bt = (Button) getActivity().findViewById(R.id.pm_search_bt);
        station_manage_bt = (Button) getActivity().findViewById(R.id.pm_station_manage_bt);
        search_edit = (EditText) getActivity().findViewById(R.id.pm_search_edit);
        listView = (ListView) getActivity().findViewById(R.id.pm_person_list) ;

        search_bt.setOnClickListener(this);
        station_manage_bt.setOnClickListener(this);
    }

    public  void initData(){
        // 保存人员的信息
        ListItems<PersonListItem> alist = new ListItems<>();
        final PersonListItem[] onePerson = new PersonListItem[1];
        // 1 找人  用户/操作员表(PUB_EMPLOYEE) 2 找人对应的岗位 	用户岗位表 PUB_EMETYPE
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 1 找人
                ResultSet rs1 = Database.SelectFromDataAll("*","PUB_EMPLOYEE");
                try {
                    while (rs1.next()) {
                            //添加
                        onePerson[0] = new PersonListItem(rs1.getString("EM_ID"),rs1.getString("EM_NAME"), rs1.getString("MTEL"),rs1.getString("HYBID"),"","");
                        alist.add(onePerson[0]);
                    }
                    /// 2  找岗位
//                    List<String> web_ID = new ArrayList<>();
                    for (int i = 0; i < alist.size() ; i ++){
                        ResultSet rs2 =  Database.SelectFromData("*","PUB_EMETYPE","EM_ID",alist.get(i).getPerson_ID());
                        if (rs2.next()){
                            String station_id = rs2.getString("ET_ID");
                            alist.get(i).setPerson_sation_ID(station_id);
                            ResultSet rs3 = Database.SelectFromData("*","PUB_TMSETYPE","ET_ID",station_id);
                            if (rs3.next()){
                                alist.get(i).setPerson_sation(rs3.getString("ET_NAME"));
                            }
                        }

                    }
                    // 3 找网点名字简称
                    for (int j=0;j < alist.size(); j++){

                        ResultSet rs4 = Database.SelectFromData("*","PUB_HYB","HYBID",alist.get(j).getPerson_webBranch());
                        if (rs4.next()){
                            alist.get(j).setPerson_webBranch(rs4.getString("WDJC"));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1040;
                Bundle bundle = new Bundle();
                bundle.putSerializable("all_person",alist);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        });
        thread.start();
        thread.interrupt();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pm_search_bt:
                searchPersonOrStation();
                break;
            case R.id.pm_station_manage_bt:
                // 跳转去 岗位管理页面
                Intent mIntent = new Intent(getContext(), StationManageActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    public void searchPersonOrStation(){
        // 先定义一个新的list
       List<PersonListItem> templist = new ArrayList<>();
        templist.clear();
        // 在原基础上查找
        String value = search_edit.getText().toString();
        // 先根据名字 岗位查找
        for (int i =0 ; i < personListItems.size() ; i ++ ){
            if (personListItems.get(i).getPerson_name().contains(value)
                    || personListItems.get(i).getPerson_sation().equals(value)){
                templist.add(personListItems.get(i));
            }
        }
        // 重新设置adapter的值
        PersonListAdapter adapter = new PersonListAdapter(getContext(),templist);
        listView.setAdapter(adapter);
        if (templist.size() == 0){
            Toast.makeText(getContext(),"没有这个员工或岗位！",Toast.LENGTH_SHORT).show();
        }

    }

}