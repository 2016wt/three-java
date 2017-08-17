package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sirius.entity.Goods;
import com.sirius.entity.LocationCity;
import com.sirius.entity.LocationProvince;

public interface LocationProvinceMapper {

	@Select("select code,province_name from location_province")
	List<LocationProvince> list();
	
	//根据省code获得省name
	@Select("select province_name from location_province where code=#{code}")
	LocationProvince provinceName(String code);
	
	@Select("select code,province_name from location_province where code=#{0}")
	LocationProvince getByCode(String code);
	
}