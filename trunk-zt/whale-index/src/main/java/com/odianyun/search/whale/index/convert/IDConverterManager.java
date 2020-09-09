package com.odianyun.search.whale.index.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.odianyun.search.whale.index.api.common.UpdateType;

public class IDConverterManager {
	
	public static IDConverterManager instanse = new IDConverterManager();
		
	private static Logger log = Logger.getLogger(IDConverterManager.class);

	private Map<UpdateType,IDConverter> converterMap = new HashMap<UpdateType,IDConverter>(){{
		put(UpdateType.merchant_product_id,new SeriesConverter());
		put(UpdateType.brand_id,new BrandConverter());
		put(UpdateType.category_tree_node_id,new CateTreeNodeConverter());
		put(UpdateType.product_id,new ProductConverter());
		put(UpdateType.merchant_series_id,new SeriesMerchantProductConverter());
		put(UpdateType.product_series_id,new ProductSeriesMerchantProductConverter());
		put(UpdateType.points_mall_product_id,new PointsMallProductConverter());

	}};
	
	public List<Long> convert(List<Long> ids, UpdateType updateType,int companyId){
		List<Long> convertIdList = new ArrayList<Long>();
		IDConverter converter = converterMap.get(updateType);
		try {
			convertIdList.addAll(converter.convert(ids, companyId));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return convertIdList;
	}
	
}
