package com.sirius.po;

import java.io.Serializable;

public class AlipayRefundInfo implements Serializable {

	private static final long serialVersionUID = 9186875365709280464L;

	private String out_trade_no;
	private String trade_no;
	private Double refund_amount;

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public Double getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(Double refund_amount) {
		this.refund_amount = refund_amount;
	}

}
