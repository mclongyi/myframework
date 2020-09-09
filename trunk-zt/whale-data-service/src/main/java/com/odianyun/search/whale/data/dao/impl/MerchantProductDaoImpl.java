package com.odianyun.search.whale.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.service.ConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class MerchantProductDaoImpl extends SqlMapClientDaoSupport implements MerchantProductDao{

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	@Autowired
	ConfigService configService;
	
	@Override
	public List<MerchantProduct> queryAllProductMerchant(int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllMerchantProduct");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<MerchantProduct> result = new ArrayList<MerchantProduct>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantProduct.class);
		}
		return result;*/
		Map<String,Object> param = new HashMap<String,Object>();
		Integer isNewDay = configService.getInt(ServiceConstants.IS_NEW_DAY,ServiceConstants.IS_NEW_DAY_NUM,companyId);
		param.put(ServiceConstants.IS_NEW_DAY,isNewDay);
		param.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryAllMerchantProduct",param);
	}

	@Override
	public List<MerchantProduct> getMerchantProducts(List<Long> merchantProductIds, int companyId)
			throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryMerchantProducts");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<MerchantProduct> result = new ArrayList<MerchantProduct>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantProduct.class);
		}
		return result;*/
		Integer isNewDay = configService.getInt(ServiceConstants.IS_NEW_DAY,ServiceConstants.IS_NEW_DAY_NUM,companyId);
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IS_NEW_DAY,isNewDay);
		return getSqlMapClientTemplate().queryForList("queryMerchantProducts",paramMap);
	}

	@Override
	public List<MerchantProduct> getMerchantProductsWithPage(long maxId, int pageSize, int companyId) throws Exception {
		Integer isNewDay = configService.getInt(ServiceConstants.IS_NEW_DAY,ServiceConstants.IS_NEW_DAY_NUM,companyId);
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(ServiceConstants.MAX_ID, maxId);
        params.put(ServiceConstants.PAGE_SIZE, pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);
		params.put(ServiceConstants.IS_NEW_DAY,isNewDay);
		/* String sql = companySqlService.getSql(companyId, "getMerchantProductsWithPage");
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,params);
		List<MerchantProduct> result = new ArrayList<MerchantProduct>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantProduct.class);
		}
		return result;*/
		return getSqlMapClientTemplate().queryForList("getMerchantProductsWithPage", params);
	}

	@Override
	public Map<Long, String> getMerchantProductUrls(List<Long> merchantProductIds, int companyId) throws Exception{
		Map<Long, String> picMap=new HashMap<Long,String>();
		Map<Long, MerchantProductPic> merchantProductPicMap=new HashMap<Long,MerchantProductPic>();
		/*String sql = companySqlService.getSql(companyId, "getMerchantProductUrls");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<MerchantProductPic> pics = new ArrayList<MerchantProductPic>();
		if(CollectionUtils.isNotEmpty(list)){
			pics = ResultConvertor.convertFromMap(list, MerchantProductPic.class);
		}*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<MerchantProductPic> pics=getSqlMapClientTemplate().queryForList("getMerchantProductUrls", paramMap);
		if(pics!=null){
			for(MerchantProductPic pic:pics){
				Long id=pic.getId();
				Integer sortValue=pic.getSort_value();
				MerchantProductPic pic2=merchantProductPicMap.get(id);
				if(pic2==null || pic2.getSort_value()==null || pic2.getSort_value()==0
						|| (sortValue!=null&&sortValue!=0&&sortValue<pic2.getSort_value())){
					merchantProductPicMap.put(id, pic);
				}		
			}			
		}
		for(Entry<Long, MerchantProductPic> entry:merchantProductPicMap.entrySet()){
			picMap.put(entry.getKey(),entry.getValue().getUrl());
		}
		return picMap;
	}

	@Override
	public List<Long> queryProductMerchantIdsByProductIds(List<Long> updateProductIds, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryProductMerchantIdsByProductIds");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, updateProductIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<Long> result = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(list)){
			for(Map map : list){
				result.add((Long)map.get(ServiceConstants.ID));
			}
//			result = ResultConvertor.convertFromMapWithBeanUtil(list, Long.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, updateProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
 		return getSqlMapClientTemplate().queryForList("queryProductMerchantIdsByProductIds", paramMap);
	}

	@Override
	public List<Long> queryProductMerchantIdsByUpdateTime(UpdateTimeRange updateTimeRange, int companyId)
			throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.putAll(ResultConvertor.convertToMapWithBeanUtil(updateTimeRange));
		paramMap.put(ServiceConstants.COMPANYID, companyId);
 		return getSqlMapClientTemplate().queryForList("queryMerchantProductIdsByUpdateTime", paramMap);
	}

	@Override
	public List<Integer> queryCompanyIds() throws Exception {
		
		return getSqlMapClientTemplate().queryForList("queryCompanyIds");
	}

	@Override
	public List<MerchantProduct> getMerchantProductsAll(List<Long> merchantProductIds, int companyId) throws Exception {
		Integer isNewDay = configService.getInt(ServiceConstants.IS_NEW_DAY,ServiceConstants.IS_NEW_DAY_NUM,companyId);
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IS_NEW_DAY,isNewDay);
		return getSqlMapClientTemplate().queryForList("queryMerchantProductsAll",paramMap);
	}

	@Override
	public List<MerchantProductSimple> queryMerchantProductsAllSimple(List<Long> merchantProductIds, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantProductsAllSimple",paramMap);
	}

	@Override
	public List<Long> queryProductIdsByMpIds(List<Long> ids, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, ids);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
 		return getSqlMapClientTemplate().queryForList("queryProductIdsByMpIds", paramMap);
	}

	@Override
	public List<String> queryAllBindEans(Long merchantProductId, int companyId) throws Exception {
		return getSqlMapClientTemplate().queryForList("queryAllBindEans",merchantProductId);
	}

	@Override
	public Map<Long, Long> queryMerchantIdByMPId(List<Long> mpIds, int companyId) throws Exception {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put(ServiceConstants.IDS,mpIds);
		param.put(ServiceConstants.COMPANYID,companyId);
		return getSqlMapClientTemplate().queryForMap("queryMerchantIdByMPId",param,"mpId","merchantId");
	}

	@Override
	public List<MerchantProductForSuggest> getMerchantProductsForSuggWithPage(long maxId, int pageSize, int companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ServiceConstants.MAX_ID, maxId);
		params.put(ServiceConstants.PAGE_SIZE, pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);

		return getSqlMapClientTemplate().queryForList("getMerchantProductsForSuggWithPage", params);

	}

	@Override
	public List<MerchantProductForSuggest> getPointMerchantProductsForSuggWithPage(long maxId, int pageSize, Integer companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ServiceConstants.MAX_ID, maxId);
		params.put(ServiceConstants.PAGE_SIZE, pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);

		return getSqlMapClientTemplate().queryForList("getPointMerchantProductsForSuggWithPage", params);
	}

	@Override
	public List<MerchantProductRelation> getStoreMerchantProductRelation(List<Long> mpIds, int companyId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(ServiceConstants.IDS,mpIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getStoreMerchantProductRelation", params);
	}

	@Override
	public List<MerchantProductRelation> queryPMPIdsBySMPIds(List<Long> mpIds, int companyId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(ServiceConstants.IDS,mpIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryPMPIdsBySMPIds", params);
	}

	@Override
	public List<MerchantProductRelation> querySMPIdsByPMPIds(List<Long> mpIds, int companyId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(ServiceConstants.IDS,mpIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("querySMPIdsByPMPIds", params);
	}

	@Override
	public List<MerchantProductRelation> queryRelationByIds(List<Long> mpIds, int companyId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(ServiceConstants.IDS,mpIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryRelationByIds", params);
	}

}
