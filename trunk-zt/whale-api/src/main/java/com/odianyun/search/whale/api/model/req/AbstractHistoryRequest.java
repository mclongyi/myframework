package com.odianyun.search.whale.api.model.req;

import com.odianyun.search.whale.index.api.model.req.HistoryType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Fu Yifan on 2017/8/15.
 */
@Data
@NoArgsConstructor
public class AbstractHistoryRequest extends AbstractSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public AbstractHistoryRequest(Integer companyId) {
        super(companyId);
    }

    //历史记录类型
    private HistoryType type;

    //店铺内搜索历史
    protected Long merchantId;
}
