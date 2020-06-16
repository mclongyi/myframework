package com.longyi.stock.rebuild.sale;

import lombok.Data;

/**
 * @author leiyi
 * @Description 电影相关的对象属性
 * @date 2020/5/4
 */
@Data
public class Movie {
    //儿童类型
    public static final int CHILDRENS = 2;
    //常规类型
    public static final int REGULAR = 0;
    //最新的
    public static final int NEW_RELEASE = 1;

    private int priceCode;

    private String title;

    private Price price;


    public Movie(String title, int priceCode) {
        this.title = title;
        setPriceCode(priceCode);
    }

    public void setPriceCode(int arg) {
        switch (arg) {
            case CHILDRENS:
                price = new ChildrenPrice();
                break;
            case REGULAR:
                price = new RegularPrice();
                break;
            case NEW_RELEASE:
                price = new RegularPrice();
                break;
            default:
                break;
        }
    }


    public double getCharge(int daysRented) {
        double thisAmount = 0;
        switch (this.getPriceCode()) {
            case REGULAR:
                thisAmount += 2;
                if (daysRented > 2) {
                    thisAmount += (daysRented - 2) * 1.5;
                }
                break;
            case NEW_RELEASE:
                thisAmount += daysRented * 3;
                break;
            case CHILDRENS:
                thisAmount += 1.5;
                if (daysRented > 3) {
                    thisAmount += (daysRented - 3) * 1.5;
                }
                break;
            default:
                break;
        }
        return thisAmount;
    }

    public int getFrequentRenterPoints(int daysRented) {
        if (this.getPriceCode() == NEW_RELEASE
                && daysRented > 1) {
            return 1;
        }
        return 2;
    }


}    
   