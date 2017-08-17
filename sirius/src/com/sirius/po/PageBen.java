package com.sirius.po;

import java.io.Serializable;
import java.util.List;

public class PageBen<T> implements Serializable {

	private static final long serialVersionUID = -8929943291923793221L;

	private List<T> list;

	private int page;

	private int pageSize;

	private int count;

	public int getMaxPage() {
		if (count % pageSize == 0) {
			return count / pageSize;
		} else {
			return count / pageSize + 1;

		}
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
