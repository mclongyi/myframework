package com.longyi.csjl.domain.dto;

import lombok.Data;

/**
 * 实仓与wms配置
 */
@Data
public class RealWarehouseWmsConfigDO {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 实体仓库id
     */
    private Long realWarehouseId;

    /**
     * 实体仓库编码
     */
    private String realWarehouseCode;

    /**
     * wms 编码
     */
    private Integer wmsCode;

    /**
     * wms名称
     */
    private String wmsName;
}
