package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.LocationArea;
import com.sirius.entity.LocationCity;
import com.sirius.entity.LocationProvince;
import com.sirius.entity.UserAddress;
import com.sirius.entity.query.Location;
import com.sirius.entity.query.UserAddressQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.LocationAreaMapper;
import com.sirius.mybatis.mapper.LocationCityMapper;
import com.sirius.mybatis.mapper.LocationProvinceMapper;
import com.sirius.mybatis.mapper.UserAddressMapper;
import com.sirius.util.StringUtil;

@Service
public class AddressService {

	@Resource
	private LocationProvinceMapper locationProvinceMapper;
	@Resource
	private LocationCityMapper locationCityMapper;
	@Resource
	private LocationAreaMapper locationAreaMapper;
	@Resource
	private UserAddressMapper userAddressMapper;

	public List<LocationProvince> provinceList() {
		return locationProvinceMapper.list();
	}
	
	/**
	 * 根据省code获得省name
	 * @return
	 */
	public LocationProvince provinceName(String code) {
		return locationProvinceMapper.provinceName(code);
	}
	
	/**
	 * 根据省code获得省name
	 * @return
	 */
	public LocationCity cityName(String code) {
		return locationCityMapper.cityName(code);
	}
	
	public List<LocationCity> cityList(String provinceCode) {
		return locationCityMapper.list(provinceCode);
	}

	public List<LocationArea> areaList(String cityCode) {
		return locationAreaMapper.list(cityCode);
	}

	public void addAddress(UserAddress address) {

		if (userAddressMapper.exitsCount(address.getUserId()) >= 10) {
			throw new XException("最多只能录入10个地址！");
		}
		if (StringUtil.isNullOrEmpty(address.getName())) {
			throw new XException("姓名缺失");
		} else if (!StringUtil.phoneCheck(address.getPhone())) {
			throw new XException("手机号格式错误");
		} else if (StringUtil.isNullOrEmpty(address.getAreaCode())) {
			throw new XException("县级行政单位缺失");
		} else if (StringUtil.isNullOrEmpty(address.getDetailed())) {
			throw new XException("详细地址缺失");
		}

		Location location = locationAreaMapper.getLocation(address.getAreaCode());
		address.setProvinceCode(location.getProvinceCode());
		address.setCityCode(location.getCityCode());
		if (address.getDefaults()) {
			userAddressMapper.clearDefaults(address.getUserId());
		}
		if (address.getId() == null)
			userAddressMapper.insert(address);
		else if (!userAddressMapper.update(address))
			throw new XException("修改失败");
	}

	public List<UserAddressQuery> addressList(long shopkeeperId) {
		return userAddressMapper.addressList(shopkeeperId);
	}

	public void removeAddress(UserAddress address) {
		if (!userAddressMapper.removeAddress(address))
			throw new XException("无法删除");

	}

	public List<UserAddressQuery> wholesalerAddressList(long wholesalerAddressId) {
		return userAddressMapper.addressList(wholesalerAddressId);
	}

	public UserAddress dafaultAddres(long shopkeeperId) {

		return userAddressMapper.defaultAddres(shopkeeperId);
	}

	public UserAddressQuery shopkeeperAddress(long addressId) {
		return userAddressMapper.getById(addressId);
	}
	

}
