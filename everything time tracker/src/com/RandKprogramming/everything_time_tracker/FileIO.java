package com.RandKprogramming.everything_time_tracker;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    public static final String CHECK_IN = "checkIn";
    public static final String CHECK_OUT = "checkOut";
    public static final String LOG_TOTAL = "logTotal";
    public static final String DAILY_TOTAL = "dailyTotalString";
    public static final String WEEKLY_TOTAL = "weeklyTotal";
    public static final String MONTHLY_TOTAL = "monthlyTotal";

    public static void saveListToInternalStorage(List list, Context context, String fileName) {
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

    public static void saveBooleanToInternalStorage(boolean bool, Context context, String fileName) {
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

    public static void saveIntToInternalStorage(int number, Context context, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeInt(number);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList readListFromInternalStorage(Context context, String fileName) {
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

    public static int readIntFromInternalStorage(Context context, String fileName) {
        int number = 0;
        FileInputStream fin;

        try {
            fin = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            number = ois.readInt();
            ois.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }
}
