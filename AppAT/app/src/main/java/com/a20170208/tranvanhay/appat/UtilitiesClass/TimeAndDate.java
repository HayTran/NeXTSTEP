package com.a20170208.tranvanhay.appat.UtilitiesClass;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Van Hay on 15-Jun-17.
 */

public class TimeAndDate {
    long miliseconds;
    public static String convertMilisecondToTimeAndDate(long miliseconds){
        Date date = new Date(miliseconds);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss --- dd/MM/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        return format.format(date) + "";
    }
}
