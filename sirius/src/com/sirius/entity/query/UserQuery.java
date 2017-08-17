package com.sirius.entity.query;

import com.sirius.entity.User;
import com.sirius.entity.UserDetailed;
import com.sirius.entity.UserShop;

public class UserQuery extends User {

	private static final long serialVersionUID = 2015354118962899582L;

	private UserShop userShop;//用户店铺表
	private UserDetailed userDetailed;//用户详细信息表
	
	private String code;
	private int levelShop;//店铺等级
	private int levelCustomer;
	private String conventionTime;//签约日期
	private String shopName;//店铺名称
	private long shopid;//顾客所属门店
	private int provinceCode;//顾客消费的店铺的省
	private int cityCode;//顾客消费的店铺的市
	private String addTime;//加入时间
	private int age;
	private String sex;
	private String address;//详细地址
	private String number;//身份证
	
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

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

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}


	public long getShopid() {
		return shopid;
	}

	public void setShopid(long shopid) {
		this.shopid = shopid;
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

	public int getLevelCustomer() {
		return levelCustomer;
	}

	public void setLevelCustomer(int levelCustomer) {
		this.levelCustomer = levelCustomer;
	}

	public UserDetailed getUserDetailed() {
		return userDetailed;
	}

	public void setUserDetailed(UserDetailed userDetailed) {
		this.userDetailed = userDetailed;
	}

	public UserShop getUserShop() {
		return userShop;
	}

	public void setUserShop(UserShop userShop) {
		this.userShop = userShop;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getLevelShop() {
		return levelShop;
	}

	public void setLevelShop(int levelShop) {
		this.levelShop = levelShop;
	}

	public String getConventionTime() {
		return conventionTime;
	}

	public void setConventionTime(String conventionTime) {
		this.conventionTime = conventionTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
