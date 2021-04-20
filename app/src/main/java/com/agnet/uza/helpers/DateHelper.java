package com.agnet.uza.helpers;

import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static String getCurrentDate(){

        //current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate= sdf.format(new Date());

        return currentDate;
    }

    public static String getCurrentTime() {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Calendar time = Calendar.getInstance();
        String currentTime = df.format(time.getTime());

        return currentTime;
    }

    public static String getTimeInterval(int interval){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Calendar time = Calendar.getInstance();
        time.add(Calendar.MINUTE, interval);
        String currentTimeInterval = df.format(time.getTime());

        return currentTimeInterval;

    }
    public static String getMonth()
    {
        Format dateFormat = new SimpleDateFormat("MMMM  yyyy");
        String res = dateFormat.format(new Date());

        return res;

    }
}
