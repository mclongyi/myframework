package com.odianyun.search.whale.common.remote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author hs
 * @date 2018/9/4.
 */
@Data
@NoArgsConstructor
public class CommissionType implements Serializable {
    private Long userId;
    private Long typeId;
    private CommissionOrderTypeEnum orderType;
    private List<Long> mpIds;
    private Long distributorId;

    public Long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getDistributorId() {
        return this.distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CommissionOrderTypeEnum getOrderType() {
        return this.orderType;
    }

    public void setOrderType(CommissionOrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public List<Long> getMpIds() {
        return this.mpIds;
    }

    public void setMpIds(List<Long> mpIds) {
        this.mpIds = mpIds;
    }
}
