package com.odianyun.search.whale.api.model.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 区域类型
 * @author yuqian
 *
 */
@Data
@NoArgsConstructor
public class AreaResult implements java.io.Serializable{
	//区域code
	private long areaCode;
	//区域名称
	private String areaName;
	//聚类统计个数
	private long count;

}
