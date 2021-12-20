package com.example.kuaiyijia.UI.yunJIa.filter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class filterDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("dialog 1");
        builder.setMessage("please choose yes or no?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MyInterface myInterface = (MyInterface)getActivity();
                myInterface.buttonYesClicked();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyInterface myInterface = (MyInterface)getActivity();
                myInterface.buttonNoClicked();
            }
        });


        return builder.create();
    }
}
