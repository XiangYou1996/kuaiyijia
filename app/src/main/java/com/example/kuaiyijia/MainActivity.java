package com.example.kuaiyijia;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 抽屉导航栏
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
               R.id.nav_order,R.id.nav_person,R.id.nav_webBranch ,R.id.nav_carManage, R.id.nav_profit,R.id.nav_zhuangchema,
                R.id.nav_scanzhuangche,R.id.nav_scanfache,R.id.nav_scandownload,R.id.nav_fenrun,R.id.nav_yunjia,R.id.nav_notification)
                .setDrawerLayout(drawer)
                .build();
        //   显示content 配置文件的内容

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

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
        int id = getIntent().getIntExtra("id",0);
        //设置展示的fragment
        switch (id){
            case 2:
                // 设置重新展示的界面
                // 1 先拿一个页面管理器
                FragmentManager manager_2  =getSupportFragmentManager();
                //2 开启一个事务处理
                manager_2.beginTransaction().replace(R.id.nav_host_fragment,new PersonManageFragment()).commit();
                break;
            case 3:
                FragmentManager manager_3  =getSupportFragmentManager();
                //2 开启一个事务处理
                manager_3.beginTransaction().replace(R.id.nav_host_fragment,new WebBranchManageFragment()).commit();
                break;
            case 4:
                // 设置重新展示的界面
                // 1 先拿一个页面管理器
                FragmentManager manager_4  =getSupportFragmentManager();
                //2 开启一个事务处理
                manager_4.beginTransaction().replace(R.id.nav_host_fragment,new CarManageFragment()).commit();
                break;
        }
        super.onResume();
    }

}