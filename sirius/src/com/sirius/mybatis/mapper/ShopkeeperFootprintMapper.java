package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sirius.entity.ShopkeeperFootprint;
import com.sirius.entity.query.ShopkeeperFootprintQuery;

public interface ShopkeeperFootprintMapper {

	List<ShopkeeperFootprintQuery> getList(
			@Param("shopkeeperId") long shopkeeperId, @Param("size") int size);

	List<ShopkeeperFootprintQuery> getListMark(
			@Param("shopkeeperId") long shopkeeperId, @Param("size") int size,
			@Param("markId") long markId);

	@Insert("insert shopkeeper_footprint(shopkeeper_id,goods_id) values(#{shopkeeperId},#{goodsId})")
	void insert(ShopkeeperFootprint shopkeeperFootprint);

	@Select("select count(1) from shopkeeper_footprint where shopkeeper_id=#{shopkeeperId} and goods_id=#{goodsId}")
	boolean checkHad(ShopkeeperFootprint shopkeeperFootprint);

	@Delete("delete from shopkeeper_footprint where shopkeeper_id=#{shopkeeperId} and goods_id=#{goodsId}")
	void delete(ShopkeeperFootprint shopkeeperFootprint);

}
