package com.odianyun.search.whale.index.business.process;

import java.util.Map;

import com.odianyun.search.whale.data.model.Brand;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.service.BrandService;
import com.odianyun.search.whale.index.business.process.base.BaseProductBrandProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class ProductBrandProcessor extends BaseProductBrandProcessor{
	
	BrandService brandService;

	public ProductBrandProcessor(){
		brandService = (BrandService) ProcessorApplication.getBean("brandService");
	}

	@Override
	public void calcProductBrand(BusinessProduct businessProduct) throws Exception{
		// TODO Auto-generated method stub
		
		Long brandId=businessProduct.getBrandId();
		if(brandId == null){
			return;
		}
		Brand brand=brandService.getBrandById(brandId, businessProduct.getCompanyId().intValue());
		if(brand!=null){
			businessProduct.setBrandId_search(String.valueOf(brandId));
			StringBuffer brandBuffer=new StringBuffer();
			if(brand.getChinese_name()!=null){
				brandBuffer.append(brand.getChinese_name()+" ");
			}
			if(brand.getEnglish_name()!=null){
				brandBuffer.append(brand.getEnglish_name()+" ");
			}
			if(brand.getName()!=null){
				brandBuffer.append(brand.getName());
			}
			businessProduct.setBrandName_search(brandBuffer.toString());
		}
	}

}
