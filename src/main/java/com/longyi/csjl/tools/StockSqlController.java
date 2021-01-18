package com.longyi.csjl.tools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

/**
 * @author ly
 * @Description TODO
 * @date 2020/9/3 10:22
 */
@Slf4j
@RestController
@RequestMapping("/stock/v1/sql")
@Api(tags={"SQL执行器"})
public class StockSqlController {

    @Autowired
    private  StockSqlService stockSqlService;



    @ApiOperation(value = "SQL查询器", nickname = "select", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public LinkedHashMap select(@RequestParam("appSecret")String appSecret, @RequestParam("sql")String sql){
    return stockSqlService.select(sql);
    }

    @ApiOperation(value = "Insert执行器", nickname = "insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public Integer insert(@RequestParam("appSecret")String appSecret, @RequestParam("sql")String sql){
        return stockSqlService.insert(sql);
    }

    @ApiOperation(value = "update执行器", nickname = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Integer update(@RequestParam("appSecret")String appSecret, @RequestParam("sql")String sql){
        return stockSqlService.update(sql);
    }


    @ApiOperation(value = "delete执行器", nickname = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Integer delete(@RequestParam("appSecret")String appSecret, @RequestParam("sql")String sql){
        return stockSqlService.delete(sql);
    }

}    
   