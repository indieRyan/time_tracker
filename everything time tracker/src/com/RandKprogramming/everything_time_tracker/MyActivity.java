package com.RandKprogramming.everything_time_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.*;


public class MyActivity extends Activity implements OnClickListener {

    // Lists for TextViews that log time
    List<TextView> dailyCheckInView = new ArrayList<TextView>();
    List<TextView> dailyCheckOutView = new ArrayList<TextView>();
    List<TextView> dailyTotalView = new ArrayList<TextView>();

    // Button IDs for TextViews that logged time
    List<Integer> checkInId = new ArrayList<Integer>();
    List<Integer> checkOutId = new ArrayList<Integer>();
    List<Integer> totalId = new ArrayList<Integer>();

    // Lists for check in/ check out times.
    List<String> checkInTime = new ArrayList<String>();
    List<String> checkOutTime = new ArrayList<String>();
    List<String> totalTime = new ArrayList<String>();

    Button bCheckIn, bCheckOut, bAddCheckIn, bAddCheckOut, bDailyTotals, bWeeklyTotals, bMonthlyTotals;
    LinearLayout columnCheckIn, columnCheckOut, columnTotal;

    Calendar calendar;
    String date;
    TextView tvDate;

    private boolean canCheckOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    //---------------------------------------------------
    // Initializing Code And Handling Input
    //---------------------------------------------------

