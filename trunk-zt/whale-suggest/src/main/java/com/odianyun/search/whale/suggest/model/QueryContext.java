package com.odianyun.search.whale.suggest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryContext {

	private String original;
	
	private int count = 0;
	
	private List<TermWord> origTokens = new ArrayList<TermWord>();
	
	private Map<Integer,List<Integer>> indexMap = new HashMap<Integer,List<Integer>>();
	
	private Map<Integer,List<TermWord>> termMap = new HashMap<Integer,List<TermWord>>();

	public QueryContext(String input){
		this.original = input;
	}
	
	public String getOriginal() {
		return original;
	}
	
	public void setOriginal(String original) {
		this.original = original;
	}

	public int getCount() {
		return this.origTokens.size();
	}
	
	public void addTerm(TermWord tw){
		this.origTokens.add(tw);
		List<Integer> termIndexs = indexMap.get(tw.getType());
		if(termIndexs==null){
			termIndexs = new ArrayList<Integer>();
		}
		termIndexs.add(tw.getIndex());
		indexMap.put(tw.getType(), termIndexs);
		
		List<TermWord> termWords = termMap.get(tw.getType());
		if(termWords==null){
			termWords = new ArrayList<TermWord>();
		}
		termWords.add(tw);
		termMap.put(tw.getType(), termWords);
		count++;
	}

	public List<TermWord> getOrigTokens() {
		return origTokens;
	}

	public Map<Integer, List<Integer>> getIndexMap() {
		return indexMap;
	}
	
	public Map<Integer,List<TermWord>> getTermMap(){
		return termMap;
	}
	
}
