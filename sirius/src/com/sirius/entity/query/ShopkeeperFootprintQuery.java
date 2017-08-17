package com.sirius.entity.query;

import com.sirius.entity.ShopkeeperFootprint;

public class ShopkeeperFootprintQuery extends ShopkeeperFootprint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 852653449216929669L;

	private GoodsQuery goods;

	public GoodsQuery getGoods() {
		return goods;
	}

	public void setGoods(GoodsQuery goods) {
		this.goods = goods;
	}

}
