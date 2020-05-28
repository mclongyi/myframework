package com.longyi.stock.rebuild.sale;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author leiyi
 * @Description 租赁相关的对象信息
 * @date 2020/5/4
 */
@Data
@AllArgsConstructor
public class Rental {
    private Movie movie;
    private int daysRented;


    public double getCharge(){
        return movie.getCharge(daysRented);
    }



    public int getFrequentRenterPoints(){
        return this.getMovie().getFrequentRenterPoints(this.getDaysRented());
    }

}    
   