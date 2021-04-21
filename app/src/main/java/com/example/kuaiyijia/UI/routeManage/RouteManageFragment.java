package com.example.kuaiyijia.UI.routeManage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.kuaiyijia.Adapter.RouteListAdapter;
import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseForMultilFragment;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.LineEntity;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Utils.BaseFragment;
import com.example.kuaiyijia.Utils.CustomDialog;
import com.example.kuaiyijia.Database.DataBaseMethods;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouteManageFragment extends BaseFragment {

    private static String routeTableName = DataBaseConfig.routeTableName;
    private static String routePriID = DataBaseConfig.routePriID;
//    private RouteListAdapter adapter;
    private Button addroute;
    private ListView listView;
    private List<LineEntity> adapterList = new ArrayList<>();
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1070:
                    adapterList.clear();
                    ListItems<LineEntity> listItems =(ListItems<LineEntity>) msg.getData().getSerializable("all_lines");
                    for (int i= 0;i<listItems.size();i++){
                        adapterList.add(listItems.get(i));
                    }
                    // new adapter
                    RouteListAdapter adapter = new RouteListAdapter(getContext(),adapterList);
                    listView = (ListView) getActivity().findViewById(R.id.rv);
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            deleteRoute(adapterList.get(position).getId(),position);
                            return true;
                        }
                    });
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        // 查找线路数据
        ListItems<LineEntity> alist = new ListItems<>();
        // new thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = Database.SelectFromData("*",routeTableName,DataBaseConfig.routeCID,"1");
                try {
                    while (rs.next()){
                        LineEntity line = new LineEntity(rs.getString(DataBaseConfig.routePriID),rs.getString(DataBaseConfig.routeStartStation), rs.getString(DataBaseConfig.routeEndStation),
                                rs.getString(DataBaseConfig.routeCID), rs.getString(DataBaseConfig.routeHYBID));
                        alist.add(line);
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                Database.closeConnect();
                Message message = new Message();
                message.what = 1070;
                Bundle bundle = new Bundle();
                bundle.putSerializable("all_lines",alist);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }

    private void initView() {
        addroute = (Button) getActivity().findViewById(R.id.tv_add_route);

        addroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext()); // 创建视图容器并设置上下文
                View view = layoutInflater.inflate(R.layout.dialog_route_edit,null); // 获取list_item布局文件的视图
                new AlertDialog.Builder(getContext()).setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get start station and end station
                        EditText et_start = view.findViewById(R.id.et_start);
                        EditText et_end = view.findViewById(R.id.et_end);
                        LineEntity line = new LineEntity("",et_start.getText().toString(),et_end.getText().toString(),"","");
                        // insert a line
                        insert(line);

                    }
                }).setNegativeButton("取消", null).show();

            }
        });
    }
    // 添加一条线路
    private void insert(LineEntity line) {
        String [] names = {DataBaseConfig.routeCID,DataBaseConfig.routeHYBID,DataBaseConfig.routeStartStation,DataBaseConfig.routeEndStation};
        String [] values = {"1","1","'"+line.getStart_station()+"'","'"+line.getEnd_station()+"'"};
        DataBaseMethods.insert(routeTableName,names,values);
        // 更新主线程UI
/*        adapterList.add(line);
//        adapter.setAdapterLists(adapterList);
        RouteListAdapter adapter = new RouteListAdapter(getContext(),adapterList);
        listView.setAdapter(adapter);*/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseForMultilFragment database =new DataBaseForMultilFragment();
                ResultSet rs = database.SelectFromData("*",routeTableName,DataBaseConfig.routeCID,"1");
                adapterList.clear();
                try {
                    while (rs.next()){
                        LineEntity line = new LineEntity(rs.getString(DataBaseConfig.routePriID),rs.getString(DataBaseConfig.routeStartStation), rs.getString(DataBaseConfig.routeEndStation),
                                rs.getString(DataBaseConfig.routeCID), rs.getString(DataBaseConfig.routeHYBID));
                        adapterList.add(line);
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RouteListAdapter adapter = new RouteListAdapter(getContext(),adapterList);
                        listView.setAdapter(adapter);
                    }
                });

            }
        });
        thread.start();
        thread.interrupt();


    }
    // 删除一条线路
    private void deleteRoute(String id, int position) {
        // 先弹出确认对话框
        CustomDialog dialog = new CustomDialog(getContext());
        dialog.setTitle("提示");
        dialog.setMessage("您确认要删除吗？");
        dialog.setConfirm("确认", new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                DataBaseMethods.deleteById(getContext(),routeTableName,routePriID,id);
                // 主线程更新UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterList.remove(position);
                        RouteListAdapter adapter = new RouteListAdapter(getContext(),adapterList);
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

}