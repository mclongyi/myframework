package com.odianyun.search.whale.api.model.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BaseResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4399350255426322800L;
	//搜索到的总商品数，非返回数
	public long totalHit;
}
