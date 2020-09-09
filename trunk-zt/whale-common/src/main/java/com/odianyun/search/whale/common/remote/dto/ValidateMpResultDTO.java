package com.odianyun.search.whale.common.remote.dto;

/**
 * @author hs
 * @date 2018/9/4.
 */
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ValidateMpResultDTO implements Serializable {
    private static final long serialVersionUID = -5932469172695482453L;
    private Integer code;
    private Long mpId;
}