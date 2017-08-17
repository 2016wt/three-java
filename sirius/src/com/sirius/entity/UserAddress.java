package com.sirius.entity;

public class UserAddress implements java.io.Serializable {
	private static final long serialVersionUID = 2786470337450477104L;
	private Long id;
	private String name;
	private String phone;
	private Boolean defaults;
	private Long userId;
	private String provinceCode;
	private String cityCode;
	private String areaCode;
	private String detailed;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDetailed() {
		return this.detailed;
	}

	public void setDetailed(String detailed) {
		this.detailed = detailed;
	}

	public Boolean getDefaults() {
		return defaults;
	}

	public void setDefaults(Boolean defaults) {
		this.defaults = defaults;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}