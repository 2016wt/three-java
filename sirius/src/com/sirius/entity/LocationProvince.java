package com.sirius.entity;

public class LocationProvince implements java.io.Serializable {

	private static final long serialVersionUID = 4225236288172601247L;
	private String code;
	private String provinceName;
	private String spell;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getSpell() {
		return this.spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

}