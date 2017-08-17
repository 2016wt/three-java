package com.sirius.entity.query;

import java.util.List;

import com.sirius.entity.Dictionary;
import com.sirius.entity.Goods;
import com.sirius.entity.GoodsImg;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.OrderContent;
import com.sirius.entity.UserAddress;
import com.sirius.util.StringUtil;

// default package

/**
 * Goods entity. @author MyEclipse Persistence Tools
 */

public class GoodsQuery extends Goods {
	private static final long serialVersionUID = 7398244115830494298L;

	private OrderContent orderContent;
	private long orderId;
	private GoodsImg goodsImg;
	// 颜色列表
	private List<String> colors;
	// 尺码列表
	private List<String> sizes;
	
	//订单钱数
	private double money;

	private String size;

	private Dictionary goodsgenreValue;

	private Dictionary stylegenreValue;

	private List<ShoppingcartQuery> shoppingcart;

	private UserAddress userAddress;

	private Integer evaluateCount;// 评论数

	// 成团数量
	private Integer clustered;

	private Integer limit;

	private Integer offset;

	private String order;

	private Integer salesVolume;// 销量

	private Double clusterSchedule;// 成团进度

	//商品分类
	private List<Integer> goodsgenres;

	//风格分类
	private List<Integer> stylegenres;

	private String imgUrl;

	private String keyword;
	
	// 开始时间
	private String createTimeStart;
	// 结束时间
	private String createTimeEnd;

	private List<GoodsEvaluateQuery> goodsEvaluates;

	// 库存
	private Integer quantity;
	
	private Integer earlyWarning;
	
	
	//商品详情表
	private List<GoodsSpecification> specifications;

	//商品图片
	private List<GoodsImg> goodsImgList;
	
	//省市
	private String province;
	private String city;

	private String sell;
	
	public String getSell() {
		return sell;
	}

	public void setSell(String sell) {
		this.sell = sell;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public OrderContent getOrderContent() {
		return orderContent;
	}

	public void setOrderContent(OrderContent orderContent) {
		this.orderContent = orderContent;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public GoodsImg getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(GoodsImg goodsImg) {
		this.goodsImg = goodsImg;
	}

	public List<GoodsImg> getGoodsImgList() {
		return goodsImgList;
	}
	
	public void setGoodsImgList(List<GoodsImg> goodsImgList) {
		this.goodsImgList = goodsImgList;
	}

	public Boolean getWarning() {
		if (quantity == null || earlyWarning == null)
			return null;
		return (quantity <=+- earlyWarning);

	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public List<String> getSizes() {
		return sizes;
	}

	public void setSizes(List<String> sizes) {
		this.sizes = sizes;
	}

	public Dictionary getGoodsgenreValue() {
		return goodsgenreValue;
	}

	public void setGoodsgenreValue(Dictionary goodsgenreValue) {
		this.goodsgenreValue = goodsgenreValue;
	}

	public Dictionary getStylegenreValue() {
		return stylegenreValue;
	}

	public void setStylegenreValue(Dictionary stylegenreValue) {
		this.stylegenreValue = stylegenreValue;
	}

	public List<ShoppingcartQuery> getShoppingcart() {
		return shoppingcart;
	}

	public void setShoppingcart(List<ShoppingcartQuery> shoppingcart) {
		this.shoppingcart = shoppingcart;
	}

	public Integer getEvaluateCount() {
		return evaluateCount;
	}

	public void setEvaluateCount(Integer evaluateCount) {
		this.evaluateCount = evaluateCount;
	}

	public Integer getClustered() {
		return clustered;
	}

	public void setClustered(Integer clustered) {
		this.clustered = clustered;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getOrder() {
		if (order != null) {
			order = order.replace("asc", "");
			order = order.trim();
			if (StringUtil.isNullOrEmpty(order)) {
				order = null;
			}
		}
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}

	public Double getClusterSchedule() {
		return clusterSchedule;
	}

	public void setClusterSchedule(Double clusterSchedule) {
		this.clusterSchedule = clusterSchedule;
	}

	public List<Integer> getGoodsgenres() {
		return goodsgenres;
	}

	public void setGoodsgenres(List<Integer> goodsgenres) {
		this.goodsgenres = goodsgenres;
	}

	public List<Integer> getStylegenres() {
		return stylegenres;
	}

	public void setStylegenres(List<Integer> stylegenres) {
		this.stylegenres = stylegenres;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<GoodsEvaluateQuery> getGoodsEvaluates() {
		return goodsEvaluates;
	}

	public void setGoodsEvaluates(List<GoodsEvaluateQuery> goodsEvaluates) {
		this.goodsEvaluates = goodsEvaluates;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public UserAddress getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Integer getEarlyWarning() {
		return earlyWarning;
	}

	public void setEarlyWarning(Integer earlyWarning) {
		this.earlyWarning = earlyWarning;
	}

	public List<GoodsSpecification> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(List<GoodsSpecification> specifications) {
		this.specifications = specifications;
	}

}