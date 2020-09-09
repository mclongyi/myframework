package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 四级区域数据对象
 * @author yuqian
 *
 */
public class Area {

	private long id;
	
	private long code;
	
	private String name="";
	
	private int level=-1;
	
	private long parentCode=-1;
	
	private String postCode="";
	
	private long company_id=-1;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id","id" );
        resultMap.put("code","code");
        resultMap.put("name","name");
        resultMap.put("level","level");
        resultMap.put("parentCode","parentCode");
        resultMap.put("postCode","postCode");
        resultMap.put("company_id","company_id");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getParentCode() {
		return parentCode;
	}

	public void setParentCode(long parentCode) {
		this.parentCode = parentCode;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
}
