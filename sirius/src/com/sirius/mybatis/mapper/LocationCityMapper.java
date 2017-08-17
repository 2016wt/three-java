package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sirius.entity.LocationCity;
import com.sirius.entity.LocationProvince;

public interface LocationCityMapper {

	@Select("select code,city_name from location_city where province_code=#{0}")
	List<LocationCity> list(String provinceCode);
	
	@Select("select code,city_name from location_city where code=#{0}")
	LocationCity getByCode(String code);

	//根据市code获得市name
	@Select("select city_name from location_city where code=#{code}")
	LocationCity cityName(String code);
	
}