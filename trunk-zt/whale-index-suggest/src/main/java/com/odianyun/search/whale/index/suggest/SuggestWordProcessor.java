package com.odianyun.search.whale.index.suggest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.odianyun.search.whale.analysis.ik.IKSegment;
import com.odianyun.search.whale.api.model.SuggestType;
import com.odianyun.search.whale.common.util.PinYin;
import com.odianyun.search.whale.data.model.suggest.SuggestBusinessWord;
import com.odianyun.search.whale.data.model.suggest.SuggestWord;
import com.odianyun.search.whale.index.api.common.SuggestIndexConstants;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public class SuggestWordProcessor implements Processor{

	static Logger logger = Logger.getLogger(SuggestWordProcessor.class);
	IKSegment smart = new IKSegment(true);
	
	@Override
	public String getName() {
		return SuggestWordProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		List<DataRecord> newDataRecords = new ArrayList<>();
		for(DataRecord<SuggestBusinessWord> dataRecord : dataRecords){
			SuggestBusinessWord suggestBusinessWord = dataRecord.getV();
			SuggestWord suggestWord = calcSuggestWord(suggestBusinessWord);
			if(suggestWord != null ){
				newDataRecords.add(new DataRecord<SuggestWord>(suggestWord));
			}
		}
		if(newDataRecords.size() > 0){
			processorContext.setDataRecords(newDataRecords);
		}
	}
	
	public SuggestWord calcSuggestWord(SuggestBusinessWord suggestBusinessWord) throws Exception{
		
		if(StringUtils.isBlank(suggestBusinessWord.getKeyword()))
			return null;
		
		SuggestWord suggWord = new SuggestWord();
		
		try {
			
			generateSuggestWord(suggWord,suggestBusinessWord);
			
			/*String spell = PinYin.toPinYin(keyword);
			suggWord.setSpell(spell);
			String firstSpell = PinYin.toFirstSpell(keyword);
			suggWord.setFirstSpell(firstSpell);
			String orderStr = PinYin.toSort(keyword);
			suggWord.setCharacterSort(orderStr);
			List<String> cuts = PinYin.cutOneChar(keyword);
			if(CollectionUtils.isNotEmpty(cuts)){
				StringBuilder sb =new StringBuilder();
				for(String c:cuts){
					sb.append(c).append(" ");
				}
				suggWord.setCharacterCut(sb.toString());
			}*/
			
			
		} catch (Exception e) {
			logger.error("SuggestWordProcessor: error calcSuggestWord-"+ e.getMessage());
			return null;
		}
		return suggWord;
	}

	private void generateSuggestWord(SuggestWord suggWord, SuggestBusinessWord suggestBusinessWord) throws Exception{
		List<Long> merchantIdList = new ArrayList<>();
		merchantIdList.add(-1l);
		String keyword = suggestBusinessWord.getKeyword();
		suggWord.setKeyword(keyword);
		keyword = keyword.toLowerCase();
		String spell = PinYin.toPinYin(keyword);
		suggWord.setSpell(spell);
		String firstSpell = PinYin.toFirstSpell(keyword);
		suggWord.setFirstSpell(firstSpell);
		suggWord.setSearchFrequency(suggestBusinessWord.getFrequency());
		/*String prefix = suggestBusinessWord.getCompanyId() + "_";
		if(suggestBusinessWord.getType() == SuggestType.AREA.getCode()){
			prefix = SuggestIndexConstants.AREA_PREFIX + prefix ;
			suggWord.setKeyword(keyword + "_" + suggestBusinessWord.getPayload());
		}
		StringBuilder input = new StringBuilder(prefix + keyword);
		input.append(",");
		input.append(prefix + spell);
		input.append(",");
		input.append(prefix + firstSpell);*/
		StringBuilder input = new StringBuilder(keyword);
		input.append(",");
		input.append(spell);
		input.append(",");
		input.append(firstSpell);

		if(suggestBusinessWord.getType() == SuggestType.AREA.getCode()){
			suggWord.setIsArea(SuggestType.AREA.getCode());
			suggWord.setKeyword(keyword + "_" + suggestBusinessWord.getPayload());
		}
		if(CollectionUtils.isNotEmpty(suggestBusinessWord.getMerchantIdList())){
			merchantIdList.addAll(suggestBusinessWord.getMerchantIdList());
		}
		if(suggestBusinessWord.getType() == SuggestType.KEYWORD.getCode()){
			List<String> segList = smart.segment(keyword);
			if(CollectionUtils.isEmpty(segList)){
				return;
			}

			for(String seg : segList){
				
				String subString = keyword.substring(keyword.indexOf(seg)+seg.length());
				if(StringUtils.isBlank(subString)){
					continue;
				}
				String subSpell = PinYin.toPinYin(subString);
				String subFirstSpell = PinYin.toFirstSpell(subString);
				
				input.append(",");
				input.append( subString);
				input.append(",");
				input.append(subSpell);
				input.append(",");
				input.append(subFirstSpell);
			}
		}
		suggWord.setInput(input.toString());
		suggWord.setCompanyId(suggestBusinessWord.getCompanyId());
		suggWord.setMerchantIdList(merchantIdList);
	}

	/*private void generateSuggestWord(SuggestWord suggWord, SuggestBusinessWord suggestBusinessWord) throws Exception{
		// TODO Auto-generated method stub
		String keyword = suggestBusinessWord.getKeyword();
		suggWord.setKeyword(keyword);
		keyword = keyword.toLowerCase();
		String spell = PinYin.toPinYin(keyword);
		suggWord.setSpell(spell);
		String firstSpell = PinYin.toFirstSpell(keyword);
		suggWord.setFirstSpell(firstSpell);
		suggWord.setSearchFrequency(suggestBusinessWord.getFrequency());
		String prefix = suggestBusinessWord.getCompanyId() + "_";
		if(suggestBusinessWord.getType() == SuggestType.AREA.getCode()){
			prefix = SuggestIndexConstants.AREA_PREFIX + prefix ;
			suggWord.setKeyword(keyword + "_" + suggestBusinessWord.getPayload());
		}
		StringBuilder input = new StringBuilder(prefix + keyword);
		input.append(",");
		input.append(prefix + spell);
		input.append(",");
		input.append(prefix + firstSpell);

		if(suggestBusinessWord.getType() == SuggestType.KEYWORD.getCode()){
			List<String> segList = smart.segment(keyword);
			if(CollectionUtils.isEmpty(segList)){
				return;
			}

			for(String seg : segList){

				String subString = keyword.substring(keyword.indexOf(seg)+seg.length());
				if(StringUtils.isBlank(subString)){
					continue;
				}
				String subSpell = PinYin.toPinYin(subString);
				String subFirstSpell = PinYin.toFirstSpell(subString);

				input.append(",");
				input.append(prefix + subString);
				input.append(",");
				input.append(prefix + subSpell);
				input.append(",");
				input.append(prefix + subFirstSpell);
			}
		}
		suggWord.setInput(input.toString());

	}*/


}
