package com.odianyun.search.whale.data.suggest.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.model.suggest.KeyWord;
import com.odianyun.search.whale.data.service.*;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.analysis.ik.IKSegment;
import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.model.suggest.WordWithCompany;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.suggest.dao.ConCurrenceDao;
import com.odianyun.search.whale.data.suggest.service.ConCurrenceService;

public class ConCurrenceServiceImpl implements ConCurrenceService {
	
	@Autowired
	ConCurrenceDao conCurrenceDao;
	@Autowired
	ConfigService configService;
	@Autowired
	MerchantProductService merchantProductService;
	@Autowired
	ProductService productService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	MerchantCategoryService merchantCategoryService;
	@Autowired
	BrandService brandService;


	static Logger logger = Logger.getLogger(ConCurrenceServiceImpl.class);

	
	static final String DEFAULT_DATA_CONFIG = "merchant_product:chinese_name,english_name;brand:name,chinese_name,english_name;category:name";
	
	static final String DEFAULT_DB_TABLE_CONFIG = "merchant_product:product;brand:product;category:product";
	
	static final String DEFAULT_NO_SEG_CONFIG = "brand:name,chinese_name,english_name;category:name";
	
	private static Pattern FILTER = Pattern.compile("^\\w+$");
	
	private static Pattern EN = Pattern.compile("^[A-Za-z]+$");


	IKSegment smart=new IKSegment(true);
	List<Integer> sizeList = Arrays.asList(2,3);
	
	@Override
	public Map<String,Integer> conCurrenceWordCalc(int companyId) throws Exception {
		Map<String,Integer> conCurrenceWordMap = new HashMap<>();
		Map<String,List<String>> dataSourceMap = loadDataSourceConfig(companyId);
//		Map<String,DBType> dbTableMap = loadDbTableConfig(companyId);
		List<String> noSegColumns = loadNoSegConfig(companyId);
		
		if(dataSourceMap.size() > 0){
			for(Map.Entry<String, List<String>> entry : dataSourceMap.entrySet()){
				String tableName = entry.getKey();
				List<String> columnList = entry.getValue();
				for(String column : columnList){
					boolean needSeg = true;
					if(noSegColumns.contains(generateColumnKey(tableName,column))){
						needSeg = false;
					}
					generateConCurrenceWord(companyId,tableName,column,needSeg,conCurrenceWordMap);
				}
			}
		}
		return conCurrenceWordMap;
		
	}

	@Override
	public Map<KeyWord, KeyWord> concurrenceKeywordCalc(int companyId) throws Exception {
		Map<KeyWord, KeyWord> concurrenceKeywordMap = new HashMap<>();

		generateConcurrenceKeyWord(companyId,concurrenceKeywordMap);

		return concurrenceKeywordMap;
	}

	@Override
	public Map<KeyWord, KeyWord> concurrencePointKeywordCalc(Integer companyId) throws Exception {
		Map<KeyWord, KeyWord> concurrencePointKeywordMap = new HashMap<>();
		generatePointConcurrenceKeyWord(companyId, concurrencePointKeywordMap);
		return concurrencePointKeywordMap;
	}

	private void generateConcurrenceKeyWord(int companyId,Map<KeyWord, KeyWord> concurrenceKeywordMap) throws Exception {
		boolean hasNext = true;
		long maxId = -1l;
		int pageSize = 1000;

		List<MerchantProductForSuggest> merchantProducts;
		while (hasNext) {
			merchantProducts = merchantProductService.getMerchantProductsForSuggWithPage(maxId, pageSize, companyId);
			if(merchantProducts == null || merchantProducts.size() == 0 || merchantProducts.size() < pageSize){
				hasNext = false;
			}

			if(merchantProducts != null && merchantProducts.size()>0) {
				logger.info("companyId: "+companyId+" merchantProducts  size : "+merchantProducts.size());

				fillMpDetails(merchantProducts,companyId);
				maxId = merchantProducts.get(merchantProducts.size() - 1).getId();
				addConcurrenceWord(merchantProducts,concurrenceKeywordMap);
				logger.info("ConCurrenceWord size : " + concurrenceKeywordMap.size());

				merchantProducts.clear();
			}

		}
	}

