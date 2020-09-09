package com.odianyun.search.whale.api.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hs
 * @date 2018/9/17.
 */
@Data
@NoArgsConstructor
public class HotWordRequest {

    /**
     * 需要排除的词汇
     */
    private List<String> distinct = new ArrayList<>();
    /**
     * 需要的热词数量,不超过50,默认5个
     */
    private Integer num=5;

}
