package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.data.service.CurrentPointsMallProductService;
import com.odianyun.search.whale.data.service.PointsMallProductService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import com.odianyun.search.whale.index.api.common.PointsMpIndexConstants;
import com.odianyun.search.whale.index.business.process.base.BasePointsBusinessProductProcessor;
import com.odianyun.search.whale.index.business.process.build.IncIndexProcessorBuilder;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import com.odianyun.search.whale.index.realtime.IndexUpdater;
import com.odianyun.search.whale.processor.DataRecord;
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
public class CurrentPointsBusinessProductProcessor extends BasePointsBusinessProductProcessor {

    CurrentPointsMallProductService pointsMallProductService;
    CategoryService categoryService;
    SegmentManager segmentManager;

    //private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd_HH-mm-ss");

    public CurrentPointsBusinessProductProcessor(){
        pointsMallProductService = (CurrentPointsMallProductService) ProcessorApplication.getBean("currentPointsMallProductService");
        categoryService = (CategoryService) ProcessorApplication.getBean("categoryService");
        segmentManager = SegmentManager.getInstance();
    }
    @Override
    protected void calcPointsMallProduct(Map<Long, BusinessProduct> map,ProcessorContext processorContext) throws Exception{
        int companyId = processorContext.getCompanyId();
        if(IndexConstants.LYF_COMPANY_ID == companyId){
            return;
        }
        // 系列品id --> 子品id
        Map<Long,List<String>> seriesMpMap = new HashMap<Long,List<String>>();

        List<Long> mpIdList = new ArrayList<>(map.keySet());
        List<PointsMallProduct> pointsMallProductList = pointsMallProductService.getPointsMallProductsByRefId(mpIdList,processorContext.getCompanyId());
        if(CollectionUtils.isEmpty(pointsMallProductList)){
            return ;
        }

        Map<String,PointsMallProduct> pointsMallProductMap = new HashMap<>();
        for(PointsMallProduct pointsMallProduct : pointsMallProductList){
            Long mpId = pointsMallProduct.getMpId();
            calcPointsMallProductCategory(pointsMallProduct);
            BusinessProduct businessProduct = map.get(mpId);

            if(businessProduct != null){
                Long merchantSeriesId = businessProduct.getMerchantSeriesId();
                if(merchantSeriesId != null && merchantSeriesId != 0){
                    List<String> pointsMpIds = seriesMpMap.get(merchantSeriesId);
                    if(pointsMpIds == null){
                        pointsMpIds = new ArrayList<String>();
                        seriesMpMap.put(merchantSeriesId, pointsMpIds);
                    }
                    pointsMpIds.add(genKey(pointsMallProduct.getId(),pointsMallProduct.getRuleId(),mpId));
                }
                pointsMallProduct.setBusinessProduct(businessProduct);
            }
            calcPointsMallProductTagWords(pointsMallProduct);
            pointsMallProductMap.put(genKey(pointsMallProduct.getId(),pointsMallProduct.getRuleId(),mpId),pointsMallProduct);

        }
        calcPointsMallProductPrice(pointsMallProductMap,seriesMpMap,processorContext);

        String indexName = processorContext.getIndexName();
        String updateTime = "";
        if(indexName.length() < 19){
            //updateTime = simpleDateFormat.format(new Date());
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

    private void calcPointsMallProductPrice(Map<String, PointsMallProduct> pointsMallProductMap, Map<Long, List<String>> seriesMpMap, ProcessorContext processorContext) {
        for(Map.Entry<String, PointsMallProduct> entry : pointsMallProductMap.entrySet()){
            PointsMallProduct pointsMallProduct = entry.getValue();
            BusinessProduct businessProduct = pointsMallProduct.getBusinessProduct();
            if(businessProduct == null){
                continue;
            }
            if(!ProductType.VIRTUAL_CODE.getCode().equals(businessProduct.getTypeOfProduct())){
                continue;
            }
            String selfKey = genKey(pointsMallProduct.getId(),pointsMallProduct.getRuleId(),pointsMallProduct.getMpId());
            Long merchantSeriesId = businessProduct.getMerchantSeriesId();
            if(merchantSeriesId == null || merchantSeriesId == 0){
                continue;
            }
            List<String> pointMpIdList = seriesMpMap.get(merchantSeriesId);
            if(CollectionUtils.isNotEmpty(pointMpIdList)){
                Double minPointPrice = Double.MAX_VALUE;
                PointsMallProduct temp = null;
                for(String pointMpId : pointMpIdList){
                    if(pointMpId.equals(selfKey)){
                        continue;
                    }
                    PointsMallProduct seriesProduct = pointsMallProductMap.get(pointMpId);
                    if(seriesProduct != null){
                        Double pointPrice = seriesProduct.getPointPrice();
                        Double price = seriesProduct.getPrice();
                        if(pointPrice < minPointPrice){
                            minPointPrice = pointPrice;
                            temp = seriesProduct;
                        }
                        Long totalExchanged = seriesProduct.getTotalExchanged();
                        Long totalLimit = seriesProduct.getTotalLimit();
                        pointsMallProduct.setTotalExchanged(pointsMallProduct.getTotalExchanged()+totalExchanged);
                        pointsMallProduct.setTotalLimit(pointsMallProduct.getTotalLimit()+totalLimit);
                    }
                }
                if(temp != null){
                    pointsMallProduct.setPointPrice(temp.getPointPrice());
                    pointsMallProduct.setPrice(temp.getPrice());
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
            merchantProductSearch.setId(pointsMallProduct.getMpId());
            merchantProductSearch.setNavCategoryId_search(pointsMallProduct.getNavCategoryIdSearch());
            merchantProductSearch.setCategoryName_search(pointsMallProduct.getCategoryNameSearch());
            merchantProductSearch.setManagementState(pointsMallProduct.getManagementState());
            merchantProductSearch.setStock(((pointsMallProduct.getTotalLimit() - pointsMallProduct.getTotalExchanged()) > 0 ? 1 : 0));
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
            merchantProductSearch.setIsDefault(pointsMallProduct.getIsDefault());
            merchantProductSearch.setRuleId(pointsMallProduct.getRuleId());
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
        if(CollectionUtils.isNotEmpty(indexIfoList) && indexName.endsWith("_alias")){
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
