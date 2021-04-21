package com.example.kuaiyijia.UI.webBranchManage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuaiyijia.Adapter.WebBranchListAdapter;
import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.ListItems;
import com.example.kuaiyijia.Entity.WebBranchListItem;
import com.example.kuaiyijia.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class WebBranchManageFragment extends Fragment implements View.OnClickListener {
    private final  static  String TAG = "WebBranchManageFragment";
    private  EditText wb_search_edit;
    private  Button wb_search_bt;
    private  Button wb_add_button;
    private  GridView gridView;
    private  List<WebBranchListItem> webBranchListItems = new ArrayList<>();
    private  WebBranchListItem webBranch;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1030:
                    webBranchListItems.clear();
                    ListItems<WebBranchListItem> listItems = (ListItems<WebBranchListItem>) msg.getData().getSerializable("all_webbranch");
                    for (int i = 0; i < listItems.size();i ++){
                        webBranch = listItems.get(i);
                        webBranchListItems.add(webBranch);
                    }
                    gridView = (GridView) getActivity().findViewById(R.id.wb_list);
                    WebBranchListAdapter adapter = new WebBranchListAdapter(getContext(),webBranchListItems);
                    gridView.setAdapter(adapter);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_branch_manage,container, false);
        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        ListItems<WebBranchListItem> alist = new ListItems<>();
        final WebBranchListItem[] webBranch = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = Database.SelectFromDataAll("*","PUB_HYB");
                try {
                    while (rs.next()){
                        webBranch[0] = new WebBranchListItem(rs.getString("HYBID"),rs.getString("HYBNAME"),
                                rs.getString("WDJC"),rs.getString("HYBTEL"),
                                rs.getString("HYBLXR"),rs.getString("HYBLXDH"),rs.getString("HYBADDR"),rs.getString(DataBaseConfig.WebBranchCID));
                        alist.add(webBranch[0]);
                    }
                    Message message = new Message();
                    message.what = 1030;
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("all_webbranch",alist);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.interrupt();
    }

    public void initView(){
        wb_search_edit = (EditText) getActivity().findViewById(R.id.wb_search_edit);
        wb_search_bt = (Button) getActivity().findViewById(R.id.wb_search_bt);
        wb_add_button = (Button) getActivity().findViewById(R.id.wb_add_button);

        wb_search_bt.setOnClickListener(this);
        wb_add_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wb_search_bt:
                searchForHYB();
                break;
            case R.id.wb_add_button:
                Intent mIntent = new Intent(getContext(),WebBranchAddActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    private void searchForHYB() {
        List<WebBranchListItem> blist= new ArrayList<>();
        // 获取搜索的货运部简称名字
        String HYB_JC = wb_search_edit.getText().toString();
        // 直接在 webbranch中开始搜索
        for (int i= 0 ; i< webBranchListItems.size();i++){
            if (webBranchListItems.get(i).getWebBranchJC().contains(HYB_JC)){
                blist.add(webBranchListItems.get(i));
            }
        }
        if (blist.size() > 0){
            WebBranchListAdapter adapter = new WebBranchListAdapter(getContext(),blist);
            gridView.setAdapter(adapter);
            Toast.makeText(getContext(),"查询网点成功！",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(),"无此网点！",Toast.LENGTH_SHORT).show();
        }
    }

}
