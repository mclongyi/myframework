package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.SeasonCategoryWeightDao;
import com.odianyun.search.whale.data.dao.SeasonDao;
import com.odianyun.search.whale.data.model.Season;
import com.odianyun.search.whale.data.model.SeasonCategoryWeight;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.data.service.SeasonCategoryWeightService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 16/11/4.
 */
public class SeasonCategoryWeightServiceImpl extends AbstractCompanyDBService implements SeasonCategoryWeightService {

    private static int weightBase=100;

    @Autowired
    SeasonDao seasonDao;

    @Autowired
    SeasonCategoryWeightDao seasonCategoryWeightDao;

    @Autowired
    ConfigService configService;

    Map<Integer,SeasonCategoryCacheContext> seasonCacheContexts=new HashMap<Integer,SeasonCategoryCacheContext>();

    // 根据后台类目id获取一个季节权重
    @Override
    public int getWeight(int companyId,long categoryId) {
        Integer weight=0;
        SeasonCategoryCacheContext seasonCategoryCacheContext=seasonCacheContexts.get(companyId);
        if(seasonCategoryCacheContext!=null){
           weight=seasonCategoryCacheContext.seasonCategoryWeightMap.get(categoryId);
           if(weight==null){
               weight=0;
           }
        }
        return weight;
    }

    @Override
    protected void tryReload(int companyId) throws Exception {
        boolean IS_SEASON_WEIGHT = configService.getBool("is_season_weight",false,companyId);
        if(!IS_SEASON_WEIGHT){
            return;
        }
        List<Season> seasons = seasonDao.queryAllSeasonData(companyId);
        Map<Long,Integer> seasonWeightMap=new HashMap<Long,Integer>();
        for(Season season : seasons){
            seasonWeightMap.put(season.getId(), season.getWeight());
        }

        List<SeasonCategoryWeight> seasonCategoryWeights = seasonCategoryWeightDao.queryAllSeasonCategoryWeight(companyId);
        SeasonCategoryCacheContext seasonCategoryCacheContext=new SeasonCategoryCacheContext();
        for(SeasonCategoryWeight seasonCategoryWeight : seasonCategoryWeights){
            long seasonId= seasonCategoryWeight.getSeasonId();
            long categoryId= seasonCategoryWeight.getCategoryId();
            int weight= seasonCategoryWeight.getWeight();
            int seasonWeight=seasonWeightMap.get(seasonId)==null?0:seasonWeightMap.get(seasonId);
            /**
             * 季节相关性可以为1,0,-1, 季节和类目相关性为-99~99
             * 季节相关性为正,sessionWeight*weightBase+weight
             * 季节相关性为0,weight=0
             * 季节相关性为负,weight=sessionWeight*weightBase-weight
             */
            if(seasonWeight>0){
                weight=seasonWeight*weightBase+weight;
            }else if(seasonWeight==0){
                weight=0;
            }else{
                weight=seasonWeight*weightBase-weight;
            }
            Integer preWeight=seasonCategoryCacheContext.seasonCategoryWeightMap.get(categoryId);
            if(preWeight!=null){
                if(preWeight>=0 && weight>=0 && preWeight>weight){
                    weight=preWeight;
                }else if(preWeight<=0 && weight<=0 && preWeight<weight){
                    weight=preWeight;
                }else if(preWeight<0 && weight>=0){
                    weight=preWeight;
                }
            }
            seasonCategoryCacheContext.seasonCategoryWeightMap.put(categoryId,weight);
        }
        seasonCacheContexts.put(companyId,seasonCategoryCacheContext);

    }

    @Override
    public int getInterval() {
        return 30;
    }

    private static class SeasonCategoryCacheContext {

        Map<Long,Integer> seasonCategoryWeightMap=new HashMap<Long, Integer>();

    }
}
