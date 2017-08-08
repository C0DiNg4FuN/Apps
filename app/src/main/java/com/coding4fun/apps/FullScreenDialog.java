package com.coding4fun.apps;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

/**
 * Created by coding4fun on 04-Jan-17.
 */

public class FullScreenDialog extends AppCompatActivity {

    String[] days,months,years,hours,minuts,am_pm;
    WheelPicker wheel_days,wheel_months,wheel_years,wheel_hours,wheel_minutes,wheel_ap_pm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_full_screen);

        days = getResources().getStringArray(R.array.days);
        months = getResources().getStringArray(R.array.months);
        years = getResources().getStringArray(R.array.years);
        hours = getResources().getStringArray(R.array.hours);
        minuts = getResources().getStringArray(R.array.minutes);
        am_pm = getResources().getStringArray(R.array.am_pm);

    }


    public void showFullScreenDialog(View view) {
        final View v = getLayoutInflater().inflate(R.layout.dialog_date_time_picker,null);

        wheel_days = (WheelPicker) v.findViewById(R.id.wheel_day);
        wheel_months = (WheelPicker) v.findViewById(R.id.wheel_month);
        wheel_years = (WheelPicker) v.findViewById(R.id.wheel_year);
        wheel_hours = (WheelPicker) v.findViewById(R.id.wheel_hour);
        wheel_minutes = (WheelPicker) v.findViewById(R.id.wheel_minute);
        wheel_ap_pm= (WheelPicker) v.findViewById(R.id.wheel_am_pm);

        AlertDialog.Builder b = new AlertDialog.Builder(this,R.style.dialog_picker_full_screen)
                .setTitle("Picker")
                .setCancelable(true)
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = "";
                        s += days[wheel_days.getCurrentItemPosition()]+"/";
                        s += months[wheel_months.getCurrentItemPosition()]+"/";
                        s += years[wheel_years.getCurrentItemPosition()]+"  ";
                        s += hours[wheel_hours.getCurrentItemPosition()]+":";
                        s += minuts[wheel_minutes.getCurrentItemPosition()]+" ";
                        s+= am_pm[wheel_ap_pm.getCurrentItemPosition()];
                        Toast.makeText(FullScreenDialog.this, s, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL",null);
        b.create().show();
    }
}