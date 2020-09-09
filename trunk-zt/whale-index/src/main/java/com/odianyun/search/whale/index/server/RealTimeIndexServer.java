package com.odianyun.search.whale.index.server;

import com.odianyun.search.whale.index.api.common.SearchUpdateSender;
import com.odianyun.search.whale.index.api.common.UpdateType;
import com.odianyun.search.whale.index.api.model.req.IndexApplyDTO;
import com.odianyun.search.whale.index.api.service.RealTimeIndexService;
import com.odianyun.search.whale.index.realtime.MerchantProductIncIndex;
import com.odianyun.soa.InputDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RealTimeIndexServer implements RealTimeIndexService{

	static Logger logger = Logger.getLogger(RealTimeIndexServer.class);

	@Autowired
	MerchantProductIncIndex merchantProductIncIndex;
	
	@Override
	public void updateIndex(List<Long> ids, UpdateType updateType,int companyId) throws Exception {

		if(CollectionUtils.isNotEmpty(ids)){
			switch(updateType){
			case brand_id :
				SearchUpdateSender.sendUpdate(ids, updateType,companyId);
//				merchantProductIncIndex.processWithBrandIds(ids);
				break;
			case merchant_product_id :
				SearchUpdateSender.sendUpdate(ids, updateType,companyId);
//				merchantProductIncIndex.process(ids, true,IndexConstants.index_alias,IndexConstants.index_type);
				break;
			case category_tree_node_id :
				SearchUpdateSender.sendUpdate(ids, updateType,companyId);
//				merchantProductIncIndex.processWithCategoryTreeNodeIds(ids);
				break;
			case product_id :
				SearchUpdateSender.sendUpdate(ids, updateType,companyId);
//				merchantProductIncIndex.processWithProductIds(ids);
				break;
			}
		}
	}

	@Override
	public void updateIndex(InputDTO<IndexApplyDTO> inputDto) throws Exception {
		IndexApplyDTO indexApply = inputDto.getData();
		updateIndex(indexApply.getIds(),indexApply.getUpdateType(),indexApply.getCompanyId());
	}

	@Override
	public void updateIndexStandard(InputDTO<IndexApplyDTO> inputDto) throws Exception {
		logger.info("soa 调用 入参:"+inputDto);
		IndexApplyDTO indexApply = inputDto.getData();
		updateIndex(indexApply.getIds(),indexApply.getUpdateType(),indexApply.getCompanyId());
	}

}
