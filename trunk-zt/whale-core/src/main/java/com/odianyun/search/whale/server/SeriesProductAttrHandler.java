package com.odianyun.search.whale.server;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.data.service.ProductAttributeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.series.SeriesAttrResponse;
import com.odianyun.search.whale.api.model.series.SeriesMerchantProduct;
import com.odianyun.search.whale.api.model.series.SeriesRequest;
import com.odianyun.search.whale.api.model.series.SeriesResponse;
import com.odianyun.search.whale.data.model.AttributeValue;
import com.odianyun.search.whale.data.model.Shop;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.data.service.AttributeValueService;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class SeriesProductAttrHandler {

	static Logger logger = Logger.getLogger(SeriesProductAttrHandler.class);

	@Autowired
	MerchantService merchantService;
	@Autowired
	AttributeValueService attributeValueService;
	@Autowired
	ProductAttributeService productAttributeService;

	// @Autowired
	// CompanyRoutingService companyRoutingService;

	public SeriesResponse handle(SeriesRequest seriesRequest) {
		int companyId = seriesRequest.getCompanyId();
		SeriesResponse seriesResponse = new SeriesResponse();
		ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(), IndexConstants.index_type);
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.COMPANYID, seriesRequest.getCompanyId()));
		boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MERCHANTSERIESID, seriesRequest.getSeriesId()));
		
		ManagementType managementType = seriesRequest.getManagementState();
		if(!ManagementType.VERIFIED.equals(managementType)){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, managementType.getCode()));
		}
		
		esSearchRequest.setQueryBuilder(boolQueryBuilder);
		List<String> fields = new ArrayList<String>();
		fields.add(IndexFieldConstants.ID);
		fields.add(IndexFieldConstants.MERCHANTID);
		// fields.add(IndexFieldConstants.PRODUCT_ID);
		fields.add(IndexFieldConstants.PRICE);
		fields.add(IndexFieldConstants.STOCK);
		fields.add(IndexFieldConstants.PICURL);
		fields.add(IndexFieldConstants.PRODUCTNAME);
		fields.add(IndexFieldConstants.TAX);
		fields.add(IndexFieldConstants.TYPE);
		fields.add(IndexFieldConstants.MERCHANTSERIESID);
		fields.add(IndexFieldConstants.COMPANYID);
		fields.add(IndexFieldConstants.CATEGORYID);
		fields.add(IndexFieldConstants.CODE);
		fields.add(IndexFieldConstants.SERIESATTRVALUEIDSEARCH);
		esSearchRequest.setFields(fields);
		esSearchRequest.setStart(0);
		esSearchRequest.setCount(Integer.MAX_VALUE);
		try {
			org.elasticsearch.action.search.SearchResponse esSearchResponse = ESService.search(esSearchRequest);
			SearchHits searchHits = esSearchResponse.getHits();
			SearchHit[] searchHitArray = searchHits.getHits();
			Map<Long, SeriesAttrResponse> seriesAttrValues = new HashMap<Long, SeriesAttrResponse>();
			List<Long> merchantProductIds = new ArrayList<>();
			for (SearchHit hit : searchHitArray) {
				merchantProductIds.add((Long) hit.fields().get("id").getValues().get(0));
			}
			// 查询merchant_prod_att_name和merchant_prod_att_value表获取
			Map<Long, Map<Long, String>> merchantProdNameAttNameMap = productAttributeService.queryMerchantProductAttributeValueCustom(merchantProductIds, companyId);
			for (SearchHit hit : searchHitArray) {
				Map<String, SearchHitField> data = hit.fields();
				MerchantProduct merchantProduct = convertSearchHitField(data);
				Map<Long, String> attrValueCustomMap = merchantProdNameAttNameMap.get(merchantProduct.getId());
				SearchHitField fieldData = data.get(IndexFieldConstants.SERIESATTRVALUEIDSEARCH);
				if (fieldData == null || fieldData.getValues() == null) {
					continue;
				}
				Object obj = fieldData.getValues().get(0);
				String str = (obj == null ? "" : (String) obj);
				if (StringUtils.isNotBlank(str)) {
					String[] seriesAttrValueIds = str.split(" ");

					for (int i=0;i<seriesAttrValueIds.length;i++) {
						Long vId = Long.valueOf(seriesAttrValueIds[i]);
						AttributeValue attributeValue = attributeValueService.getAttributeValue(vId, companyId);
						if (attributeValue != null && !attributeValueService
								.isAttributeValueGroup(attributeValue.getAttrValueId(), companyId)) {
							SeriesAttrResponse seriesAttrResponse = seriesAttrValues
									.get(attributeValue.getAttrNameId());
							if (seriesAttrResponse == null) {
								seriesAttrResponse = new SeriesAttrResponse();
								seriesAttrResponse.setSeriesAttrId(attributeValue.getAttrNameId());
								seriesAttrResponse.setSeriesAttrName(attributeValue.getAttrName());
								seriesAttrResponse.setSortValue(i);
								seriesAttrValues.put(attributeValue.getAttrNameId(), seriesAttrResponse);
							}
							SeriesMerchantProduct seriesMerchantProduct = new SeriesMerchantProduct();
							seriesMerchantProduct.setSeriesAttrValueId(attributeValue.getAttrValueId());
							if (attrValueCustomMap != null && attrValueCustomMap.get(attributeValue.getAttrValueId()) != null && !attrValueCustomMap.get(attributeValue.getAttrValueId()).trim().equals("")) {
								seriesMerchantProduct.setSeriesAttrValue(attrValueCustomMap.get(attributeValue.getAttrValueId()));
							} else {
								seriesMerchantProduct.setSeriesAttrValue(attributeValue.getAttrValue());
							}
							seriesMerchantProduct.setSortValue(attributeValue.getSortValue());
							seriesMerchantProduct.getMerchantProducts().add(merchantProduct);
							seriesAttrResponse.getSeriesMerchantProducts().add(seriesMerchantProduct);
						}

					}
				}
			}
			if (seriesAttrValues.size() > 0) {
				List<SeriesAttrResponse> seriesAttrResponses = new LinkedList<SeriesAttrResponse>(
						seriesAttrValues.values());
				Collections.sort(seriesAttrResponses);
				for (SeriesAttrResponse seriesAttrResponse : seriesAttrResponses) {
					Collections.sort(seriesAttrResponse.getSeriesMerchantProducts());
				}
				seriesResponse.setSeriesAttrResponses(seriesAttrResponses);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SearchException(e.getMessage(), e);
		}

		return seriesResponse;

	}

	private MerchantProduct convertSearchHitField(Map<String, SearchHitField> data) throws Exception {
		// TODO Auto-generated method stub
		MerchantProduct merchantProduct = new MerchantProduct();
		if (data == null || data.size() == 0) {
			return merchantProduct;
		}
		Field[] fields = MerchantProduct.class.getDeclaredFields();
		for (Field field : fields) {
			String propertyName = field.getName();
			String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
			Method setMethod = MerchantProduct.class.getMethod(setMethodName, field.getType());
			if (setMethod == null)
				continue;
			String fieldType = field.getType().getSimpleName();
			if (data.get(propertyName) == null || data.get(propertyName).getValues() == null
					|| data.get(propertyName).getValues().size() == 0) {
				continue;
			}
			Object obj = data.get(propertyName).getValues().get(0);
			if ("Integer".equals(fieldType)) {
				setMethod.invoke(merchantProduct, (Integer) obj);
			} else if ("Long".equals(fieldType)) {
				if ("Integer".equals(obj.getClass().getSimpleName())) {
					setMethod.invoke(merchantProduct, ((Integer) obj).longValue());
				} else {
					setMethod.invoke(merchantProduct, (Long) obj);
				}
			} else if ("Double".equals(fieldType)) {
				setMethod.invoke(merchantProduct, (Double) obj);
			} else if ("String".equals(fieldType)) {
				if ("Integer".equals(obj.getClass().getSimpleName())) {
					setMethod.invoke(merchantProduct, ((Integer) obj).toString());
				} else {
					setMethod.invoke(merchantProduct, (String) obj);
				}
			}
		}
		Long merchantId = merchantProduct.getMerchantId();
		int companyId = merchantProduct.getCompanyId().intValue();
		if (merchantId != null && merchantId != 0) {
			Shop shop = merchantService.getShop(merchantId, companyId);
			if (shop != null) {
				merchantProduct.setShopId(shop.getId());
				merchantProduct.setNewShopType(shop.getShop_type());
				merchantProduct.setShopName(shop.getName());
			}
		}
		return merchantProduct;
	}
}
