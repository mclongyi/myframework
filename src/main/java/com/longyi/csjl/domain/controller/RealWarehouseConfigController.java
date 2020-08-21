package com.longyi.csjl.domain.controller;

import com.longyi.csjl.domain.dto.RealWarehouseWmsConfigDO;
import com.longyi.csjl.domain.service.RealWarehouseConfigService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/21 17:10
 */
@RestController
@RequestMapping("test")
public class RealWarehouseConfigController {

    private final RealWarehouseConfigService realWarehouseConfigService;

    public RealWarehouseConfigController(RealWarehouseConfigService realWarehouseConfigService) {
        this.realWarehouseConfigService = realWarehouseConfigService;
    }

    @ApiOperation(value = "查询实仓配置信息", nickname = "getById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PostMapping("/getById/{id}")
    public List<RealWarehouseWmsConfigDO> getById(@PathVariable("id")Long id){
        return realWarehouseConfigService.getById(id);
    }


}    
   