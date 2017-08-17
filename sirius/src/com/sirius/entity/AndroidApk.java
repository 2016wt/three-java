package com.sirius.entity;

// default package

/**
 * AndroidApk entity. @author MyEclipse Persistence Tools
 */

public class AndroidApk implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6663488938252566469L;
	private Long id;
	private String url;
	private Integer version;
	private Boolean constraint;
	private Integer size;
	private Integer type;
	private String createTime;

	// Constructors

	/** default constructor */
	public AndroidApk() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getConstraint() {
		return this.constraint;
	}

	public void setConstraint(Boolean constraint) {
		this.constraint = constraint;
	}

	public Integer getSize() {
		return this.size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}