	private void generatePointConcurrenceKeyWord(Integer companyId, Map<KeyWord, KeyWord> concurrencePointKeywordMap) throws Exception {
		boolean hasNext = true;
		long maxId = -1l;
		int pageSize = 1000;

		List<MerchantProductForSuggest> merchantProducts;
		while (hasNext) {
			merchantProducts = merchantProductService.getPointMerchantProductsForSuggWithPage(maxId, pageSize, companyId);
			if(merchantProducts == null || merchantProducts.size() == 0 || merchantProducts.size() < pageSize){
				hasNext = false;
			}

			if(merchantProducts != null && merchantProducts.size()>0) {
				logger.info("companyId: "+companyId+" merchantProducts  size : "+merchantProducts.size());

				fillMpDetails(merchantProducts,companyId);
				maxId = merchantProducts.get(merchantProducts.size() - 1).getId();
				addConcurrenceWord(merchantProducts,concurrencePointKeywordMap);
				logger.info("ConCurrenceWord size : " + concurrencePointKeywordMap.size());

				merchantProducts.clear();
			}

		}
	}

	private void fillMpDetails(List<MerchantProductForSuggest> merchantProducts, int companyId) throws Exception {
		List<Long> productIds = new ArrayList<>();
		for(MerchantProductForSuggest mp : merchantProducts){
			productIds.add(mp.getId());
		}

		Map<Long,Product> productMap = productService.getProducts(productIds,companyId);
		if(productMap == null || productMap.size() == 0){
			return;
		}
		for(MerchantProductForSuggest mp : merchantProducts) {
			Long productId = mp.getProductId();
			Product product = productMap.get(productId);
			if(product != null){
				Long brandId = product.getBrandId();
				if(brandId != null && brandId != 0){
					Brand brand = brandService.getBrandById(brandId,companyId);
					if(brand != null){
						mp.setBrandId(brand.getId());
						mp.setBrandName(brand.getName());
						mp.setBrandChineseName(brand.getChinese_name());
						mp.setBrandEnglishName(brand.getEnglish_name());
					}
				}
				Long categoryId = product.getCategory_id();
				if(categoryId != null && categoryId != 0){
					Category category = categoryService.getCategory(categoryId,companyId);
					if(category != null){
						mp.setCategoryId(category.getId());
						mp.setCategoryName(category.getName());
					}
				}
			}

		}

	}

	private void addConcurrenceWord(List<MerchantProductForSuggest> merchantProducts, Map<KeyWord, KeyWord> concurrenceKeywordMap) throws Exception {
		if(CollectionUtils.isEmpty(merchantProducts)){
			return;
		}
		for(MerchantProductForSuggest mp : merchantProducts){
			Long merchantId = mp.getMerchantId();
			Long companyId = mp.getCompanyId();
			addConcurrenceWord(mp.getBrandName(),concurrenceKeywordMap,false,merchantId,companyId,100);
			addConcurrenceWord(mp.getBrandChineseName(),concurrenceKeywordMap,false,merchantId,companyId,100);
			addConcurrenceWord(mp.getBrandEnglishName(),concurrenceKeywordMap,false,merchantId,companyId,100);
			addConcurrenceWord(mp.getCategoryName(),concurrenceKeywordMap,false,merchantId,companyId,100);
			addConcurrenceWord(mp.getChineseName(),concurrenceKeywordMap,true,merchantId,companyId,1);
			addConcurrenceWord(mp.getEnglishName(),concurrenceKeywordMap,true,merchantId,companyId,1);
			addConcurrenceWord(mp.getSubtitle(),concurrenceKeywordMap,true,merchantId,companyId,1);

		}
	}

	private void addConcurrenceWord(String word, Map<KeyWord, KeyWord> concurrenceKeywordMap,boolean needSeg,Long merchantId,Long companyId,int boost) throws Exception {
		if(org.apache.commons.lang3.StringUtils.isBlank(word)){
			return;
		}
		KeyWord key;
		if(needSeg){
			List<String> segList = smart.segment(word);
			if(CollectionUtils.isEmpty(segList)){
				return;
			}
			Set<String> set = new LinkedHashSet<>();
			for(String str : segList){
				if(isFilterdWord(str)){
					continue;
				}
				set.add(str);
			}
			if(CollectionUtils.isEmpty(set)){
				return;
			}
			List<String> concurrenceWordList = getSubList(new ArrayList<>(set),sizeList);
			if(CollectionUtils.isNotEmpty(concurrenceWordList)){
				for(String concurrenceWord : concurrenceWordList){

					addConcurrenceWord(concurrenceWord,concurrenceKeywordMap,false,merchantId,companyId,1);

				}
			}
		}else{

			key = new KeyWord();
			key.setCompanyId(companyId);
			key.setKeyword(word);
			KeyWord keyWord = concurrenceKeywordMap.get(key);
			if(keyWord == null){
				keyWord = key;
			}
			keyWord.getMerchantIdSet().add(merchantId);
			keyWord.setFrequency(keyWord.getFrequency() + boost);
			concurrenceKeywordMap.put(key, keyWord);

		}

	}



