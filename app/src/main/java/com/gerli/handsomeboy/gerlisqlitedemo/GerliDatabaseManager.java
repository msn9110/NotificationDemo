package com.gerli.handsomeboy.gerlisqlitedemo;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.gerli.handsomeboy.gerliUnit.AccountType;
import com.gerli.handsomeboy.gerliUnit.CalendarManager;
import com.gerli.handsomeboy.gerliUnit.Info_type;
import com.gerli.handsomeboy.gerliUnit.Table;
import com.gerli.handsomeboy.gerliUnit.UnitPackage;
import com.gerli.handsomeboy.gerliUnit.UnitPackage.*;

/**
 * Created by HandsomeBoy on 2016/10/27.
 */
public class GerliDatabaseManager {
    private static final int VERSION = 7;
    private final String DatabaseName = "Gerli_DB";

    //操作Database的內部成員
    SQLiteDB sqLiteDB;
    SQLiteDatabase db;

    CalendarManager calendarManager;

    /**
     * GerliDatabaseManager的建構子
     * 用來初始化資料庫設定與取得資料庫
     * @param context Activity 的 context
     */
    public GerliDatabaseManager(Context context){
        sqLiteDB = new SQLiteDB(context,DatabaseName,null,VERSION);
        db = sqLiteDB.getWritableDatabase();
        calendarManager = new CalendarManager();
    }

    //region BarChart

    /**
     * 根據type來生成長條圖表的資料
     * type可以放入WEEK跟YEAR來取得周統計圖或年統計圖
     * @param type 欲取得的日期間格。給 Info_type.WEEK 或 Info_type.YEAR
     * @return 打包成日期與支出的資料包，失敗回傳null
     */
    public GerliPackage getBarChart(Info_type type){
        if(type == Info_type.WEEK){
            return getBarChartByWeek();
        }
        else if(type == Info_type.YEAR){
            return getBarChartByYear();
        }
        else{
            Log.d("DebugError","getBarChart : Info_type not correct(use WEEK or YEAR)");
            return null;
        }
    }


    public BarChartPackage getBarChartByWeek(){
        return getBarChartByWeek(calendarManager.getDayCalendar());
    }

    public BarChartPackage getBarChartByWeek(Calendar dayOfWeek){
        ArrayList<String> weekList;
        float[] expenseArr = new float[7];
        weekList =  calendarManager.getWeekArrList(dayOfWeek);
        Cursor cursor =  getCursor_expenseByWeek(weekList.get(0),weekList.get(6));    //0為禮拜天,1為禮拜一,...,6為禮拜六

        int row_num = cursor.getCount();
        String[] arr = weekList.toArray(new String[weekList.size()]);
        cursor.moveToFirst();
        for (int i = 0,j=0; i < row_num; i++){
            String day = cursor.getString(0);
            float expense = cursor.getFloat(1);
            while(!arr[j].equals(day)){
                expenseArr[j] = 0;
                j++;
            }
            expenseArr[j] = expense;
        }

        return new UnitPackage().new BarChartPackage(weekList,expenseArr);
    }

    public BarChartPackage getBarChartByYear(){
        return getBarChartByYear(calendarManager.getDayCalendar().get(Calendar.YEAR));
    }

    public BarChartPackage getBarChartByYear(Calendar calendar){
        return getBarChartByYear(calendar.get(Calendar.YEAR));
    }

    public BarChartPackage getBarChartByYear(int year){
        ArrayList<String> yearList;
        float[] expenseArr = new float[12];
        yearList =  calendarManager.getYearArrList(year);
        Cursor cursor =  getCursor_expenseByYear(yearList.get(0),yearList.get(11));    //0為1月,1為二月,...,11為12月

        int row_num = cursor.getCount();
        String[] arr = yearList.toArray(new String[yearList.size()]);
        cursor.moveToFirst();
        for (int i = 0,j=0; i < row_num; i++){
            String month = cursor.getString(0);
            float expense = cursor.getFloat(1);
            while(!arr[j].equals(month)){
                expenseArr[j] = 0;
                j++;
            }
            expenseArr[j] = expense;
        }
        return new UnitPackage().new BarChartPackage(yearList,expenseArr);
    }

