package com.sirius.entity.query;

import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.ShoppingcartContent;

public class ShoppingcartContentQuery extends ShoppingcartContent {
	private static final long serialVersionUID = -2256334374742558074L;

	private GoodsSpecification goodsSpecification;

	private GoodsQuery goods;

	private Double money;

	public GoodsSpecification getGoodsSpecification() {
		return goodsSpecification;
	}

	public void setGoodsSpecification(GoodsSpecification goodsSpecification) {
		this.goodsSpecification = goodsSpecification;
	}

	public GoodsQuery getGoods() {
		return goods;
	}

	public void setGoods(GoodsQuery goods) {
		this.goods = goods;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

}
