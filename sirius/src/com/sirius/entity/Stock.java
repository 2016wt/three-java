package com.sirius.entity;

// default package


/**
 * Stock entity. @author MyEclipse Persistence Tools
 */

public class Stock implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2873512603919740330L;
	private Long id;
	private String sku;
	private String magneticStripe;
	private Boolean outed;
	private Long userId;
	private Long goodsSpecificationId;
	private String createTime;
	private String outTime;
	private Long orderId;

	private Integer error;
	// Property accessors

	public Long getId() {
		return this.id;
	}

	public Integer getError() {
		return error;
	}

	public void setError(Integer error) {
		this.error = error;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getMagneticStripe() {
		return this.magneticStripe;
	}

	public void setMagneticStripe(String magneticStripe) {
		this.magneticStripe = magneticStripe;
	}

	public Boolean getOuted() {
		return outed;
	}

	public void setOuted(Boolean outed) {
		this.outed = outed;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGoodsSpecificationId() {
		return this.goodsSpecificationId;
	}

	public void setGoodsSpecificationId(Long goodsSpecificationId) {
		this.goodsSpecificationId = goodsSpecificationId;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOutTime() {
		return this.outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

}