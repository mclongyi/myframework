package com.odianyun.search.whale.index.business.process;

import java.util.*;

import com.odianyun.search.whale.common.util.LyfStringUtils;
import com.odianyun.search.whale.data.model.MerchantCateTreeNode;
import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.data.service.MerchantCategoryService;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.index.business.process.base.BaseProductCategoryProcessor;
import com.odianyun.search.whale.index.common.O2OConstants;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.log4j.Logger;

public class ProductCategoryProcessor extends BaseProductCategoryProcessor {

    CategoryService categoryService;

    MerchantCategoryService merchantCategoryService;

    MerchantService merchantService;

    static Logger logger = Logger.getLogger(ProductCategoryProcessor.class);

    public ProductCategoryProcessor() {
        categoryService = (CategoryService) ProcessorApplication.getBean("categoryService");
        merchantCategoryService = (MerchantCategoryService) ProcessorApplication.getBean("merchantCategoryService");
        merchantService = (MerchantService) ProcessorApplication.getBean("merchantService");
    }

    @Override
    public void calcProductCategory(BusinessProduct businessProduct) throws Exception {
        calcCategory(businessProduct);
        calcNaviCategoryAllType(businessProduct);
        calcMerchantCategory(businessProduct);

        filterRepeatedSearch(businessProduct);
    }

    /**
     * 过滤重复数据
     * @param businessProduct
     */
    private void filterRepeatedSearch(BusinessProduct businessProduct) {
        businessProduct.setCategoryName_search(LyfStringUtils.filterRepeatedString(businessProduct.getCategoryName_search()," "));
        businessProduct.setCategoryId_search(LyfStringUtils.filterRepeatedString(businessProduct.getCategoryId_search()," "));

        businessProduct.setMerchantName_search(LyfStringUtils.filterRepeatedString(businessProduct.getMerchantName_search()," "));
        businessProduct.setMerchantCategoryId_search(LyfStringUtils.filterRepeatedString(businessProduct.getMerchantCategoryId_search()," "));

        businessProduct.setNavCategoryId_search(LyfStringUtils.filterRepeatedString(businessProduct.getNavCategoryId_search()," "));
    }

    @Override
    public void calcProductNaviCategory(List<BusinessProduct> businessProducts, Integer companyId) throws Exception {
        calcMerchantProductNaviCategory(businessProducts, companyId);
    }

    private void calcMerchantCategory(BusinessProduct businessProduct) throws Exception {
        int companyId = businessProduct.getCompanyId().intValue();
        List<Long> merchantTreeNodeIds = businessProduct.getMerchant_cate_tree_node_ids();
        if (merchantTreeNodeIds == null) {
            merchantTreeNodeIds = new ArrayList<Long>();
        }
        Long merchantId = businessProduct.getMerchantId();
        if (O2OConstants.MemberType_O2O.equals(businessProduct.getMerchantType())) {
            Merchant merchant = merchantService.getMerchantById(merchantId, companyId);
            if (merchant != null && merchant.getParentId() != 0L) {
                merchantId = merchant.getParentId();
            }
        }

        Long parentCategoryId = businessProduct.getCategoryId();
        while (parentCategoryId != null && parentCategoryId > 0) {
            List<Long> merchantCategoyIdRefs = merchantCategoryService.getMerchantCategoryIdByCategoryIdMerchantId(parentCategoryId, merchantId, companyId);
            if (CollectionUtils.isNotEmpty(merchantCategoyIdRefs)) {
                merchantTreeNodeIds.addAll(merchantCategoyIdRefs);
            }
            parentCategoryId = categoryService.getParentCategoryId(parentCategoryId, companyId);
        }
        if (CollectionUtils.isEmpty(merchantTreeNodeIds)) {
            return;
        }
        StringBuffer merchantCategoryNameSearch = new StringBuffer(businessProduct.getCategoryName_search() == null ? "" : businessProduct.getCategoryName_search());

        StringBuffer merchantCategoryIdSearch = new StringBuffer();
        StringBuffer merchantLeafCategoryIds = new StringBuffer();

        for (Long merchantTreeNodeId : merchantTreeNodeIds) {
            merchantLeafCategoryIds.append(" ").append(merchantTreeNodeId);
            List<MerchantCateTreeNode> merchantCateTreeNodes = merchantCategoryService.getFullPathMerchantCategoryByTreeNodeId(merchantTreeNodeId, companyId);
            if (merchantCateTreeNodes != null) {
                for (MerchantCateTreeNode merchantCateTreeNode : merchantCateTreeNodes) {
                    merchantCategoryIdSearch.append(" ").append(merchantCateTreeNode.getId());
                    merchantCategoryNameSearch.append(" ").append(merchantCateTreeNode.getName());
                }
            }
        }
        businessProduct.setMerchant_categoryId(merchantLeafCategoryIds.toString());
        businessProduct.setMerchantCategoryId_search(merchantCategoryIdSearch.toString());
        businessProduct.setCategoryName_search(merchantCategoryNameSearch.toString());
    }