    //endregion

    //region PieChart

    public GerliPackage getPieChart(Info_type type){
        if(type == Info_type.DAY){
            return getBarChartByWeek();
        }
        else if(type == Info_type.WEEK){
            return getBarChartByYear();
        }
        else if(type == Info_type.MONTH){
            return getBarChartByYear();
        }
        else if(type == Info_type.YEAR){
            return getBarChartByYear();
        }
        else{
            Log.d("DebugError","getPieChart : Info_type not correct(use DAY、WEEK、MONTH or YEAR)");
            return null;
        }
    }

    public PieChartPackage getPieChartByDay(int limit){
        return getPieChartByDay(calendarManager.getDayCalendar(),limit);
    }

    public PieChartPackage getPieChartByDay(int year,int month,int day,int limit){
        return getPieChartByDay(calendarManager.getDayCalendar(year,month,day),limit);
    }

    public PieChartPackage getPieChartByDay(Calendar day,int limit){
        ArrayList<String> typeList = new ArrayList<>();
        float[] expenseRateArr;
        Cursor cursor =  getCursor_expenseRateByDay(calendarManager.getDay(day),limit);

        int row_num = cursor.getCount();
        expenseRateArr = new float[row_num];
        cursor.moveToFirst();
        for (int i = 0; i < row_num; i++){
            String type = AccountType.getString(cursor.getInt(0));
            float expense = cursor.getFloat(1);
            typeList.add(type);
            expenseRateArr[i] = expense;
            cursor.moveToNext();
        }

        return new UnitPackage().new PieChartPackage(typeList,expenseRateArr);
    }

    public PieChartPackage getPieChartByWeek(int limit){
        return getPieChartByWeek(calendarManager.getDayCalendar(),limit);
    }

    public PieChartPackage getPieChartByWeek(int year,int month,int day,int limit){
        return getPieChartByWeek(calendarManager.getDayCalendar(year,month,day),limit);
    }

    public PieChartPackage getPieChartByWeek(Calendar dayOfWeek,int limit){
        ArrayList<String> typeList = new ArrayList<>();
        float[] expenseRateArr;
        Cursor cursor =  getCursor_expenseRateByWeek(dayOfWeek,limit);

        int row_num = cursor.getCount();
        expenseRateArr = new float[row_num];
        cursor.moveToFirst();
        for (int i = 0; i < row_num; i++){
            String type = AccountType.getString(cursor.getInt(0));
            float expense = cursor.getFloat(1);
            typeList.add(type);
            expenseRateArr[i] = expense;
            cursor.moveToNext();
        }

        return new UnitPackage().new PieChartPackage(typeList,expenseRateArr);
    }

    public PieChartPackage getPieChartByMonth(int limit){
        return getPieChartByMonth(calendarManager.getDayCalendar(),limit);
    }

    public PieChartPackage getPieChartByMonth(int year,int month,int limit){
        return getPieChartByMonth(calendarManager.getDayCalendar(year,month,1),limit);
    }

    public PieChartPackage getPieChartByMonth(Calendar dayOfMonth,int limit){
        ArrayList<String> typeList = new ArrayList<>();
        float[] expenseRateArr;
        Cursor cursor =  getCursor_expenseRateByMonth(calendarManager.getMonth(dayOfMonth),limit);

        int row_num = cursor.getCount();
        expenseRateArr = new float[row_num];
        cursor.moveToFirst();
        for (int i = 0; i < row_num; i++){
            String type = AccountType.getString(cursor.getInt(0));
            float expense = cursor.getFloat(1);
            typeList.add(type);
            expenseRateArr[i] = expense;
            cursor.moveToNext();
        }

        return new UnitPackage().new PieChartPackage(typeList,expenseRateArr);
    }

    public PieChartPackage getPieChartByYear(int limit){
        return getPieChartByYear(calendarManager.getDayCalendar(),limit);
    }

    public PieChartPackage getPieChartByYear(int year,int limit){
        return getPieChartByYear(calendarManager.getDayCalendar(year,1,1),limit);
    }

