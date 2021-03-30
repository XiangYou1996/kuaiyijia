package com.example.kuaiyijia.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.kuaiyijia.R;
import com.example.kuaiyijia.ui.carManage.CarManageFragment;
import com.example.kuaiyijia.ui.personManage.PersonManageFragment;
import com.example.kuaiyijia.ui.webBranchManage.WebBranchManageFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    //退出的广播频段
    private static final String EXITACTION = "action2exit";

    private ExitReceiver exitReceiver = new ExitReceiver();

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
        setContentView(R.layout.activity_main);
        // 工具栏
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 抽屉导航栏
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
               R.id.nav_order,R.id.nav_person,R.id.nav_webBranch ,R.id.nav_carManage, R.id.nav_profit,R.id.nav_zhuangchema,
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

}