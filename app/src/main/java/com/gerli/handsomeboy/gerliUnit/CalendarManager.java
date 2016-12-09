package com.gerli.handsomeboy.gerliUnit;

import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by HandsomeBoy on 2016/12/4.
 */
public class CalendarManager {
    static SimpleDateFormat dayDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat monthDateFormat = new SimpleDateFormat("yyyy-MM");
    static SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");
    static SimpleDateFormat timeDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
    static int rawOffSet =  TimeZone.getTimeZone("GMT+8:00").getRawOffset();

    public CalendarManager(){

    }
    public static Date getLatestRecordTime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Date date = calendar.getTime();
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
        return date;
    }

    public static String getYear(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        return yearDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getYear(Calendar calendar){
        return yearDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getYear(int year){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        calendar.set(year,0,1);
        return yearDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getMonth(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        return monthDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getMonth(Calendar calendar){
        return monthDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getMonth(int year,int month){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        calendar.set(year,month - 1,1);
        return monthDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getDay(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        return dayDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getDay(Calendar calendar){
        return dayDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getDay(int year,int month,int day){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        calendar.set(year,month-1,day);
        return dayDateFormat.format(calendar.getTimeInMillis() + rawOffSet);
    }

    public static String getTime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis() + TimeZone.getTimeZone("GMT+8:00").getRawOffset());

        return timestamp.toString();
    }

    public Calendar getDayCalendar(){
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
    }

    public Calendar getDayCalendar(int year,int month,int day){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        calendar.set(year,month,day);
        return calendar;
    }

    public ArrayList<String> getWeekArrList(Calendar calendar){
        CalendarManager.setToFirstDayOfWeek(calendar);

        ArrayList<String> weekList= new ArrayList<String>();
        for(int i =0;i<7;i++){
            weekList.add(getDay(calendar));
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        return weekList;
    }

    public ArrayList<String> getYearArrList(int year){
        Calendar calendar = getDayCalendar();
        ArrayList<String> yearList = new ArrayList<String>();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        for(int i = 0;i<12;i++){
            calendar.set(Calendar.MONTH,i);
            yearList.add(getMonth(calendar));
        }
        return yearList;
    }

    public static void setToFirstDayOfWeek(Calendar calendar){
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek){
            case Calendar.SUNDAY:
                calendar.add(Calendar.DAY_OF_MONTH,0);
                break;
            case Calendar.MONDAY:
                calendar.add(Calendar.DAY_OF_MONTH,-1);
                break;
            case Calendar.TUESDAY:
                calendar.add(Calendar.DAY_OF_MONTH,-2);
                break;
            case Calendar.WEDNESDAY:
                calendar.add(Calendar.DAY_OF_MONTH,-3);
                break;
            case Calendar.THURSDAY:
                calendar.add(Calendar.DAY_OF_MONTH,-4);
                break;
            case Calendar.FRIDAY:
                calendar.add(Calendar.DAY_OF_MONTH,-5);
                break;
            case Calendar.SATURDAY:
                calendar.add(Calendar.DAY_OF_MONTH,-6);
                break;
        }
    }
}