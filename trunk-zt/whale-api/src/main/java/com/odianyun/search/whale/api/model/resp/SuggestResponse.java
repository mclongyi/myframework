package com.odianyun.search.whale.api.model.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SuggestResponse extends BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SuggestResult> suggestResult;

}
