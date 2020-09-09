package com.odianyun.search.whale.index.suggest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.odianyun.search.whale.data.model.suggest.SuggestWord;
import com.odianyun.search.whale.data.suggest.dao.SuggestWordDao;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;
import com.odianyun.search.whale.index.suggest.common.ProcessorApplication;

public class SaveDBProcessor implements Processor{

	static Logger logger = Logger.getLogger(SaveDBProcessor.class);
	
	SuggestWordDao suggestWordDao;
	
	public SaveDBProcessor(){
		suggestWordDao = (SuggestWordDao) ProcessorApplication.getBean("suggestWordDao");
	}
	
	@Override
	public String getName() {
		return SaveDBProcessor.class.getSimpleName();
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
			suggestWordDao.storeToTemp(list);
		}
		
	}
	

}
