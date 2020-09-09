package com.odianyun.search.whale.suggest.check;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.odianyun.search.whale.suggest.common.LexemeTypeConstants;
import com.odianyun.search.whale.suggest.model.QueryContext;
import com.odianyun.search.whale.suggest.model.QueryResult;
import com.odianyun.search.whale.suggest.model.TermWord;

import java.util.Random;
import java.util.Set;

public class DelCheckStrategy implements CheckStrategy {

	@Override
	public List<QueryResult> reWrite(QueryContext context) throws Exception{
		
		List<QueryResult> results = new ArrayList<QueryResult>();
//		List<TermWord> orgiList =context.getOrigTokens();
		Map<Integer,List<TermWord>> termWordMap = context.getTermMap();
		List<TermWord> brandlist=termWordMap.get(LexemeTypeConstants.TYPE_BRAND);
		List<TermWord> modelist = termWordMap.get(LexemeTypeConstants.TYPE_MODEL);
		List<TermWord> categorylist = termWordMap.get(LexemeTypeConstants.TYPE_CATEGORY);
		
		results = UnionRules(categorylist, brandlist, modelist);
		List<TermWord> others = selectOtherType(context);
		if(!results.isEmpty()){
			if(others.size()>0){
				for(QueryResult qr:results)
					qr.addAllTerms(others);
			}
		}else if(!others.isEmpty()){
			QueryResult ret = new QueryResult();
			ret.addAllTerms(others);
			results.add(ret);
		}
		return results;

	}
	
	/**
	 * 如果产品品牌的个数大于等于2,每次只保留一个产品品牌，和其它的分词连在一起形成新的Query；
	如果产品型号的个数大于2,每次只保留一个产品型号，和其它的分词连在一起形成新的Query；
	当产品类型个数大于2，且存在产品类型和产品品牌时，每次只保留一个产品类型。
	当只含有产品类型且个数大于等于3,不存在产品型号和品牌时，一定保留的是最后一个产品类型；
	 * @param categorylist
	 * @param brandlist
	 * @param modelist
	 * @param origWordlist
	 * @return
	 */
	public List<QueryResult> UnionRules(List<TermWord> categorylist,
			List<TermWord> brandlist,List<TermWord> modelist){
		
		Set<QueryResult> indexResults=new HashSet<QueryResult>(); 
		if(brandlist.size()>0){
			for(int i=0;i<brandlist.size();i++){
				if(modelist.size()>0){
					for(int j=0;j<modelist.size();j++){
						if(categorylist.size()>0){
							for(int k =0;k<categorylist.size();k++){
								QueryResult ret = new QueryResult();
								ret.addTermWord(brandlist.get(i));
								ret.addTermWord(modelist.get(j));
								ret.addTermWord(categorylist.get(k));
								indexResults.add(ret);
							}
						}
					}
				}
				else if(categorylist.size()>0){
					for(int k =0;k<categorylist.size();k++){
						QueryResult ret = new QueryResult();
						ret.addTermWord(brandlist.get(i));
						ret.addTermWord(categorylist.get(k));
						indexResults.add(ret);
					}
				}
				
			}
		}else if(modelist.size()==0){
			int size = categorylist.size();
			if(size>=3){
				QueryResult ret = new QueryResult();
				ret.addTermWord(categorylist.get(size-1));
				indexResults.add(ret);
			}
		}
		return new ArrayList<QueryResult>(indexResults);
	}
	
	/**
	 * 针对其他Type词进行选择过滤，每个类型只取熵最大的词
	 * @param context
	 * @return
	 */
	public List<TermWord> selectOtherType(QueryContext context){
		List<TermWord> otherList = new ArrayList<TermWord>();
		Iterator<Entry<Integer, List<TermWord>>> iterator =context.getTermMap().entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Integer, List<TermWord>> entry = iterator.next();
			if(entry.getKey()==LexemeTypeConstants.TYPE_BRAND||
					entry.getKey()==LexemeTypeConstants.TYPE_CATEGORY||
					entry.getKey()==LexemeTypeConstants.TYPE_MODEL){
				continue;
			}
			
			List<TermWord> types = entry.getValue();
			if(types.size()==1){
				otherList.add(types.get(0));
			}else{
				TermWord maxWord = findMaxWeight(types);
				otherList.add(maxWord);
			}
		}
		return otherList;
	}
	
	public TermWord findMaxWeight(List<TermWord> wordlist){
		TermWord result =null;
		float max = Float.MIN_VALUE;
		int index1=0,index2=0;
		for(int i=0;i<wordlist.size();i++){
			TermWord tw = wordlist.get(i);
			if(tw.getWeight()!=0.0 && tw.getWeight() >max){
				max = tw.getWeight();
				if(tw.isSelected()){
					index1 = i;
				}else{
					index2 = i;
				}
			}
		}
		if(index2!=0){
			TermWord temp = wordlist.get(index2);
			temp.setSelected(true);
			result = temp;
		}else{
			TermWord temp = wordlist.get(index1);
			temp.setSelected(true);
			result = temp;
		}
		return result;
	}
	
	public TermWord findMinWeight(List<TermWord> wordlist){
		TermWord result =null;
		float min = Float.MAX_VALUE;
		int index1 = 0,index2=0;
		for(int i=0;i<wordlist.size();i++){
			TermWord tw = wordlist.get(i);
			if(tw.getWeight()!=0.0 && tw.getWeight()<min){
				min = tw.getWeight();
				if(tw.isSelected()){
					index1 = i;
				}else{
					index2 = i;
				}
			}
		}
		if(index2!=0){
			TermWord temp = wordlist.get(index2);
			temp.setSelected(true);
			result = temp;
		}else{
			TermWord temp = wordlist.get(index1);
			temp.setSelected(true);
			result = temp;
		}
		return result;
	}

	public static void main(String[] args) {
		DelCheckStrategy testStrategy = new DelCheckStrategy();
		/*List<TermWord> wordlist =new ArrayList<TermWord>();
		Random rd1 = new Random();
		for(int i=1;i<10;i++){
			TermWord tw =new TermWord(i+"",0);
			if(i%2==0){
				tw.setSelected(true);
			}
			tw.setWeight(rd1.nextFloat());
			wordlist.add(tw);
			System.out.println(tw.toString());
		}
		TermWord min=testStrategy.findMinWeight(wordlist);
		System.out.println("最小的TermWord为："+min.toString());*/
		
		List<Integer> brandlist =new ArrayList<Integer>();
//		brandlist.add(1);brandlist.add(2);brandlist.add(3);
		List<Integer> modelist =new ArrayList<Integer>();
//		modelist.add(4);modelist.add(5);modelist.add(6);
		List<Integer> categorylist =new ArrayList<Integer>();
		categorylist.add(7);categorylist.add(8);categorylist.add(9);
//		testStrategy.UnionRules(categorylist, brandlist, modelist);
	}
}
