package com.sirius.entity;
// default package

/**
 * GoodsImg entity. @author MyEclipse Persistence Tools
 */

public class GoodsImg implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7108472285931730605L;
	private Long id;
	private Long goodsId;
	private String imgUrl;

	// Constructors

	/** default constructor */
	public GoodsImg() {
	}

	/** full constructor */
	public GoodsImg(Long goodsId, String imgUrl) {
		this.goodsId = goodsId;
		this.imgUrl = imgUrl;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}