    private void calcNaviCategoryAllType(BusinessProduct businessProduct) throws Exception {
        calcNaviCategory(businessProduct);
        calcNaviCategoryInclude(businessProduct);
        calcNaviCategoryExclude(businessProduct);
        //calcMerchantProductNaviCategory(businessProduct);
    }

    private void calcMerchantProductNaviCategory(List<BusinessProduct> businessProducts, Integer companyId) throws Exception {
        List<Long> ids = new ArrayList<Long>();
        List<Long> o2oSeriesParentIds = new ArrayList<>();
        for (BusinessProduct businessProduct : businessProducts) {
            ids.add(businessProduct.getId());
            //外卖单独处理
            if (O2OConstants.MemberType_O2O.equals(businessProduct.getMerchantType())) {
                if (businessProduct.getSeriesParentId() != null && businessProduct.getSeriesParentId() != 0) {
                    o2oSeriesParentIds.add(businessProduct.getSeriesParentId());
                }
            }
        }
        Map<Long, Set<Category>> retMap = categoryService.getNavicCategoryByMpIds(ids, companyId);
        Map<Long, Long> categoryIdByMpIds = categoryService.getNavicFrontCategoryIdByMpIds(o2oSeriesParentIds, companyId);
        if (retMap.isEmpty() && categoryIdByMpIds.isEmpty()) {
            return;
        }
        for (BusinessProduct businessProduct : businessProducts) {
            StringBuffer caNameSearch = new StringBuffer(businessProduct.getCategoryName_search() == null ? "" : businessProduct.getCategoryName_search());
            StringBuffer caIdSearch = new StringBuffer(businessProduct.getNavCategoryId_search() == null ? "" : businessProduct.getNavCategoryId_search());
            StringBuffer mCaIdSearch = new StringBuffer(businessProduct.getMerchantCategoryId_search() == null ? "" : businessProduct.getMerchantCategoryId_search());
            //商品自己的类目关系
            Set<Category> categorys = retMap.get(businessProduct.getId());
            if (CollectionUtils.isNotEmpty(categorys)) {
                for (Category cate : categorys) {
                    caIdSearch.append(" ").append(cate.getId());
                    caNameSearch.append(" ").append(cate.getName());
                }
            }
            //聚合类目关系
            //针对门店外卖商品 加一个类目ID--》创建索引时，根据父商品id seriesParentId 查类目 fore_category_tree_node_id     `series_parent_id` bigint(20) DEFAULT NULL COMMENT '父品id',
            if (O2OConstants.MemberType_O2O.equals(businessProduct.getMerchantType())) {
                if (businessProduct.getSeriesParentId() != null && businessProduct.getSeriesParentId() != 0) {
                    Long frontCategoryId = categoryIdByMpIds.get(businessProduct.getSeriesParentId());
                    if (frontCategoryId != null) {
                        caIdSearch.append(" ").append(frontCategoryId.toString());
                        mCaIdSearch.append(" ").append(frontCategoryId.toString());
                    }
                }
            }
            businessProduct.setCategoryName_search(caNameSearch.toString());
            businessProduct.setNavCategoryId_search(caIdSearch.toString());
            businessProduct.setMerchantCategoryId_search(mCaIdSearch.toString());
        }

    }

    private void calcNaviCategoryInclude(BusinessProduct businessProduct) throws Exception {
        int companyId = businessProduct.getCompanyId().intValue();
        Set<Long> naviCategoryIds = categoryService.getIncludeNaviCategorys(businessProduct.getProductId(), companyId);
        if (CollectionUtils.isEmpty(naviCategoryIds)) {
            return;
        }
        Set<Category> categorySet = new HashSet<Category>();
        for (Long id : naviCategoryIds) {
            categorySet.addAll(categoryService.getNaviCategorys(id, companyId));
        }
        appendCategoryNameAndNavCategoryId(categorySet, businessProduct);
    }

    private void calcNaviCategoryExclude(BusinessProduct businessProduct) throws Exception {
        int companyId = businessProduct.getCompanyId().intValue();
        Set<Long> excludeNaviCategoryIds = categoryService.getExcludeNaviCategorys(businessProduct.getProductId(), companyId);
        if (CollectionUtils.isEmpty(excludeNaviCategoryIds)) {
            return;
        }

        Long categoryId = businessProduct.getCategoryId();
        if (categoryId == null || categoryId == 0) {
            return;
        }
        List<Long> categoryIds = new ArrayList<Long>();
        categoryIds.add(categoryId);
        List<Long> parentCategoryIds = categoryService.getAllParentCategoryId(categoryId, businessProduct.getCompanyId().intValue());
        if (CollectionUtils.isNotEmpty(parentCategoryIds)) {
            categoryIds.addAll(parentCategoryIds);
        }
        List<Category> categorys = categoryService.getNaviCategorys(categoryIds, excludeNaviCategoryIds, companyId);
        appendCategoryNameAndNavCategoryId(categorys, businessProduct);

    }

