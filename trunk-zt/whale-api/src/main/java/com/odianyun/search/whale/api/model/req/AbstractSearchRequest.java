package com.odianyun.search.whale.api.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 抽象父类，所有的搜索请求都必须继承该父类
 *
 * @author zengfenghua
 *
 */
@Data
@NoArgsConstructor
public abstract class AbstractSearchRequest implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	//用户Id
	private String userId;
	//设备唯一标识符
	private String deviceMac;
	//公司id
	private Integer companyId;
	//分页start,第一页为0*count=0，第二页为1*count，第三页2*count
    private int start=0;
	//每页的个数，eg:每页为10，第一页为10,第二页依然为10，。。。
	private int count=10;

	public AbstractSearchRequest(Integer companyId) {
		super();
		this.companyId = companyId;
	}
}
