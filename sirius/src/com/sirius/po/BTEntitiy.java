package com.sirius.po;

import java.io.Serializable;
import java.util.List;

/**
 * bootstarp-table 数据返回格式
 * 
 * @author dohko
 * 
 * @param <?>
 */
public class BTEntitiy implements Serializable {
	private static final long serialVersionUID = 872568913505698676L;

	public BTEntitiy() {
	}

	public BTEntitiy(int total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}

	private int total;

	private List<?> rows;

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
