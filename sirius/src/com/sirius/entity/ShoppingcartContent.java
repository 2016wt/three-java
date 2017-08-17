package com.sirius.entity;

// default package

/**
 * ShoppingcartContent entity. @author MyEclipse Persistence Tools
 */

public class ShoppingcartContent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5513616869144321403L;
	private Long id;
	private Long shoppingcartId;
	private Long goodsSpecificationId;
	private Integer amount;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShoppingcartId() {
		return this.shoppingcartId;
	}

	public void setShoppingcartId(Long shoppingcartId) {
		this.shoppingcartId = shoppingcartId;
	}

	public Long getGoodsSpecificationId() {
		return this.goodsSpecificationId;
	}

	public void setGoodsSpecificationId(Long goodsSpecificationId) {
		this.goodsSpecificationId = goodsSpecificationId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}