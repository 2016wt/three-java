package com.sirius.entity;

public class LocationCity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1286827972822188131L;
	private String code;
	private String cityName;
	private String spell;
	private String provinceCode;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getSpell() {
		return this.spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public String getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

}