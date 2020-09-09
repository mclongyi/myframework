package com.odianyun.search.whale.index.business.process;


import com.odianyun.search.whale.analysis.PinYin;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.data.service.MerchantProductCombineService;
import com.odianyun.search.whale.index.business.process.base.BaseSearchWordProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchWordProcessor extends BaseSearchWordProcessor{

	private MerchantProductCombineService merchantProductCombineService ;

	ConfigService configService;

    public SearchWordProcessor(){
    	merchantProductCombineService = (MerchantProductCombineService) ProcessorApplication.getBean("merchantProductCombineService");
		configService = (ConfigService) ProcessorApplication.getBean("configService");
    }

	@Override
	public void calcSearchWord(Map<Long,BusinessProduct> map,Integer companyId) throws Exception {
		boolean is_single_name = configService.getBool("is_single_name",false,companyId);
		boolean is_single_pinyin = configService.getBool("is_single_pinyin",false,companyId);
		List<Long> mpIds = new ArrayList<>(map.keySet());
		Map<Long,List<MerchantProduct>> subProductMapList = merchantProductCombineService.querySubMerchantProducts(mpIds, companyId) ;
		if(map !=null && map.size()>0){
			for(Map.Entry<Long,BusinessProduct> entry : map.entrySet()){
				BusinessProduct businessProduct = entry.getValue();
				StringBuffer tag_wordsBuffer=new StringBuffer();
				String chineseName=businessProduct.getChinese_name();
				if(chineseName!=null){
					tag_wordsBuffer.append(chineseName);
                    if(is_single_name){
						for(int i=0;i<chineseName.length();i++){
							String singleName=chineseName.substring(i,i+1);
							if(isChinese(singleName.charAt(0))){
								tag_wordsBuffer.append(" "+singleName);
							}
						}
					}
					if (is_single_pinyin && chineseName.length() > 0) {
						String firstSingleName = chineseName.substring(0,1);
						String firstSpell = PinYin.toFirstSpell(firstSingleName);
						tag_wordsBuffer.append(" "+firstSpell);
					}
				}
				if(businessProduct.getEnglish_name()!=null){
					tag_wordsBuffer.append(" "+businessProduct.getEnglish_name());
				}

				if(businessProduct.getSubtitle()!=null){
					tag_wordsBuffer.append(" "+businessProduct.getSubtitle());
				}
				if(subProductMapList.containsKey(entry.getKey())){
					List<MerchantProduct> subProductList = subProductMapList.get(entry.getKey());
					if(CollectionUtils.isNotEmpty(subProductList)){
						for(MerchantProduct subProduct : subProductList){
							if(subProduct.getChinese_name()!=null){
								tag_wordsBuffer.append(" "+subProduct.getChinese_name());
							}
							if(subProduct.getEnglish_name()!=null){
								tag_wordsBuffer.append(" "+subProduct.getEnglish_name());
							}
							if(subProduct.getSubtitle()!=null){
								tag_wordsBuffer.append(" "+subProduct.getSubtitle());
							}
						}
					}
				}
				businessProduct.setTag_words(tag_wordsBuffer.toString());
			}
		}
	}

	public static boolean isChinese(char c) {
		return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
	}


}
