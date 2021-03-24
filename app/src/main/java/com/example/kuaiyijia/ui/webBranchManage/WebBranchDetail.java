package com.example.kuaiyijia.ui.webBranchManage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuaiyijia.Database.Database;
import com.example.kuaiyijia.Entity.WebBranchListItem;
import com.example.kuaiyijia.ui.MainActivity;
import com.example.kuaiyijia.R;
import com.example.kuaiyijia.Tools.CustomDialog;


public class WebBranchDetail extends AppCompatActivity implements View.OnClickListener {

    private Button modify;
    private Button delete;
    private Button confirm;
    private Button back_bt;
    private TextView wb_detail_name;
    private TextView wb_detail_abbreviation;
    private TextView wb_detail_tel;
    private TextView wb_detail_person_name;
    private TextView wb_detail_person_tel;
    private TextView wb_detail_concretAddress;
    private WebBranchListItem webBranch;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1036:
                    int result = msg.getData().getInt("result");
                    if (result == 0){
                        Toast.makeText(getApplicationContext(),"删除失败！",Toast.LENGTH_SHORT);
                    }
                    else {
                        Intent mIntent = new Intent(WebBranchDetail.this, MainActivity.class);
                        mIntent.putExtra("id",3);
                        startActivity(mIntent);
                        Toast.makeText(getApplicationContext(),"删除成功！",Toast.LENGTH_SHORT);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏使用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_web_branch_detail);
        initView();
        initData();
    }

    private void initData() {
        Intent mIntent = getIntent();
        webBranch = (WebBranchListItem) mIntent.getExtras().getParcelable("webBranch");
        wb_detail_name.setText(webBranch.getWebBranchName());
        wb_detail_abbreviation.setText(webBranch.getWebBranchJC());
        wb_detail_tel.setText(webBranch.getHuoYunTel());
        wb_detail_person_name.setText(webBranch.getCantact());
        wb_detail_person_tel.setText(webBranch.getCantactTel());
        wb_detail_concretAddress.setText(webBranch.getDetailAddress());
    }

    void initView(){
        modify = findViewById(R.id.wb_detail_modify);
        delete = findViewById(R.id.wb_detail_delete);
        confirm = findViewById(R.id.wb_detail_confirm);
        wb_detail_name = (TextView) findViewById(R.id.wb_detail_name);
        wb_detail_abbreviation = (TextView) findViewById(R.id.wb_detail_abbreviation);
        wb_detail_tel = (TextView) findViewById(R.id.wb_detail_tel);
        wb_detail_person_name = (TextView) findViewById(R.id.wb_detail_person_name);
        wb_detail_person_tel = (TextView) findViewById(R.id.wb_detail_person_tel);
        wb_detail_concretAddress = (TextView) findViewById(R.id.wb_detail_concretAddress);
        back_bt = (Button) findViewById(R.id.backtolast);
        modify.setOnClickListener(this);
        delete.setOnClickListener(this);
        confirm.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wb_detail_modify:
                Intent intent_modify = new Intent(this,WebBranchModify.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("webBranch", webBranch);
                intent_modify.putExtras(bundle);
                startActivity(intent_modify);
                break;
            case R.id.wb_detail_delete:
                // 弹出对话框确认
                CustomDialog dialog = new CustomDialog(WebBranchDetail.this);
                dialog.setTitle("提示");
                dialog.setMessage("您确定要删除该网点吗？");
                dialog.setCancel("取消",new CustomDialog.IOnCancelListener() {
                    @Override
                    public void onCancel(CustomDialog dialog) {
                        Toast.makeText(getApplicationContext(),"取消删除~",Toast.LENGTH_SHORT);
                    }
                });
                dialog.setConfirm("确定", new CustomDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(CustomDialog dialog) {
                        deleteWebBranch();
                    }
                });
                dialog.show();
                break;
            case R.id.wb_detail_confirm:
            case R.id.backtolast:
                Intent mIntent = new Intent(WebBranchDetail.this, MainActivity.class);
                mIntent.putExtra("id",3);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    private void deleteWebBranch() {
        String ID_Name = "HYBID";
        String  ID_Value  = webBranch.getWebBranchID();
        // 开启新线程来进行删除
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = Database.deleteFromData("PUB_HYB",ID_Name, ID_Value);
                Message message = new Message();
                message.what = 1036;
                Bundle bundle = new Bundle();
                bundle.putInt("result",result);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
        thread.start();
        thread.interrupt();
    }
}