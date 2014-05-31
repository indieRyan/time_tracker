package com.RandKprogramming.everything_time_tracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class WeeklyTotals extends Activity implements OnClickListener {

    LinearLayout columnDailyTotal, columnDate;
    Button back, next;
    TextView tvDateRange, tvTotalTime;

    Calendar calendar;
    int startDay, startMonth, startYear;
    String startDate, endDate;

    int totalTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_totals);
        init();
    }

    private void init() {
        // Date and total LinearLayouts
        columnDailyTotal = (LinearLayout) findViewById(R.id.column_weekly_day_total);
        columnDate = (LinearLayout) findViewById(R.id.column_weekly_day_date);

        // TextViews/Buttons
        next = (Button) findViewById(R.id.weekly_back);
        next.setOnClickListener(this);
        back = (Button) findViewById(R.id.weekly_next);
        back.setOnClickListener(this);
        tvDateRange = (TextView) findViewById(R.id.weekly_range);
        tvTotalTime = (TextView) findViewById(R.id.weekly_total_time);
        tvTotalTime.setText("test");

        // Init date
        calendar = Calendar.getInstance();
        // Getting first day of the current week
        startDay = calendar.get(Calendar.DAY_OF_MONTH) - (calendar.get(Calendar.DAY_OF_WEEK) - 1);
        startMonth = calendar.get(Calendar.MONTH) + 1;
        startYear = calendar.get(Calendar.YEAR);
        startDate = getDate(false);
        endDate = getDate(true);
        tvDateRange.setText(startDate.replace('%', '/') + " - " + endDate.replace('%', '/'));

        loadDailyTotals();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weekly_back:
                totalTime = 0;
                decreaseDay(6);
                startDate = getDate(false);
                endDate = getDate(true);
                tvDateRange.setText(startDate.replace('%', '/') + " - " + endDate.replace('%', '/'));
                loadDailyTotals();
                break;
            case R.id.weekly_next:
                totalTime = 0;
                increaseDay(6);
                startDate = getDate(false);
                endDate = getDate(true);
                tvDateRange.setText(startDate.replace('%', '/') + " - " + endDate.replace('%', '/'));
                loadDailyTotals();
                break;
        }
    }

    private String getDate(boolean endDate) {
        if (endDate) increaseDay(6);
        String date = "";
        if (startMonth < 10) date += 0;
        date += startMonth + "%";
        if (startDay < 10) date += 0;
        date += startDay + "%";
        date += startYear;
        if (endDate) decreaseDay(6);
        return date;
    }

    private void loadDailyTotals() {
        columnDate.removeAllViews();
        columnDailyTotal.removeAllViews();

        for (int i = 0; i < 7; i++) {
            addTotalTextView(startDate);
            addDateTextView(startDate);
            increaseDay(1);
            startDate = getDate(false);
            totalTime += FileIO.readIntFromInternalStorage(this, startDate + FileIO.DAILY_TOTAL);
        }
        decreaseDay(7);
        startDate = getDate(false);

        tvTotalTime.setText("Total Time: " + getTimeFormat(totalTime));
    }

    private void increaseDay(int amount) {
        for (int i = 0; i < amount; i++) {
            startDay++;
            if (startDay > getLastDayOfMonth(startMonth - 1)) {
                startMonth++;
                startDay = 1;
            }
            if (startMonth > 12) {
                startYear++;
                startMonth = 1;
            }
        }
    }

    private void decreaseDay(int amount) {
        for (int i = 0; i < amount; i++) {
            startDay--;
            if (startDay < 1) {
                startMonth--;
                startDay = getLastDayOfMonth(startMonth - 1);
                if (startMonth < 1) {
                    startMonth = 12;
                    startYear--;
                }
                startDay = getLastDayOfMonth(startMonth - 1);
            }
        }
    }

    private int getLastDayOfMonth(int month) {
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

    private String getTimeFormat(int seconds) {
        String time = "";
        int totalSeconds;
        totalSeconds = seconds;

        int hours = 0, minutes = 0;
        if (totalSeconds > 3600) {
            hours = totalSeconds / 3600;
            totalSeconds = totalSeconds % 3600;
        }
        if (totalSeconds >= 60) {
            minutes = totalSeconds / 60;
        }

        time += hours + ":";
        if (minutes < 10) time += "0";
        time += minutes;
        return time;
    }


    private void addTotalTextView(String date) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        textView.setLayoutParams(params);
        textView.setText(getTimeFormat(FileIO.readIntFromInternalStorage(this, date + FileIO.DAILY_TOTAL)));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.log_total_time);
        columnDailyTotal.addView(textView);
    }

    private void addDateTextView(String date) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        textView.setLayoutParams(params);
        textView.setText(date.replace('%', '/'));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.check_in_button);
        columnDate.addView(textView);
    }
}
