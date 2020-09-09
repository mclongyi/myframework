package com.odianyun.search.whale.common.remote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hs
 * @date 2018/9/4.
 */
@Data
@NoArgsConstructor
public class MpCommission implements Serializable {
    private Long mpId;
    private BigDecimal totalMoney;

    public MpCommission(Long mpId, BigDecimal totalMoney) {
        this.mpId = mpId;
        this.totalMoney = totalMoney;
    }

    public Long getMpId() {
        return this.mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public BigDecimal getTotalMoney() {
        return this.totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "MpCommission [mpId=" + this.mpId + ", totalMoney=" + this.totalMoney + "]";
    }
}