    public PieChartPackage getPieChartByYear(Calendar dayOfYear,int limit){
        ArrayList<String> typeList = new ArrayList<>();
        float[] expenseRateArr;
        Cursor cursor =  getCursor_expenseRateByYear(calendarManager.getYear(dayOfYear),limit);

        int row_num = cursor.getCount();
        expenseRateArr = new float[row_num];
        cursor.moveToFirst();
        for (int i = 0; i < row_num; i++){
            String type = AccountType.getString(cursor.getInt(0));
            float expense = cursor.getFloat(1);
            typeList.add(type);
            expenseRateArr[i] = expense;
            cursor.moveToNext();
        }

        return new UnitPackage().new PieChartPackage(typeList,expenseRateArr);
    }

    //endregion

    /**
     * 把tableName的表格中所有資料都取出來
     * @param tableName 表格名
     * @return 整個表格所有欄位資料
     */
    public Cursor getCursorByTable(String tableName){
        return db.rawQuery( "select * from " + tableName, null);
    }

    public Date getLatestRecordTime(){
        Cursor cursor = db.rawQuery("SELECT Time FROM " + sqLiteDB.accountTable +
                " ORDER BY Time DESC" +
                " LIMIT " + 1 ,null);


        Date data = new Date(0);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            String timeStr = cursor.getString(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            try{
                data = format.parse(timeStr);
                TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
            }catch (ParseException e){
                Log.d("DatabaseError","getLatestRecordTime : SimpleDateFormat parse error");
            }
        }

        return data;
    }

    //region expenseFunctions

    public Cursor getCursor_expenseByWeek(String start, String end){
        return getCursor_DayTotal(Info_type.EXPENSE,start,end);
    }

    public Cursor getCursor_expenseByYear(String startMonth, String endMonth){
        return getCursor_MonthTotal(Info_type.EXPENSE,startMonth,endMonth);
    }


    public Cursor getCursor_expenseRateByDay(String day,int limit){
        return getCursor_DayRate(Info_type.EXPENSE,day,limit);
    }

    public Cursor getCursor_expenseRateByWeek(Calendar calendar,int limit){
        return getCursor_WeekRate(Info_type.EXPENSE,calendar,limit);
    }

    public Cursor getCursor_expenseRateByMonth(String month,int limit){
        return getCursor_MonthRate(Info_type.EXPENSE,month,limit);
    }

    public Cursor getCursor_expenseRateByYear(String year,int limit){
        return getCursor_YearRate(Info_type.EXPENSE,year,limit);
    }

    //endregion


    public Cursor getCursor_total(Info_type type){
        String whereCaluse = "";
        switch (type){
            case EXPENSE:
                whereCaluse = " money > 0 ";
                break;
            case INCOME:
                whereCaluse = " money < 0 ";
                break;
            case CLASS:
            case DAY:
            case WEEK:
            case MONTH:
            case YEAR:
                Log.d("DatabaseDebug","getCursor_total : Info_type not correct(use EXPENSE or INCOME)");
                return null;
        }

        return db.rawQuery("select  sum(money),Time from " + sqLiteDB.accountTable +
                " where " + whereCaluse, null);
    }

    public Cursor getCursor_todayItem(){
        String today = calendarManager.getDay();

        Log.d("DatabaseDebug","getCursor_todayItem : Today = " + today);

        return db.rawQuery("select * from " + sqLiteDB.accountTable +
                " where substr(Time, 1, 10)='" + today + "'", null);
    }

    public Cursor getCursor_dayItem(String day){

        Log.d("DatabaseDebug","getCursor_dayItem : day = " + day);

        return db.rawQuery("select * from " + sqLiteDB.accountTable +
                " where substr(Time, 1, 10)='" + day + "'", null);
    }


    public TotalPackage getTodayTotal(){
        Cursor cursor = getCursor_todayItem();
        int row_num = cursor.getCount();

        if(row_num==0){
            Log.d("DatabaseData","today no record");
            return new UnitPackage().new TotalPackage(0,0);
        }

        int expense = 0;
        int income = 0;
        cursor.moveToFirst();
        for(int i =0; i < row_num; i++){
            int money = cursor.getInt(2);

            if(money > 0){
                expense += money;
            }
            else if(money < 0){
                income += money;
            }
            cursor.moveToNext();
        }

        return new UnitPackage().new TotalPackage(expense,income);
    }

