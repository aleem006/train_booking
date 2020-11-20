package com.example.subwaybooking.Util;

import android.widget.Toast;

import com.example.subwaybooking.App.AppController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static void showToast(String message, boolean isLong) { // to show toast
        Toast toast;
        if (isLong)
            toast = Toast.makeText(AppController.getInstance(), message, Toast.LENGTH_LONG);
        else
            toast = Toast.makeText(AppController.getInstance(), message, Toast.LENGTH_SHORT);

        toast.show();
    }

    public static String getCurrentDate(){ // function to get current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDate = simpleDateFormat.format(c.getTime());
        return currentDate;
    }
}
