package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.Brand;


public interface BrandService {
	
	public Brand getBrandById(Long brandId,int companyId) throws Exception;

	public Map<Integer,List<String>> getAllBrandsName() throws Exception;

	public Map<Long,Brand> getBrands(List<Long> brandIds,int companyId) throws Exception;
	
	//返回brandId->BrandName
	public Map<Long, Brand> getBrandMap(List<Long> brandIds,int companyId) throws Exception;

	public Brand getBrand(String name,int companyId) throws Exception;

}
