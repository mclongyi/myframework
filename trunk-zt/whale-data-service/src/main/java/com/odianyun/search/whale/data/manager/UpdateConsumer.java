package com.odianyun.search.whale.data.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.geo.dao.POIDao;
import com.odianyun.search.whale.data.geo.service.POIService;
import com.odianyun.search.whale.data.geo.service.impl.POIServiceImpl;
import com.odianyun.search.whale.data.model.MerchantProdMerchantCateTreeNode;
import com.odianyun.search.whale.data.model.MerchantProductRelation;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.service.impl.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.lucene.Lucene;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.mq.common.consumer.ConsumerType;
import com.odianyun.mq.common.message.Destination;
import com.odianyun.mq.common.message.Message;
import com.odianyun.mq.consumer.BackoutMessageException;
import com.odianyun.mq.consumer.Consumer;
import com.odianyun.mq.consumer.ConsumerConfig;
import com.odianyun.mq.consumer.ConsumerFactory;
import com.odianyun.mq.consumer.MessageListener;
import com.odianyun.mq.consumer.impl.ConsumerFactoryImpl;
import com.odianyun.search.whale.common.util.NetUtil;
import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.dao.ProductDao;
import com.odianyun.search.whale.data.saas.service.CompanyService;
import com.odianyun.search.whale.index.api.common.UpdateMessage;
import com.odianyun.search.whale.index.api.common.UpdateType;

public class UpdateConsumer {

	// private static String topic = "search_cache_update";

	private static Logger log = Logger.getLogger(UpdateConsumer.class);

	@Autowired
	CompanyService companyService;

	@Autowired
	protected MerchantProductDao merchantProductDao;

	@Autowired
	ProductDao productDao;

	@Autowired
	POIService poiService;

	@Autowired
	private CategoryService categoryService;

	protected boolean isStartWithOplusO = true;

	public void startConsumerReload(String topic) {
		String consumerId = genDefaultConsumerId();
		startConsumerReload(topic, consumerId);
	}

	public static String genDefaultConsumerId() {
		String consumerId = genConsumerIdWithSuffix(String.valueOf(System.currentTimeMillis()));
		return consumerId;
	}

	public static String genConsumerIdWithSuffix(String suffix) {
		if(org.apache.commons.lang3.StringUtils.isBlank(suffix)){
			suffix = String.valueOf(System.currentTimeMillis());
		}
		String consumerId = "c" + NetUtil.getLocalIP().replaceAll("\\.", "-") +"-"+ suffix;
		// 架构对consumerId长度做了限制  最大28
		if(consumerId.length() > 28){
			consumerId = consumerId.substring(0,28);
		}
		return consumerId;
	}

	public void startConsumerReload(String topic,String consumerId) {
		startConsumerReload(topic,consumerId, null);
	}

