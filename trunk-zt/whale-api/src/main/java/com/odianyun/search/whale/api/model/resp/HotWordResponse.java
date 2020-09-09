package com.odianyun.search.whale.api.model.resp;

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
public class HotWordResponse {

    private List<String> hotwordList = new ArrayList<>();
}
