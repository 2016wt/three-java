package com.sirius.entity;


public class Out implements java.io.Serializable {

	private static final long serialVersionUID = -6231007178189840102L;
	
	private Long id;
	private Long userId;//店铺Id
	private String outNo;
	private String sku;
	private String magneticStripe;
	private Integer status;
	private String outTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOutNo() {
		return outNo;
	}
	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getMagneticStripe() {
		return magneticStripe;
	}
	public void setMagneticStripe(String magneticStripe) {
		this.magneticStripe = magneticStripe;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
}