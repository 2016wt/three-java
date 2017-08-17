package com.sirius.entity;

public class InformationImg implements java.io.Serializable {

	private static final long serialVersionUID = 3046217978796153413L;
	private Long id;
	private Long informationId;
	private String url;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInformationId() {
		return this.informationId;
	}

	public void setInformationId(Long informationId) {
		this.informationId = informationId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}