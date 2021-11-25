package com.example.kuaiyijia.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.example.kuaiyijia.Adapter.RouteListAdapter;
import com.example.kuaiyijia.Database.DataBaseConfig;
import com.example.kuaiyijia.Database.DataBaseMethods;
import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.UserEntity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.UI.FreightBinding.BindingFreightActivity;
import com.example.kuaiyijia.UI.PersoninfoModify.PersoninfoActivity;
import com.example.kuaiyijia.Utils.BaseActivity;
import com.example.kuaiyijia.Utils.CustomDialog;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.sql.ResultSet;

public class MainActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
//    private Toolbar toolbar;
//    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    //退出的广播频段
    private static final String EXITACTION = "action2exit";

    private ExitReceiver exitReceiver = new ExitReceiver();
    private UserEntity user;

    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播时，finish
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 抽屉导航栏
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
               R.id.nav_order,R.id.nav_person,R.id.nav_webBranch ,R.id.nav_carManage, R.id.nav_profit,R.id.nav_route,R.id.nav_zhuangchema,
                R.id.nav_scanzhuangche,R.id.nav_scanfache,R.id.nav_scandownload,R.id.nav_fenrun,R.id.nav_yunjia,R.id.nav_notification,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        //   显示content 配置文件的内容

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //界面创建时注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXITACTION);
        registerReceiver(exitReceiver, filter);

        // 头像点击事件
        View headview =  navigationView.getHeaderView(0);
        ImageView img = (ImageView) headview.findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                navigateToWithData(PersoninfoActivity.class,bundle);
                finish();
            }
        });

        initData();

    }

    private void initData() {
        user = (UserEntity) getIntent().getExtras().getSerializable("user");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultSet rs = Database.SelectFromData("*", DataBaseConfig.UserTableName,DataBaseConfig.UserAccount,user.getAccount());
                try {
                    while (rs.next()){
                        String hyb = rs.getString(DataBaseConfig.UserHYBID);
                        String wls =rs.getString(DataBaseConfig.UserCID);
                        if (hyb!=null){
                            user.setHYB_ID(hyb);
                            user.setC_ID(wls);
                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 弹框询问绑定货运部
                                    queryBindingFreight();
                                }
                            });
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public void queryBindingFreight(){
        // 先弹出确认对话框
        CustomDialog dialog = new CustomDialog(this);
        dialog.setTitle("重要提示");
        dialog.setMessage("您还未绑定货运部，是否前往绑定？");
        dialog.setConfirm("前往绑定", new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                Bundle bundle =new Bundle();
                bundle.putSerializable("user",user);
                navigateToWithData(BindingFreightActivity.class, bundle);
                finish();
            }
        });
        dialog.setCancel("下次再说", new CustomDialog.IOnCancelListener() {
            @Override
            public void onCancel(CustomDialog dialog) {
            }
        });
        dialog.show();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = getIntent().getIntExtra("id",0);
        //设置展示的fragment
        switch (id){
            case 1:
                // 设置重新展示的界面
                navController.navigate(R.id.nav_order);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
                break;
            case 2:
                // 设置重新展示的界面
                navController.navigate(R.id.nav_person);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
                break;
            case 3:
                navController.navigate(R.id.nav_webBranch);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
                break;
            case 4:
                // 设置重新展示的界面
                navController.navigate(R.id.nav_carManage);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
                break;
            case 5:
                // 设置重新展示的界面
                navController.navigate(R.id.nav_profit);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
                break;

            case 7:
                navController.navigate(R.id.nav_route);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
                break;
            default:
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面销毁时解除广播
        unregisterReceiver(exitReceiver);
    }

    @Override
    public void onBackPressed() {
        // 先弹出确认对话框
        CustomDialog dialog = new CustomDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("您是否要退出应用？");
        dialog.setConfirm("是的", new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                finish();
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