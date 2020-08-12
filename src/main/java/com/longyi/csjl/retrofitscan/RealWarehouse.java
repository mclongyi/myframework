package com.longyi.csjl.retrofitscan;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class RealWarehouse implements Serializable {


    private Long id;

    private String realWarehouseCode;

    private String realWarehouseOutCode;

    private String factoryCode;

    private String factoryName;

    private String realWarehouseName;

    private Integer realWarehouseType;

    private String shopCode;

    private Integer realWarehouseStatus;

    private Integer realWarehousePriority;

}
