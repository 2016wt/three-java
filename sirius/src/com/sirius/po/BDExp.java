package com.sirius.po;

import java.io.Serializable;
import java.util.List;

/**
 * 百度物流服务服务
 * 
 * @author dohko
 * 
 */
public class BDExp implements Serializable {

	private static final long serialVersionUID = -5046127357374525716L;

	private int status;
	private String msg;
	private Result result;

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

	public static class Result implements Serializable {

		private static final long serialVersionUID = -5923433269486982020L;

		private String number;
		private String type;
		private List<Status> list;
		// 1在途中 2派件中 3已签收 4派送失败(拒签等)
		private int deliverystatus;
		private int issign;

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<Status> getList() {
			return list;
		}

		public void setList(List<Status> list) {
			this.list = list;
		}

		public int getDeliverystatus() {
			return deliverystatus;
		}

		public void setDeliverystatus(int deliverystatus) {
			this.deliverystatus = deliverystatus;
		}

		public int getIssign() {
			return issign;
		}

		public void setIssign(int issign) {
			this.issign = issign;
		}

	}

	public static class Status implements Serializable {

		private static final long serialVersionUID = -4835009078244621541L;

		private String time;
		private String status;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}

}
