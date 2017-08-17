package com.sirius.entity;

public class Information implements java.io.Serializable {

	private static final long serialVersionUID = -7082916233294338825L;

	private Long id;
	private String title;
	private String subheading;
	private String content;
	private Integer type;
	private Integer read;
	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubheading() {
		return subheading;
	}

	public void setSubheading(String subheading) {
		this.subheading = subheading;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getRead() {
		return read;
	}

	public void setRead(Integer read) {
		this.read = read;
	}

}