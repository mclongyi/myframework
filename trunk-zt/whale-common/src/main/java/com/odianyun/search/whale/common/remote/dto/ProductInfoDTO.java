package com.odianyun.search.whale.common.remote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hs
 * @date 2018/8/27.
 */
@Data
@NoArgsConstructor
public class ProductInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal price;
    private Long mpId;
    private List<ProductInfoDTO> childs = new ArrayList();

    public List<ProductInfoDTO> getChilds() {
        return this.childs;
    }

    public void setChilds(List<ProductInfoDTO> childs) {
        this.childs = childs;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal bigDecimal) {
        this.price = bigDecimal;
    }

    public Long getMpId() {
        return this.mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }
}