package com.sirius.entity.query;

import java.util.List;

import com.sirius.entity.Shoppingcart;
import com.sirius.entity.ShoppingcartContent;

public class ShoppingcartQuery extends Shoppingcart {
	private static final long serialVersionUID = -8320200735008022212L;

	private GoodsQuery goods;
	private List<ShoppingcartContent> contents;
	public GoodsQuery getGoods() {
		return goods;
	}
	public void setGoods(GoodsQuery goods) {
		this.goods = goods;
	}
	public List<ShoppingcartContent> getContents() {
		return contents;
	}
	public void setContents(List<ShoppingcartContent> contents) {
		this.contents = contents;
	}

}
