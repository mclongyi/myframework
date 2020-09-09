package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.Brand;

public interface BrandDao {
	/**
	 * 读取所有的品牌数据,适用于全量索引
	 * @return
	 * @throws Exception
	 */
	public List<Brand> queryAllBrand(int companyId) throws Exception;
	
	/**
	 * 根据品牌ID列表获取相应的品牌数据，适用于实时索引
	 * @param brandIds
	 * @return
	 * @throws Exception
	 */
	public List<Brand> getBrands(List<Long> brandIds, int companyId) throws Exception;

}
