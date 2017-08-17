package com.sirius.entity;

public class Dictionary implements java.io.Serializable {

	private static final long serialVersionUID = -999938940728771618L;

	
	public static final String DEPOT = "depot";// 仓库

	private Long id;
	private String name;
	private String type;
	private String describe;
	private Long userId;
	private Boolean exist;

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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Boolean getExist() {
		return this.exist;
	}

	public void setExist(Boolean exist) {
		this.exist = exist;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}