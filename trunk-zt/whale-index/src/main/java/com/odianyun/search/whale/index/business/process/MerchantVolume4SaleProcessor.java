package com.odianyun.search.whale.index.business.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantProductSale;
import com.odianyun.search.whale.index.sort.MerchantProductSaleSorter;
import com.odianyun.search.whale.data.model.ProductType;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.Volume4Sale;
import com.odianyun.search.whale.data.service.Volume4SaleService;
import com.odianyun.search.whale.index.business.process.base.BaseMerchantVolume4SaleProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class MerchantVolume4SaleProcessor extends BaseMerchantVolume4SaleProcessor {
	
	Volume4SaleService volume4SaleService;
	
	//构造函数加载service
	public MerchantVolume4SaleProcessor(){
		volume4SaleService = (Volume4SaleService) ProcessorApplication.getBean("volume4SaleService");
	}
	
	@Override
	public void calcMerchantVolume4Sale(Map<Long, BusinessProduct> map,int companyId,String indexName)
			throws Exception {
		//拿到商品Id 的 集合
		List<Long> merchantProductIds = new ArrayList<Long>(map.keySet()); 
		//产生商品和销售数量的Map
//		Map<Long, Long> volume4SaleMap = volume4SaleService.getMerchantVolume4SaleByIds(merchantProductIds,companyId);
		Map<Long, Volume4Sale> volume4SaleMap = volume4SaleService.getMerchantVolume4SaleDetailByIds(merchantProductIds,companyId);

		
		if(volume4SaleMap == null || volume4SaleMap.size() == 0 ){
			return ;
		}

		//获得商品Id,商品 ，通过Id获取销售数  加入到business中去 
		for(Map.Entry<Long,BusinessProduct> entry : map.entrySet()){
			Long merchantProductId = entry.getKey();
			BusinessProduct businessProduct = entry.getValue();
			/*if (StringUtils.isEmpty(volume4SaleMap.get(merchantProductId))) { 	//非空处理
				businessProduct.setVolume4sale(0L);
			} else {
				businessProduct.setVolume4sale(volume4SaleMap.get(merchantProductId)); 
			}*/
			Volume4Sale volume4Sale = volume4SaleMap.get(merchantProductId);
			if(null != volume4Sale){
				if(ProductType.VIRTUAL_CODE.getCode().equals(businessProduct.getTypeOfProduct()) ||
						ProductType.NORMAL.getCode().equals(businessProduct.getTypeOfProduct())){
					businessProduct.setVolume4sale(volume4Sale.getSale());
				}else{
					businessProduct.setVolume4sale(volume4Sale.getRealSale());
				}
				businessProduct.setRealVolume4sale(volume4Sale.getRealSale());
			}
			if(!indexName.contains("alias")){
				if(businessProduct.getManagementState() == 1){
					calcHotSaleMerchantProduct(businessProduct,indexName);
				}
			}
		}
		
	}

	private void calcHotSaleMerchantProduct(BusinessProduct businessProduct,String indexName) {

		MerchantProductSale mpSale = new MerchantProductSale(businessProduct.getCompanyId(),
				businessProduct.getMerchantId(),
				businessProduct.getId(),
				businessProduct.getVolume4sale());
		mpSale.setVersion(indexName);
		MerchantProductSaleSorter.instance.doSort(mpSale);

	}

}
