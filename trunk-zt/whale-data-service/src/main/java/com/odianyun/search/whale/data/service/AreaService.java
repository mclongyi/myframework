package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.Area;

public interface AreaService {

	/**
	 * 根据区域code获得四级区域的全路径
	 * @param areaCode
	 * @return
	 * @throws Exception
	 */
	public List<Area> getFullPathArea(Long areaCode,int companyId) throws Exception;
	
	/**
	 * 根据区域code获得其上级区域
	 * @param areaCode
	 * @return
	 * @throws Exception
	 */
	public List<Long> getAllParentAreaCode(Long areaCode,int companyId) throws Exception;
	
	/**
	 * 根据区域code获得其直接上级区域
	 * @param areaCode
	 * @return
	 * @throws Exception
	 */
	public Area getParentArea(Long areaCode,int companyId) throws Exception;
	
	/**
	 * 根据区域code获得其直接上级区域
	 * @param areaName
	 * @return
	 * @throws Exception
	 */
	public Area getAreaByName(String areaName,int companyId) throws Exception;
	
	/**
	 * 根据区域code获得该区域对象
	 * @param areaCode
	 * @return
	 * @throws Exception
	 */
	public Area getAreaByCode(Long areaCode,int companyId) throws Exception;
	
	/**
	 * 根据区域code列表查取区域对象数据
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	public Map<Long,Area> getAreaMap(List<Long> codes,int companyId) throws Exception;
}
