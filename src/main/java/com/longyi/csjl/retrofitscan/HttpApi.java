package com.longyi.csjl.retrofitscan;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * @author ly
 * @Description retrofitscan http API接口定义
 * @date 2020/8/12 9:36
 */
@RetrofitClient(baseUrl = "http://127.0.0.1:8083/")
public interface HttpApi {
    /**
     * 查询接口
     * @param factoryCode
     * @param type
     * @return
     */
    @POST("stock/v1/stockQuery/queryRealWarehouseByFactoryCodeAndRealWarehouseType")
    Response queryRealWarehouseByFactoryCodeAndRealWarehouseType(@Query("factoryCode") String factoryCode, @Query("type") Integer type);

}
