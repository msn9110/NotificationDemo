package com.gerli.handsomeboy.gerliUnit;

/**
 * Created by HandsomeBoy on 2016/12/4.
 */
public enum AccountType{

    BREAKFAST(0),LUNCH(1),DINNER(2),SUPPER(3),DRINK(4),SNACK(5),
    CLOTHES(6),ACCESSORY(7),SHOES(8),
    RENT_FOR_HOUSE(9),DAILY_SUPPLIES(10),PAYMENT(11),
    TRANSPORT(12),FUEL(13),AUTOMOBILE(14),
    BOOK(15),STATIONERY(16),ART(17),
    ENTERTAINMENT(18),SHOPPING(19),INVEST(20),GIFTS(21),
    OTHERS(22);

    int value;

    AccountType(int value){
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static String getString(int type){
        AccountType accountType;
        switch (type){
            case 1:
                accountType = BREAKFAST;
                break;
            case 2:
                accountType = LUNCH;
                break;
            case 3:
                accountType = DINNER;
                break;
            case 4:
                accountType = SUPPER;
                break;
            case 5:
                accountType = DRINK;
                break;
            case 6:
                accountType = SNACK;
                break;
            case 7:
                accountType = CLOTHES;
                break;
            case 8:
                accountType = ACCESSORY;
                break;
            case 9:
                accountType = SHOES;
                break;
            case 10:
                accountType = RENT_FOR_HOUSE;
                break;
            case 11:
                accountType = DAILY_SUPPLIES;
                break;
            case 12:
                accountType = PAYMENT;
                break;
            case 13:
                accountType = TRANSPORT;
                break;
            case 14:
                accountType = FUEL;
                break;
            case 15:
                accountType = AUTOMOBILE;
                break;
            case 16:
                accountType = BOOK;
                break;
            case 17:
                accountType = STATIONERY;
                break;
            case 18:
                accountType = ART;
                break;
            case 19:
                accountType = ENTERTAINMENT;
                break;
            case 20:
                accountType = SHOPPING;
                break;
            case 21:
                accountType = INVEST;
                break;
            case 22:
                accountType = GIFTS;
                break;
            case 23:
                accountType = OTHERS;
                break;
            default:
                accountType = null;
        }
        return AccountType.getString(accountType);
    }

    public static String getString(AccountType type){
        String typeName;
        switch (type) {
            case BREAKFAST:
                typeName = "早餐";
                break;
            case LUNCH:
                typeName = "午餐";
                break;
            case DINNER:
                typeName = "晚餐";
                break;
            case SUPPER:
                typeName = "宵夜";
                break;
            case DRINK:
                typeName = "飲料";
                break;
            case SNACK:
                typeName = "點心";
                break;
            case CLOTHES:
                typeName = "衣服";
                break;
            case ACCESSORY:
                typeName = "配飾";
                break;
            case SHOES:
                typeName = "鞋子";
                break;
            case RENT_FOR_HOUSE:
                typeName = "房租";
                break;
            case DAILY_SUPPLIES:
                typeName = "日常用品";
                break;
            case PAYMENT:
                typeName = "繳費";
                break;
            case TRANSPORT:
                typeName = "交通";
                break;
            case FUEL:
                typeName = "燃料";
                break;
            case AUTOMOBILE:
                typeName = "汽機車";
                break;
            case BOOK:
                typeName = "書籍";
                break;
            case STATIONERY:
                typeName = "文具";
                break;
            case ART:
                typeName = "美術用具";
                break;
            case ENTERTAINMENT:
                typeName = "娛樂";
                break;
            case SHOPPING:
                typeName = "購物";
                break;
            case INVEST:
                typeName = "投資";
                break;
            case GIFTS:
                typeName = "送禮";
                break;
            case OTHERS:
                typeName = "其他";
                break;
            default:
                typeName = null;
        }
        return typeName;
    }

}