    private void init() {
        // Columns for TextView "check in, check out, total time"
        columnCheckIn = (LinearLayout) findViewById(R.id.column_check_in);
        columnCheckOut = (LinearLayout) findViewById(R.id.check_out_column);
        columnTotal = (LinearLayout) findViewById(R.id.total_time_column);

        date = getCurrentDate();
        tvDate = (TextView) findViewById(R.id.main_date);
        tvDate.setText("Date: " + date.replace('%', '/'));
        loadTodayLog();

        // Buttons
        bCheckIn = (Button) findViewById(R.id.check_in);
        bCheckIn.setOnClickListener(this);
        bCheckOut = (Button) findViewById(R.id.check_out);
        bCheckOut.setOnClickListener(this);
        bAddCheckIn = (Button) findViewById(R.id.add_check_in);
        bAddCheckIn.setOnClickListener(this);
        bAddCheckOut = (Button) findViewById(R.id.add_check_out);
        bAddCheckOut.setOnClickListener(this);
        bDailyTotals = (Button) findViewById(R.id.day_total);
        bDailyTotals.setOnClickListener(this);
        bWeeklyTotals = (Button) findViewById(R.id.week_total);
        bWeeklyTotals.setOnClickListener(this);
        bMonthlyTotals = (Button) findViewById(R.id.month_total);
        bMonthlyTotals.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_in:
                // Check if check_out was pressed last
                if (!canCheckOut) {
                    createCheckInTextView();
                    canCheckOut = true;
                }
                break;
            case R.id.check_out:
                // Check if check_in was pressed last
                if (canCheckOut) {
                    createCheckOutTextView();
                    createTotalTextView();
                    canCheckOut = false;
                }
                break;
            case R.id.add_check_in:
                if (!canCheckOut) {
                    System.out.println("Add check in");
                }
                break;
            case R.id.add_check_out:
                if (canCheckOut) {
                    System.out.println("Add check out");
                }
                break;
            case R.id.day_total:
                System.out.println("Day Totals");
                Intent intent = new Intent(this, DailyTotals.class);
                startActivity(intent);
                break;
            case R.id.week_total:
                System.out.println("Week totals");
                break;
            case R.id.month_total:
                System.out.println("Month totals");
                break;

            // Checks to see if any of the dynamically created TextView have been pressed
            default:
                for (int i = 0; i < checkInId.size(); i++) {
                    if (checkInId.get(i) == v.getId()) {
                        System.out.println("Check in " + v.getId());
                        break;
                    }
                }

                for (int j = 0; j < checkOutId.size(); j++) {
                    if (checkOutId.get(j) == v.getId()) {
                        System.out.println("Check out " + v.getId());
                        break;
                    }
                }
                for (int k = 0; k < totalId.size(); k++) {
                    if (totalId.get(k) == v.getId()) {
                        System.out.println("Total " + v.getId());
                        break;
                    }
                    break;
                }
        }
    }

    //---------------------------------------------------
    // Get Time/Date Methods
    //---------------------------------------------------

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

    /**
     * Gets the current time
     *
     * @return (String) Current time in the format hours:minutes:seconds
     */
    private String getCurrentTime() {
        calendar = Calendar.getInstance();
        String time = "";
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        time += hour + ":";
        time += minute;
        if (calendar.get(Calendar.AM_PM) == 0) time += " AM";
        else time += " PM";
        return time;
    }

    /**
     * Gets the total time between check in and check out. The check in, check out times are held in the {@link #dailyCheckInView} and
     * {@link #dailyCheckOutView}.
     *
     * @param position The position in the array to retrieve the check in, check out times.
     * @return (int) The time between check in and check out in seconds.
     */
    private String getTotalTime(int position) {
        String time = "";
        String startTime = checkInTime.get(position);
        String endTime = checkOutTime.get(position);
        int start;
        int end;
        int totalSeconds;
        int totalStartSeconds = 0;
        int totalEndSeconds = 0;

        // Convert check in time into seconds
        start = 0;
        end = startTime.indexOf(':');
        totalStartSeconds += Integer.parseInt(startTime.substring(start, end)) * 3600;
        start = end + 1;
        end = startTime.length() - 3;
        totalStartSeconds += Integer.parseInt(startTime.substring(start, end)) * 60;

        // Convert check out time into seconds
        start = 0;
        end = endTime.indexOf(':');
        totalEndSeconds += Integer.parseInt(endTime.substring(start, end)) * 3600;
        start = end + 1;
        end = startTime.length() - 3;
        totalEndSeconds += Integer.parseInt(endTime.substring(start, end)) * 60;

        // Calculates total of seconds from check in time to check out time
        totalSeconds = totalEndSeconds - totalStartSeconds;

        // Convert totalSeconds into a String ("format" hours : minutes);
        int hours = 0, minutes = 0;
        if (totalSeconds > 3600) {
            hours = totalSeconds / 3600;
            totalSeconds = totalSeconds % 3600;
        }
        if (totalSeconds > 60) {
            minutes = totalSeconds / 60;
        }

        time += hours + ":" + minutes;

        return time;
    }

    //---------------------------------------------------
    // Methods For Dynamically Creating TextViews
    //---------------------------------------------------

    /**
     * Creates and adds a new TextView to {@link #columnCheckIn} and {@link #dailyCheckInView}. Sets the Id and saves it to {@code checkInId}.
     * Also saves the current time in {@link #checkInTime}.
     */
    private void createCheckInTextView() {
        // Retrieving current time
        String time = getCurrentTime();

        // Creating TextView
        TextView checkIn = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        checkIn.setLayoutParams(params);
        checkIn.setId(checkInId.size() + 100);
        checkIn.setOnClickListener(this);
        checkIn.setText(time);
        checkIn.setGravity(Gravity.CENTER);
        checkIn.setBackgroundResource(R.drawable.check_in_button);

        // Saving the TextView, TextView id and the time logged. Also adding the TextView to the corresponding LinearLayout.
        dailyCheckInView.add(checkIn);
        checkInId.add(checkIn.getId());
        checkInTime.add(time);
        columnCheckIn.addView(checkIn);

        // Saving the check out time to internal storage
        FileIO.saveToInternalStorage(checkInTime, this, date + FileIO.CHECK_IN);
    }

    /**
     * Creates and adds a new TextView to {@link #columnCheckOut} and {@link #dailyCheckOutView}. Sets the Id and saves it to {@link #checkOutId}.
     * Also saves the current time in {@link #checkOutTime}.
     */
    private void createCheckOutTextView() {
        // Retrieving current time
        String time = getCurrentTime();

        // Creating TextView
        TextView checkOut = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        checkOut.setLayoutParams(params);
        checkOut.setId(checkOutId.size() + 200);
        checkOut.setOnClickListener(this);
        checkOut.setText(time);
        checkOut.setGravity(Gravity.CENTER);
        checkOut.setBackgroundResource(R.drawable.check_out_button);

        // Saving the TextView, TextView id and the time logged. Also adding the TextView to the corresponding LinearLayout.
        dailyCheckOutView.add(checkOut);
        checkOutId.add(checkOut.getId());
        checkOutTime.add(time);
        columnCheckOut.addView(checkOut);

        // Saving the check out time to internal storage
        FileIO.saveToInternalStorage(checkOutTime, this, date + FileIO.CHECK_OUT);
    }

    /**
     * creates and adds a new TextView to {@link #columnTotal} and {@link #dailyTotalView}. Sets the Id and saves it to {@link #totalId}.
     * Saves the difference between check in and check out times in {@link #totalTime}.
     */
    private void createTotalTextView() {
        // Retrieving time between check in and check out
        String time = getTotalTime(dailyTotalView.size());

        // Create TextView. Set size and layout.
        TextView logTotal = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        logTotal.setLayoutParams(params);
        logTotal.setId(totalId.size() + 300);
        logTotal.setOnClickListener(this);
        logTotal.setText(time);
        logTotal.setGravity(Gravity.CENTER);
        logTotal.setBackgroundResource(R.drawable.log_total_time);

        // Saving the TextView, TextView id and the time logged. Also adding the TextView to the corresponding LinearLayout.
        dailyTotalView.add(logTotal);
        totalId.add(logTotal.getId());
        totalTime.add(time);
        columnTotal.addView(logTotal);

        // Saving the total time to internal storage.
        FileIO.saveToInternalStorage(totalTime, this, date + FileIO.DAILY_TOTAL);
    }

    //---------------------------------------------------
    // Methods For Loading And Creating TextViews Of Current Day Logs
    //---------------------------------------------------

    private void addCheckInTextView(int position) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(0, 5, 0, 5);
        textView.setLayoutParams(params);
        textView.setId(position + 100);
        textView.setOnClickListener(this);
        textView.setText(checkInTime.get(position));
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
        textView.setOnClickListener(this);
        textView.setText(checkOutTime.get(position));
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
        textView.setOnClickListener(this);
        textView.setText(totalTime.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.log_total_time);
        columnTotal.addView(textView);
    }

    private void loadTodayLog() {

        checkInTime = FileIO.readFromInternalStorage(this, date + FileIO.CHECK_IN);
        for (int i = 0; i < checkInTime.size(); i++) {
            addCheckInTextView(i);
        }


        checkOutTime = FileIO.readFromInternalStorage(this, date + FileIO.CHECK_OUT);
        for (int i = 0; i < checkOutTime.size(); i++) {
            addCheckOutTextView(i);
        }

        totalTime = FileIO.readFromInternalStorage(this, date + FileIO.DAILY_TOTAL);
        for (int i = 0; i < totalTime.size(); i++) {
            addTotalTextView(i);
        }
    }
}
