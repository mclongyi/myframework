package com.odianyun.search.whale.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttributeValueResult implements java.io.Serializable{
	
	private long id;
	
	private String value;
	
	private long attr_id;
	
	private long count;
	
	private Integer sortValue;
	
	public AttributeValueResult(long id,String value,long attrId,long count,Integer sortValue){
		this.id=id;
		this.value=value;
		this.attr_id=attrId;
		this.count=count;
		this.sortValue=sortValue;
	}
}
