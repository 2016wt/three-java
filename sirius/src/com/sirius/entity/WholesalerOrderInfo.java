package com.sirius.entity;
// default package

/**
 * WholesalerOrderInfo entity. @author MyEclipse Persistence Tools
 */

public class WholesalerOrderInfo implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1370625713839612175L;
	private Long id;
	private Long wholesalerOrderId;
	private Long wholesalerGoodsId;
	private Integer quantity;

	// Constructors

	/** default constructor */
	public WholesalerOrderInfo() {
	}

	/** full constructor */
	public WholesalerOrderInfo(Long wholesalerOrderId, Long wholesalerGoodsId,
			Integer quantity) {
		this.wholesalerOrderId = wholesalerOrderId;
		this.wholesalerGoodsId = wholesalerGoodsId;
		this.quantity = quantity;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWholesalerOrderId() {
		return this.wholesalerOrderId;
	}

	public void setWholesalerOrderId(Long wholesalerOrderId) {
		this.wholesalerOrderId = wholesalerOrderId;
	}

	public Long getWholesalerGoodsId() {
		return this.wholesalerGoodsId;
	}

	public void setWholesalerGoodsId(Long wholesalerGoodsId) {
		this.wholesalerGoodsId = wholesalerGoodsId;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}