package com.sirius.entity.query;

import com.sirius.entity.GoodsEvaluate;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.User;

public class GoodsEvaluateQuery extends GoodsEvaluate {

	private static final long serialVersionUID = -486817586235352529L;

	private GoodsSpecification goodsSpecification;

	private User shopkeeper;

	public GoodsSpecification getGoodsSpecification() {
		return goodsSpecification;
	}

	public void setGoodsSpecification(GoodsSpecification goodsSpecification) {
		this.goodsSpecification = goodsSpecification;
	}

	public User getShopkeeper() {
		return shopkeeper;
	}

	public void setShopkeeper(User shopkeeper) {
		this.shopkeeper = shopkeeper;
	}

}