    public TotalPackage getDayTotal(String day){
        Cursor cursor = getCursor_dayItem(day);
        int row_num = cursor.getCount();

        if(row_num==0){
            Log.d("DatabaseData","today no record");
            return new UnitPackage().new TotalPackage(0,0);
        }

        int expense = 0;
        int income = 0;
        cursor.moveToFirst();
        for(int i =0; i < row_num; i++){
            int money = cursor.getInt(2);

            if(money > 0){
                expense += money;
            }
            else if(money < 0){
                income += money;
            }
            cursor.moveToNext();
        }

        return new UnitPackage().new TotalPackage(expense,income);
    }

    public Cursor getCursor_DayRate(Info_type type,String day,int limit){
        String sqlDay = "'" + day + "'";
        if (type ==Info_type.EXPENSE) {
            return db.rawQuery("SELECT type, sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money > 0 " + "AND substr(time,1,10)=" + sqlDay +
                    " GROUP BY type " +
                    " ORDER BY sum(money) DESC" +
                    " LIMIT " +limit ,null);
        }
        else if(type ==Info_type.INCOME){
            return db.rawQuery("SELECT type, sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money < 0 " + "AND substr(time,1,10)=" + sqlDay +
                    " GROUP BY type" +
                    " ORDER BY sum(money) DESC " +
                    " LIMIT " +limit,null);
        }
        else{
            return null;
        }
    }

    public Cursor getCursor_DayRate(Info_type type,String start,String end,int limit){
        String sqlStart = "'" + start + "'";
        String sqlEnd = "'" + end + "'";
        if (type ==Info_type.EXPENSE) {
            return db.rawQuery("SELECT type, sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money > 0 " + " AND substr(time,1,10)>= " + sqlStart + " AND substr(time,1,10)<= " + sqlEnd +
                    " GROUP BY type " +
                    " ORDER BY sum(money) DESC " +
                    " LIMIT " +limit ,null);
        }
        else if(type ==Info_type.INCOME){
            return db.rawQuery("SELECT type, sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money < 0 " + " AND substr(time,1,10)>= " + sqlStart + " AND substr(time,1,10)<= " + sqlEnd +
                    " GROUP BY type " +
                    " ORDER BY sum(money) DESC " +
                    " LIMIT " +limit ,null);
        }
        else{
            return null;
        }
    }

    public Cursor getCursor_WeekRate(Info_type type,Calendar calendar,int limit){
        String start,end;
        CalendarManager.setToFirstDayOfWeek(calendar);
        start = calendarManager.getDay(calendar);
        calendar.add(Calendar.DAY_OF_MONTH,6);
        end = calendarManager.getDay(calendar);
        return getCursor_DayRate(type,start,end,limit);
    }

    public Cursor getCursor_MonthRate(Info_type type, String month, int limit){
        String sqlMonth = "'" + month + "'";
        if (type ==Info_type.EXPENSE) {
            return db.rawQuery("SELECT type, sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money > 0 " + " AND substr(time,1,7)= " + sqlMonth +
                    " GROUP BY type " +
                    " ORDER BY sum(money) DESC " +
                    " LIMIT " +limit ,null);
        }
        else if(type ==Info_type.INCOME){
            return db.rawQuery("SELECT type, sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money < 0 " + " AND substr(time,1,7)>= " + sqlMonth +
                    " GROUP BY type " +
                    " ORDER BY sum(money) DESC " +
                    " LIMIT " +limit ,null);
        }
        else{
            return null;
        }
    }

    public Cursor getCursor_YearRate(Info_type type,String year,int limit){
        String sqlYear = "'" + year + "'";
        if (type ==Info_type.EXPENSE) {
            return db.rawQuery("SELECT type, sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money > 0 " + " AND substr(time,1,4)= " + sqlYear +
                    " GROUP BY type " +
                    " ORDER BY sum(money) DESC " +
                    " LIMIT " +limit ,null);
        }
        else if(type ==Info_type.INCOME){
            return db.rawQuery("SELECT type, sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money < 0 " + " AND substr(time,1,4)= " + sqlYear +
                    " GROUP BY type " +
                    " ORDER BY sum(money) DESC " +
                    " LIMIT " +limit ,null);
        }
        else{
            return null;
        }
    }

