package com.sirius.entity.query;

import java.util.List;

import com.sirius.entity.Order;

public class OrderQuery extends Order {
	private static final long serialVersionUID = 4640991399288676988L;

	private Integer limit;
	private Integer offset;

	// 订单内容列表
	private List<OrderContentQuery> orderContents;

	private Long wholesalerId;

	private String order;

	private String username;

	// 商品名
	private String goodsName;

	// 商品编号列表
	private List<String> goodsId;
	
	//商品spu
	private List<String> spu;


	private String goodsid;

	// 搜索下单日期开始
	private String paytimeStart;
	// 搜索下单日期结束
	private String paytimeEnd;

	private String keyword;
	

	public List<String> getSpu() {
		return spu;
	}

	public void setSpu(List<String> spu) {
		this.spu = spu;
	}

	public String getPaytimeStart() {
		return paytimeStart;
	}

	public void setPaytimeStart(String paytimeStart) {
		this.paytimeStart = paytimeStart;
	}

	public String getPaytimeEnd() {
		return paytimeEnd;
	}

	public void setPaytimeEnd(String paytimeEnd) {
		this.paytimeEnd = paytimeEnd;
	}

	public List<String> getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(List<String> goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public List<OrderContentQuery> getOrderContents() {
		return orderContents;
	}

	public void setOrderContents(List<OrderContentQuery> orderContents) {
		this.orderContents = orderContents;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getWholesalerId() {
		return wholesalerId;
	}

	public void setWholesalerId(Long wholesalerId) {
		this.wholesalerId = wholesalerId;
	}
}
