package com.sirius.entity;

public class OrderContent implements java.io.Serializable {
	private static final long serialVersionUID = -6447001353389289447L;
	private Long id;
	private Long orderId;
	private Long goodsId;
	private Long goodsSpecificationId;
	private Integer number;
	private Integer count;//未绑定磁条的件数
	private Double money;
	private Integer status;
	private Integer payType;
	private String sonOrderNo;
	private Boolean stockup;
	private Long wholesalerId;
	
	public Long getWholesalerId() {
		return wholesalerId;
	}

	public void setWholesalerId(Long wholesalerId) {
		this.wholesalerId = wholesalerId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getGoodsSpecificationId() {
		return this.goodsSpecificationId;
	}

	public void setGoodsSpecificationId(Long goodsSpecificationId) {
		this.goodsSpecificationId = goodsSpecificationId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getSonOrderNo() {
		return sonOrderNo;
	}

	public void setSonOrderNo(String sonOrderNo) {
		this.sonOrderNo = sonOrderNo;
	}

	public Boolean getStockup() {
		return stockup;
	}

	public void setStockup(Boolean stockup) {
		this.stockup = stockup;
	}

}