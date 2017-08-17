package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sirius.entity.ShopkeeperVip;

public interface ShopkeeperVipMapper {

	@Select("select * from shopkeeper_vip")
	List<ShopkeeperVip> getAll();

	@Select("select * from shopkeeper_vip where id=#{0}")
	ShopkeeperVip getById(int vipId);

}
