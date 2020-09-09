package com.odianyun.search.whale.index.geo;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import com.odianyun.search.whale.index.api.common.PointsMpIndexConstants;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.client.Client;

import com.odianyun.search.whale.data.model.geo.O2OStore;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public class IncIndexProcessor implements Processor{

//	CompanyRoutingService companyRoutingService;
	public List<IndexInfo> getIndexInfoList() {
	return indexInfoList;
}
	private List<IndexInfo> indexInfoList = new ArrayList<IndexInfo>();
	public IncIndexProcessor(){
//		this.companyRoutingService = (CompanyRoutingService) GeoProcessorApplication.getBean("companyRoutingService");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return IncIndexProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		List<O2OStore> list = new ArrayList<>();
		for(DataRecord<O2OStore> dataRecord : dataRecords){
			O2OStore o2oStore = dataRecord.getV();
			list.add(o2oStore);
		}
		if(list.size() > 0){
			/*ESClusterConfig esClusterConfig = companyRoutingService.getCompanyESClusterConfig(processorContext.getCompanyId(), CompanyAppType.O2O);

			if(null != esClusterConfig){
				Client client = ESClient.getClient(esClusterConfig.getClusterName(), esClusterConfig.getClusterNode());
				IndexUpdater.update(client,list,processorContext.getIndexName(),processorContext.getIndexType());
			}*/
			Client client = ESClient.getClient();
			IndexUpdater.update(client,list,processorContext.getIndexName(),processorContext.getIndexType());
			IndexUpdater.update(client,list,processorContext.getIndexName().replace(OplusOIndexConstants.indexName_pre, PointsMpIndexConstants.indexName_pre),processorContext.getIndexType());

			if(CollectionUtils.isNotEmpty(indexInfoList)){
				for(IndexInfo indexInfo : indexInfoList){
					IndexUpdater.update(client,list,indexInfo.getIndexName(),indexInfo.getIndexType());
					IndexUpdater.update(client,list,indexInfo.getIndexName().replace(OplusOIndexConstants.indexName_pre, PointsMpIndexConstants.indexName_pre),indexInfo.getIndexType());
				}
			}
		}
	}

	public void register(IndexInfo indexInfo){
		indexInfoList.add(indexInfo);
	}

	public void remove(IndexInfo indexInfo){
		indexInfoList.remove(indexInfo);
	}

	public void clear(){
		indexInfoList.clear();
	}

	public int indexInfoSize(){
		if(CollectionUtils.isEmpty(indexInfoList)){
			return 0;
		}
		return indexInfoList.size();
	}
	public static class IndexInfo{
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((indexName == null) ? 0 : indexName.hashCode());
			result = prime * result + ((indexType == null) ? 0 : indexType.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			IndexInfo other = (IndexInfo) obj;
			if (indexName == null) {
				if (other.indexName != null)
					return false;
			} else if (!indexName.equals(other.indexName))
				return false;
			if (indexType == null) {
				if (other.indexType != null)
					return false;
			} else if (!indexType.equals(other.indexType))
				return false;
			return true;
		}
		/**
		 * @return the indexName
		 */
		public String getIndexName() {
			return indexName;
		}
		/**
		 * @param indexName the indexName to set
		 */
		public void setIndexName(String indexName) {
			this.indexName = indexName;
		}
		/**
		 * @return the indexType
		 */
		public String getIndexType() {
			return indexType;
		}
		/**
		 * @param indexType the indexType to set
		 */
		public void setIndexType(String indexType) {
			this.indexType = indexType;
		}
		private String indexName;
		private String indexType;
		public IndexInfo(String indexName,String indexType){
			this.indexName = indexName;
			this.indexType = indexType;
		}


	}

	
}
