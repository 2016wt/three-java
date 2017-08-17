package com.sirius.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.sirius.entity.Inventory;

public interface InventoryMapper {

	@Insert("insert into inventory(magnetic_stripe,sku,goods_specification_id,user_id) "
			+ "values(#{magneticStripe},#{sku},#{goodsSpecificationId},#{userId})")
	boolean insert(Inventory inventory);

	@Insert("insert into inventory(magnetic_stripe,sku,goods_specification_id,user_id,serial_number,create_time) "
			+ "values(#{magneticStripe},#{sku},#{goodsSpecificationId},#{userId},#{serialNumber},#{createTime})")
	void insertInventory(Inventory inventory);
}
