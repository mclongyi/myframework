package com.odianyun.search.whale.api.model.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 自动提示结果
 * @author yuqian
 *
 */
@Data
@NoArgsConstructor
public class SuggestResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//关键词
	private String keyword;
	//搜索结果数
	private int count;
	//附加的信息
	private Map attachedInfo;
	
	public SuggestResult(String keyword,int count){
		this.keyword = keyword;
		this.count = count;
	}
	
}
