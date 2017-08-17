package com.sirius.entity;

// default package

/**
 * GoodsEvaluate entity. @author MyEclipse Persistence Tools
 */

public class GoodsEvaluate implements java.io.Serializable {

	private static final long serialVersionUID = 3368427518497255972L;

	private Long id;
	private Long goodsId;
	private Long goodsSpecificationId;
	private Long shopkeeperId;
	private Long orderId;
	private String content;
	private Double grade;
	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getShopkeeperId() {
		return shopkeeperId;
	}

	public void setShopkeeperId(Long shopkeeperId) {
		this.shopkeeperId = shopkeeperId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getGoodsSpecificationId() {
		return goodsSpecificationId;
	}

	public void setGoodsSpecificationId(Long goodsSpecificationId) {
		this.goodsSpecificationId = goodsSpecificationId;
	}

}