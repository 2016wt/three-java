package com.sirius.entity;

public class Inventory implements java.io.Serializable {

	private static final long serialVersionUID = -2947511723557806116L;
	private Long id;
	private String magneticStripe;
	private String sku;
	private Long goodsSpecificationId;
	private Long userId;
	private String createTime;
	private String serialNumber;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMagneticStripe() {
		return this.magneticStripe;
	}

	public void setMagneticStripe(String magneticStripe) {
		this.magneticStripe = magneticStripe;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getGoodsSpecificationId() {
		return this.goodsSpecificationId;
	}

	public void setGoodsSpecificationId(Long goodsSpecificationId) {
		this.goodsSpecificationId = goodsSpecificationId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}