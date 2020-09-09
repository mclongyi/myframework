package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 商家Model
 * @author yuqian
 *
 */
public class Merchant {

	private long id;
	
	private String name;
	
	private String company_name=""; //公司名称
	
	private Integer merchant_type=0; //公司类型   10: 自营  11:第三方商户
	
	private Long company_id;//项目id，例如宜和、卓仕
	//联系人
    private String linkman;
	//联系电话
	private String tel;
    //是否虚拟商家,1-实体，0-虚拟
	private Integer virtualType;
	//门店
	private Integer merchant_flag;

	private long parentId;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id","id" );
        resultMap.put("name","name" );
        resultMap.put("company_name","company_name");
        resultMap.put("merchant_type","merchant_type");
        resultMap.put("linkman","identity_card_name");
        resultMap.put("tel","mobile");
        resultMap.put("virtualType","is_leaf");
        resultMap.put("company_id","company_id");
		resultMap.put("merchant_flag","merchant_flag");
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public Integer getMerchant_type() {
		return merchant_type;
	}
	public void setMerchant_type(Integer merchant_type) {
		this.merchant_type = merchant_type;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Integer getVirtualType() {
		return virtualType;
	}
	public void setVirtualType(Integer virtualType) {
		this.virtualType = virtualType;
	}

	public Integer getMerchant_flag() {
		return merchant_flag;
	}

	public void setMerchant_flag(Integer merchant_flag) {
		this.merchant_flag = merchant_flag;
	}

	public static Map<String, String> getResultmap() {
		return resultMap;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
}
