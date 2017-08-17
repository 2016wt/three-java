package com.sirius.entity.query;

import java.util.List;

import com.sirius.entity.Information;

public class InformationQuery extends Information {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8258861531112477525L;

	private List<String> imgs;

	private Integer size;
	private Long markId;

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getMarkId() {
		return markId;
	}

	public void setMarkId(Long markId) {
		this.markId = markId;
	}

}
