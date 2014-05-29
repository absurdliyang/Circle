package com.absurd.circle.util;

import android.provider.CalendarContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by absurd on 14-3-26.
 */
public class TimeUtil {

    public static String formatShowTime(Date time){
        if(time != null) {
            Calendar nowCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            int nowYear = nowCalendar.get(Calendar.YEAR);
            int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
            Calendar publishCalendar = Calendar.getInstance();
            publishCalendar.setTime(time);
            publishCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            int publishYear = publishCalendar.get(Calendar.YEAR);
            int publishMonth = publishCalendar.get(Calendar.MONTH);
            int publishDay = publishCalendar.get(Calendar.DAY_OF_MONTH);
            int publishHour = publishCalendar.get(Calendar.HOUR_OF_DAY);
            int publishMinute = publishCalendar.get(Calendar.MINUTE);
            if (publishDay == nowDay) {
                long dis = nowCalendar.getTimeInMillis() - publishCalendar.getTimeInMillis();
                if (dis < 3600000) {
                    return dis / 60000 + "分钟前";
                } else {
                    SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
                    formater.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    return "今天 " + formater.format(time);
                }
            }
            if (publishYear < nowYear) {
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                formater.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                return formater.format(time);
            } else {
                SimpleDateFormat formater = new SimpleDateFormat("MM-dd HH:mm");
                formater.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                return formater.format(time);
            }
        }else{
            return "";
        }

    }

    public static String toAge(Date birthday){
        if(birthday == null){
            return "0";
        }
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthday);
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + "";
    }
}
