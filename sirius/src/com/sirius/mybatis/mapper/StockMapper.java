package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.Stock;
import com.sirius.entity.query.StockQuery;

public interface StockMapper {

	@Insert("insert into stock(sku,magnetic_stripe,user_id,goods_specification_id,"
			+ "order_id) values(#{sku},#{magneticStripe},#{userId},"
			+ "#{goodsSpecificationId},#{orderId})")
	boolean insert(Stock stock);

	//************
	@Select("select * from stock where magnetic_stripe=#{magneticStripe}")
	Stock selec(Stock stock);
	
	@Select("select * from stock where magnetic_stripe=#{magneticStripe} and user_id=#{userId} order by id desc limit 1")
	Stock getPut(Stock stock);

	@Update("update stock set outed=1,out_time=now() where id=#{0}")
	void outLibrary(long stockId);

	@Select("select count(1) from stock where user_id=#{0}")
	int scannerPutGoods(long userId);

	@Select("select count(1) from stock where user_id=#{0} and outed")
	int scannerOutGoods(long userId);

	void updateError(@Param("error")int error,@Param("sku")List<String> sku);
	
	//根据sku获得磁条编号
	@Select("select magnetic_stripe from stock where error=#{error} and outed=#{outed} and sku=#{sku}")
	List<String> getBysku(Stock stock);
	
	//根据sku获得磁条编号
	@Select("select goods_specification_id  from stock where sku=#{sku}")
	Long getGoodsSpecificationIdBysku(String sku);
	
	//根据sku进行出库
	@Update("update stock set outed=#{outed} where sku=#{sku}")
	void updateOuted(Stock stock);
	
	//根据sku和磁条编号进行出库
	@Update("update stock set outed=#{outed} where sku=#{sku} and error=#{error} and RAND(#{num})")
	void updateStatus(StockQuery stock);
	
	//根据sku和磁条编号进行出库
	@Select("select stock.*,(select count(1) from stock where sku=#{sku})num from stock where sku=#{sku}")
	StockQuery getStockBysku(String sku);
	
}
