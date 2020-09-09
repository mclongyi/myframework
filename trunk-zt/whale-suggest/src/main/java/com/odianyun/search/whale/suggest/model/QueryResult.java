package com.odianyun.search.whale.suggest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * 被重写的Query
 * @author yuqian
 *
 */
public class QueryResult {

	private String checkQuery; 
	private List<TermWord> twlist;
	private float score;
	public String getCheckQuery() {
		if(CollectionUtils.isNotEmpty(twlist)){
			Collections.sort(twlist, new Comparator<TermWord>(){
				public int compare(TermWord s1, TermWord s2) {  
					   return s1.getIndex()>s2.getIndex()?1:0;  
				   } 
			});
			StringBuilder sb = new StringBuilder();
			for(TermWord tw:twlist){
				sb.append(tw.getText());
			}
			checkQuery =sb.toString();
		}
		return checkQuery;
	}
	public void setCheckQuery(String checkQuery) {
		this.checkQuery = checkQuery;
	}
	public List<TermWord> getTwlist() {
		return twlist;
	}
	public void setTwlist(List<TermWord> twlist) {
		this.twlist = twlist;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	} 
	public void addTermWord(TermWord tw){
		if(twlist==null){
			twlist = new ArrayList<TermWord>();
		}
			twlist.add(tw);
	}
	public void addAllTerms(List<TermWord> words){
		if(twlist==null)
			twlist = words;
		else
			twlist.addAll(words);
	}
	@Override
	public boolean equals(Object obj){
		if(obj==null)
			return false;
		if(getClass()!=obj.getClass())
			return false;
		QueryResult other = (QueryResult) obj;
		if(this.twlist!=other.twlist)
			return false;
		return true;
	}
}
