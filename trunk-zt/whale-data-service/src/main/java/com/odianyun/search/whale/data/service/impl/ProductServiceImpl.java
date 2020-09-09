package com.odianyun.search.whale.data.service.impl;

import java.util.*;

import com.odianyun.search.whale.data.dao.MerchantProductBarcodeDao;
import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.model.ProductBarcodeBind;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.ProductDao;
import com.odianyun.search.whale.data.model.MerchantProdMerchantCateTreeNode;
import com.odianyun.search.whale.data.model.Product;
import com.odianyun.search.whale.data.service.ProductService;

public class ProductServiceImpl implements ProductService{
	
	@Autowired
	ProductDao productDao;

	@Autowired
	MerchantProductDao merchantProductDao;

	@Autowired
	MerchantProductBarcodeDao merchantProductBarcodeDao;


	
	/*Map<Long,Product> productMap;

	@Override
	public Map<Long,Product> getAllProducts() throws Exception {	
		if(productMap != null && productMap.size() > 0){
			return productMap;
		}
		reload();
		return productMap;
	}*/

	@Override
	public Map<Long,Product> getProducts(List<Long> productIds, int companyId) throws Exception {
		List<Product> products= productDao.getProducts(productIds, companyId);
		Map<Long,Product> productMap=new HashMap<Long,Product>();
		if(products!=null){
			for(Product p:products){
				productMap.put(p.getId(), p);
			}
		}
		return productMap;
	}

	/*@Override
	public void reload() throws Exception {
		List<Product> products= productDao.queryAllProducts();
		Map<Long,Product> tempProductMap=new HashMap<Long,Product>();
		if(products!=null){
			for(Product p:products){
				tempProductMap.put(p.getId(), p);
			}
		}		
		if(tempProductMap.size() > 0){
			productMap = tempProductMap;
		}
	}*/

	@Override
	public Map<Long, List<Long>> getMerchantCateTreeNodeIds(List<Long> merchantProductIds, int companyId) throws Exception{
		// TODO Auto-generated method stub
		Map<Long,List<Long>> map = new HashMap<Long,List<Long>>();
		List<MerchantProdMerchantCateTreeNode> nodeList = productDao.getMerchantCateTreeNodeIds(merchantProductIds, companyId);
		if(nodeList != null && nodeList.size() > 0){
			for(MerchantProdMerchantCateTreeNode node :nodeList){
				Long merchantCateTreeNodeId = node.getMerchantCateTreeNodeId();
				Long merchantProductId = node.getMerchantMroductId();
				List<Long> merchantCateTreeNodeIds = map.get(merchantProductId);
				if(merchantCateTreeNodeIds == null){
					merchantCateTreeNodeIds = new ArrayList<Long>();
					map.put(merchantProductId, merchantCateTreeNodeIds);
				}
				merchantCateTreeNodeIds.add(merchantCateTreeNodeId);
			}
		}
		return map;
	}

	@Override
	public Map<Long, List<String>> getAllMp_Eans(List<Long> merchantProductIds, int companyId) throws Exception {
		Map<Long,List<String>> result = new HashMap<Long,List<String>>();
		if(null!=merchantProductIds && !merchantProductIds.isEmpty()){
			for (Long mpId:merchantProductIds) {
				List<String> eans = merchantProductDao.queryAllBindEans(mpId,companyId);
				result.put(mpId,eans);
			}
		}
		return result;
	}

	@Override
	public Map<String,List<String>> getSrcbarcodes(List<String> destBarcodes,Integer companyId) throws Exception {
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		List<ProductBarcodeBind> prodBars = merchantProductBarcodeDao.queryEanByDestBarcode(destBarcodes,companyId);
		if(CollectionUtils.isNotEmpty(prodBars)){
			for (ProductBarcodeBind proBar:prodBars) {
				String dest = proBar.getDestBarcode();
				String src = proBar.getSrcBarcode();

				List<String> srcList=result.get(dest);
				if(CollectionUtils.isEmpty(srcList)){
					srcList=new ArrayList<String>();
					result.put(dest,srcList);
				}
				srcList.add(src);
			}
		}
		return result;
	}


}
