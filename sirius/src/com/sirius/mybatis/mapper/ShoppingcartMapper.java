package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sirius.entity.Shoppingcart;
import com.sirius.entity.query.ShoppingcartQuery;

public interface ShoppingcartMapper {

	List<ShoppingcartQuery> shoppingcartByGoods(long shopkeeperId);

	@Select("select * from shoppingcart where goods_id=#{0} and shopkeeper_id=#{1}")
	Shoppingcart getByGoodsAndShopkeeper(long goodsId, long shopkeeperId);

	boolean insert(Shoppingcart shoppingcart);

	List<ShoppingcartQuery> shoppingcarts(
			@Param("list") List<Long> shoppingcartIds);


	void clearByIds(@Param("list") List<Long> shoppingcartIds);

	@Delete("delete from shoppingcart")
	void clearAll();

	double moneyByCarts(List<Long> shoppingcartIds);

	double moneyByCartsVip(List<Long> shoppingcartIds);

}
