package com.odianyun.search.whale.data.model;

import java.util.Date;

/**
 * Created by admin on 2016/12/1.
 */
public class MerProScript {
    private Long mpId;//mpid
    private Long superscriptId;//角标id
    private Date validFrom;//有效期开始
    private Date validTo;  //有效期结束

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public Long getSuperscriptId() {
        return superscriptId;
    }

    public void setSuperscriptId(Long superscriptId) {
        this.superscriptId = superscriptId;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }


}