	public void startConsumerReload(String topic,String consumerId,String nameSpace) {
		try {
			ConsumerFactory consumerFactory = ConsumerFactoryImpl.getInstance();
			ConsumerConfig config = new ConsumerConfig();
			config.setConsumerType(ConsumerType.CLIENT_ACKNOWLEDGE);
			config.setThreadPoolSize(10);
			Destination destination = null;
			if (StringUtils.isNotBlank(nameSpace)) {
				destination = Destination.topic(nameSpace, topic);
			} else {
				destination = Destination.topic(topic);
			}
			Consumer consumer = consumerFactory.createLocalConsumer(destination,
					consumerId, config);
			consumer.setListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) throws BackoutMessageException {
					// 更新merchantId对应的cache
					UpdateMessage updateMessage = msg.transferContentToBean(UpdateMessage.class);
					updateByMessage(updateMessage);
				}
			});
			consumer.start();
			isStartWithOplusO = ConfigUtil.getBool("isStartWithOplusO", true);
		} catch (Exception e) {
			log.error("start comsumer failed==================================================", e);
		}
	}

	protected void updateByMessage(UpdateMessage updateMessage) {
		List<Long> ids = updateMessage.getIds();
		if (CollectionUtils.isNotEmpty(ids)){
			try {
				int companyId = updateMessage.getCompanyId();
				//处理关联的父子商家的商品数据添加到ids
				addRelationUpdateIds(ids,companyId);
				updateByIds(ids, updateMessage.getUpdateType(), companyId);
			} catch (Throwable e) {
				log.error("updateCacheByIds failed: ", e);
			}
		}

	}

	private void addRelationUpdateIds(List<Long> ids, int companyId) throws Exception{
		List<MerchantProductRelation> relations = merchantProductDao.queryRelationByIds(ids,companyId);
		if(CollectionUtils.isNotEmpty(relations)){
			Set<Long> idSet = new HashSet<Long>();
			for(MerchantProductRelation re:relations){
				idSet.add(re.getMpId());
				idSet.add(re.getRefId());
			}
			if(CollectionUtils.isNotEmpty(idSet)){
				ids.addAll(idSet);
			}
		}
	}

	protected void updateByIds(List<Long> ids, UpdateType updateType, int companyId) throws Exception {
		log.info("consumer ids: " + ids + "companyId : " + companyId + " updateType: " + updateType);
		updateCacheByIds(ids, updateType, companyId);
	}

	private void updateCacheByIds(List<Long> ids, UpdateType updateType, int companyId) throws Exception {
		log.info("updateCache ids: " + ids + "companyId : " + companyId + " updateType: " + updateType);
		switch (updateType) {
		// category_tree_node_id 已经都被改为categoryId
			case category_tree_node_id:
				CompanyDBCacheManager.instance.reload(CategoryServiceImpl.class.getCanonicalName(), ids, companyId);
				break;
			case brand_id:
				CompanyDBCacheManager.instance.reload(BrandServiceImpl.class.getCanonicalName(), ids, companyId);
				break;
			case merchant_product_id:
				List<Long> productIdList = merchantProductDao.queryProductIdsByMpIds(ids, companyId);
				updateCacheByProductIds(productIdList, companyId);

			/*updateMerchantCategory(ids,companyId);
			List<Long> categoryTreeNodeIds = getCategoryTreeNodeIdsByMPid(ids,companyId);
			if(CollectionUtils.isNotEmpty(categoryTreeNodeIds)){
				CompanyDBCacheManager.instance.reload(CategoryServiceImpl.class.getCanonicalName(),categoryTreeNodeIds,companyId);
			}*/
				//更新商品角标
				CompanyDBCacheManager.instance.reload(SuperScriptServiceImpl.class.getCanonicalName(),ids,companyId);

				List<Long> merchantCateTreeNodeIdList  = getMerchantCateTreeNodeIds(ids,companyId);
				if(CollectionUtils.isNotEmpty(merchantCateTreeNodeIdList)){
					CompanyDBCacheManager.instance.reload(MerchantCategoryServiceImpl.class.getCanonicalName(), merchantCateTreeNodeIdList, companyId);
				}
				break;
			case product_id:
				updateCacheByProductIds(ids, companyId);
				break;
			case GEO_MERCHANT_ID:
				updateGeoStoreByMerchantIds(ids,companyId);
				break;
		}
	}


	/*private List<Long> getCategoryTreeNodeIdsByMPid(List<Long> ids, int companyId) throws Exception{
		Set<Long> ret = categoryService.getNavicCategoryIdsByMpIds(ids,companyId);
		return new ArrayList<Long>(ret);
	}*/

	private List<Long> getMerchantCateTreeNodeIds(List<Long> ids,int companyId) throws Exception {
		List<Long>  merchantCateTreeNodeIds = new ArrayList<>();
		List<MerchantProdMerchantCateTreeNode> nodeList = productDao.getMerchantCateTreeNodeIds(ids, companyId);
		if(CollectionUtils.isNotEmpty(nodeList)){
			for(MerchantProdMerchantCateTreeNode node : nodeList){
				Long merchantCateTreeNodeId = node.getMerchantCateTreeNodeId();
				if(merchantCateTreeNodeId != null && merchantCateTreeNodeId != 0){
					merchantCateTreeNodeIds.add(merchantCateTreeNodeId);
				}
			}
		}

		return merchantCateTreeNodeIds;
	}

	private void updateGeoStoreByMerchantIds(List<Long> ids, int companyId) throws Exception {
		CompanyDBCacheManager.instance.reload(MerchantServiceImpl.class.getCanonicalName(),ids,companyId);
		CompanyDBCacheManager.instance.reload(POIServiceImpl.class.getCanonicalName(),ids,companyId);
	}

	private void updateCacheByProductIds(List<Long> productIdList, int companyId) throws Exception {
		if (CollectionUtils.isEmpty(productIdList)) {
			return;
		}
		List<Long> brandIdList = productDao.getBrandsByProductIds(productIdList, companyId);
		List<Long> leftCategoryIdList = new ArrayList<Long>();
		leftCategoryIdList.addAll(productDao.getLeftCategoryIdsByProductIds(productIdList, companyId));
		leftCategoryIdList.addAll(productDao.getLeftCategoryIdsByProductIds2(productIdList, companyId));

		if (CollectionUtils.isNotEmpty(brandIdList)) {
			updateCacheByIds(brandIdList, UpdateType.brand_id, companyId);
		}
		if (CollectionUtils.isNotEmpty(leftCategoryIdList)) {
			updateCacheByIds(leftCategoryIdList, UpdateType.category_tree_node_id, companyId);
		}
	}

}
