package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.User;
import com.sirius.entity.UserAddress;
import com.sirius.entity.query.UserAddressQuery;

public interface UserAddressMapper {

	@Insert("insert user_address(name,phone,defaults,user_id,province_code,city_code,area_code,detailed) "
			+ "values(#{name},#{phone},#{defaults},#{userId},#{provinceCode},#{cityCode},#{areaCode},#{detailed})")
	boolean insert(UserAddress shopkeeperAddress);

	List<UserAddressQuery> addressList(long userId);

	@Select("select count(1) from user_address where user_id=#{0} and defaults")
	boolean existsDefault(long userId);

	@Select("select * from user_address where id=#{0}")
	UserAddress getBaseById(long addressId);

	@Delete("delete from user_address  where id=#{id} and user_id=#{userId}")
	boolean removeAddress(UserAddress address);

	@Select("select count(1) from user_address where user_id=#{userId}")
	int exitsCount(long userId);

	@Update("update user_address set defaults=0 where user_id=#{userId}")
	void clearDefaults(long userId);

	
	UserAddressQuery defaultAddres(long userId);

	UserAddressQuery getById(long addressId);

	@Update("update user_address set name=#{name},phone=#{phone},defaults=#{defaults},province_code=#{provinceCode}"
			+ ",city_code=#{cityCode},area_code=#{areaCode},detailed=#{detailed} "
			+ " where id=#{id} and user_id=#{userId}")
	boolean update(UserAddress shopkeeperAddress);
}