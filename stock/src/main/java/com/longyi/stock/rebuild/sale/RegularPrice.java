package com.longyi.stock.rebuild.sale;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/4
 */
public class RegularPrice extends Price {
    @Override
    int getPriceCode() {
        return Movie.REGULAR;
    }
}
   