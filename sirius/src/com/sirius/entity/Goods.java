package com.sirius.entity;

import com.sirius.util.StringUtil;

public class Goods implements java.io.Serializable {

	private static final long serialVersionUID = -8985121865014097005L;

	private Long id;
	private Long wholesalerId;
	private String goodsName;
	private String spu;
	private Double price;
	private Double vipPrice;
	private Double marketPrice;
	private String describe;
	private String thickness;
	private String quality;
	private Integer goodsgenre;
	private Integer stylegenre;
	private String buyTimeStart;
	private String buyTimeEnd;
	private Long wholesalerAddressId;
	private Integer clustering;
	private Boolean putaway;
	private String createTime;
	private Boolean exist;
	// 流行元素
	private Integer fashionId;

	// 季节
	private Integer season;

	// 上市年份
	private String marketYear;

	// 出厂日期
	private String factoryDate;

	// 版型
	private String model;

	// 产地
	private String productionPlace;

	// 省
	private String productionProvince;

	// 品牌
	private Integer trademarkId;

	// 城市编码
	private Integer cityCode;

	public String getProductionProvince() {
		return productionProvince;
	}

	public void setProductionProvince(String productionProvince) {
		this.productionProvince = productionProvince;
	}

	public Integer getFashionId() {
		return fashionId;
	}

	public void setFashionId(Integer fashionId) {
		this.fashionId = fashionId;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public String getMarketYear() {
		return marketYear;
	}

	public void setMarketYear(String marketYear) {
		this.marketYear = marketYear;
	}

	public String getFactoryDate() {
		return StringUtil.isNullOrEmpty(factoryDate) ? factoryDate
				: factoryDate.split(" ")[0];
	}

	public void setFactoryDate(String factoryDate) {
		this.factoryDate = factoryDate;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
	}

	public Integer getTrademarkId() {
		return trademarkId;
	}

	public void setTrademarkId(Integer trademarkId) {
		this.trademarkId = trademarkId;
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public Boolean getExist() {
		return exist;
	}

	public void setExist(Boolean exist) {
		this.exist = exist;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWholesalerId() {
		return this.wholesalerId;
	}

	public void setWholesalerId(Long wholesalerId) {
		this.wholesalerId = wholesalerId;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getBuyTimeStart() {
		return this.buyTimeStart;
	}

	public void setBuyTimeStart(String buyTimeStart) {
		this.buyTimeStart = buyTimeStart;
	}

	public String getBuyTimeEnd() {
		return this.buyTimeEnd;
	}

	public void setBuyTimeEnd(String buyTimeEnd) {
		this.buyTimeEnd = buyTimeEnd;
	}

	public Integer getClustering() {
		return this.clustering;
	}

	public void setClustering(Integer clustering) {
		this.clustering = clustering;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public Integer getStylegenre() {
		return stylegenre;
	}

	public void setStylegenre(Integer stylegenre) {
		this.stylegenre = stylegenre;
	}

	public Integer getGoodsgenre() {
		return goodsgenre;
	}

	public void setGoodsgenre(Integer goodsgenre) {
		this.goodsgenre = goodsgenre;
	}

	public Double getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(Double vipPrice) {
		this.vipPrice = vipPrice;
	}

	public Boolean getPutaway() {
		return putaway;
	}

	public void setPutaway(Boolean putaway) {
		this.putaway = putaway;
	}

	public Long getWholesalerAddressId() {
		return wholesalerAddressId;
	}

	public void setWholesalerAddressId(Long wholesalerAddressId) {
		this.wholesalerAddressId = wholesalerAddressId;
	}

	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

}