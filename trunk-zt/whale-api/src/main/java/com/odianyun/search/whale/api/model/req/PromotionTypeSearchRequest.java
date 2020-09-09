package com.odianyun.search.whale.api.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by fishcus on 16/11/13.
 */
@Data
@NoArgsConstructor
public class PromotionTypeSearchRequest implements Serializable {

    private Long brandId;

    private Long companyId;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
