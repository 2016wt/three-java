package com.sirius.entity.query;

import java.util.List;

import com.sirius.entity.GoodsSpecification;

public class GoodsSpecificationQuery extends GoodsSpecification {
	private static final long serialVersionUID = 8128045144361811005L;

	private Double price;
	private String goodsName;
	private String imgUrl;
	private String cou;
	private String order;
	private int warningNum;

	private String num;//数量
	private String singlePrice;//单价
	private String totalPrice;//商品总价
	
	public String getSinglePrice() {
		return singlePrice;
	}

	public void setSinglePrice(String singlePrice) {
		this.singlePrice = singlePrice;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public int getWarningNum() {
		return warningNum;
	}

	public void setWarningNum(int warningNum) {
		this.warningNum = warningNum;
	}

	public String getCou() {
		return cou;
	}

	public void setCou(String cou) {
		this.cou = cou;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	// 不同颜色下的尺码信息
	private List<GoodsSpecification> list;

	public List<GoodsSpecification> getList() {
		return list;
	}

	public void setList(List<GoodsSpecification> list) {
		this.list = list;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
