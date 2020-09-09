package com.odianyun.search.whale.index.business.process;

import java.util.*;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.CombineProduct;
import com.odianyun.search.whale.data.service.MerchantProductCombineService;
import com.odianyun.search.whale.data.service.MerchantProductStockService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.business.process.base.BaseProductStockProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class ProductStockProcessor extends BaseProductStockProcessor{

	MerchantProductStockService merchantProductStockService;
	private MerchantProductCombineService merchantProductCombineService;

	public ProductStockProcessor(){
		merchantProductStockService = (MerchantProductStockService) ProcessorApplication.getBean("merchantProductStockService");
		merchantProductCombineService = (MerchantProductCombineService) ProcessorApplication.getBean("merchantProductCombineService");
	}
	
	@Override
	public void calcProductStock(Map<Long,BusinessProduct> map,int companyId) throws Exception {
		List<Long> merchantProductIds = new ArrayList<Long>(map.keySet());
//		Map<Long, List<MerchantProductStock>> stockMap=merchantProductStockService.getMerchantProductStocksByTable(merchantProductIds,companyId);
		/*Map<Long, Long> stockMap=merchantProductStockService.getMerchantProductStocksByTable2(merchantProductIds,companyId);
		if(stockMap == null || stockMap.size() == 0){
			return;
		}*/
		Set<Long> stockSet = merchantProductStockService.getMerchantProductWithStocksByTable(merchantProductIds,companyId);
		for(Map.Entry<Long, BusinessProduct> entry : map.entrySet()){
			Long merchantProductId = entry.getKey();
			BusinessProduct businessProduct = entry.getValue();
			int stock=0;
//			List<MerchantProductStock> merchantProductStocks = stockMap.get(merchantProductId);
			/*if(CollectionUtils.isNotEmpty(merchantProductStocks)){
				for(MerchantProductStock merchantProductStock:merchantProductStocks){
					stock=(merchantProductStock.getReal_stock_num()-merchantProductStock.getReal_frozen_stock_num())>0?1:0;
					if(stock>0){
						break;
					}
				}
			}*/
			/*Long mpStock = stockMap.get(merchantProductId);
			if(null != mpStock && mpStock > 0){
				stock = 1;
			}*/
			if(stockSet.contains(merchantProductId)){
				stock = IndexConstants.HAS_STOCK;
			}
			businessProduct.setStock(stock);
		}

		//计算组合商品库存
		List<CombineProduct> combineProductList =  merchantProductCombineService.queryCombineProductsByMpids(merchantProductIds,companyId);
		Set<Long> subMpIds = new HashSet<Long>();
		if(combineProductList.size()==0){//无组合商品
			return;
		}
		for(CombineProduct cp : combineProductList){
			subMpIds.add(cp.getSub_merchant_prod_id());
		}
		Map<Long,Long> stockMap = merchantProductStockService.getMerchantProductTotalStocksByTable(new ArrayList<Long>(subMpIds),companyId);
		Map<Long,Long> combineStock = new HashMap<Long,Long>();
		Set<Long> errorCombine = new HashSet<Long>();//异常的组合商品
		//循环计算
		for(CombineProduct cp : combineProductList){
			Long stock  = combineStock.get(cp.getCombine_product_id());
			if(stock == null){
				stock = -1L;//库存为空
			}
			if(stockMap.containsKey(cp.getSub_merchant_prod_id())){
				if(cp.getCount()!=0) {
					Long tmpStock = stockMap.get(cp.getSub_merchant_prod_id()) / cp.getCount();
					if(stock==-1L){
						stock = tmpStock;
					}else {
						if(tmpStock<stock){
							stock = tmpStock;
						}
					}
				}else{
					errorCombine.add(cp.getCombine_product_id());
				}
			}else {
				errorCombine.add(cp.getCombine_product_id());
			}
			combineStock.put(cp.getCombine_product_id(),stock);
		}
		//移除异常组合商品
		if(errorCombine.size()>0){
			for(Long id : errorCombine){
				if(combineStock.containsKey(id)){
					combineStock.remove(id);
				}
			}
		}
		for(Map.Entry<Long,Long> entry : combineStock.entrySet()){
			if(map.containsKey(entry.getKey())){
				BusinessProduct businessProduct = map.get(entry.getKey());
				if(entry.getValue()>=1){//组合商品有库存
					businessProduct.setStock(IndexConstants.HAS_STOCK);
				}else {
					businessProduct.setStock(0);//无库存
				}
			}
		}

	}
}
