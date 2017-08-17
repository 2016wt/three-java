package com.sirius.entity.query;

import java.util.List;

import com.sirius.entity.OrderContentStripe;

public class OrderContentStripeQuery extends OrderContentStripe {

	private static final long serialVersionUID = 574972836799354280L;

	private List<String> stripes;

	public List<String> getStripes() {
		return stripes;
	}

	public void setStripes(List<String> stripes) {
		this.stripes = stripes;
	}
}
