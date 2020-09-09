package com.odianyun.search.whale.api.model;

import java.util.List;

import com.odianyun.search.whale.api.model.geo.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Merchant implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8382255757018481584L;

	private Long id;
	
	private String name;
	//联系人
	private String linkman;
	//联系电话
	private String tel;
	//经纬度
	private Point point;
	//商家距离搜索位置的距离
	private String distance;
	//保留字段
	private Double rating;
	//商家所属区域code，非覆盖区域，顺序为浦东新区-上海市-上海—中国
	private List<Long> areaCodes;
	//是否虚拟商家,1-实体，0-虚拟
	private Integer virtualType;

	//父商家id
	private Long parentId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public Integer getVirtualType() {
		return virtualType;
	}

	public void setVirtualType(Integer virtualType) {
		this.virtualType = virtualType;
	}

	public List<Long> getAreaCodes() {
		return areaCodes;
	}

	public void setAreaCodes(List<Long> areaCodes) {
		this.areaCodes = areaCodes;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "Merchant [id=" + id + ", name=" + name + ", linkman=" + linkman
				+ ", tel=" + tel + ", point=" + point + ", distance="
				+ distance + ", rating=" + rating + ", areaCodes=" + areaCodes
				+ ", virtualType=" + virtualType + "]";
	}
	
	

}
