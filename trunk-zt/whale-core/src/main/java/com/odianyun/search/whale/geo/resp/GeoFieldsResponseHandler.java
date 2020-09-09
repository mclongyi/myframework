package com.odianyun.search.whale.geo.resp;

import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.common.PointConverter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.springframework.beans.factory.annotation.Autowired;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.data.model.MerchantBelongArea;
import com.odianyun.search.whale.data.service.AreaService;
import com.odianyun.search.whale.data.service.MerchantService;
import java.util.List;
import java.util.ArrayList;

public class GeoFieldsResponseHandler implements GeoResponseHandler{
	
	@Autowired
	MerchantService merchantService;
	@Autowired
	AreaService areaService;

	@Override
	public void handle(SearchResponse esSearchResponse,
			GeoSearchResponse geoSearchResponse) throws Exception {
		int companyId=geoSearchResponse.getCompanyId();
		geoSearchResponse.setTotalHit(esSearchResponse.getHits().getTotalHits());
		SearchHit[] searchHitArray=esSearchResponse.getHits().getHits();
		List<Merchant> merchants=new ArrayList<Merchant>();
		for(SearchHit hit:searchHitArray){
	    	Long id=Long.valueOf(hit.getId());   	
	    	Merchant merchant=new Merchant();
	    	merchant.setId(id);
			SearchHitField point = hit.field("location");
			if(point!=null) {
				merchant.setPoint(PointConverter.convert(point));
			}
	    	Object[] objArray=hit.getSortValues();
			//TODO
			// geo只按距离排序,所以取objArray[0],
			// olpus 排序加入了营业时间的业务排序,距离排序是最后,所以取objArray[2],
			// 都是取出最后一个,以后需要根据sortBuilder中distanceSorter的位置取distance
			int distanceIndex = objArray.length - 1 ;
	    	if(objArray!=null&&objArray.length>0){
	    		Double distance=(Double) objArray[distanceIndex];
	    		merchant.setDistance(String.format("%.2f", distance).toString());
	    	}
	    	com.odianyun.search.whale.data.model.Merchant merchantDTO=merchantService.getMerchantById(id,companyId);
	    	if(merchantDTO!=null){
	    		merchant.setName(merchantDTO.getName());
		    	merchant.setVirtualType(merchantDTO.getVirtualType());
		    	merchant.setLinkman(merchantDTO.getLinkman());
		    	merchant.setTel(merchantDTO.getTel());
				merchant.setName(merchantDTO.getName());
				merchant.setParentId(merchantDTO.getParentId());
	    	}
	    	MerchantBelongArea belongArea = merchantService.getBelongAreaByMerchantId(id,companyId);
	    	if(belongArea!=null && belongArea.getArea_code()!=null){
	    		List<Long> areaCodes=new ArrayList<Long>();
	    		areaCodes.add(belongArea.getArea_code());
    			List<Long> parentAreaCodes=areaService.getAllParentAreaCode(belongArea.getArea_code(),companyId);
    			if(parentAreaCodes!=null){
    				areaCodes.addAll(parentAreaCodes);
    			}
    			merchant.setAreaCodes(areaCodes);
	    	}
	        merchants.add(merchant);
	    }
		geoSearchResponse.setMerchants(merchants);
	}


}