	private String generateColumnKey(String tableName, String column) {
		return tableName +"."+column;
	}

	private List<String> loadNoSegConfig(int companyId) {
		List<String> noSegList = new ArrayList<>();
		String noSegConfig = configService.get("noSegConfig",DEFAULT_NO_SEG_CONFIG,companyId);
		if(StringUtils.isBlank(noSegConfig)){
			return noSegList;
		}
		String[] noSegConfigs = noSegConfig.split(";");
		if(noSegConfigs == null || noSegConfigs.length == 0){
			return noSegList;
		}
		for(String config : noSegConfigs){
			String[] tempStr = config.split(":");
			if(tempStr == null || tempStr.length !=2){
				continue;
			}
			String tableName = tempStr[0];
			String[] columns = tempStr[1].split(",");
			for(String column : columns){
				noSegList.add(generateColumnKey(tableName,column));
			}
		}
		
		return noSegList;
	}

	private Map<String,DBType> loadDbTableConfig(int companyId){
		Map<String,DBType> dbTableMap = new HashMap<>();
		String dbTbaleConfig = configService.get("dbTbaleConfig",DEFAULT_DB_TABLE_CONFIG,companyId);

		if(StringUtils.isBlank(dbTbaleConfig)){
			return dbTableMap;
		}
		String[] dbTbaleConfigs = dbTbaleConfig.split(";");
		if(dbTbaleConfigs == null || dbTbaleConfigs.length == 0){
			return dbTableMap;
		}
		
		for(String config : dbTbaleConfigs){
			String[] tempStr = config.split(":");
			if(tempStr == null || tempStr.length !=2){
				continue;
			}
			dbTableMap.put(tempStr[0], convertDBType(tempStr[1]));
		}
		return dbTableMap;
	}
	
	private Map<String,List<String>> loadDataSourceConfig(int companyId){
		Map<String,List<String>> dataSourceMap = new HashMap<>();
		String dataSourceConfig = configService.get("dataSourceConfig",DEFAULT_DATA_CONFIG,companyId);
		if(StringUtils.isBlank(dataSourceConfig)){
			return dataSourceMap;
		}
		String[] dataSourceConfigs = dataSourceConfig.split(";");
		if(dataSourceConfigs == null || dataSourceConfigs.length == 0){
			return dataSourceMap;
		}
		for(String config : dataSourceConfigs){
			String[] tempStr = config.split(":");
			if(tempStr == null || tempStr.length !=2){
				continue;
			}
			String tableName = tempStr[0];
			String[] columns = tempStr[1].split(",");
			List<String> columnList = new ArrayList<>();
			for(String column : columns){
				columnList.add(column);
			}
			if(columnList.size() > 0){
				dataSourceMap.put(tableName, columnList);
			}
		}
		return dataSourceMap;
	}

	private DBType convertDBType(String dbStr) {
		for(DBType dbType : DBType.values()){
			if(dbType.toString().equals(dbStr)){
				return dbType;
			}
		}
		return null;
	}

	private void generateConCurrenceWord(int companyId,String tableName, String column,boolean needSeg, Map<String,Integer> conCurrenceWordMap) throws Exception{
		long maxId = -1;
		int pageSize = 1000;
		boolean hasNext = true;
		List<WordWithCompany> wordList;
		while(hasNext){
			wordList = conCurrenceDao.getWordsWithPage(companyId,tableName,column,maxId, pageSize);
			if(CollectionUtils.isEmpty(wordList) || wordList.size() < pageSize){
				hasNext = false;
			}
			if(CollectionUtils.isNotEmpty(wordList)){
				logger.info("companyId: "+companyId+" tableName : "+tableName+ " column : "+column+" wordList size : "+wordList.size());
				addConCurrenceWord(conCurrenceWordMap,wordList,needSeg);
				logger.info("ConCurrenceWord size : " + conCurrenceWordMap.size());
				maxId = wordList.get(wordList.size()-1).getId();
				wordList.clear();
			}
			
		}
	}

