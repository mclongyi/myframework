package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.service.PointsMallProductService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import com.odianyun.search.whale.index.api.common.PointsMpIndexConstants;
import com.odianyun.search.whale.index.business.process.base.BasePointsBusinessProductProcessor;
import com.odianyun.search.whale.index.business.process.build.IncIndexProcessorBuilder;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import com.odianyun.search.whale.index.realtime.IndexUpdater;
import com.odianyun.search.whale.processor.ProcessorContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.StringUtils;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fishcus on 17/8/19.
 */
public class PointsBusinessProductProcessor extends BasePointsBusinessProductProcessor {

    PointsMallProductService pointsMallProductService;
    CategoryService categoryService;
    SegmentManager segmentManager;

    //private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd_HH-mm-ss");

    public PointsBusinessProductProcessor(){
        pointsMallProductService = (PointsMallProductService) ProcessorApplication.getBean("pointsMallProductService");
        categoryService = (CategoryService) ProcessorApplication.getBean("categoryService");
        segmentManager = SegmentManager.getInstance();
    }
    @Override
    protected void calcPointsMallProduct(Map<Long, BusinessProduct> map,ProcessorContext processorContext) throws Exception{
        int companyId = processorContext.getCompanyId();
        if(IndexConstants.LYF_COMPANY_ID != companyId){
            return;
        }
        List<Long> mpIdList = new ArrayList<>(map.keySet());
        List<PointsMallProduct> pointsMallProductList = pointsMallProductService.getPointsMallProductsByRefId(mpIdList,null,processorContext.getCompanyId());
        if(CollectionUtils.isEmpty(pointsMallProductList)){
            return ;
        }
        List<Long> pointsProductIdList = new ArrayList<>();
        List<Long> pointsRuleIdList = new ArrayList<>();
        List<Long> pointsMpIdList = new ArrayList<>();

        Map<String,PointsMallProduct> pointsMallProductMap = new HashMap<>();
        for(PointsMallProduct pointsMallProduct : pointsMallProductList){
            Long mpId = pointsMallProduct.getMpId();
            calcPointsMallProductCategory(pointsMallProduct);
            BusinessProduct businessProduct = map.get(mpId);
            if(businessProduct != null){
                pointsMallProduct.setBusinessProduct(businessProduct);
            }
            calcPointsMallProductTagWords(pointsMallProduct);
            pointsProductIdList.add(pointsMallProduct.getId());
            pointsRuleIdList.add(pointsMallProduct.getRuleId());
            pointsMpIdList.add(mpId);

            pointsMallProductMap.put(genKey(pointsMallProduct.getId(),pointsMallProduct.getRuleId(),mpId),pointsMallProduct);
        }
        calcPointsMallProductPrice(pointsMallProductMap,pointsProductIdList,pointsRuleIdList,pointsMpIdList,processorContext);

        String indexName = processorContext.getIndexName();
        String updateTime = "";
        if(indexName.length() < 19){
            updateTime = fdf.format(new Date());
        }else{
            //"yyyy-MM-dd_HH-mm-ss".length()=19
            updateTime = indexName.substring(indexName.length() - 19);
        }

        List<MerchantProductSearch> mpSearchList = convertList(pointsMallProductList,updateTime);

        if(CollectionUtils.isNotEmpty(mpSearchList)){
            incIndex(mpSearchList,processorContext);
        }


    }

    private void calcPointsMallProductPrice(Map<String, PointsMallProduct> pointsMallProductMap,List<Long> pointsProductIdList,List<Long> pointsRuleIdList,List<Long> pointsMpIdList, ProcessorContext processorContext) throws Exception {
        List<PointsMallProductPrice> priceList = pointsMallProductService.getPointsMallProductPrice(pointsProductIdList,pointsRuleIdList,pointsMpIdList,processorContext.getCompanyId());
        if(CollectionUtils.isNotEmpty(priceList)){
            for(PointsMallProductPrice price : priceList){
                Long pointsProductId = price.getPointsMallProductId();
                Long pointsRuleId = price.getPointRuleId();
                Long mpId = price.getMpId();
                String key = genKey(pointsProductId,pointsRuleId,mpId);
                PointsMallProduct pointsMallProduct = pointsMallProductMap.get(key);
                if(pointsMallProduct != null){
                    pointsMallProduct.setPrice(price.getPrice());
                    pointsMallProduct.setPointPrice(price.getPointPrice());
                }
            }
        }
    }

    private String genKey(Long pointsProductId, Long pointsRuleId, Long mpId) {
        return pointsProductId + "_" + pointsRuleId + "_" + mpId;
    }

