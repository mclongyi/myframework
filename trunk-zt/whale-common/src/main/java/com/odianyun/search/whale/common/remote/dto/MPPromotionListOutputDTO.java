package com.odianyun.search.whale.common.remote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author hs
 * @date 2018/8/27.
 */
@Data
@NoArgsConstructor
public class MPPromotionListOutputDTO implements Serializable {
    private static final long serialVersionUID = -3225711886911470758L;
    private Map<Long, MPPromotionOutputDTO> mpPromotionMapOutput;
}

