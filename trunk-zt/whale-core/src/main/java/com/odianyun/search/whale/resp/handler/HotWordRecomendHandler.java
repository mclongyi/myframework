package com.odianyun.search.whale.resp.handler;

import com.odianyun.search.whale.api.model.BrandResult;
import com.odianyun.search.whale.api.model.CategoryTreeResult;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.data.service.BrandService;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.manager.IKSegmentManager;
import com.odianyun.search.whale.server.SearchHandler;
import com.odianyun.soa.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HotWordRecomendHandler {

    @Autowired
    BrandService brandService;

    @Autowired
    SearchHandler idSearchHandler;
    @Autowired
    CategoryService categoryService;

    int maxLeafNodeNum = 3; //热词推荐时，取得searchResponse 的CategoryTreeResult叶子节点的数目 以备后续查找和找出的推荐词阈值;
    int maxCateNum = 3; //类目直接进入 进行热词推荐时，取得自 CategoryTreeResult 的热词数目;
    int maxBrandNum = 3; //聚合出brandResult 找出count数前三多的 品牌名;
    int hotWordsSize = 6; //热搜词的取词数目

    /**
     * @param esSearchResponse
     * @param searchResponse
     * @param searchRequest
     * @throws Exception 进行热词推荐：分为 类目直接进入推荐；关键词搜索推荐
     */
    public void handle(SearchResponse esSearchResponse,
                       com.odianyun.search.whale.api.model.SearchResponse searchResponse,
                       SearchRequest searchRequest) throws Exception {
        if(!searchRequest.isRecommendWordsHandler()){
            return;
        }
        if (esSearchResponse.getHits().getTotalHits() <= 0)
            return;

        List<String> hotwordsRecommendList = null;

        if (CollectionUtils.isEmpty(searchRequest.getNavCategoryIds())
                && StringUtils.isNotBlank(searchRequest.getKeyword())) { //如果不是从类目直接进入 则依据类目树进行推荐
            hotwordsRecommendList = recommendWithKeyword(searchResponse, searchRequest);
        } else if (CollectionUtils.isNotEmpty(searchRequest.getNavCategoryIds())
                && StringUtils.isBlank(searchRequest.getKeyword())) {//如果NavCategoryIds为null keyword 不为空则
            hotwordsRecommendList = recommendByCategory(searchResponse, searchRequest);
        }

        searchResponse.setHotwordsRecommended(hotwordsRecommendList);

    }


    /**
     * 找出后台类目树的，前三多的叶子节点；
     * 找出品牌聚类结果的 前三多的品牌名	最后给出6个推荐词
     */
    private List<String> recommendWithKeyword(com.odianyun.search.whale.api.model.SearchResponse searchResponse,
                                              SearchRequest searchRequest) throws Exception {
        List<String> hotwordsRecommendList = new ArrayList<>();
        List<CategoryTreeResult> hotWordCategory = new ArrayList<>(); //实例化变量 为后续存储我们得到的叶子节点 的信息
        List<String> tokens = IKSegmentManager.segment(searchRequest.getKeyword());
        List<CategoryTreeResult> leafHotwordsRecommend = getLeafNodeByCategoryTree(searchResponse.getCategoryTreeResult(), hotWordCategory);

        if (CollectionUtils.isEmpty(leafHotwordsRecommend))
            return hotwordsRecommendList;

        //得到我们后台类目树的叶子节点 并依据count数排序
        if (leafHotwordsRecommend.size() > maxLeafNodeNum) {
            Collections.sort(leafHotwordsRecommend, new Comparator<CategoryTreeResult>() {
                public int compare(CategoryTreeResult o1, CategoryTreeResult o2) {
                    if (o1.getCount() > o2.getCount()) {
                        return -1;
                    }
                    if (o1.getCount() == o2.getCount()) {
                        return 0;
                    }
                    return 1;
                }
            });
        }

        //提前根据count数 提取前三多的的category
        for (CategoryTreeResult category : leafHotwordsRecommend) {
            if (hotwordsRecommendList.size() > maxLeafNodeNum)
                break;

            if (tokens.contains(category.getName()) || category.getName().equals(searchRequest.getKeyword()))
                continue;

            hotwordsRecommendList.add(category.getName());
        }

        List<BrandResult> brands = searchResponse.getBrandResult();
        if (CollectionUtils.isNotEmpty(brands)) {
            for (BrandResult brand : brands) {
                if (hotwordsRecommendList.size() > hotWordsSize)
                    break;

                if (tokens.contains(brand.getName())
                        || hotwordsRecommendList.contains(brand.getName())
                        || brand.getName().equals(searchRequest.getKeyword()))
                    continue;

                hotwordsRecommendList.add(brand.getName());
            }
        }

        return hotwordsRecommendList;
    }

    /**
     * 1）如果是一级类目：直接找到brandResulet 聚合结果 得到品牌名即可；
     * 2）如果是非一级类目-->根据NavCategoryIds （去	CategoryServiceImpl 的  getParentCategoryId的方法） 找到父类目，
     *    根据父类目搜索，聚合出brandResult 找出count数前三多的 品牌名；
     *    再根据后台聚类树找出 父类目的子类目前三多的品牌名；
     *    最后得出6个推荐热词
     */
    private List<String> recommendByCategory(com.odianyun.search.whale.api.model.SearchResponse searchResponse,
                                             SearchRequest searchRequest) throws Exception {
        List<String> hotwordsRecommendList = new ArrayList<>();
        List<CategoryTreeResult> hotWordCategory = new ArrayList<>(); //实例化变量 为后续存储我们得到的叶子节点 的信息

        long categoryId = searchRequest.getNavCategoryIds().get(0);
        int companyId = searchRequest.getCompanyId();
        String categoryName = categoryService.getCategory(categoryId, companyId).getName();
        Long parentCategoryId = categoryService.getParentCategoryId(categoryId, companyId);
        if (parentCategoryId != null) {
            List<Long> parentCategoryIdList = new ArrayList<>();
            parentCategoryIdList.add(parentCategoryId);
            SearchRequest parentIdesearchRequest = new SearchRequest(companyId);
            BeanUtils.copyProperties(searchRequest, parentIdesearchRequest);
            parentIdesearchRequest.setKeyword(null);
            parentIdesearchRequest.setNavCategoryIds(parentCategoryIdList);

            com.odianyun.search.whale.api.model.SearchResponse idSearchResponse
                    = new com.odianyun.search.whale.api.model.SearchResponse();
            idSearchHandler.search(parentIdesearchRequest, idSearchResponse);
            List<BrandResult> brandResult = idSearchResponse.getBrandResult();
            for (BrandResult brand : brandResult) {
                if (hotwordsRecommendList.size() > maxBrandNum)
                    break;

                if (brand.getName().equals(categoryName))
                    continue;

                hotwordsRecommendList.add(brand.getName());
            }

            getBrotherCatByParentId(idSearchResponse.getCategoryTreeResult(), parentCategoryId, hotWordCategory);
            if (CollectionUtils.isNotEmpty(hotWordCategory)) {
                for (CategoryTreeResult category : hotWordCategory) {
                    if (hotwordsRecommendList.size() > hotWordsSize)
                        break;

                    if (category.getName().equals(categoryName)
                            || hotwordsRecommendList.contains(category.getName()))
                        continue;

                    hotwordsRecommendList.add(category.getName());
                }
            }
        } else {//一级类目
            List<BrandResult> brands = searchResponse.getBrandResult();
            if (CollectionUtils.isNotEmpty(brands)) {
                int hotWordsSize = maxBrandNum + maxCateNum;
                for (BrandResult brand : brands) {
                    if (hotwordsRecommendList.size() > hotWordsSize)
                        break;

                    if (brand.getName().equals(categoryName))
                        continue;

                    hotwordsRecommendList.add(brand.getName());
                }
            }
        }

        return hotwordsRecommendList;
    }

    /**
     * @param categoryTreeResult SearchResponse中的categoryTreeResult 结果
     * @param hotWordCategory    取得自categoryTreeResult 中的热词存储位置
     * @return
     */
    private List<CategoryTreeResult> getLeafNodeByCategoryTree(List<CategoryTreeResult> categoryTreeResult, List<CategoryTreeResult> hotWordCategory) {
        if (CollectionUtils.isEmpty(categoryTreeResult)) {
            return hotWordCategory;
        } else {
            for (int i = 0; i < categoryTreeResult.size(); i++) {
                if (CollectionUtils.isEmpty(categoryTreeResult.get(i).getChildren())) {
                    hotWordCategory.add(categoryTreeResult.get(i));
                } else {
                    getLeafNodeByCategoryTree(categoryTreeResult.get(i).getChildren(), hotWordCategory);
                }
            }
            return hotWordCategory;
        }
    }

    /**
     * @param categoryTreeResult 根据取得的parentCategoryId 进行搜索 得到的SearchResponse中的categoryTreeResult 结果
     * @param parentCategoryId   父类目CategoryId
     * @param hotWordCategory    取得自categoryTreeResult 中的热词存储位置
     * @return
     */
    private List<CategoryTreeResult> getBrotherCatByParentId(List<CategoryTreeResult> categoryTreeResult, Long parentCategoryId, List<CategoryTreeResult> hotWordCategory) {

        if (CollectionUtils.isEmpty(categoryTreeResult)) {
            return hotWordCategory;
        } else {
            for (int i = 0; i < categoryTreeResult.size(); i++) {
                if (CollectionUtils.isNotEmpty(hotWordCategory)) return hotWordCategory;
                if (categoryTreeResult.get(i).getId() == parentCategoryId) {
                    List<CategoryTreeResult> brothersCategory = categoryTreeResult.get(i).getChildren();
                    if (CollectionUtils.isNotEmpty(brothersCategory)) {
                        int needCountNum = brothersCategory.size() > maxLeafNodeNum ? maxLeafNodeNum : brothersCategory.size();
                        for (int ii = 0; ii < needCountNum; ii++) {
                            hotWordCategory.add(brothersCategory.get(ii));
                        }
                        return hotWordCategory;
                    } else {
                        return hotWordCategory;
                    }
                } else {
                    getBrotherCatByParentId(categoryTreeResult.get(i).getChildren(), parentCategoryId, hotWordCategory);
                }
            }
            return hotWordCategory;
        }
    }

    public SearchHandler getIdSearchHandler() {
        return idSearchHandler;
    }

    public void setIdSearchHandler(SearchHandler idSearchHandler) {
        this.idSearchHandler = idSearchHandler;
    }

}
