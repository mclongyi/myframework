package com.odianyun.search.whale.index.suggest;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.Client;

import com.odianyun.search.whale.data.model.suggest.SuggestWord;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.index.suggest.common.IndexUpdater;
import com.odianyun.search.whale.index.suggest.common.ProcessorApplication;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public class IncIndexProcessor implements Processor{


	public IncIndexProcessor(){
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return IncIndexProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		List<SuggestWord> list = new ArrayList<>();
		for(DataRecord<SuggestWord> dataRecord : dataRecords){
			SuggestWord suggestWord = dataRecord.getV();
			list.add(suggestWord);
		}
		if(list.size() > 0){
			IndexUpdater.update(ESClient.getClient(),list,processorContext.getIndexName(),processorContext.getIndexType());

		}
		
		
	}
	
}
