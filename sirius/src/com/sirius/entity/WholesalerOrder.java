package com.sirius.entity;
// default package


/**
 * WholesalerOrder entity. @author MyEclipse Persistence Tools
 */

public class WholesalerOrder implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1320219257590496863L;
	private Long id;
	private Long wholesalerId;
	private String orderNo;
	private String createTime;

	// Constructors

	/** default constructor */
	public WholesalerOrder() {
	}


	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWholesalerId() {
		return this.wholesalerId;
	}

	public void setWholesalerId(Long wholesalerId) {
		this.wholesalerId = wholesalerId;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}