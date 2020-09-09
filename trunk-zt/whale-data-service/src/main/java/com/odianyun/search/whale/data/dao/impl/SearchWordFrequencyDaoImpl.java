package com.odianyun.search.whale.data.dao.impl;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.odianyun.search.whale.data.dao.SearchWordFrequencyDao;
import com.odianyun.search.whale.data.model.hotword.SearchWordFrequencyVO;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.sql.SQLException;
import java.util.List;

/**
 * @author hs
 * @date 2018/9/13.
 */
public class SearchWordFrequencyDaoImpl extends SqlMapClientDaoSupport implements SearchWordFrequencyDao {

    static final int BATCH_NUM = 500;
    //头一天无数据,只有当前搜索量至少超过THRESHOLD_NUM次才能够进入热搜榜
    static final int THRESHOLD_NUM = 150;
    //取排名前50个词为热词
    static final int RANK_NUM = 50;

    @Override
    public List<SearchWordFrequencyVO> selectByDateAndStatus(SearchWordFrequencyVO vo) {
        return getSqlMapClientTemplate().queryForList("selectByDateAndStatus", vo);
    }

    @Override
    public void insertKeyword(SearchWordFrequencyVO vo) {
        getSqlMapClientTemplate().insert("insertKeyword", vo);
    }

    @Override
    public Long countByDate(SearchWordFrequencyVO vo) {
        return (Long) getSqlMapClientTemplate().queryForObject("countByDate", vo);
    }

    @Override
    public void batchUpdateStatus(final List<SearchWordFrequencyVO> voList) {
        this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            @Override
            public Object doInSqlMapClient(SqlMapExecutor sqlMapExecutor) throws SQLException {
                sqlMapExecutor.startBatch();
                int count = 0;
                for (SearchWordFrequencyVO vo : voList) {
                    count++;
                    sqlMapExecutor.insert("updateCalculateResult",vo);
                    if (count==BATCH_NUM){
                        sqlMapExecutor.executeBatch();
                        count = 0;
                    }
//                    if (count==0) throw new RuntimeException("test tx");
                }
                sqlMapExecutor.executeBatch();
                return null;
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {

//        Set s = new HashSet();
//        Random random = new Random();
//        int loop = 0;
//        while (s.size()<10){
//            loop++;
//            int r = random.nextInt(15);
//            s.add(r);
//            System.out.println(r);
//
//            if (loop==10000){
//                break;
//            }
//        }
//        System.out.println("------"+loop);
//        System.out.println("------"+s);

//        Integer num = 78;
//        int loop = num / BATCH_NUM;
//        System.out.println(loop);
//
//        Date date = DateUtils.addDays(new Date(), -1);
//        System.out.println(date);
//        Date date1 = LyfDateUtilS.addDays(new Date(), -1);
//        System.out.println(date1);
//        String millis2String = LyfDateUtilS.getMillis2String(date1.getTime(), LyfDateUtilS.PATTERN_YMD);
//        System.out.println(millis2String);

//        System.setProperty("global.config.path", "/Users/hs/Desktop/lyf/env-edu");
//        OccPropertiesLoaderUtils.getProperties("osoa");
//        CacheProxy cacheProxy=("product","basics-price-service","basics-price/basics-price-business/basics-price-business-memcache.xml");
//        CacheProxy cacheProxy= CacheBuilder.buildCache("search","search","search/common/memcache.xml");

//        cacheProxy.putWithSecond("test_000", "99999", 40);
//        System.out.println(cacheProxy.get("test_000"));
//        Thread.sleep(1000 * 3);
//        System.out.println(cacheProxy.get("test_000"));
//        Thread.sleep(1000 * 3);
//        System.out.println(cacheProxy.get("test_000"));
    }
}