    private void calcNaviCategory(BusinessProduct businessProduct) throws Exception {
        Long categoryId = businessProduct.getCategoryId();
        if (categoryId == null || categoryId == 0) {
            return;
        }
        List<Long> categoryIds = new ArrayList<Long>();
        categoryIds.add(categoryId);
        List<Long> parentCategoryIds = categoryService.getAllParentCategoryId(categoryId, businessProduct.getCompanyId().intValue());
        if (CollectionUtils.isNotEmpty(parentCategoryIds)) {
            categoryIds.addAll(parentCategoryIds);
        }

        List<Category> categorys = categoryService.getLeftCategorys(categoryIds, businessProduct.getCompanyId().intValue());
        appendCategoryNameAndNavCategoryId(categorys, businessProduct);

        //处理父类目
        StringBuffer categoryName_sb = new StringBuffer(businessProduct.getCategoryName_search() == null ? "" : businessProduct.getCategoryName_search());
        StringBuffer navCategoryId_sb = new StringBuffer(businessProduct.getNavCategoryId_search() == null ? "" : businessProduct.getNavCategoryId_search());
        for (Category c : categorys) {
            List<Long> allParentCategoryId = categoryService.getAllParentCategoryId(c.getId(), businessProduct.getCompanyId().intValue());
            for (Long parentCategoryId : allParentCategoryId) {
                Category category = categoryService.getCategory(parentCategoryId, businessProduct.getCompanyId().intValue());
                if (category != null) {
                    categoryName_sb.append(" ").append(category.getName());
                }
                navCategoryId_sb.append(" ").append(parentCategoryId);
            }
        }
        businessProduct.setCategoryName_search(categoryName_sb.toString());
        businessProduct.setNavCategoryId_search(navCategoryId_sb.toString());
    }

    private void calcCategory(BusinessProduct businessProduct) throws Exception {
        Long categoryId = businessProduct.getCategoryId();
        if (categoryId == null || categoryId == 0) {
            return;
        }
        List<Category> categories = categoryService.getFullPathCategory(categoryId, businessProduct.getCompanyId().intValue());

        businessProduct.setCategoryName_search(appendCategoryName(categories, businessProduct.getCategoryName_search()));
        businessProduct.setCategoryNameBuff(businessProduct.getCategoryName_search());
        businessProduct.setCategoryId_search(appendCategoryId(categories, businessProduct.getCategoryId_search()));
    }

    /**
     * 将类目名词和导航ID添加到商品搜索字段中去
     *
     * @param categories
     * @param businessProduct
     */
    private void appendCategoryNameAndNavCategoryId(Collection<Category> categories, BusinessProduct businessProduct) {
        if (categories != null && !categories.isEmpty() && businessProduct != null) {
            StringBuffer categoryNameSearch = new StringBuffer(businessProduct.getCategoryName_search() == null ? "" : businessProduct.getCategoryName_search());
            StringBuffer navCategoryIdSearch = new StringBuffer(businessProduct.getNavCategoryId_search() == null ? "" : businessProduct.getNavCategoryId_search());
            for (Category cate : categories) {
                categoryNameSearch.append(" ").append(cate.getName());
                navCategoryIdSearch.append(" ").append(cate.getId());
            }
            businessProduct.setCategoryName_search(categoryNameSearch.toString());
            businessProduct.setNavCategoryId_search(navCategoryIdSearch.toString());
        }
    }

    /**
     * 拼接类目名字
     *
     * @param categories
     * @param orign
     * @return
     */
    private String appendCategoryName(Collection<Category> categories, String orign) {
        StringBuffer categoryNameSearch = new StringBuffer(orign == null ? "" : orign);
        if (categories != null && !categories.isEmpty()) {
            for (Category cate : categories) {
                categoryNameSearch.append(" ").append(cate.getName());
            }
        }
        return categoryNameSearch.toString();
    }

    /**
     * 拼接类目ID
     *
     * @param categories
     * @param orign
     * @return
     */
    private String appendCategoryId(Collection<Category> categories, String orign) {
        StringBuffer categoryIdSearch = new StringBuffer(orign == null ? "" : orign);
        if (categories != null && !categories.isEmpty()) {
            for (Category cate : categories) {
                categoryIdSearch.append(" ").append(cate.getId());
            }
        }
        return categoryIdSearch.toString();
    }
}
