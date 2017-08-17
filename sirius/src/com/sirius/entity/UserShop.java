package com.sirius.entity;

import java.io.Serializable;

public class UserShop implements Serializable{

	private static final long serialVersionUID = 5374356172634999518L;
	
	private long id;
	private long userId;
	private String shopName;
	private int level;//店铺级别
	private int salesmanId;//业务员
	private String conventionTime;//签约日期
	private int provinceCode;
	private int cityCode;
	private int areaCode;
	private String detailedAddress;//详细地址
	private int runStyle;//运营风格
	private String area;//经营面积
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getSalesmanId() {
		return salesmanId;
	}
	public void setSalesmanId(int salesmanId) {
		this.salesmanId = salesmanId;
	}
	public String getConventionTime() {
		return conventionTime;
	}
	public void setConventionTime(String conventionTime) {
		this.conventionTime = conventionTime;
	}
	public int getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(int provinceCode) {
		this.provinceCode = provinceCode;
	}
	public int getCityCode() {
		return cityCode;
	}
	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}
	public int getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(int areaCode) {
		this.areaCode = areaCode;
	}
	public String getDetailedAddress() {
		return detailedAddress;
	}
	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}
	public int getRunStyle() {
		return runStyle;
	}
	public void setRunStyle(int runStyle) {
		this.runStyle = runStyle;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
}
