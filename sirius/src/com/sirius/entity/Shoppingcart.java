package com.sirius.entity;

// default package

/**
 * Shoppingcart entity. @author MyEclipse Persistence Tools
 */

public class Shoppingcart implements java.io.Serializable {
	private static final long serialVersionUID = 7712104191130112870L;
	private Long id;
	private Long shopkeeperId;
	private Long goodsId;

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

	public Long getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

}