    public Cursor getCursor_DayTotal(Info_type type,String day){
        String sqlDay = "'" + day + "'";
        if (type ==Info_type.EXPENSE) {
            return db.rawQuery("SELECT substr(time,1,10), sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money > 0 " + "AND substr(time,1,10)=" + sqlDay +
                    " GROUP BY substr(time,1,10)",null);
        }
        else if(type ==Info_type.INCOME){
            return db.rawQuery("SELECT substr(time,1,10), sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money < 0 " + "AND substr(time,1,10)=" + sqlDay +
                    " GROUP BY substr(time,1,10)",null);
        }
        else{
            return null;
        }
    }

    public Cursor getCursor_DayTotal(Info_type type,String start,String end){
        String sqlStart = "'" + start + "'";
        String sqlEnd = "'" + end + "'";
        if (type ==Info_type.EXPENSE) {
            return db.rawQuery("SELECT substr(time,1,10), sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money > 0 " + " AND substr(time,1,10)>=" + sqlStart + " AND substr(time,1,10)<=" + sqlEnd +
                    " GROUP BY substr(time,1,10)",null);
        }
        else if(type ==Info_type.INCOME){
            return db.rawQuery("SELECT substr(time,1,10), sum(money) FROM " + sqLiteDB.accountTable +
                    " WHERE money < 0 " + " AND substr(time,1,10)>=" + sqlStart + " AND substr(time,1,10)<=" + sqlEnd +
                    " GROUP BY substr(time,1,10)",null);
        }
        else{
            return null;
        }
    }

    public Cursor getCursor_MonthTotal(Info_type type,String start,String end){
        String sqlStart = "'" + start + "'";
        String sqlEnd = "'" + end + "'";
        if (type ==Info_type.EXPENSE) {
            return db.rawQuery("SELECT substr(time,1,7), sum(money) FROM " + sqLiteDB.accountTable +
                    " where money > 0 " + " AND substr(time,1,7)>=" + sqlStart +" AND substr(time,1,7)<=" + sqlEnd +
                    " GROUP BY substr(time,1,7)",null);
        }
        else if(type ==Info_type.INCOME){
            return db.rawQuery("SELECT substr(time,1,7), sum(money) FROM " + sqLiteDB.accountTable +
                    " where money  0 " + " AND substr(time,1,7)>=" + sqlStart +" AND substr(time,1,7)<=" + sqlEnd +
                    " GROUP BY substr(time,1,7)",null);
        }
        else{
            return null;
        }
    }

    public Cursor getCursor_allDayTotal(Info_type type){
        if (type ==Info_type.EXPENSE) {
            return db.rawQuery("SELECT substr(time,1,10), sum(money) FROM " + sqLiteDB.accountTable + " where money > 0 GROUP BY substr(time,1,10)",null);
        }
        else if(type ==Info_type.INCOME){
            return db.rawQuery("SELECT substr(time,1,10), sum(money) FROM " + sqLiteDB.accountTable + " where money < 0 GROUP BY substr(time,1,10)",null);
        }
        else{
            return null;
        }
    }


    //region CRUD

    public boolean insert(Table table, String nullColumnHack, ContentValues values){
        String tableStr = getTableName(table);
        return db.insert(tableStr, nullColumnHack, values) != -1;
    }
    public boolean insertAccount(String name, int money, AccountType type, String time, String description){
        ContentValues values = new ContentValues();
        values.put("Name",name);
        values.put("Money",money);
        values.put("Type",type.getValue());
        values.put("Time",time);
        values.put("Description",description);
        return db.insert(getTableName(Table.ACCOUNT), null, values) != -1;
    }

    public boolean insertMonthPlan(int year,int month,int day,String description){
        ContentValues values = new ContentValues();
        values.put("PlanYear",year);
        values.put("PlanMonth",month);
        values.put("Day",day);
        values.put("Description",description);

        return db.insert(getTableName(Table.MONTH_PLAN), null, values) != -1;
    }

