package com.odianyun.search.whale.resp.handler;
////
/////**
//// * Created by ody on 2016/8/12.
//// */

import com.odianyun.search.whale.common.query.ChineseToHanYuPY;
import com.odianyun.search.whale.data.common.DBManagerInitedEvent;
import com.odianyun.search.whale.data.manager.DBCacheManager;
import com.odianyun.search.whale.data.service.BrandService;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.data.service.impl.AbstractDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

@Component
public class GenerativeDictionary extends AbstractDBService {
    static Logger logger = Logger.getLogger(GenerativeDictionary.class);
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    //最终生成的 companyId :拼音-->汉字 词典
    private Map<Integer,Map<String,Map<String,String>>> pinyinChineseDic=new HashMap<>();
    int minNameLength=1; //进行汉字转换拼音时  汉字的长度必须是大于此值
    int minPinyinLength=2; //转换后的拼音长度必须大于此值
    int preFixNum=3; //进行公司的词典计算时，是根据拼音的前面几个拼音存储的 所以此处设置就是根据此值来存储
    private  void initPinyinDict(int prefix)  {
        Map<Integer,Map<String,TreeMap<String,Integer>>> pinyinChineseTmp = new HashMap<>();
        Map<Integer,Map<String,String>> pinyinChineseMap=new HashMap<>();
        try {
            Map<Integer,List<String>> allBrands=brandService.getAllBrandsName();
            Map<Integer,List<String>> allCategoryMap=categoryService.getAllCategorysName();
            allCompanyDicAdd(allBrands,pinyinChineseTmp);
            allCompanyDicAdd(allCategoryMap,pinyinChineseTmp);
            //下面词循环 是为了生成公司的词典的唯一值：eg 例如 一个拼音 对应了多个汉语 则此时需要留下频次最多的汉语即可
            if(!pinyinChineseTmp.isEmpty()) {
                for (Map.Entry<Integer,Map<String,TreeMap<String,Integer>>> c : pinyinChineseTmp.entrySet()) {
                    int companyId=c.getKey();
                    Map<String,TreeMap<String,Integer>> allCategoryOrBrandName=c.getValue();
                    Map<String,String> companyDic = new HashMap<>();
                    pinyinChineseMap.put(companyId,companyDic);
                    if (!c.getValue().isEmpty()) {
                        for (Map.Entry<String, TreeMap<String, Integer>> dicInfo : allCategoryOrBrandName.entrySet()) {
                            companyDic.put(dicInfo.getKey(), dicInfo.getValue().lastKey());
                        }
                    }
                }
            }
            //此处是根据上面得到了公司的 拼音：汉字 一一对应词典 ，现在 在设置成根据 首拼：拼音：汉字的模式存储
            Map<Integer,Map<String,Map<String,String>>> pinyinChineseDicTmp=new HashMap<>();
            for(Map.Entry<Integer,Map<String,String>> c:pinyinChineseMap.entrySet()) {
                int companyId=c.getKey();
                Map<String,String> companyValue=c.getValue();
                Map<String,Map<String,String>> companyDic = new HashMap<>();
                pinyinChineseDicTmp.put(companyId,companyDic);
                for(Map.Entry<String,String> pinyinChinese:companyValue.entrySet()) {
                    String preFixString=pinyinChinese.getKey().trim().substring(0,prefix);
                    if (companyDic.get(preFixString) == null) {
                        companyDic.put(preFixString, new HashMap<String, String>());
                    }
                    Map<String,String> preFixList = companyDic.get(preFixString);
                    preFixList.put(pinyinChinese.getKey(),pinyinChinese.getValue());
                }
            }
            if(pinyinChineseDicTmp.size()>0){
                pinyinChineseDic = pinyinChineseDicTmp;
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
    /**
     *
     * @param companyDicList 公司的词典名
     * @param pinyinChineseTmp 公司词典的存储位置
     * @throws Exception
     */
    private  void loadCompanyDic(List<String> companyDicList, Map<String,TreeMap<String,Integer>> pinyinChineseTmp) throws Exception{
        if(!companyDicList.isEmpty()) {
               int listSize=companyDicList.size();
                for(int i=0;i<listSize;i++){
                String name = companyDicList.get(i);
                if (name != null && name.length() > minNameLength) {
                    String pinyin = ChineseToHanYuPY.instance.convertChineseToPinyin(name.trim()).toLowerCase();
                    if (pinyin.length() > minPinyinLength) {
                        TreeMap<String, Integer> treeMap= pinyinChineseTmp.get(pinyin);
                        if(treeMap==null){
                            treeMap=new TreeMap<String, Integer>();
                            pinyinChineseTmp.put(pinyin,treeMap);
                        }
                        Integer count=treeMap.get(name);
                        if(count==null){
                            count=0;
                        }
                        treeMap.put(name,count++);
                    }
                }
            }
        }
    }

    /**
     * 初始化 所有的公司的拼音词典，得到companyId 对应的拼音：map(汉字：频次（汉字出现的频次）) 的一个Map 以备后续查询使用
     * @param allCompanyDic 所有公司的 词典list
     * @param pinyinChineseTmp 所有公司的词典list 经过处理后所存放的位置
     * @throws Exception
     */
    private void allCompanyDicAdd(Map<Integer,List<String>> allCompanyDic,Map<Integer,Map<String,TreeMap<String,Integer>>> pinyinChineseTmp) throws Exception{
        for (Map.Entry<Integer,List<String>> c:allCompanyDic.entrySet()){
            int companyID=c.getKey();
            List<String> companyDic=c.getValue();
            if(pinyinChineseTmp.get(companyID)==null) {//这边不能简化 因为其他地方还会运用此函数 如果修改容易造成每次清空
                pinyinChineseTmp.put(companyID,new HashMap<String, TreeMap<String, Integer>>());
            }
            Map<String, TreeMap<String, Integer>> tmp=pinyinChineseTmp.get(companyID);
            if(c.getValue()!=null) {
                loadCompanyDic(companyDic, tmp);
            }
        }
    }
    public Map<Integer, Map<String, Map<String, String>>> getPinyinChineseDic() {
        return pinyinChineseDic;
    }

    @Override
    protected void tryReload() throws Exception {
        Long start = System.currentTimeMillis();
        logger.info("reload dict start----------");
        initPinyinDict(preFixNum);
        logger.info("reload dict end------");
        logger.info("reload dist cost:"+(System.currentTimeMillis() - start));

    }
    public int getInterval(){
        return 10;
    }

    @Override
    protected void tryReload(List<Long> ids) throws Exception {

    }
}
