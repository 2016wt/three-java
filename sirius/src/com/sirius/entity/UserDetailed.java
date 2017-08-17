package com.sirius.entity;

import java.io.Serializable;

public class UserDetailed implements Serializable {

	
	private static final long serialVersionUID = 5416952809776405827L;

	private long id;
	private long userId;
	private int levelCustomer;//等级
	private long shopId;//所属店铺
	private String addTime;//加入时间
	private int age;
	private	String sex;
	private String address;
	private	int provinceCode;//个人居住地址
	private int cityCode;
	private int areaCode;
	private String number;//身份证
	private int work;
	private String marry;//是否为婚
	private	int nativeProvince;//ji'guan
	private int nativeCity;
	private int nativeArea;
	private String property;//生肖
	private String speciality;//特长
	private String constellation;//星座
	private String style;//对应店铺
	private	int shopProvince;
	private int shopCity;
	private int shopArea;
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getWork() {
		return work;
	}
	public void setWork(int work) {
		this.work = work;
	}
	public String getMarry() {
		return marry;
	}
	public void setMarry(String marry) {
		this.marry = marry;
	}
	public int getNativeProvince() {
		return nativeProvince;
	}
	public void setNativeProvince(int nativeProvince) {
		this.nativeProvince = nativeProvince;
	}
	public int getNativeCity() {
		return nativeCity;
	}
	public void setNativeCity(int nativeCity) {
		this.nativeCity = nativeCity;
	}
	public int getNativeArea() {
		return nativeArea;
	}
	public void setNativeArea(int nativeArea) {
		this.nativeArea = nativeArea;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public int getShopProvince() {
		return shopProvince;
	}
	public void setShopProvince(int shopProvince) {
		this.shopProvince = shopProvince;
	}
	public int getShopCity() {
		return shopCity;
	}
	public void setShopCity(int shopCity) {
		this.shopCity = shopCity;
	}
	public int getShopArea() {
		return shopArea;
	}
	public void setShopArea(int shopArea) {
		this.shopArea = shopArea;
	}
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
	public int getLevelCustomer() {
		return levelCustomer;
	}
	public void setLevelCustomer(int levelCustomer) {
		this.levelCustomer = levelCustomer;
	}
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
}
