package com.sirius.entity;

public class OrderContentStripe implements java.io.Serializable {

	private static final long serialVersionUID = -5553572347183113344L;
	private Long id;
	private Long orderContentId;
	private Long orderId;
	private Long goodsId;
	private Long goodsSpecificationId;
	private String magneticStripe;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderContentId() {
		return this.orderContentId;
	}

	public void setOrderContentId(Long orderContentId) {
		this.orderContentId = orderContentId;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getMagneticStripe() {
		return this.magneticStripe;
	}

	public void setMagneticStripe(String magneticStripe) {
		this.magneticStripe = magneticStripe;
	}

	public Long getGoodsSpecificationId() {
		return goodsSpecificationId;
	}

	public void setGoodsSpecificationId(Long goodsSpecificationId) {
		this.goodsSpecificationId = goodsSpecificationId;
	}

}