package com.longyi.csjl.domain.service;

import com.longyi.csjl.domain.dto.RealWarehouseWmsConfigDO;
import com.longyi.csjl.domain.resporitory.RealWarehouseWmsConfigMapper;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/21 17:06
 */
@Service
public class RealWarehouseConfigService {
    @Autowired
    private RealWarehouseWmsConfigMapper realWarehouseWmsConfigMapper;


    public List<RealWarehouseWmsConfigDO> getById(Long id){
        return realWarehouseWmsConfigMapper.getRealWarehouseWmsConfigByVMId(id);
    }
}    
   