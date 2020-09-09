package com.odianyun.search.whale.index.suggest;




import java.text.SimpleDateFormat;
import java.util.*;

import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.suggest.KeyWord;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.SuggestType;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.model.suggest.SuggestBusinessWord;
import com.odianyun.search.whale.data.model.suggest.WordWithCompany;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.suggest.service.AreaSuggestWordService;
import com.odianyun.search.whale.data.suggest.service.HotWordService;
import com.odianyun.search.whale.data.suggest.service.SuggestWordService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.SuggestIndexConstants;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.IndexFlow;
import com.odianyun.search.whale.processor.ProcessScheduler;
import com.odianyun.search.whale.processor.Processor;


public class SuggestWordIndexFlowImpl implements IndexFlow {

	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	String index_start_time;

	static Logger logger = Logger.getLogger(SuggestWordIndexFlowImpl.class);

	ProcessScheduler processScheduler;
	
	static int INDEX_NUM = 3;
	
	static int FREQUENCY = 1;
	
	@Autowired
	SuggestIndexSwitcher switcher;
	
	@Autowired
	SuggestWordService suggestWordService;
	
	@Autowired
	AreaSuggestWordService areaSuggestWordService;
	
	@Autowired
	HotWordService hotWordService;
	
	@Autowired
	MerchantProductService merchantProductService;
	
	@Autowired
	ConfigService configService;

	List<Integer> companyIds;

	@Override
	public void init() throws Exception {
		this.companyIds = merchantProductService.queryCompanyIds();

		SegmentManager.getInstance().reload();
		index_start_time=simpleDateFormat.format(new Date());
		if(processScheduler==null){
			List<Processor> processors =new ArrayList<Processor>();
			processors.add(new SuggestWordProcessor());
			processors.add(new IncIndexProcessor());
			processScheduler = new ProcessScheduler(processors,200);
//			processScheduler.schedule();
		}
		processScheduler.setIndexName(SuggestIndexConstants.indexName_pre + index_start_time);
		processScheduler.setIndexType(SuggestIndexConstants.index_type);
		switcher.createIndex(index_start_time, SuggestIndexConstants.indexName_pre, "/es/"+SuggestIndexConstants.index_mapping_name);

	}

	@Override
	public boolean process() throws Exception {
		FREQUENCY = configService.getInt("suggest_frequency", 1, IndexConstants.DEFAULT_COMPANY_ID);
//		boolean distinguishCompany = configService.getBool("distinguish_company",false,IndexConstants.DEFAULT_COMPANY_ID);
//		boolean hasBusiness = configService.getBool("suggest_has_business", true, IndexConstants.DEFAULT_COMPANY_ID);
//		logger.info("config suggest hasBusiness : " + hasBusiness);
//		logger.info("config distinguishCompany : " + distinguishCompany);

		if(CollectionUtils.isNotEmpty(companyIds)){
			for(Integer companyId : companyIds){
				processScheduler.setCompanyId(companyId);
//				if(hasBusiness){
//					processSuggestBusinessWord(companyId);
//				}else{
//					processSuggestWord(companyId);
//				}
				// 对没有merchantId的数据设为-1
				processSuggestBusinessWord(companyId);
				processAreaSuggestWord(companyId);
			}
			processScheduler.setCompanyId(IndexConstants.DEFAULT_COMPANY_ID);
			processAreaSuggestWord(IndexConstants.DEFAULT_COMPANY_ID);
//			if(!distinguishCompany){
//				processAreaSuggestWord(IndexConstants.DEFAULT_COMPANY_ID);
//			}
		}
		
		processScheduler.close();
		return true;
	}

	private void processAreaSuggestWord(Integer companyId) throws Exception {

		List<Area> areaList;
		if(IndexConstants.DEFAULT_COMPANY_ID == companyId){
			areaList = areaSuggestWordService.getAreaSuggestWords();
		}else {
			areaList = areaSuggestWordService.getAreaSuggestWords(companyId);
		}
		if(CollectionUtils.isNotEmpty(areaList)){
			for(Area area : areaList){
				if(area.getLevel() != 2 || StringUtils.isBlank(area.getName())){
					continue;
				}
				SuggestBusinessWord suggestBusinessWord = new SuggestBusinessWord();
				suggestBusinessWord.setKeyword(area.getName());
				suggestBusinessWord.setPayload(String.valueOf(area.getCode()));
				suggestBusinessWord.setType(SuggestType.AREA.getCode());
				suggestBusinessWord.setCompanyId(companyId);
				processScheduler.put(new DataRecord<SuggestBusinessWord>(suggestBusinessWord));
			}
			areaList.clear();
		}
	}

