package com.gerli.handsomeboy.gerliUnit;

/**
 * Created by HandsomeBoy on 2016/12/4.
 */
public enum Info_type{
    EXPENSE,INCOME,CLASS,DAY,WEEK,MONTH,YEAR;

    public static String getInfoTypeName(Info_type info_type){
        String infoName;
        switch (info_type){
            case EXPENSE:
                infoName = "expense";
                break;
            case INCOME:
                infoName = "income";
                break;
            case CLASS:
                infoName = "class";
                break;
            case DAY:
                infoName = "day";
                break;
            case WEEK:
                infoName = "week";
                break;
            case MONTH:
                infoName = "month";
                break;
            case YEAR:
                infoName = "year";
                break;
            default:
                infoName = null;
                break;
        }
        return infoName;
    }

    public static boolean isExpense(Info_type type){
        return type == EXPENSE;
    }

    public static boolean isIncome(Info_type type){
        return type == INCOME;
    }

    public static boolean isClass(Info_type type){
        return type == CLASS;
    }

    public static boolean isDay(Info_type type){
        return type == DAY;
    }

    public static boolean isWeek(Info_type type){
        return type == WEEK;
    }

    public static boolean isMonth(Info_type type){
        return type == MONTH;
    }

    public static boolean isYear(Info_type type){
        return type == YEAR;
    }
}