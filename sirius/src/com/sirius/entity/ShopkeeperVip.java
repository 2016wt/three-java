package com.sirius.entity;

public class ShopkeeperVip implements java.io.Serializable {

	private static final long serialVersionUID = 1985503226448108034L;

	private Integer id;
	private Integer days;
	private String describe;
	private Integer price;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDays() {
		return this.days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

}