package com.RandKprogramming.everything_time_tracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class DailyTotals extends Activity{
    ArrayList<String> checkIn;
    ArrayList<String> checkOut;
    ArrayList<String> total;

    LinearLayout columnCheckIn, columnCheckOut, columnTotal;

    Calendar calendar;
    String date;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_totals);
        init();
        loadDailyTimes();
    }

    private void init() {
        calendar = Calendar.getInstance();
        date = getCurrentDate();
        tvDate = (TextView) findViewById(R.id.daily_date);
        tvDate.setText("Date: " + date.replace('%', '/'));

        columnCheckIn = (LinearLayout) findViewById(R.id.daily_total_in);
        columnCheckOut = (LinearLayout)findViewById(R.id.daily_total_out);
        columnTotal = (LinearLayout) findViewById(R.id.daily_total_total);
    }

    private String getCurrentDate() {
        calendar = Calendar.getInstance();
        String date = "";
        if (calendar.get(Calendar.MONTH) < 10) date += 0;
        date += (calendar.get(Calendar.MONTH) + 1) + "%";
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) date += 0;
        date += calendar.get(Calendar.DAY_OF_MONTH) + "%";
        date += calendar.get(Calendar.YEAR);
        return date;
    }


    private void loadDailyTimes() {

        checkIn = FileIO.readFromInternalStorage(this,  date +FileIO.CHECK_IN);
        checkOut = FileIO.readFromInternalStorage(this, date + FileIO.CHECK_OUT);
        total = FileIO.readFromInternalStorage(this, date + FileIO.DAILY_TOTAL);


        for (int i = 0; i < checkIn.size(); i++){
            addCheckInTextView(i);
        }
        for (int i = 0; i < checkOut.size(); i++) {
            addCheckOutTextView(i);
        }
        for (int i = 0; i < total.size(); i++) {
            addTotalTextView(i);
        }
    }

    private void addCheckInTextView(int position) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        textView.setLayoutParams(params);
        textView.setId(position + 100);
        textView.setText(checkIn.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.check_in_button);
        columnCheckIn.addView(textView);
    }

    private void addCheckOutTextView(int position) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        textView.setLayoutParams(params);
        textView.setId(position + 200);
        textView.setText(checkOut.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.check_out_button);
        columnCheckOut.addView(textView);
    }

    private void addTotalTextView(int position) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        textView.setLayoutParams(params);
        textView.setId(position + 300);
        textView.setText(total.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.log_total_time);
        columnTotal.addView(textView);
    }
}
