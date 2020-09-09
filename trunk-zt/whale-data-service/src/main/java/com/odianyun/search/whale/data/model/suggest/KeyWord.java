package com.odianyun.search.whale.data.model.suggest;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fishcus on 17/3/1.
 */
public class KeyWord {

    private String keyword;

    private Long companyId;

    private Set<Long> merchantIdSet = new HashSet<>();

    private Integer frequency = 0;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Set<Long> getMerchantIdSet() {
        return merchantIdSet;
    }

    public void setMerchantIdSet(Set<Long> merchantIdSet) {
        this.merchantIdSet = merchantIdSet;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyWord keyWord = (KeyWord) o;

        if (keyword != null ? !keyword.equals(keyWord.keyword) : keyWord.keyword != null) return false;
        return !(companyId != null ? !companyId.equals(keyWord.companyId) : keyWord.companyId != null);

    }

    @Override
    public int hashCode() {
        int result = keyword != null ? keyword.hashCode() : 0;
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        return result;
    }


}