    private void calcPointsMallProductTagWords(PointsMallProduct pointsMallProduct) throws Exception {
        String mpName = pointsMallProduct.getMpName();
        if(StringUtils.isNotBlank(mpName)){
            String tagWords = segmentManager.doProcess(mpName);
            pointsMallProduct.setTagWords(tagWords);
        }
        String categoryNameSearch = pointsMallProduct.getCategoryNameSearch();
        BusinessProduct businessProduct = pointsMallProduct.getBusinessProduct();
        if(businessProduct != null){
            categoryNameSearch = categoryNameSearch + " " + businessProduct.getCategoryNameBuff();
        }
        if(StringUtils.isNotBlank(categoryNameSearch)){
            String categoryNameSearchBuff = segmentManager.doProcess(categoryNameSearch);
            if(StringUtils.isNotBlank(categoryNameSearchBuff)){
                pointsMallProduct.setCategoryNameSearch(categoryNameSearchBuff);
            }
        }

    }

    private List<MerchantProductSearch> convertList(List<PointsMallProduct> pointsMallProductList,String updateTime) {
        List<MerchantProductSearch> list = new ArrayList<>();
        if(CollectionUtils.isEmpty(pointsMallProductList)){
            return list;
        }
        MerchantProductSearch merchantProductSearch = null;
        for(PointsMallProduct pointsMallProduct : pointsMallProductList){
            merchantProductSearch = convert(pointsMallProduct,updateTime);
            if(merchantProductSearch != null){
                list.add(merchantProductSearch);
            }
        }

        return list;
    }

    private MerchantProductSearch convert(PointsMallProduct pointsMallProduct,String updateTime) {
        if(pointsMallProduct == null){
            return null;
        }
        BusinessProduct businessProduct = pointsMallProduct.getBusinessProduct();
        MerchantProductSearch merchantProductSearch = MerchantProductSearchProcessor.doConvert(businessProduct,updateTime);
        if(merchantProductSearch != null){
            merchantProductSearch.setId(pointsMallProduct.getId());
            merchantProductSearch.setNavCategoryId_search(pointsMallProduct.getNavCategoryIdSearch());
            merchantProductSearch.setCategoryName_search(pointsMallProduct.getCategoryNameSearch());
            merchantProductSearch.setManagementState(pointsMallProduct.getManagementState());
            merchantProductSearch.setStock(((pointsMallProduct.getTotalLimit() - pointsMallProduct.getTotalExchanged())>0 ? 1:0));
            if(StringUtils.isNotBlank(pointsMallProduct.getRefPic())){
                merchantProductSearch.setPicUrl(pointsMallProduct.getRefPic());
            }
            merchantProductSearch.setTag_words(merchantProductSearch.getTag_words() + " " + pointsMallProduct.getTagWords());
            merchantProductSearch.setStartTime(fdf.format(pointsMallProduct.getStartTime()));
            merchantProductSearch.setEndTime(fdf.format(pointsMallProduct.getEndTime()));
            merchantProductSearch.setPriceType(pointsMallProduct.getPriceType());
            merchantProductSearch.setPointType(pointsMallProduct.getPointType());
            merchantProductSearch.setPointPrice(pointsMallProduct.getPointPrice());
            merchantProductSearch.setPrice(pointsMallProduct.getPrice());
            if(StringUtils.isNotBlank(pointsMallProduct.getMpName())){
                merchantProductSearch.setProductName(pointsMallProduct.getMpName());
            }
            merchantProductSearch.setNavCategoryId_search(pointsMallProduct.getNavCategoryIdSearch());
        }
        return merchantProductSearch;
    }

    private void incIndex(List<MerchantProductSearch> mpSearchList, ProcessorContext processorContext) throws Exception {
        String indexName = processorContext.getIndexName().replace(OplusOIndexConstants.indexName_pre, PointsMpIndexConstants.indexName_pre);
        String indexType = IndexConstants.index_type;
        IndexUpdater.update(ESClient.getClient(),mpSearchList,indexName,indexType);
        List<IncIndexProcessor.IndexInfo> indexIfoList = IncIndexProcessorBuilder.getIndexInfoList();
        if(CollectionUtils.isNotEmpty(indexIfoList)){
            for(IncIndexProcessor.IndexInfo indexInfo : indexIfoList){
                IndexUpdater.update(ESClient.getClient(),mpSearchList,indexInfo.getIndexName().replace(OplusOIndexConstants.indexName_pre, PointsMpIndexConstants.indexName_pre),indexInfo.getIndexType());
            }
        }
    }

    private void calcPointsMallProductCategory(PointsMallProduct pointsMallProduct) throws Exception {
        // 积分商品挂载的类目id  是前台类目id
        Long navCategoryId = pointsMallProduct.getCategoryId();
        if(navCategoryId == null || navCategoryId == 0){
            return;
        }
        Collection<Category> navCategoryList = categoryService.getNaviCategorys(navCategoryId,pointsMallProduct.getCompanyId().intValue());
        if(CollectionUtils.isEmpty(navCategoryList)){
            return;
        }
        StringBuilder categoryStr = new StringBuilder();
        StringBuilder navCategoryIdStr = new StringBuilder();

        for(Category category : navCategoryList){
            categoryStr.append(category.getName() + " ");
            navCategoryIdStr.append(category.getId()+ " ");
        }
        pointsMallProduct.setNavCategoryIdSearch(navCategoryIdStr.toString());
        //这里只是积分商品类目
        pointsMallProduct.setCategoryNameSearch(categoryStr.toString());
    }

}
