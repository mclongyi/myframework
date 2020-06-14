package com.longyi.stock.rebuild.sale;

import lombok.Data;

import java.util.Enumeration;
import java.util.Vector;

/**
 * @author leiyi
 * @Description 顾客相关的属性信息
 * @date 2020/5/4
 */

@Data
public class Customer {
    private String name;
    private Vector rentals=new Vector();

    public String statement(){
        Enumeration elements = rentals.elements();
        String result="Rental Record for "+getName()+"\n";
        while (elements.hasMoreElements()){
            Rental rental=(Rental)elements.nextElement();
            double thisAmount = rental.getCharge();
            result+="\t"+rental.getMovie().getTitle()+"\t"+
                    rental.getCharge() +"\n";
        }
        result+="Amount owed is"+ getTotalAmount() +"\n"
                +"You earned "+ getFrequentRenterPoints() +"frequent renter points ";
        return result;
    }

    private int getFrequentRenterPoints(){
        int frequentRenterPoints=0;
        Enumeration elements = rentals.elements();
        while (elements.hasMoreElements()){
            Rental rental=(Rental)elements.nextElement();
            frequentRenterPoints+=rental.getFrequentRenterPoints();
        }
        return frequentRenterPoints;
    }


    private double getTotalAmount(){
        double totalAmount=0;
        Enumeration elements = rentals.elements();
        while (elements.hasMoreElements()){
            Rental rental=(Rental)elements.nextElement();
            totalAmount+=rental.getCharge();
        }
        return totalAmount;
    }




}
   