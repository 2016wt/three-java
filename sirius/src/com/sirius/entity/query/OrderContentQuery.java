package com.sirius.entity.query;

import com.sirius.entity.OrderContent;

public class OrderContentQuery extends OrderContent {
	private static final long serialVersionUID = -1548113148886670747L;

	private String goodsName;
	private String imgUrl;
	private String color;
	private String size;
	private String sku;
	private Double price;
	private Integer gross;
	private Integer quantity;
	private GoodsQuery goods;

	private String orderNo;
	private String tradeNo;
	private Integer limit;
	private Integer offset;

	private String totalNum;//总数量
	private String totalMoney;//总金额

	private String yesterdayGoodsNum;//昨天销售的数量
	
	private String yesterdaytotalMoney;//昨天销售的金额
	
	private Integer number;
	
	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getYesterdayGoodsNum() {
		return yesterdayGoodsNum;
	}

	public void setYesterdayGoodsNum(String yesterdayGoodsNum) {
		this.yesterdayGoodsNum = yesterdayGoodsNum;
	}

	public String getYesterdaytotalMoney() {
		return yesterdaytotalMoney;
	}

	public void setYesterdaytotalMoney(String yesterdaytotalMoney) {
		this.yesterdaytotalMoney = yesterdaytotalMoney;
	}


	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public GoodsQuery getGoods() {
		return goods;
	}

	public void setGoods(GoodsQuery goods) {
		this.goods = goods;
	}

	public Integer getGross() {
		return gross;
	}

	public void setGross(Integer gross) {
		this.gross = gross;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}
