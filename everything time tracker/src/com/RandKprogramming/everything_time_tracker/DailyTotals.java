package com.RandKprogramming.everything_time_tracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class DailyTotals extends Activity implements OnClickListener {
    ArrayList<String> checkIn;
    ArrayList<String> checkOut;
    ArrayList<String> total;

    LinearLayout columnCheckIn, columnCheckOut, columnTotal;
    Button bBack, bNext;

    Calendar calendar;
    String date;
    int timeCount = 0, day, month, year;
    TextView tvDate, tvTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_totals);
        init();
        loadDailyTimes();
    }

    private void init() {
        calendar = Calendar.getInstance();

        bBack = (Button) findViewById(R.id.daily_back);
        bBack.setOnClickListener(this);
        bNext = (Button) findViewById(R.id.daily_next);
        bNext.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.daily_date);
        tvTotalTime = (TextView) findViewById(R.id.daily_total);
        date = getCurrentDate();
        tvDate.setText("Date: " + date.replace('%', '/'));

        columnCheckIn = (LinearLayout) findViewById(R.id.daily_total_in);
        columnCheckOut = (LinearLayout) findViewById(R.id.daily_total_out);
        columnTotal = (LinearLayout) findViewById(R.id.daily_total_total);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.daily_next:
                date = nextDay();
                tvDate.setText("Date: " + date.replace('%', '/'));
                loadDailyTimes();
                break;
            case R.id.daily_back:
                date = previousDay();
                tvDate.setText("Date: " + date.replace('%', '/'));
                loadDailyTimes();
                break;
        }
    }

    //---------------------
    // TextView Methods
    //---------------------

    private void addCheckInTextView(int position) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        textView.setLayoutParams(params);
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
        textView.setText(total.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.log_total_time);
        columnTotal.addView(textView);
    }

    //------------------------
    // Time/Date Methods
    //------------------------

    private String getCurrentDate() {
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        String date = "";
        if (month < 10) date += 0;
        date += month + "%";
        if (day < 10) date += 0;
        date += day + "%";
        date += year;
        return date;
    }

    private String nextDay() {
        String date = "";

        System.out.println(month + "/" + day + "/" + year);
        day++;
        if (day > getLastDayOfMonth(month - 1)) {
            month++;
            day = 1;
        }
        if (month > 12) {
            year++;
            month = 1;
        }

        if (month < 10) date += "0";
        date += month + "%";
        if (day < 10) date += "0";
        date += day + "%";
        date += year;
        return date;
    }

    private String previousDay() {
        String date = "";

        day--;
        if (day < 1) {
            month--;
            if (month < 1) {
                month = 12;
                year--;
            }
            day = getLastDayOfMonth(month - 1);
        }

        if (month < 10) date += "0";
        date += month + "%";
        if (day < 10) date += "0";
        date += day + "%";
        date += year;
        return date;
    }

    private int getLastDayOfMonth(int month) {
        System.out.println("MONTH!!!" + month);
        int day = 0;
        switch (month) {
            case Calendar.JANUARY:
                day = 31;
                break;
            case Calendar.FEBRUARY:
                day = 28;
                break;
            case Calendar.MARCH:
                day = 31;
                break;
            case Calendar.APRIL:
                day = 30;
                break;
            case Calendar.MAY:
                day = 31;
                break;
            case Calendar.JUNE:
                day = 30;
                break;
            case Calendar.JULY:
                day = 31;
                break;
            case Calendar.AUGUST:
                day = 31;
                break;
            case Calendar.SEPTEMBER:
                day = 30;
                break;
            case Calendar.OCTOBER:
                day = 31;
                break;
            case Calendar.NOVEMBER:
                day = 30;
                break;
            case Calendar.DECEMBER:
                day = 31;
                break;
        }
        return day;
    }

    private void loadDailyTimes() {
        // Reset Column for dynamic TextViews
        columnCheckIn.removeAllViews();
        columnCheckOut.removeAllViews();
        columnTotal.removeAllViews();

        // Reset ArrayList
        checkIn = FileIO.readListFromInternalStorage(this, date + FileIO.CHECK_IN);
        checkOut = FileIO.readListFromInternalStorage(this, date + FileIO.CHECK_OUT);
        total = FileIO.readListFromInternalStorage(this, date + FileIO.LOG_TOTAL);

        // Reset TotalTime
        timeCount = 0;


        for (int i = 0; i < checkIn.size(); i++) {
            addCheckInTextView(i);
        }
        for (int i = 0; i < checkOut.size(); i++) {
            addCheckOutTextView(i);
        }
        for (int i = 0; i < total.size(); i++) {
            addTotalTextView(i);
            addToDailyTotal(total.get(i));
        }
        tvTotalTime.setText("Total Time: " + createDailyTotalString(timeCount));
    }

    private void addToDailyTotal(String log) {
        int start;
        int end;

        start = 0;
        end = log.indexOf(':');
        timeCount += Integer.parseInt(log.substring(start, end)) * 3600;
        start = end + 1;
        end = log.length();
        timeCount += Integer.parseInt(log.substring(start, end)) * 60;
    }

    private String createDailyTotalString(int seconds) {
        String dailyTotal = "";
        // Convert totalSeconds into a String ("format" hours : minutes);
        int hours = 0, minutes = 0;
        if (seconds > 3600) {
            hours = seconds / 3600;
            seconds = seconds % 3600;
        }
        if (seconds >= 60) {
            minutes = seconds / 60;
        }

        dailyTotal += hours + ":";
        if (minutes < 10) dailyTotal += "0";
        dailyTotal += minutes;
        return dailyTotal;
    }
}