	private void processSuggestWord(Integer companyId) throws Exception {
		Map<String,Integer> hotWordMap = hotWordService.getHotWords(companyId);
		if(hotWordMap != null && hotWordMap.size() > 0){
			for(Map.Entry<String, Integer> entry : hotWordMap.entrySet()){
				Integer frequency = entry.getValue();
				if(frequency <= FREQUENCY || StringUtils.isBlank(entry.getKey())){
					continue;
				}
				SuggestBusinessWord suggestBusinessWord = new SuggestBusinessWord();
				suggestBusinessWord.setKeyword(entry.getKey());
				suggestBusinessWord.setCompanyId(companyId);
				suggestBusinessWord.setFrequency(frequency);
				processScheduler.put(new DataRecord<SuggestBusinessWord>(suggestBusinessWord));
			}
			hotWordMap.clear();
		}
	}

	private void processSuggestBusinessWord(Integer companyId) throws Exception {
		Map<KeyWord,KeyWord> hotWordMap = hotWordService.getHotKeywords(companyId);
		if(hotWordMap != null && hotWordMap.size() > 0){
			for(Map.Entry<KeyWord, KeyWord> entry : hotWordMap.entrySet()){
				KeyWord keyword = entry.getValue();
				Integer frequency = keyword.getFrequency();
				if(frequency <= FREQUENCY || StringUtils.isBlank(keyword.getKeyword())){
					continue;
				}
				SuggestBusinessWord suggestBusinessWord = new SuggestBusinessWord();
				suggestBusinessWord.setKeyword(keyword.getKeyword());
				suggestBusinessWord.setCompanyId(companyId);
				suggestBusinessWord.setFrequency(frequency);
				suggestBusinessWord.setType(SuggestType.KEYWORD.getCode());
				if (keyword.getMerchantIdSet() == null || keyword.getMerchantIdSet().size() == 0){
					suggestBusinessWord.setMerchantIdList(Arrays.asList(-1L));
				} else {
					suggestBusinessWord.setMerchantIdList(new ArrayList<Long>(keyword.getMerchantIdSet()));
				}
				processScheduler.put(new DataRecord<SuggestBusinessWord>(suggestBusinessWord));
			}
			hotWordMap.clear();
		}

		// 获取积分商城下拉词
		Map<KeyWord,KeyWord> pointWordMap = hotWordService.getPointHotKeywords(companyId);
		if (pointWordMap != null && pointWordMap.size() > 0) {
			for (Map.Entry<KeyWord, KeyWord> entry : pointWordMap.entrySet()) {
				KeyWord keyword = entry.getValue();
				Integer frequency = keyword.getFrequency();
				if(frequency <= FREQUENCY || StringUtils.isBlank(keyword.getKeyword())){
					continue;
				}
				SuggestBusinessWord suggestBusinessWord = new SuggestBusinessWord();
				suggestBusinessWord.setKeyword(keyword.getKeyword());
				suggestBusinessWord.setCompanyId(companyId);
				suggestBusinessWord.setFrequency(frequency);
				suggestBusinessWord.setType(SuggestType.POINT.getCode());
				if (keyword.getMerchantIdSet() == null || keyword.getMerchantIdSet().size() == 0){
					suggestBusinessWord.setMerchantIdList(Arrays.asList(-1L));
				} else {
					suggestBusinessWord.setMerchantIdList(new ArrayList<Long>(keyword.getMerchantIdSet()));
				}
				processScheduler.put(new DataRecord<SuggestBusinessWord>(suggestBusinessWord));
			}
			pointWordMap.clear();
		}
	}

	@Override
	public void done(boolean needValidation) throws Exception {
		
		if (needValidation) {
			if (!switcher.validate(
					ESClient.getClient(),
					SuggestIndexConstants.indexName_pre.replace("_", ""), SuggestIndexConstants.index_type,
					index_start_time)) {
				ESService.deleteIndex(SuggestIndexConstants.indexName_pre + index_start_time);
				return;
			}
		}
		INDEX_NUM = configService.getInt("suggest_index_num", 3, IndexConstants.DEFAULT_COMPANY_ID);
		switcher.switchIndex( index_start_time, SuggestIndexConstants.indexName_pre,
				"/es/" + SuggestIndexConstants.index_mapping_name, SuggestIndexConstants.index_alias, needValidation, companyIds,INDEX_NUM);
	}

	@Override
	public void afterDone() {

	}


}
