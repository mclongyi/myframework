package com.longyi.csjl.domain.resporitory;

import com.longyi.csjl.domain.dto.RealWarehouseWmsConfigDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/21 17:01
 */
public interface RealWarehouseWmsConfigMapper {

    /**
     * 查询配置信息
     * @param id
     * @return
     */
    List<RealWarehouseWmsConfigDO> getRealWarehouseWmsConfigByVMId(@Param("id") Long id);



}