    public boolean insertYearPlan(int year,int month,String description){
        ContentValues values = new ContentValues();
        values.put("PlanYear",year);
        values.put("Month",month);
        values.put("Description",description);

        return db.insert(getTableName(Table.YEAR_PLAN), null, values) != -1;
    }

    public boolean update(Table table,ContentValues values,long id){
        String tableStr = getTableName(table);
        String id_str = Long.toString(id);
        if(tableStr == null){
            Log.d("DatabaseError","DataBase delete : table name not found");
            return false;
        }
        int e = db.update(tableStr, values,"_id=" + id_str, null);

        if(e == 0){
            Log.d("DatabaseError","DataBase delete : id is not match in table");
            return false;
        }
        return true;

    }

    public boolean delete(Table table,long id) {
        String tableStr = getTableName(table);
        String id_str = Long.toString(id);
        if(tableStr == null){
            Log.d("DatabaseError","DataBase delete : table name not found");
            return false;
        }

        int e = db.delete(tableStr, "_id=" + id_str, null);

        if(e == 0){
            Log.d("DatabaseError","DataBase delete : id is not match in table");
            return false;
        }
        return true;
    }

    public boolean delete(Table table) {
        String tableStr = getTableName(table);
        if(tableStr == null){
            Log.d("DatabaseError","DataBase delete : table name not found");
            return false;
        }

        int e = db.delete(tableStr, "1", null);

        if(e == 0){
            Log.d("DatabaseError","DataBase delete : id is not match in table");
            return false;
        }
        return true;
    }

    //endregion

    //region staticFunction

    public static String getTableName(Table table){
        String tableName;
        switch (table){
            case ACCOUNT:
                tableName = "account";
                break;
            case MONTH_PLAN:
                tableName = "monthPlan";
                break;
            case YEAR_PLAN:
                tableName = "yearPlan";
                break;
            default:
                tableName = null;
                break;
        }
        return tableName;
    }

    //endregion



}






class SQLiteDB extends SQLiteOpenHelper {



    public final String accountTable = "Account";
    public final String monthTable = "MonthPlan";
    public final String yearTable = "YearPlan";

    public SQLiteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d("DatabasePosition","DataBase constructor : finish");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabasePosition","DataBase Create : Begin");

        Log.d("DatabasePosition","DataBase Create : Create Account table");
        final String createAccount = "CREATE TABLE IF NOT EXISTS "
                + accountTable + "("
                + "_id" + " INTEGER PRIMARY KEY, "
                + "Name" + " TEXT NOT NULL, "
                + "Money" + " INTEGER NOT NULL,"
                + "Type" + " UNSIGNED INTEGER NOT NULL,"
                + "Time" + " TEXT NOT NULL,"
                + "Description" + " TEXT"
                + ")";
        db.execSQL(createAccount);

        Log.d("DatabasePosition","DataBase Create : Create MonthPlan table");
        final String createMonthPlan = "CREATE TABLE IF NOT EXISTS "
                + monthTable + "("
                + "_id" + " INTEGER PRIMARY KEY, "
                + "PlanYear" + " UNSIGNED INTEGER NOT NULL, "
                + "PlanMonth" + " UNSIGNED INTEGER NOT NULL,"
                + "Day" + " UNSIGNED INTEGER NOT NULL,"
                + "Description" + " TEXT"
                + ")";
        db.execSQL(createMonthPlan);

        Log.d("DatabasePosition","DataBase Create : Create YearPlan table");
        final String createYearPlan = "CREATE TABLE IF NOT EXISTS "
                + yearTable + "("
                + "_id" + " INTEGER PRIMARY KEY, "
                + "PlanYear" + " UNSIGNED INTEGER NOT NULL, "
                + "Month" + " UNSIGNED INTEGER NOT NULL,"
                + "Description" + " TEXT"
                + ")";
        db.execSQL(createYearPlan);


        Log.d("DatabasePosition","DataBase Create : Finish");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabasePosition","DataBase Upgrade : Begin");

        final String drop = "DROP TABLE " + accountTable;
        db.execSQL(drop);
        onCreate(db);

        Log.d("DatabasePosition","DataBase Upgrade : Finish");
    }
}