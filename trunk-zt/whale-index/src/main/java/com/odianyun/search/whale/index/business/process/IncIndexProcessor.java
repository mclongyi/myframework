package com.odianyun.search.whale.index.business.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.client.Client;

import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.index.business.process.base.BaseIncIndexProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import com.odianyun.search.whale.index.realtime.IndexUpdater;

public class IncIndexProcessor extends BaseIncIndexProcessor{
	
	/**
	 * @return the indexInfoList
	 */
	public List<IndexInfo> getIndexInfoList() {
		return indexInfoList;
	}

	public IncIndexProcessor(){
//		this.companyRoutingService = (CompanyRoutingService) ProcessorApplication.getBean("companyRoutingService");
	}
	
	private List<IndexInfo> indexInfoList = new ArrayList<IndexInfo>();
//	CompanyRoutingService companyRoutingService;

	@Override
	public void index(List<MerchantProductSearch> merchantProductSearchs, String indexName, String type, int companyId) throws Exception {
		//暂时注释实时索引进数据库的操作  日志里可以确认
//		merchantProductSearchService.insertOrUpdateMerchantProductSearchs(merchantProductSearchs);
		/*ESClusterConfig esClusterConfig = companyRoutingService.getCompanyESClusterConfig(companyId, CompanyAppType.B2C);
		if(null != esClusterConfig && merchantProductSearchs.size()>0){
			Client client = ESClient.getClient(esClusterConfig.getClusterName(), esClusterConfig.getClusterNode());
			IndexUpdater.update(client,merchantProductSearchs,indexName,type);
		}*/
		if(merchantProductSearchs.size()>0){
			Client client = ESClient.getClient();
			IndexUpdater.update(client,merchantProductSearchs,indexName,type);
			if(CollectionUtils.isNotEmpty(indexInfoList)){
				for(IndexInfo indexInfo : indexInfoList){
					IndexUpdater.update(client,merchantProductSearchs,indexInfo.getIndexName(),indexInfo.getIndexType());
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
