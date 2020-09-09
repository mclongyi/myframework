package com.odianyun.search.whale.rest.action;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.frontier.global.exception.RemoteServiceException;
import com.odianyun.frontier.global.web.ApiBaseController;
import com.odianyun.frontier.global.web.JsonResult;
import com.odianyun.frontier.global.web.LoginContext;
import com.odianyun.search.whale.api.model.req.HistoryCleanRequest;
import com.odianyun.search.whale.api.service.HistoryClient;
import com.odianyun.search.whale.data.service.LyfHotWordService;
import com.odianyun.search.whale.data.service.impl.LyfHotWordServiceImpl;
import com.odianyun.search.whale.history.server.HistoryServer;
import com.odianyun.search.whale.index.api.model.req.HistoryType;
import com.odianyun.search.whale.rest.model.constant.SearchConstants;
import com.odianyun.search.whale.rest.model.vo.HistoryKeywordJsonObjectVO;
import com.odianyun.user.dto.framework.UserCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fu Yifan on 2017/8/16.
 */
@Controller
public class SearchWriteAction extends ApiBaseController {
    private static Logger log = Logger.getLogger(SearchWriteAction.class);

    @Autowired
    private HistoryServer historyServer;

    @Autowired
    private LyfHotWordService lyfHotWordService;

    /**
     * 清除历史记录
     *
     * @param historyKeywordJson
     * @param user
     * @return
     */
    @RequestMapping(value = "/cleanSearchHistory", method = RequestMethod.POST)
    public @ResponseBody
    JsonResult cleanSearchHistory(@RequestParam(required = false) String historyKeywordJson, @RequestParam(required = false) Long merchantId, @RequestParam(required = false) Integer historyType, @LoginContext(required = false) UserCache user) {
        Long companyId = SystemContext.getCompanyId();
        if (user == null || user.getId() == null) {
            returnError(null, "用户未登录");
        }
        Long userId = user.getId();
        if (merchantId == null && user != null && user.getStoreInfo() != null && user.getStoreInfo().getMerchantId() != null) {
            merchantId = user.getStoreInfo().getMerchantId();
        }
        HistoryKeywordJsonObjectVO historyKeyword = com.alibaba.fastjson.JSONObject.parseObject(historyKeywordJson, HistoryKeywordJsonObjectVO.class);
        List<String> historyKeywordList = new ArrayList<>();
        if (historyKeyword != null && historyKeyword.getHistoryKeywordList() != null) {
            historyKeywordList = historyKeyword.getHistoryKeywordList();
        }
        HistoryCleanRequest request = new HistoryCleanRequest(companyId.intValue(), userId.toString(), historyKeywordList);
        if (merchantId != null && merchantId != 0) {
            request.setType(HistoryType.MERCHANT);
            request.setMerchantId(merchantId);
        }
        if (historyType != null && historyType == 2) {
            request.setType(HistoryType.POINT);
        }
        try {
            HistoryClient.getHistoryService(SearchConstants.POOL_NAME).cleanSearchHistory(request);
        } catch (Exception e) {
            log.error("历史记录清除接口异常HistoryClient.getHistoryService.cleanSearchHistory:", e);
            throw new RemoteServiceException("清除历史记录接口异常");
        }
        return returnSuccess("清除历史记录成功");

    }

    /**
     * 重置缓存数据
     *
     * @param reCal 是否重算热词,1是,0不算
     * @return
     */
    @RequestMapping(value = "/reloadHotwordCache", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult reloadHotwordCache(@RequestParam(required = false, defaultValue = "0") Integer reCal) {
        return returnSuccess(lyfHotWordService.reloadHotwordCache(reCal));
    }
}
