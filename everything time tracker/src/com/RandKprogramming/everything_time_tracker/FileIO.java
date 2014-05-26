package com.RandKprogramming.everything_time_tracker;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    public static final String CHECK_IN = "bCheckIn";
    public static final String CHECK_OUT = "check_out";
    public static final String DAILY_TOTAL = "dailyTotal";
    public static final String WEEKLY_TOTAL = "weeklyTotal";
    public static final String MONTHLY_TOTAL = "monthlyTotal";

    public static void saveToInternalStorage(List list, Context context, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveToInternalStorage(boolean bool, Context context, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeBoolean(bool);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readListFromInternalStorage(Context context, String fileName) {
        ArrayList list = new ArrayList();
        FileInputStream fin;

        try {
            fin = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            list = (ArrayList) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        }
        return list;
    }

    public static boolean readBooleanFromInternalStorage(Context context, String fileName) {
        boolean bool = false;
        FileInputStream fin;

        try {
            fin = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            bool = ois.readBoolean();
            ois.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }
}
