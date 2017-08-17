package com.sirius.entity;

// default package


/**
 * ShopkeeperFootprint entity. @author MyEclipse Persistence Tools
 */

public class ShopkeeperFootprint implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9062240394693891096L;
	// Fields

	private Long id;
	private Long shopkeeperId;
	private Long goodsId;
	private String createTime;

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

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}