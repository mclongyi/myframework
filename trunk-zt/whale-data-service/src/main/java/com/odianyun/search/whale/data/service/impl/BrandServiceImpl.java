package com.odianyun.search.whale.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.BrandDao;
import com.odianyun.search.whale.data.model.Brand;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.BrandService;

public class BrandServiceImpl extends AbstractCompanyDBService implements BrandService{
	
	@Autowired
	BrandDao brandDao;

	Map<Integer,BrandCacheContext> brandCacheContexts=new HashMap<Integer,BrandCacheContext>();
	
	@Override
	public Brand getBrandById(Long brandId,int companyId) throws Exception{
		BrandCacheContext brandCacheContext = brandCacheContexts.get(companyId);
		if(null != brandCacheContext){
			return brandCacheContext.brandMap.get(brandId);
		}
		return null;
	}

	@Override
	public Map<Long,Brand> getBrands(List<Long> brandIds,int companyId) throws Exception {
		List<Brand> brands=brandDao.getBrands(brandIds,companyId);
		Map<Long,Brand> brandMap=new HashMap<Long,Brand>();
		if(brands!=null){
			for(Brand b:brands){
				brandMap.put(b.getId(),b);
			}
		}
		return brandMap;
	}

	@Override
	public Map<Integer,List<String>> getAllBrandsName() throws Exception {
		Map<Integer,List<String>> allBrandsName= new HashMap<>();
		for (Map.Entry<Integer,BrandCacheContext> c:brandCacheContexts.entrySet()){
			List<String> tmp=new ArrayList<>();
			allBrandsName.put(c.getKey(), tmp);
			Map<Long,Brand> brandMap=c.getValue().brandMap;
			for(Map.Entry<Long,Brand> cc:brandMap.entrySet()){
				tmp.add(cc.getValue().getName());
			}
		}
		return allBrandsName;
	}
	@Override
	protected void tryReload(int companyId) throws Exception {
		loadBrandData(companyId);
	}

	private void loadBrandData(int companyId) throws Exception{
		List<Brand> brands=brandDao.queryAllBrand(companyId);
		Map<Long,Brand> tempBrandMap=new ConcurrentHashMap<Long,Brand>();
		Map<String,Brand> tempBrandNameMap=new ConcurrentHashMap<String,Brand>();
		BrandCacheContext brandCacheContext=new BrandCacheContext();
		brandCacheContext.brandMap=tempBrandMap;
		brandCacheContext.brandNameMap=tempBrandNameMap;
		if(brands!=null){
			for(Brand b:brands){
				tempBrandMap.put(b.getId(),b);
				String brandName=b.getName();
				if(StringUtils.isNotBlank(brandName)){
					tempBrandNameMap.put(brandName.trim().toLowerCase(),b);
				}

			}

		}		
		brandCacheContexts.put(companyId, brandCacheContext);
	}


	@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		return 30;
	}

	@Override
	public Map<Long, Brand> getBrandMap(List<Long> brandIds,int companyId) throws Exception {
		Map<Long, Brand> map = new HashMap<Long,Brand>();
		BrandCacheContext brandCacheContext=brandCacheContexts.get(companyId);
		if(CollectionUtils.isNotEmpty(brandIds) && brandCacheContext!=null){
			for(Long id : brandIds){
				Brand brand = brandCacheContext.brandMap.get(id);
				if(brand != null){
					map.put(id, brand);
				}
			}
		}
		return map;
	}

	@Override
	public Brand getBrand(String name, int companyId) throws Exception {
		Brand brand=null;
		BrandCacheContext brandCacheContext=brandCacheContexts.get(companyId);
		if(brandCacheContext!=null){
             brand=brandCacheContext.brandNameMap.get(name.trim().toLowerCase());
		}
		return brand;
	}

	@Override
	protected void tryReload(List<Long> ids,int companyId) throws Exception {
		loadBrandData(ids,companyId);
	}

	private void loadBrandData(List<Long> ids,int companyId) throws Exception{
		List<Brand> brands = brandDao.getBrands(ids,companyId);
		BrandCacheContext brandCacheContext=brandCacheContexts.get(companyId);
		if(brandCacheContext == null){
			brandCacheContext=new BrandCacheContext();
			brandCacheContexts.put(companyId,brandCacheContext);
		}
		if(brands != null && brands.size() > 0){
			for(Brand b:brands){
				brandCacheContext.brandMap.put(b.getId(),b);
				brandCacheContext.brandNameMap.put(b.getName().trim().toLowerCase(),b);
			}
		}		
	}

	public static class BrandCacheContext{
		Map<Long,Brand> brandMap=new ConcurrentHashMap<Long,Brand>();
		Map<String,Brand> brandNameMap=new ConcurrentHashMap<String,Brand>();
	}

}
