package com.sirius.entity;

public class PayRecord implements java.io.Serializable {
	private static final long serialVersionUID = 3575484418667972068L;
	private Long id;
	private Long shopkeeperId;
	private String orderNo;
	private String tradeNo;
	private Integer type;
	private Integer intention;
	private Integer status;
	private Long relevanceId;
	private Double money;
	private String describe;
	private String updateTime;
	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIntention() {
		return intention;
	}

	public void setIntention(Integer intention) {
		this.intention = intention;
	}

	public Long getRelevanceId() {
		return relevanceId;
	}

	public void setRelevanceId(Long relevanceId) {
		this.relevanceId = relevanceId;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getShopkeeperId() {
		return shopkeeperId;
	}

	public void setShopkeeperId(Long shopkeeperId) {
		this.shopkeeperId = shopkeeperId;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

}