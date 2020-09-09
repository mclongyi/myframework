package com.odianyun.search.whale.rest.action;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.frontier.global.utils.BeanMapper;
import com.odianyun.frontier.global.utils.Collections3;
import com.odianyun.frontier.global.web.ApiBaseController;
import com.odianyun.frontier.global.web.JsonResult;
import com.odianyun.frontier.global.web.LoginContext;
import com.odianyun.search.whale.api.model.SuggestType;
import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.HistoryResponse;
import com.odianyun.search.whale.api.model.resp.SuggestResponse;
import com.odianyun.search.whale.api.model.resp.SuggestResult;
import com.odianyun.search.whale.history.server.HistoryServer;
import com.odianyun.search.whale.index.api.model.req.HistoryResult;
import com.odianyun.search.whale.index.api.model.req.HistoryType;
import com.odianyun.search.whale.rest.model.vo.SearchKeywordVO;
import com.odianyun.search.whale.rest.model.vo.SuggestVO;
import com.odianyun.search.whale.suggest.server.SuggestServer;
import com.odianyun.user.dto.framework.UserCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fu Yifan on 2017/8/16.
 */
@Controller
public class SearchReadAction extends ApiBaseController {
    private static Logger log = Logger.getLogger(SearchReadAction.class);

    @Autowired
    private HistoryServer historyServer;
    @Autowired
    private SuggestServer suggestServer;

    /**
     * 下拉自动提示词
     * @param keyword
     * @param merchantId
     * @return
     */
    @RequestMapping(value = "/auto")
    public @ResponseBody JsonResult getAutoCompleteKeyword(@RequestParam(required = true) String keyword, @RequestParam(required = false) Long merchantId, @RequestParam(required = false)Integer suggestType, @LoginContext(required = false) UserCache user) {
        Long companyId = SystemContext.getCompanyId();
        if(merchantId == null && user != null && user.getStoreInfo() != null && user.getStoreInfo().getMerchantId() != null){
            merchantId = user.getStoreInfo().getMerchantId();
        }
        List<SuggestVO> resultList = new ArrayList<>();
        try {
            SuggestRequest request = new SuggestRequest(companyId.intValue(),keyword);
            if (merchantId != null && merchantId != 0) {
                request.setMerchantId(merchantId);
            }
            if (suggestType != null && suggestType == 2) {
                request.setType(SuggestType.POINT);
            }
            SuggestResponse response = suggestServer.autoComplete(request);
            if (response != null) {
                List<SuggestResult> suggestResultList = response.getSuggestResult();
                if (Collections3.isNotEmpty(suggestResultList)) {
                    for (SuggestResult suggestResult : suggestResultList) {
                        SuggestVO suggest = BeanMapper.map(suggestResult, SuggestVO.class);
                        resultList.add(suggest);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取下拉提示词异常", e);
        }
        return returnSuccess(resultList);
    }

    /**
     * 搜索历史记录
     * @param count
     * @param merchantId
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/searchHistoryList")
    public @ResponseBody JsonResult getHistoryList(@RequestParam(required = false) Integer count, @RequestParam(required = false) Long merchantId, @RequestParam(required = false) Integer historyType, @LoginContext(required = true) UserCache user) throws Exception{
        Long companyId = SystemContext.getCompanyId();
        if (user == null || user.getId() == null) {
            returnError(null, "用户未登录");
        }
        Long userId = user.getId();
        if(merchantId == null && user != null && user.getStoreInfo() != null && user.getStoreInfo().getMerchantId() != null){
            merchantId = user.getStoreInfo().getMerchantId();
        }
        List<SearchKeywordVO> searchKeywordList = new ArrayList<SearchKeywordVO>();
        try {
            HistoryReadRequest request = new HistoryReadRequest(companyId.intValue(), userId.toString(), count);
            if (merchantId != null && merchantId != 0) {
                request.setType(HistoryType.MERCHANT);
                request.setMerchantId(merchantId);
            }
            if (historyType != null && historyType == 2) {
                request.setType(HistoryType.POINT);
            }
            HistoryResponse response = historyServer.autoSearchHistory(request);
            if (response != null && response.getHistoryResult() != null && response.getHistoryResult().size() > 0) {
                for (HistoryResult historyResult : response.getHistoryResult()) {
                    SearchKeywordVO searchKeyword = new SearchKeywordVO();
                    searchKeyword.setKeyword(historyResult.getWord());
                    searchKeyword.setType(historyResult.getType().getCode());
                    searchKeywordList.add(searchKeyword);
                }
            }
        } catch (Exception e) {
            log.error("获取用户搜索历史记录异常", e);
            returnSuccess(new ArrayList<>());
        }
        return returnSuccess(searchKeywordList);
    }


}
