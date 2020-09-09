package com.odianyun.search.whale.data.service.impl;

import com.odianyun.cache.CacheProxy;
import com.odianyun.search.whale.api.model.req.HotWordRequest;
import com.odianyun.search.whale.api.model.resp.HotWordResponse;
import com.odianyun.search.whale.common.cache.ocache.CacheBuilder;
import com.odianyun.search.whale.common.util.RedisUtil;
import com.odianyun.search.whale.data.common.util.LyfDateUtilS;
import com.odianyun.search.whale.data.dao.SearchWordFrequencyDao;
import com.odianyun.search.whale.data.model.hotword.SearchWordFrequencyVO;
import com.odianyun.search.whale.data.model.hotword.WordStatus;
import com.odianyun.search.whale.data.service.LyfHotWordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Soundbank;
import java.util.*;

/**
 * @author hs
 * @date 2018/9/14.
 */
//@Component("lyfHotWordService")
public class LyfHotWordServiceImpl implements LyfHotWordService {


    Logger logger = Logger.getLogger(getClass());


    static final int BATCH_NUM = 500;
    //头一天无数据,只有当前搜索量至少超过THRESHOLD_NUM次才能够进入热搜榜
    static final int THRESHOLD_NUM = 150;
    //取排名前50个词为热词
    static final int RANK_NUM = 50;
    //缓存时间:10分
    static final int CACHE_TIME_MILLS = 10;

    static final String HOTWORD_CACHE_KEY_LIST = "com.odianyun.search.whale.data.service.impl.LyfHotWordServiceImpl.calculateHotWordWithTx.hotword.list";

    private CacheProxy cacheProxy;

    public LyfHotWordServiceImpl(){
        try{
            cacheProxy= CacheBuilder.buildCache();
            logger.warn("初始化cacheProxy="+cacheProxy);
        }catch(Throwable e){
            logger.error(e.getMessage(),e);
        }
    }

    @Autowired
    private SearchWordFrequencyDao searchWordFrequencyDao;

    @Override
    public HotWordResponse getHotWordDistinctRandom(HotWordRequest request) {
        HotWordResponse response = new HotWordResponse();
        try {
            List<String> result = new ArrayList<>();

            List<String> distinct = request.getDistinct();
            List<String> list = getHotword();
            list.removeAll(distinct);

            //随机获取坐标  数目
            if (request.getNum() >= list.size()) {
                result = list;
            } else {
                Set<Integer> rIndexSet = new HashSet();
                Random random = new Random();
                int loop = 0;
                while (rIndexSet.size() < request.getNum()) {
                    int r = random.nextInt(list.size());
                    rIndexSet.add(r);

                    loop++;
                    if (loop == 100000) {
                        logger.warn("未能随机到足够的坐标" + request + "," + rIndexSet + "," + list);
                        break;
                    }
                }
                for (Integer i : rIndexSet) {
                    result.add(list.get(i));
                }
            }
            response.setHotwordList(result);
        } catch (Exception e) {
            logger.error("获取热词失败," + request, e);
        }
        return response;
    }

    @Override
    public HotWordResponse getHotWordDistinct(HotWordRequest request) {
        HotWordResponse response = new HotWordResponse();
        try {
            List<String> result = new ArrayList<>();
            List<String> distinct = request.getDistinct();
            List<String> list = getHotword();
            list.removeAll(distinct);
            for (String k : list) {
                result.add(k);
                if (result.size() == request.getNum()) {
                    break;
                }
            }
            response.setHotwordList(result);
        } catch (Exception e) {
            logger.error("获取热词失败," + request, e);
        }
        logger.warn("获取热词" + request + "," + response);
        return response;
    }

