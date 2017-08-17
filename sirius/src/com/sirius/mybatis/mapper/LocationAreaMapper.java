package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sirius.entity.LocationArea;
import com.sirius.entity.query.Location;

public interface LocationAreaMapper {

	@Select("select code,area_name from location_area where city_code=#{0}")
	List<LocationArea> list(String cityCode);

	/**
	 * 根据县级行政单位code获得省市县的信息
	 * 
	 * @param areaCode
	 * @return
	 */
	@Select("select location_area.`code` area_code,location_area.area_name ,location_city.`code` city_code,location_city.city_name,location_province.`code` province_code,location_province.province_name "
			+ "from location_area left join location_city on location_area.city_code=location_city.`code` "
			+ "left join location_province on location_city.province_code=location_province.`code` where location_area.`code`=#{0}")
	Location getLocation(String areaCode);

	@Select("select code,area_name from location_area where code=#{0}")
	LocationArea getByCode(String code);
}