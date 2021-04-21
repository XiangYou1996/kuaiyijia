package com.example.kuaiyijia.UI.LoginRegister;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.kuaiyijia.Adapter.BanciAdapter;
import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseForMultilFragment;
import com.example.kuaiyijia.Database.DataBaseMethods;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.UserEntity;
import com.example.kuaiyijia.Entity.WebBranchListItem;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.MainActivity;
import com.example.kuaiyijia.UI.routeManage.BanciActivity;
import com.example.kuaiyijia.Utils.BaseActivity;
import com.example.kuaiyijia.Utils.CustomDialog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BindingFreightActivity extends BaseActivity implements View.OnClickListener {
    private List<WebBranchListItem> adpterLists= new ArrayList<>();
    private EditText et_freightName;
    private RecyclerView rv;
    private TextView bt_search;
    private UserEntity user;
    private SharedPreferences sp;
    private TextView bt_addhyb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        // 注册页面传过来的个人信息
        user = (UserEntity) getIntent().getExtras().getSerializable("user");
        // 弹出所有的货运部，可进行查找
        displayAllFreight();
    }

    private void displayAllFreight() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //
                DataBaseForMultilFragment database = new DataBaseForMultilFragment();
                ResultSet rs = database.SelectFromAllData("*", DataBaseConfig.WebBranchTableName);
                try {
                    while (rs.next()){
                        WebBranchListItem wb= new WebBranchListItem(rs.getString(DataBaseConfig.WebBranchID),rs.getString(DataBaseConfig.WebBranchName),
                                rs.getString(DataBaseConfig.WebBranchJC),rs.getString(DataBaseConfig.HuoYunTel),rs.getString(DataBaseConfig.WebBranchCantact),
                                rs.getString(DataBaseConfig.WebBranchCantactTel),rs.getString(DataBaseConfig.WebBranchDetailAddress),rs.getString(DataBaseConfig.WebBranchCID));
                        adpterLists.add(wb);
                    }
                    // 主线程操作界面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BindingFreightAdapter adapter = new BindingFreightAdapter(adpterLists);
                            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    bindFreight(position);
                                }
                            });
                            rv.setLayoutManager(new LinearLayoutManager(BindingFreightActivity.this,LinearLayoutManager.VERTICAL,false));
                            rv.setAdapter(adapter);
                        }
                    });
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        thread.interrupt();
    }

    private void bindFreight(int position) {
         // 确认对话框
        // 先弹出确认对话框
        CustomDialog dialog = new CustomDialog(BindingFreightActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("您确认要绑定该货运部吗？");
        dialog.setConfirm("确认", new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                // 完善 user 信息
                user.setHYB_ID(adpterLists.get(position).getWebBranchID());
                user.setC_ID(adpterLists.get(position).getCID());
                // 插入数据
                String [] names = {DataBaseConfig.UserHYBID,DataBaseConfig.UserCID};
                String [] values = {user.getHYB_ID(),user.getC_ID()};
                DataBaseMethods.updateById(DataBaseConfig.UserTableName,DataBaseConfig.UserPriID,user.getUserID(),names,values);
                showToast("绑定成功~");
                // 开始跳转到主功能页面
                int flag =Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
                navigateWithDestroyBefore(LoginActivity.class ,flag);
            }
        });
        dialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
            @Override
            public void onCancel(CustomDialog dialog) {
            }
        });
        dialog.show();
    }

    private void initView() {
        et_freightName = (EditText) findViewById(R.id.et_search);
        rv = findViewById(R.id.freight_rv);
        bt_search = findViewById(R.id.tv_search);
        bt_addhyb = findViewById(R.id.tv_add_hyb);
        bt_search.setOnClickListener(this);
        bt_addhyb.setOnClickListener(this);

    }

    @Override
    protected int initLayout() {
        return R.layout.activity_binding_freightctivity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                searchFreights();
                break;
            case R.id.tv_add_hyb:
                Bundle bundle =new Bundle();
                bundle.putSerializable("user",user);
                navigateToWithData(AddFreightActivity.class,bundle);
                break;
        }
    }

    private void searchFreights() {
        String webinfo = et_freightName.getText().toString();
        List<WebBranchListItem> list= new ArrayList<>();
        if (webinfo.isEmpty()){
            showToast("请输入货运部相关信息~");
            return;
        }
        for (WebBranchListItem wb : adpterLists){
            if (wb.getWebBranchName().contains(webinfo)||wb.getDetailAddress().contains(webinfo)||wb.getCantact().contains(webinfo)){
                list.add(wb);
            }
        }
        if (list.size() > 0) {
            BindingFreightAdapter adapter = new BindingFreightAdapter(list);
//            rv.setLayoutManager(new LinearLayoutManager(BindingFreightActivity.this,LinearLayoutManager.VERTICAL,false));
            rv.setAdapter(adapter);
        }

    }
    static class BindingFreightAdapter extends BaseQuickAdapter<WebBranchListItem, BaseViewHolder>{


        public BindingFreightAdapter( @Nullable List<WebBranchListItem> data) {
            super(R.layout.item_search, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, WebBranchListItem item) {
            helper.addOnClickListener(R.id.ll);
            helper.setText(R.id.tv_name,item.getWebBranchName());
            helper.setText(R.id.tv_user,"联系人:"+item.getCantact()+ "  电话: "+item.getCantactTel());
            helper.setText(R.id.tv_address,"地址:"+ item.getDetailAddress());
        }
    }

}