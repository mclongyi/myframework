package com.odianyun.search.whale.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AttributeResult implements java.io.Serializable{
	
	private long id;
	
    private String name;
    
    private List<AttributeValueResult> attributeValues=new ArrayList<AttributeValueResult>();
    
    private long count;
    
    public AttributeResult(long id,String name){
    	this.id=id;
    	this.name=name;
    }
}
