package com.odianyun.search.whale.suggest.model;

public class TermWord {

	private String text;
	private int type;
	private int index;
	private int flag;
	private float weight=0.0f;
	private boolean selected=false;
	private double IG=0.0;
	private double tfidf=0.0;
	
	public TermWord(String word,int type){
		this.text=word;
		this.type=type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public double getIG() {
		return IG;
	}
	public void setIG(double iG) {
		IG = iG;
	}
	public double getTfidf() {
		return tfidf;
	}
	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}
	
	@Override
	public boolean equals(Object obj){
	  if(obj==null)
		  return false;
	  if(getClass()!= obj.getClass())
		  return false;
	  TermWord other = (TermWord) obj;
	  if(this.text!= other.text)
		  return false;
	  if(this.type!= other.type)
		  return false;
	  if(this.weight!=other.weight)
		  return false;
	  if(this.index!=other.index)
		  return false;
	  return true;
	}
	
	@Override
	public String toString(){
		return "TermWord:[text="+text+",type="+type
				+",weight="+weight+",selected="+selected+"]";
	}
}
