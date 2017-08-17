package com.sirius.entity;

public class Order implements java.io.Serializable {
	private static final long serialVersionUID = 8061759826598529791L;
	private Long id;
	private String name;
	private String phone;
	private Long shopkeeperId;
	private String orderNo;
	private Double money;
	private String provinceCode;
	private String cityCode;
	private String areaCode;
	private String fullAddress;
	private Integer status;
	private String remark;
	private String expressNo;
	private String createTime;
	private String payTime;
	private Integer payType;
	private Boolean refunded;
	private Boolean evaluated;
	private Boolean exist;
	private String updateTime;

	private String goodsName;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopkeeperId() {
		return this.shopkeeperId;
	}

	public void setShopkeeperId(Long shopkeeperId) {
		this.shopkeeperId = shopkeeperId;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getFullAddress() {
		return this.fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public Boolean getRefunded() {
		return refunded;
	}

	public void setRefunded(Boolean refunded) {
		this.refunded = refunded;
	}

	public Boolean getEvaluated() {
		return evaluated;
	}

	public void setEvaluated(Boolean evaluated) {
		this.evaluated = evaluated;
	}

	public Boolean getExist() {
		return exist;
	}

	public void setExist(Boolean exist) {
		this.exist = exist;
	}

}