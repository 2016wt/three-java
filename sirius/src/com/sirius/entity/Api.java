package com.sirius.entity;



public class Api implements java.io.Serializable {

	private static final long serialVersionUID = -1208603834078432791L;
	
	private Integer id;
	private String url;
	private String createTime;


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}