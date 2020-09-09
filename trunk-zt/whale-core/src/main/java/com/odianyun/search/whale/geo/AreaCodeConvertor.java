package com.odianyun.search.whale.geo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.odianyun.search.whale.api.model.geo.Area;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.data.service.AreaService;

public class AreaCodeConvertor {
	
	static Logger log = Logger.getLogger(AreaCodeConvertor.class);
	
	@Autowired
	AreaService areaService;
	
	public Long convert(Area area,int companyId){
		Long areaCode=null;
		List<String> areaNames=new ArrayList<String>();
		if(area.getAreaName()!=null){
			areaNames.add(area.getAreaName());
		}
		if(area.getCityName()!=null){
			areaNames.add(area.getAreaName());
		}
		
		if(area.getProvinceName()!=null){
			areaNames.add(area.getProvinceName());
		}
		
        for(String areaName:areaNames){
        	areaCode=getAreaCode(areaName,companyId);
        	if(areaCode!=null){
        		return areaCode;
        	}
        }
        return areaCode;
		
	}
	
	private Long getAreaCode(String areaName,int companyId){
		com.odianyun.search.whale.data.model.Area area;
		try {
			area = areaService.getAreaByName(areaName,companyId);
			if(area!=null){
				return area.getCode();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

}
