package com.odianyun.search.whale.index.geo;

import java.util.ArrayList;
import java.util.List;


import com.odianyun.search.whale.data.geo.dao.O2OStoreDao;
import com.odianyun.search.whale.data.model.geo.O2OStore;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public class SaveToDBProcessor implements Processor{

    O2OStoreDao storeDao;

    public SaveToDBProcessor(){
    	storeDao = (O2OStoreDao) GeoProcessorApplication.getBean("storeDao");
    }
    

	public void index(List<O2OStore> storelist) throws Exception {
//		storeDao.saveO2OStore(storelist);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return SaveToDBProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		List<O2OStore> storelist = new ArrayList<O2OStore>();
		for(DataRecord<O2OStore> dataRecord : dataRecords){
			O2OStore store = dataRecord.getV();
			storelist.add(store);
		}
		index(storelist);
	}

}
