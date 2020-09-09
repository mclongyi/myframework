package com.odianyun.search.whale.suggest.server;

//import java.util.ArrayList;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.HotSearchRequest;
import com.odianyun.search.whale.api.model.req.HotWordRequest;
import com.odianyun.search.whale.api.model.req.SpellCheckerRequest;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.api.model.resp.HotWordResponse;
import com.odianyun.search.whale.api.model.resp.SpellCheckerResponse;
import com.odianyun.search.whale.api.model.resp.SuggestResponse;
import com.odianyun.search.whale.api.service.SuggestService;
import com.odianyun.search.whale.data.service.LyfHotWordService;
import com.odianyun.search.whale.suggest.handler.AreaSuggestHandler;
import com.odianyun.search.whale.suggest.handler.SpellCheckerHandler;
import com.odianyun.search.whale.suggest.handler.SuggestHandler;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author yuqian
 */
public class SuggestServer implements SuggestService {

    static Logger logger = Logger.getLogger(SuggestServer.class);

    @Autowired
    SuggestHandler suggestHandler;

    @Autowired
    AreaSuggestHandler areaSuggestHandler;

    @Autowired
    SpellCheckerHandler spellchecker;

    @Autowired
    private LyfHotWordService lyfHotWordService;


    @Override
    public SuggestResponse autoComplete(SuggestRequest request) throws SearchException {
        return suggestHandler.handler(request);
    }

    @Override
    public List<String> recommendWordsWithZero(SuggestRequest request) throws SearchException {
        return null;
    }

    @Override
    public AreaSuggestResponse areaAutoComplete(SuggestRequest request) throws SearchException {
        return areaSuggestHandler.handler(request);
    }

    @Override
    public SpellCheckerResponse spellcheck(SpellCheckerRequest request) throws SearchException {
        return this.spellchecker.handle(request);
    }

    //----------------------标准化改造---------------------------------

    @Override
    public OutputDTO<SuggestResponse> autoCompleteStandard(InputDTO<SuggestRequest> inputDTO) throws SearchException {
        logger.info("soa 调用开始......" + inputDTO.toString());
        SuggestResponse suggestResponse = null;
        try {
            suggestResponse = autoComplete(inputDTO.getData());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("autoCompleteStandard调用失败," + inputDTO.toString() + e);
        }
        logger.info("soa 调用结束......" + suggestResponse);
        return SoaUtil.resultSucess(suggestResponse);
    }

    @Override
    public OutputDTO<AreaSuggestResponse> areaAutoCompleteStandard(InputDTO<SuggestRequest> inputDTO) throws SearchException {
        logger.info("soa 调用开始......" + inputDTO.toString());
        AreaSuggestResponse areaSuggestResponse = null;
        try {
            areaSuggestResponse = areaAutoComplete(inputDTO.getData());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("areaAutoCompleteStandard调用失败," + inputDTO.toString() + e);
        }
        logger.info("soa 调用结束......" + areaSuggestResponse);
        return SoaUtil.resultSucess(areaSuggestResponse);
    }


    @Override
    public OutputDTO<SpellCheckerResponse> spellcheckStandard(InputDTO<SpellCheckerRequest> inputDTO) throws SearchException {
        logger.info("soa 调用开始......" + inputDTO.toString());
        SpellCheckerResponse spellcheck = null;
        try {
            spellcheck = spellcheck(inputDTO.getData());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("autoCompleteStandard调用失败," + inputDTO.toString() + e);
        }
        logger.info("soa 调用结束......" + spellcheck);
        return SoaUtil.resultSucess(spellcheck);
    }

    @Override
    public OutputDTO<HotWordResponse> getHotwordDistinct(InputDTO<HotWordRequest> inputDTO) throws SearchException {
        logger.info("soa 调用开始......" + inputDTO.getData());
        HotWordResponse hotWordResponse = null;
        try {
            HotWordRequest request = inputDTO.getData();
            hotWordResponse = lyfHotWordService.getHotWordDistinct(request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getHotwordDistinct调用失败," + inputDTO.toString() + e);
        }
        logger.info("soa 调用结束......" + hotWordResponse);
        return SoaUtil.resultSucess(hotWordResponse);
    }

}