    /**
     * 获取热词,
     * 1.缓存中没有就重新查
     * 2.数据库重新计算,
     *
     * @return
     */
    private List<String> getHotword() {
        Set<String> list = (Set<String>) cacheProxy.get(HOTWORD_CACHE_KEY_LIST);
        List<String>  res = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            try {
                //查数据库
                String yesterdayDate = LyfDateUtilS.getDate2String(LyfDateUtilS.addDays(new Date(), -1), LyfDateUtilS.PATTERN_YMD);
                SearchWordFrequencyVO vo = new SearchWordFrequencyVO();
                vo.setDate(yesterdayDate);
                vo.setWordStatus(WordStatus.HOTWORD.getCode());
                vo.setLimit(null);
                vo.setOffset(null);
                List<SearchWordFrequencyVO> searchWordFrequencyVOS = searchWordFrequencyDao.selectByDateAndStatus(vo);
                if (searchWordFrequencyVOS==null || searchWordFrequencyVOS.isEmpty()){
                    //如果是0-2点,bi没有推数据过来,取头一天的数据
                    String today = LyfDateUtilS.getMillis2String(System.currentTimeMillis(), LyfDateUtilS.PATTERN_YMD);
                    if (LyfDateUtilS.checkNowIsInTime(today+" 00:00:00",today+" 02:10:00")){
                        String beforeYesterDay = LyfDateUtilS.getDate2String(LyfDateUtilS.addDays(new Date(), -2), LyfDateUtilS.PATTERN_YMD);
                        vo.setDate(beforeYesterDay);
                        searchWordFrequencyVOS = searchWordFrequencyDao.selectByDateAndStatus(vo);
                    }
                }

                Set<String> hotWords = new HashSet<>();
                if (searchWordFrequencyVOS==null || searchWordFrequencyVOS.isEmpty()){
                    logger.warn("热词不存在,重新计算热词,"+yesterdayDate);
                    hotWords = calculateHotWordWithTx(RANK_NUM);
                }else {
                    logger.warn("热词存在,加入热词.");
                    for (SearchWordFrequencyVO frequencyVO : searchWordFrequencyVOS) {
                        hotWords.add(frequencyVO.getKeyword());
                    }
                }
                if (!hotWords.isEmpty()) {
                    cacheProxy.put(HOTWORD_CACHE_KEY_LIST, hotWords, CACHE_TIME_MILLS);
                }
                res.addAll(hotWords);
            } catch (Exception e) {
                logger.error("计算热词失败," + list, e);
            }
        }else {
            res.addAll(list);
        }
        return res;
    }


    /**
     * 计算热词,返回计算得到的排名前N的词汇
     */
    @Transactional
    @Override
    public Set<String> calculateHotWordWithTx(Integer num) {
        logger.info("----计算热词 start----------");
        Set<String> hotWord = new HashSet<>();
        try {
            //获取昨天和前天
            String yesterdayDate = LyfDateUtilS.getDate2String(LyfDateUtilS.addDays(new Date(), -1), LyfDateUtilS.PATTERN_YMD);
            String beforeYesterdayDate = LyfDateUtilS.getDate2String(LyfDateUtilS.addDays(new Date(), -2), LyfDateUtilS.PATTERN_YMD);

            SearchWordFrequencyVO selectVo = new SearchWordFrequencyVO();
            selectVo.setDate(yesterdayDate);
            selectVo.setWordStatus(null);
            List<SearchWordFrequencyVO> yesterdayList = searchWordFrequencyDao.selectByDateAndStatus(selectVo);
            selectVo.setDate(beforeYesterdayDate);
            List<SearchWordFrequencyVO> beforeYesterdayList = searchWordFrequencyDao.selectByDateAndStatus(selectVo);
            Map<String, Integer> kf = tran2Map(beforeYesterdayList);
            for (SearchWordFrequencyVO vo : yesterdayList) {
                Integer pCount = kf.get(vo.getKeyword());
                pCount = pCount == null ? 0 : pCount;
                Double coefficient = calCoolingCoefficient(vo.getFrequency(), pCount, 24);
                Integer tomorrowFrequency = calTomorrowFrequency(coefficient, vo.getFrequency(), 24);
                vo.setPreFrequency(pCount);
                vo.setCoolingCoefficient(coefficient);
                vo.setTomorrowFrequency(tomorrowFrequency);
                vo.setFrontKeyword(tran2FrontKeyword(vo.getKeyword()));
            }
            //按照预测搜索量进行排序:按照降序排序,名次预测搜索量越大越靠前
            Collections.sort(yesterdayList, new Comparator<SearchWordFrequencyVO>() {
                @Override
                public int compare(SearchWordFrequencyVO o1, SearchWordFrequencyVO o2) {
                    if (o1.getTomorrowFrequency() > o2.getTomorrowFrequency()) {
                        return -1;
                    } else if (o1.getTomorrowFrequency() < o2.getTomorrowFrequency()) {
                        return 1;
                    }
                    return 0;
                }
            });
            for (int i = 0; i < yesterdayList.size(); i++) {
                SearchWordFrequencyVO vo = yesterdayList.get(i);
                vo.setRank(i + 1);
                vo.setWordStatus(WordStatus.NOT_HOTWORD.getCode());
                vo.setUpdateTime(new Date());
                if (hotWord.size() < num) {
                    vo.setWordStatus(WordStatus.HOTWORD.getCode());
                    hotWord.add(vo.getFrontKeyword());
                }
            }
            if (!hotWord.isEmpty()) {
                cacheProxy.put(HOTWORD_CACHE_KEY_LIST, hotWord, CACHE_TIME_MILLS);
            }
            //更新数据库
            searchWordFrequencyDao.batchUpdateStatus(yesterdayList);
        } catch (Exception e) {
            logger.error("计算热词失败," + hotWord, e);
            throw new RuntimeException(e);
        }
        logger.info("----计算热词 end----------" + hotWord);
        return hotWord;
    }

    @Override
    public Set<String> reloadHotwordCache(Integer recal) {
        Set<String> strings;
        if (recal==1){
            //重算
            strings = calculateHotWordWithTx(RANK_NUM);
        }else {
//            重置缓存
            boolean remove = cacheProxy.remove(HOTWORD_CACHE_KEY_LIST);
            logger.warn("移除缓存结果:"+remove);
            strings = new HashSet<>(getHotword());
        }
        return strings;
    }

    /**
     * 将实际搜索词汇转化成前台搜索词
     * 1.拼音转化成 汉字
     * 2.正常词汇是原词
     * 3.其它敏感词,无意义的词汇,将其转化为空字符串,不做展示
     *
     * @param keyword
     * @return
     */
    private String tran2FrontKeyword(String keyword) {
        return keyword;
    }

    /**
     * 获取词汇与数量之间的关系
     *
     * @return <k,v>=<keyword,frequency>
     */
    private Map<String, Integer> tran2Map(List<SearchWordFrequencyVO> beforeYesterdayList) {
        Map<String, Integer> kf = new HashMap<>();
        if (beforeYesterdayList != null && !beforeYesterdayList.isEmpty()) {
            for (SearchWordFrequencyVO vo : beforeYesterdayList) {
                if (!StringUtils.isEmpty(vo.getKeyword())) {
                    vo.setKeyword(vo.getKeyword());
                    kf.put(vo.getKeyword(), vo.getFrequency());
                }
            }
        }
        return kf;
    }

    /**
     * 计算冷却系数
     *
     * @param currentNum 当前数量
     * @param historyNum 历史数量
     * @param timeDiff   时间间隔
     * @return
     */
    private Double calCoolingCoefficient(Integer currentNum, Integer historyNum, Integer timeDiff) {
        Double cc = 0.0;
        try {
            if (timeDiff == 0 || currentNum == 0 || historyNum == null || historyNum <= 0) {
                cc = 0.0;
            } else if (historyNum > 0) {
                cc = -Math.log((double) (currentNum + 1) / (double) (historyNum + 1)) / timeDiff;
            }
        } catch (Exception e) {
            logger.error("计算冷却系数出错.." + currentNum + "," + historyNum + "," + timeDiff);
        }
        if (Double.isInfinite(cc)) {
            cc = 0.0;
        }
        return cc;
    }

    /**
     * 预测明日的搜索量
     * //冷却系数
     * double hc=0.04317049715361565;
     * //历史词频
     * double historyNum = 30;
     * //时间间隔
     * double timeDiff = 24;
     * //当前词频
     * double currentNum = historyNum*Math.exp(-hc*timeDiff);
     *
     * @return
     */
    private Integer calTomorrowFrequency(Double coefficient, Integer historyNum, Integer timeDiff) {
        try {
            if (coefficient == null || coefficient == 0 || timeDiff == 0) {
                return historyNum;
            }
            Double t = historyNum * Math.exp(-coefficient * timeDiff);
            if (Double.isInfinite(t)) {
                return historyNum;
            }
            return t.intValue();
        } catch (Exception e) {
            logger.error("预测明日的搜索量计算出错..." + coefficient + "," + historyNum + "," + timeDiff);
        }
        return historyNum;
    }


    //0 0 2 * * ?  每天凌晨2点执行
    @Scheduled(cron = "0 30 02 * * ?")
    public void updateHotwordJob() {
        System.out.println("计算热词" + LyfDateUtilS.getMillis2StringByDefault(System.currentTimeMillis()));
        try {
            Set<String> strings = calculateHotWordWithTx(RANK_NUM);
            logger.warn("定时任务热词计算结果"+strings);
        } catch (Exception e) {
            logger.error("计算热词失败", e);
        }
    }

    public static void main(String[] args) throws Exception {
        double v = -Math.log((73d+1)/ (79d+1)) / 24;
        System.out.println(v);
        System.out.println(1/v);
        Double t = 79 * Math.exp(-v * 24);
        System.out.println(t);
        if (Double.isInfinite(v)) {
            v = 0.0;
        }
        System.out.println(v);
        System.out.println("-----------------");
        double log = Math.log(73 / 79);
        System.out.println(log);
        System.out.println(73d/79d);

        Integer currentNum=73;
        Integer historyNum=79;
        Integer timeDiff=24;
        Double cc = 0.0;
        try {
            if (timeDiff == 0 || currentNum == 0 || historyNum == null || historyNum <= 0) {
                cc = 0.0;
            } else if (historyNum > 0) {
                cc = -Math.log((double) (currentNum + 1) / (double) (historyNum + 1)) / timeDiff;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Double.isInfinite(cc)) {
            cc = 0.0;
        }
        System.out.println(cc);

    }
}
