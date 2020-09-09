package com.odianyun.search.whale.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import org.springframework.beans.factory.annotation.Autowired;



import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.data.dao.AreaDao;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.AreaService;
  
 /**
  * 
  * @author yuqian
  *
  */
public class AreaServiceImpl extends AbstractCompanyDBService  implements
		AreaService {

	AreaDao areaDao;
	Map<Integer,AreaCacheContext> areaCacheContexts=new ConcurrentHashMap<Integer,AreaCacheContext>();
	
	public void reloadAreaData(AreaCacheContext areaCacheContext,int companyId) throws Exception{
		List<Area> areaList = areaDao.queryAllAreas(companyId);
		if(areaList==null|| areaList.isEmpty())
			return;
		Map<Long,Area> areaMap_temp = new HashMap<Long,Area>();
		Map<Long,Long> code2ParentCodeMap_temp = new HashMap<Long,Long>();
		Map<String,Area> areaNameMap_temp=new HashMap<String,Area>();
		for(Area a:areaList){
			areaMap_temp.put(a.getCode(), a);
			code2ParentCodeMap_temp.put(a.getCode(), a.getParentCode());
			areaNameMap_temp.put(a.getName(), a);
		}
		areaCacheContext.areaMap=areaMap_temp;
		areaCacheContext.code2ParentCodeMap=code2ParentCodeMap_temp;
		areaCacheContext.areaNameMap=areaNameMap_temp;
		
	}
	
	@Override
	public List<Area> getFullPathArea(Long areaCode,int companyId) throws Exception {
		Map<Long,Area> areaMap=areaCacheContexts.get(companyId).areaMap;
		List<Area> ret = new ArrayList<Area>();
		Area area = areaMap.get(areaCode);
		while(area!=null){
			ret.add(area);
			Long parentCode = area.getParentCode();
			if(parentCode!=null){
				area = areaMap.get(parentCode);
			}else{
				area=null;
			}
			
		}
		return ret;
	}

	@Override
	public List<Long> getAllParentAreaCode(Long areaCode,int companyId) throws Exception {
		Map<Long,Long> code2ParentCodeMap = areaCacheContexts.get(companyId).code2ParentCodeMap;
		List<Long> ret = new ArrayList<Long>();
		Long parentCode = code2ParentCodeMap.get(areaCode);
		while(parentCode!=null&&parentCode!=0){
			ret.add(parentCode);
			parentCode = code2ParentCodeMap.get(parentCode);
		}
		return ret;
	}

	@Override
	public Area getParentArea(Long areaCode,int companyId) throws Exception {
		Map<Long,Area> areaMap=areaCacheContexts.get(companyId).areaMap;
		if(areaMap!=null){
			Area area = areaMap.get(areaCode);
			if(area!=null)
				return areaMap.get(area.getParentCode());
		}
		return null;
	}

	@Override
	public Area getAreaByCode(Long areaCode,int companyId) throws Exception {
		return areaCacheContexts.get(companyId).areaMap.get(areaCode);
	}

	@Override
	public Map<Long, Area> getAreaMap(List<Long> codes,int companyId) throws Exception {
		Map<Long,Area> ret = new HashMap<Long,Area>();
		Map<Long,Area> areaMap=areaCacheContexts.get(companyId).areaMap;
		if(CollectionUtils.isNotEmpty(codes)){
			for(Long code:codes){
				Area area = areaMap.get(code);
				if(area!=null)
					ret.put(code, area);
			}
		}
		return ret;
	}

	@Override
	protected void tryReload(int companyId) throws Exception {
		AreaCacheContext areaCacheContext=new AreaCacheContext();
		reloadAreaData(areaCacheContext,companyId);
		areaCacheContexts.put(companyId, areaCacheContext);
	}

	@Override
	public int getInterval() {
		return 60*2;
	}

	public AreaDao getAreaDao() {
		return areaDao;
	}

	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}

	@Override
	public Area getAreaByName(String areaName,int companyId) throws Exception {
		AreaCacheContext context = areaCacheContexts.get(companyId);
		if(context == null){
			return null;
		}
		Map<String,Area> areaNameMap=context.areaNameMap;
		Area area=areaNameMap.get(areaName);
		if(area!=null){
			return area;
		}
		if(areaName.endsWith("省") || areaName.endsWith("市") 
				|| areaName.endsWith("县")
				|| areaName.endsWith("区") || areaName.endsWith("镇")){
			areaName=areaName.substring(0, areaName.length()-1);
			area=areaNameMap.get(areaName);	
		}
		return area;
	}
	
	private static class AreaCacheContext{
		Map<Long,Area> areaMap = new HashMap<Long,Area>();
		Map<Long,Long> code2ParentCodeMap = new HashMap<Long,Long>();
		Map<String,Area> areaNameMap=new HashMap<String,Area>();
	}

}
