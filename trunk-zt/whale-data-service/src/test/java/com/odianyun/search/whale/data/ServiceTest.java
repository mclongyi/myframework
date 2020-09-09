package com.odianyun.search.whale.data;

import com.odianyun.search.whale.api.model.req.HotWordRequest;
import com.odianyun.search.whale.api.model.resp.HotWordResponse;
import com.odianyun.search.whale.data.dao.SearchWordFrequencyDao;
import com.odianyun.search.whale.data.model.hotword.SearchWordFrequencyVO;
import com.odianyun.search.whale.data.model.hotword.WordStatus;
import com.odianyun.search.whale.data.service.LyfHotWordService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.odianyun.search.whale.data.service.impl.CategoryServiceImpl;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ServiceTest extends TestCase {

    @Test
    public void test() throws Exception {
        System.setProperty("global.config.path", "/data/env");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-common.xml");

        CategoryServiceImpl categoryService = (CategoryServiceImpl) applicationContext.getBean("categoryService");
        categoryService.tryReload(2);


    }

    @Test
    public void testSearchDB() {
        System.setProperty("global.config.path", "/Users/hs/Desktop/lyf/env-edu");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-common.xml");
        System.out.println("==========================================");

        SearchWordFrequencyDao searchWordFrequencyDao = (SearchWordFrequencyDao) applicationContext.getBean("searchWordFrequencyDao");
        SearchWordFrequencyVO vo = new SearchWordFrequencyVO();
        vo.setDate("2018-09-11");
        vo.setKeyword("坚果派");
        vo.setFrequency(30);
        searchWordFrequencyDao.insertKeyword(vo);

        Long count = searchWordFrequencyDao.countByDate(vo);

        vo.setWordStatus(null);
        List<SearchWordFrequencyVO> v = searchWordFrequencyDao.selectByDateAndStatus(vo);
        for (SearchWordFrequencyVO frequencyVO : v) {
            frequencyVO.setWordStatus(WordStatus.HOTWORD.getCode());
            frequencyVO.setFrontKeyword(frequencyVO.getKeyword() + "_front");
            frequencyVO.setRank(1);
            frequencyVO.setFrequency(10);
            frequencyVO.setCoolingCoefficient(1.0d);
            frequencyVO.setTomorrowFrequency(15);
        }
        searchWordFrequencyDao.batchUpdateStatus(v);
        System.out.println("==========================================");
    }

    @Test
    public void testHotword() {
        System.setProperty("global.config.path", "/Users/hs/Desktop/lyf/env-edu");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-common.xml");
        System.out.println("==========================================");
        LyfHotWordService lyfHotWordService = (LyfHotWordService) applicationContext.getBean("lyfHotWordService");
//        Set<String> strings = lyfHotWordService.calculateHotWordWithTx(11);
//        System.out.println(strings);
//
        HotWordRequest request = new HotWordRequest();
        request.setNum(50);
//        request.setDistinct(Arrays.asList("坚果","豆子"));
//        HotWordResponse hotWordDistinct = lyfHotWordService.getHotWordDistinct(request);
//        HotWordResponse hotWordDistinctRandom = lyfHotWordService.getHotWordDistinctRandom(request);
//        System.out.println(hotWordDistinct);
//        System.out.println(hotWordDistinctRandom);

    }
}
