package com.odianyun.search.whale.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import com.odianyun.search.whale.api.model.*;
import com.odianyun.search.whale.api.model.MerProScript;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.service.SuperScriptService;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.search.SearchHitField;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.service.BrandService;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class MerchantProductConvertor {
	
	@Autowired
	MerchantService merchantService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	CategoryService categoryService;

	@Autowired
	private SuperScriptService superScriptService;
	
	public MerchantProduct convertFromSearchHitField(Map<String, SearchHitField> data) throws Exception{
		MerchantProduct merchantProduct = new MerchantProduct();
		if(data == null || data.size() == 0){
			return merchantProduct;
		}
		Field[] fields=MerchantProduct.class.getDeclaredFields();
		for(Field field:fields){
			String propertyName = field.getName();
			String setMethodName="set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
			Method setMethod=MerchantProduct.class.getMethod(setMethodName, field.getType());
			if(setMethod==null)
            	continue;        
            String fieldType = field.getType().getSimpleName();  
            if(data.get(propertyName) == null || data.get(propertyName).getValues() == null || data.get(propertyName).getValues().size() == 0){
            	continue;
            }
            Object obj = data.get(propertyName).getValues().get(0);
            if("Integer".equals(fieldType)){
            	if("Long".equals(obj.getClass().getSimpleName())){
		            setMethod.invoke(merchantProduct,((Long)obj).intValue());
				}else{
		            setMethod.invoke(merchantProduct,(Integer)obj);
				}
            	
			}else if("Long".equals(fieldType)){
				if("Integer".equals(obj.getClass().getSimpleName())){
		            setMethod.invoke(merchantProduct,((Integer)obj).longValue());
				}else{
		            setMethod.invoke(merchantProduct,(Long)obj);
				}
			}else if("Double".equals(fieldType)){
	            setMethod.invoke(merchantProduct,(Double)obj);
			}else if("String".equals(fieldType)){
				if("Integer".equals(obj.getClass().getSimpleName())){
		            setMethod.invoke(merchantProduct,((Integer)obj).toString());
				}else{
		            setMethod.invoke(merchantProduct,(String)obj);
				}
			}
		}
		Long merchantId = merchantProduct.getMerchantId();
		int companyId=merchantProduct.getCompanyId().intValue();
		if(merchantId != null && merchantId != 0){
			Shop shop = merchantService.getShop(merchantId,companyId);
			Merchant merchant=merchantService.getMerchantById(merchantId,companyId);
			if(shop!=null){
				merchantProduct.setShopId(shop.getId());
				merchantProduct.setNewShopType(shop.getShop_type());
				merchantProduct.setShopName(shop.getName());
			}
			if(merchant!=null){
				merchantProduct.setMerchantName(merchant.getName());
				merchantProduct.setMerchantType(merchant.getMerchant_type());
			}
        } 
		Long categoryId = merchantProduct.getCategoryId();
		if(categoryId != null && categoryId != 0){
			Category category= categoryService.getCategory(categoryId,merchantProduct.getCompanyId().intValue());
			if(category != null){
				merchantProduct.setCategoryName(category.getName());
			}
		}
		try{
			Object obj = data.get(IndexFieldConstants.BRANDID_SEARCH).getValues().get(0);
            Long brandId=Long.valueOf((String)obj);
            Brand brand = brandService.getBrandById(brandId,companyId);
            if(null != brand){
            	merchantProduct.setBrandName(brand.getName());
            }
            merchantProduct.setBrandId(brandId);
            
		}catch(Exception e){
        	
        }


		//添加商品角标
		SearchHitField scriptHitField = data.get(IndexFieldConstants.SCRIPT_IDS);
		if(scriptHitField!=null) {
			List<Object> objects = scriptHitField.getValues();
			if (objects != null && objects.size() > 0) {
				Object obj = objects.get(0);
				String[] superScriptIds = null;
				if (obj != null) {
					superScriptIds = obj.toString().split(" ");
				}
				List<Long> ids = new ArrayList<Long>();
				if (superScriptIds != null && superScriptIds.length > 0) {
					for (String s : superScriptIds) {
						ids.add(Long.valueOf(s));
					}
					List<SuperScript> merProScripts = superScriptService.queryMerPorScriptByIds(ids, companyId);
					if (CollectionUtils.isNotEmpty(merProScripts)) {
						merchantProduct.setMerProScripts(coverSuperScript(merProScripts));
					}
				}
			}
		}
		return merchantProduct;
	}

	private List<MerProScript> coverSuperScript(List<SuperScript> list) {
		List<MerProScript> retList = new ArrayList<MerProScript>();
		for(SuperScript s:list){
			MerProScript mp = new MerProScript();
			mp.setSuperscriptId(s.getSuperscriptId());
			mp.setDisplayType(s.getDisplayType());
			mp.setIconUrl(s.getIconUrl());
			mp.setName(s.getName());
			mp.setPriority(s.getPriority());
			mp.setCreateTime(s.getCreateTime());
			retList.add(mp);
		}
		if(retList.size()>1){
			Collections.sort(retList, new Comparator<MerProScript>() {
				@Override
				public int compare(MerProScript o1, MerProScript o2) {
					if(o1.getPriority()>o2.getPriority()){
						return -1;
					}else if (o1.getPriority() < o1.getPriority()){
						return 1;
					}else {
						Date date1 = o1.getCreateTime();
						Date date2 = o2.getCreateTime();
						if(date1 == null || date2 == null){
							return 0;
						}
						if(date1.after(date2)){
							return 1;
						}else if(date1.before(date2)){
							return -1;
						}else {
							return 0;
						}
					}
				}
			});
		}
		return retList;
	}

	public MerchantProduct convertFromSearchHitFieldSimple(Map<String, SearchHitField> data) throws Exception{
		MerchantProduct merchantProduct = new MerchantProduct();
		if(data == null || data.size() == 0){
			return merchantProduct;
		}
		Field[] fields=MerchantProduct.class.getDeclaredFields();
		for(Field field:fields){
			String propertyName = field.getName();
			String setMethodName="set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
			Method setMethod=MerchantProduct.class.getMethod(setMethodName, field.getType());
			if(setMethod==null)
            	continue;        
            String fieldType = field.getType().getSimpleName();  
            if(data.get(propertyName) == null || data.get(propertyName).getValues() == null || data.get(propertyName).getValues().size() == 0){
            	continue;
            }
            Object obj = data.get(propertyName).getValues().get(0);
            if("Integer".equals(fieldType)){
	            setMethod.invoke(merchantProduct,(Integer)obj);
			}else if("Long".equals(fieldType)){
				if("Integer".equals(obj.getClass().getSimpleName())){
		            setMethod.invoke(merchantProduct,((Integer)obj).longValue());
				}else{
		            setMethod.invoke(merchantProduct,(Long)obj);
				}
			}else if("Double".equals(fieldType)){
	            setMethod.invoke(merchantProduct,(Double)obj);
			}else if("String".equals(fieldType)){
				if("Integer".equals(obj.getClass().getSimpleName())){
		            setMethod.invoke(merchantProduct,((Integer)obj).toString());
				}else{
		            setMethod.invoke(merchantProduct,(String)obj);
				}
			}
		}
		Long merchantId = merchantProduct.getMerchantId();
		int companyId=merchantProduct.getCompanyId().intValue();
		if(merchantId != null && merchantId != 0){
			Merchant merchant=merchantService.getMerchantById(merchantId,companyId);
			if(merchant!=null){
				merchantProduct.setMerchantName(merchant.getName());
			}
        } 
		return merchantProduct;
	}

}
