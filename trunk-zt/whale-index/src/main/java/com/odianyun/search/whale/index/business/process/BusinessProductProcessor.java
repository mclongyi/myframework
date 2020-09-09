package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.data.service.ProductService;
import com.odianyun.search.whale.index.business.process.base.BaseBusinessProductProcessor;

import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessProductProcessor extends BaseBusinessProductProcessor{

	static Logger log = Logger.getLogger(BusinessProductProcessor.class);
	
    ProductService productService;
    //20180516 DateFormat 非线程安全，用FastDateFormat替换
    //static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss"); 
    
	MerchantService merchantService;
	

    public BusinessProductProcessor(){
    	productService = (ProductService) ProcessorApplication.getBean("productService");
    	
    	merchantService = (MerchantService) ProcessorApplication.getBean("merchantService");
    	
    }
    
	@Override
	public BusinessProduct convert(MerchantProduct merchantProduct) throws Exception {
		// TODO Auto-generated method stub
		if(merchantProduct == null){
			return null;
		}

		BusinessProduct businessProduct = new BusinessProduct();
		businessProduct.setId(merchantProduct.getId());
		businessProduct.setProductId(merchantProduct.getProduct_id());
		businessProduct.setMerchantId(merchantProduct.getMerchant_id());
		businessProduct.setChinese_name(merchantProduct.getChinese_name());
		businessProduct.setEnglish_name(merchantProduct.getEnglish_name());
		businessProduct.setCreate_time(merchantProduct.getCreate_time());
		businessProduct.setIs_available(merchantProduct.getIs_available());
		businessProduct.setIs_deleted(merchantProduct.getIs_deleted());
		businessProduct.setCode(merchantProduct.getCode());
		businessProduct.setIsNew(merchantProduct.getIsNew());
		businessProduct.setSubtitle(merchantProduct.getSubtitle());
		businessProduct.setCompanyId(merchantProduct.getCompany_id());
		businessProduct.setMerchantSeriesId(merchantProduct.getMerchantSeriesId());
		businessProduct.setType(merchantProduct.getType());
		businessProduct.setTax(merchantProduct.getTax());
        businessProduct.setSale_type(merchantProduct.getSale_type());
        businessProduct.setManagementState(merchantProduct.getManagementState());
		businessProduct.setTypeOfProduct(merchantProduct.getTypeOfProduct());
		if(merchantProduct.getFirst_shelf_time()!=null) {
			//businessProduct.setFirst_shelf_time(df.format(merchantProduct.getFirst_shelf_time()));
			businessProduct.setFirst_shelf_time(fdf.format(merchantProduct.getFirst_shelf_time()));
			
		}
		businessProduct.setThirdCode(merchantProduct.getThirdCode());
		//外卖新增字段start
		businessProduct.setSeriesParentId(merchantProduct.getSeriesParentId());
		businessProduct.setMinSize(merchantProduct.getMinSize());
		businessProduct.setMaxSize(merchantProduct.getMaxSize());
		businessProduct.setMerchantType(merchantProduct.getMerchantType());

		businessProduct.setPlaceOfOrigin(merchantProduct.getPlaceOfOrigin());
		businessProduct.setPlaceOfOriginLogo(merchantProduct.getPlaceOfOriginLogo());
		businessProduct.setCardType(merchantProduct.getCardType());
		businessProduct.setCardId(merchantProduct.getCardId());
		//外卖新增字段end
		return businessProduct;
	}

	@Override
	public void calcProduct(Map<Long, BusinessProduct> map) throws Exception {
		List<Long> merchantProductIds = new ArrayList<Long>(map.keySet());
		List<Long> productIds = new ArrayList<Long>();
		int companyId=-1;
		for(Map.Entry<Long, BusinessProduct> entry : map.entrySet()){
			productIds.add(entry.getValue().getProductId());
			companyId=entry.getValue().getCompanyId().intValue();
		}
		List<String> eans = new ArrayList<String>();
		Map<Long,Product> productMap=productService.getProducts(productIds,companyId);
		if(productMap!=null && productMap.size()>0){
			for(Map.Entry<Long,Product> productEntry:productMap.entrySet()){
				String ean = productEntry.getValue().getEan_no();
				if(StringUtils.isNotBlank(ean) && !ean.equals("0")){
					eans.add(ean);
				}
			}
		}
		Map<Long,List<Long>> merchantCateTreeNodeIdMap= productService.getMerchantCateTreeNodeIds(merchantProductIds,companyId);

		//查询所有关联ean
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		if(CollectionUtils.isNotEmpty(eans)){
			try{
				result = productService.getSrcbarcodes(eans,companyId);
			}catch (Exception e){
				log.error("eans is "+eans+",companyId is "+companyId);
				log.error(e.getMessage(),e);
			}

		}

		for(Map.Entry<Long, BusinessProduct> entry : map.entrySet()){
			Long merchantProductId = entry.getKey();
			BusinessProduct businessProduct = entry.getValue();	
			if(productMap != null && productMap.size() > 0){
				Product product=productMap.get(businessProduct.getProductId());

				if(product != null){
					if(product.getEan_no()!=null){
						//拼接ean码
						StringBuffer sb = new StringBuffer("");
						sb.append(product.getEan_no());
						List<String> destBars = result.get(product.getEan_no());
						if(CollectionUtils.isNotEmpty(destBars)){
							for (String dest:destBars) {
								sb.append(" "+dest);
							}
						}
						businessProduct.setEan_no(sb.toString());
					}
					businessProduct.setBrandId(product.getBrandId());
//					businessProduct.setCategoryTreeNodeId(product.getCategory_tree_node_id());
					businessProduct.setCategoryId(product.getCategory_id());

					businessProduct.setProductCode(product.getCode());
					businessProduct.setCalculation_unit(product.getCalculation_unit());
					businessProduct.setStandard(product.getStandard());
				}
			}
			
			if(merchantCateTreeNodeIdMap != null && merchantCateTreeNodeIdMap.size() > 0){
				businessProduct.setMerchant_cate_tree_node_ids(merchantCateTreeNodeIdMap.get(merchantProductId));
			}
		}
	}
	
	//外卖 ProductMerchantProcessor 迁移到此
	@Override
	public void calcProductMerchant(BusinessProduct businessProduct) throws Exception {
		Long merchantId = businessProduct.getMerchantId();
		if(merchantId == null){
			businessProduct.setMerchant_status(false);
			return;
		}
		int companyId=businessProduct.getCompanyId().intValue();
		Merchant merchant = merchantService.getMerchantById(merchantId,companyId);
		if(merchant!=null){
//		    businessProduct.setMerchantName_search(merchant.getCompany_name());
			if(businessProduct.getMerchantType() == null || businessProduct.getMerchantType() == 0){
				businessProduct.setMerchantType(merchant.getMerchant_type());
			}
			businessProduct.setMerchantName_search(merchant.getName());
		}else{
			businessProduct.setMerchant_status(false);
		}
		Shop shop = merchantService.getShop(merchantId,companyId);
		if(shop!=null){
			String merchantName=businessProduct.getMerchantName_search();
			if(StringUtils.isBlank(merchantName)){
				merchantName=shop.getName();
			}else{
				if(shop.getName()!=null){
					merchantName=merchantName+" "+shop.getName();
				}
			}
			businessProduct.setMerchantName_search(merchantName);
		}
	}
}
