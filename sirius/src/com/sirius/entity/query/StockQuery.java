package com.sirius.entity.query;

import com.sirius.entity.Stock;


public class StockQuery extends Stock {
	
	private static final long serialVersionUID = -6271112833902381530L;
	
	private Integer num;//sku的数量

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