	private void addConCurrenceWord(Map<String,Integer> conCurrenceWordMap, List<WordWithCompany> wordList,boolean needSeg) throws Exception{

		if(CollectionUtils.isEmpty(wordList)){
			return;
		}
		
		for(WordWithCompany word : wordList){
			if(needSeg){
				List<String> segList = smart.segment(word.getKeyword());
				if(CollectionUtils.isEmpty(segList)){
					continue;
				}
				Set<String> set = new LinkedHashSet<>();
				for(String str : segList){
					if(isFilterdWord(str)){
						continue;
					}
					set.add(str);
				}
				if(CollectionUtils.isEmpty(set)){
					continue;
				}
				List<String> conCurrenceWordList = getSubList(new ArrayList<>(set),sizeList);
				if(CollectionUtils.isNotEmpty(conCurrenceWordList)){	
					for(String conCurrenceWord : conCurrenceWordList){
						String key = conCurrenceWord;
						Integer num = conCurrenceWordMap.get(key);
						conCurrenceWordMap.put(key, num == null ? 1 :(num+1));
					}
				}
			}else{
				String key = word.getKeyword();
				Integer num = conCurrenceWordMap.get(key);
				conCurrenceWordMap.put(key, num == null ? 100 :(num+100));
			}
			
		}
	}

	private boolean isFilterdWord(String str) {
		if(str.length() <= 1){
			return true;
		}
		Matcher n = EN.matcher(str);
		if(n.matches()){
			return false;
		}
		if(str.length() > 4){
			return true;
		}
		n = FILTER.matcher(str);
		if(n.matches() || str.contains("-")){
			return true;
		}
		return false;
	}

	private  List<String> getSubList(List<String> list,List<Integer> sizeList){
		List<String> result = new ArrayList<>();	//用来存放子集的集合，如{{},{1},{2},{1,2}}
		int length = list.size();
		int num = length == 0 ? 0 : 1<<(length);	//2的n次方，若集合set为空，num为0；若集合set有4个元素，那么num为16.
		int maxSize = genMax(sizeList);

		//从0到2^n-1（[00...00]到[11...11]）
		for(int i = 1; i < num; i++){
			boolean flag = false;
			List<Integer> indexList = new ArrayList<>();
			int index = i;
			for(int j = 0; j < length; j++){
				if((index & 1) == 1){		//每次判断index最低位是否为1，为1则把集合set的第j个元素放到子集中
					indexList.add(j);
					if(indexList.size() > maxSize){
						flag = true;
						break;
					}
				}
				index >>= 1;		//右移一位
			}
			if(flag){
				continue;
			}
			if(sizeList.contains(indexList.size())){
				if(isSequence(indexList)){
					String subString = "";
					for(Integer subIndex : indexList){
						subString += list.get(subIndex);
					}
					result.add(subString);		//把子集存储起来
				}
			}
		}
		return result;
	}

	private static int genMax(List<Integer> sizeList) {
		int num = 0;
		if(CollectionUtils.isNotEmpty(sizeList)){
			for(Integer size : sizeList){
				if(size > num){
					num = size;
				}
			}
		}

		return num;
	}

	private boolean isSequence(List<Integer> sizeList) {

		if(CollectionUtils.isEmpty(sizeList)){
			return false;
		}
		if(sizeList.size() < 2){
			return false;
		}
		int length = sizeList.size() - 1;
		if(sizeList.get(length) - sizeList.get(0) >= sizeList.size()){
			return false;
		}

		return true;
	}

	/*@Override
	public List<WordWithCompany> getConCurrenceWordsWithPage(int pageNo, int pageSize,int companyId) throws Exception {
		return conCurrenceDao.getConCurrenceWordsWithPage(pageNo,pageSize);
	}*/

//	@Override
	protected void tryReload(int companyId) throws Exception {
		conCurrenceWordCalc(companyId);
	}

	/*@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		return 24*60;
	}*/

	/*public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("奥迪");
		list.add("A8");
		list.add("技师");
		list.add("专修");
		list.add("大众");
		list.add("北京");
		list.add("门店");
		List<Integer> sizeList = new ArrayList<>();
		sizeList.add(2);
		sizeList.add(3);

		long start = System.currentTimeMillis();
		System.out.println(getSubList(list,sizeList));
		System.out.println("cost : " + (System.currentTimeMillis() -start)+" ms");
	}*/
	public static void main(String[] args) {
		String keyword = "sss";
		boolean flag = false;
		if(keyword.length() <= 1){
			flag =  true;
		}
		Matcher n = EN.matcher(keyword);
		if(n.matches()){
			flag = false;
			System.out.println(flag);

		}
		if(keyword.length() > 4){
			flag =  true;
		}
		n = FILTER.matcher(keyword);
		if(n.matches()){
			flag =  true;
		}
		System.out.println(flag);
	}
}