/*
package com.example.kuaiyijia.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TimePicker;

import com.example.kuaiyijia.R;

import java.util.Calendar;

*/
/*
Author by: xy
Coding On 2021/4/19;
*//*

public class TimePickerDialog {
    private static  Context mContext;
    private TimePickerDialog(){

    }
    public static void getInstance(Context context){
        mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.popup_time_select_layout, null);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        builder.setView(view);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        builder.setTitle("班次时间");
        builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                StringBuilder sb = new StringBuilder();
                sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
                System.out.println(sb);
                dialog.cancel();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

}
*/
