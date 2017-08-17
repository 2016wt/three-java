package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.ShoppingcartContent;
import com.sirius.entity.query.ShoppingcartContentQuery;

public interface ShoppingcartContentMapper {

	@Insert("insert into shoppingcart_content(shoppingcart_id,goods_specification_id,amount) "
			+ "values(#{shoppingcartId},#{goodsSpecificationId},#{amount})")
	boolean insert(ShoppingcartContent shoppingcart);

	@Select("select count(1) from shoppingcart_content where shoppingcart_id=#{shoppingcartId} and goods_specification_id=#{goodsSpecificationId}")
	boolean checkHaving(ShoppingcartContent shoppingcart);

	@Update("update shoppingcart_content set amount=amount+#{amount} where shoppingcart_id=#{shoppingcartId} and goods_specification_id=#{goodsSpecificationId}")
	boolean amountPlus(ShoppingcartContent shoppingcart);

	List<ShoppingcartContent> getByshoppingcartIds(
			@Param("shoppingcartIds") List<Long> shoppingcartIds);

	List<ShoppingcartContent> getByIds(
			@Param("list") List<Long> shoppingcartContentIds);

	void clearByCartIds(@Param("list") List<Long> shoppingcartIds);

	@Delete("delete from shoppingcart_content")
	void clearAll();

	@Select("select * from shoppingcart_content where id=#{0}")
	ShoppingcartContent getBaseById(long shoppingcartContentId);

	@Update("update shoppingcart_content set amount=amount+#{amount} where id=#{id}")
	void shoppingcartContentChange(ShoppingcartContent shoppingcartContent);

	double moneyByCarts(List<Long> shoppingcartContentIds);

	List<ShoppingcartContentQuery> shoppingcartContents(
			@Param("list") List<Long> shoppingcartContentIds);

	Double moneyByIds(List<Long> shoppingcartContentIds);

	Double moneyByIdsVip(List<Long> shoppingcartContentIds);

	void clearByIds(List<Long> shoppingcartContentIds);

	@Delete("delete from shoppingcart_content where id=#{0}")
	boolean contentDelete(long shoppingcartContentId);

	@Delete("delete from shoppingcart_content "
			+ "where goods_specification_id in (select id from goods_specification where goods_id in (select id from goods where putaway=0)) "
			+ "and shoppingcart_id in (select id from shoppingcart where shopkeeper_id=#{0})")
	void clearLoseEfficacy(long shopkeeperId);

}
