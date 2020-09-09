package com.odianyun.search.whale.common.remote.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hs
 * @date 2018/8/27.
 */

public class MPPromotionListInputDTO implements Serializable {
    private static final long serialVersionUID = 3762544818132550525L;
    private List<MPPromotionInputDTO> mpPromotionListInput = new ArrayList();
    private boolean baseInfoFlag;
    private Integer platform;
    private boolean limitInfoFlag;
    private boolean patchAndCutFlag = false;

    public MPPromotionListInputDTO() {
    }

    public List<MPPromotionInputDTO> getMpPromotionListInput() {
        return this.mpPromotionListInput;
    }

    public void setMpPromotionListInput(List<MPPromotionInputDTO> mpPromotionListInput) {
        this.mpPromotionListInput = mpPromotionListInput;
    }

    public boolean isBaseInfoFlag() {
        return this.baseInfoFlag;
    }

    public void setBaseInfoFlag(boolean baseInfoFlag) {
        this.baseInfoFlag = baseInfoFlag;
    }

    public Integer getPlatform() {
        return this.platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public boolean isLimitInfoFlag() {
        return this.limitInfoFlag;
    }

    public void setLimitInfoFlag(boolean limitInfoFlag) {
        this.limitInfoFlag = limitInfoFlag;
    }

    public boolean isPatchAndCutFlag() {
        return this.patchAndCutFlag;
    }

    public void setPatchAndCutFlag(boolean patchAndCutFlag) {
        this.patchAndCutFlag = patchAndCutFlag;
    }
}
