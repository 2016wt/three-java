package com.sirius.entity.query;

import com.sirius.entity.LocationArea;
import com.sirius.entity.LocationCity;
import com.sirius.entity.LocationProvince;
import com.sirius.entity.UserAddress;

public class UserAddressQuery extends UserAddress {
	private static final long serialVersionUID = 3171130138153202212L;

	private LocationProvince locationProvince;
	private LocationCity locationCity;
	private LocationArea locationArea;

	public LocationProvince getLocationProvince() {
		return locationProvince;
	}

	public void setLocationProvince(LocationProvince locationProvince) {
		this.locationProvince = locationProvince;
	}

	public LocationCity getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(LocationCity locationCity) {
		this.locationCity = locationCity;
	}

	public LocationArea getLocationArea() {
		return locationArea;
	}

	public void setLocationArea(LocationArea locationArea) {
		this.locationArea = locationArea;
	}

}
