package com.sirius.mybatis.mapper;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.Goods;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.query.GoodsSpecificationQuery;

public interface GoodsSpecificationMapper {

	// void insertList(@Param("list") List<GoodsSpecification> list);
	//
	// @Delete("delete from goods_specification where goods_id=#{0}")
	// void clearGoodsInfo(long goodsId);

	@Select("select * from goods_specification where goods_id=#{0}")
	List<GoodsSpecificationQuery> specificationList(long goodsId);

	// @Select("select * from goods_specification where id=#{0}")
	GoodsSpecificationQuery getById(long goodsSpecificationId);

	@Select("select * from goods_specification where goods_id=#{0}")
	List<GoodsSpecification> getByGoodsId(long goodsId);

	@Select("select count(1) from goods_specification where goods_id=#{goodsId} and color=#{color} and size=#{size}")
	boolean exit(GoodsSpecification goodsSpecification);

	@Update("update goods_specification set quantity=#{quantity} where goods_id=#{goodsId} and color=#{color} and size=#{size}")
	boolean editQquantity(GoodsSpecification goodsSpecification);

	@Insert("insert into goods_specification(goods_id,quantity,color,size) values(#{goodsId},#{quantity},#{color},#{size}) ")
	boolean insert(GoodsSpecification goodsSpecification);

	@Select("select * from goods_specification where id=#{0}")
	GoodsSpecification getBaseById(long goodsSpecificationId);

	@Update("update goods_specification set quantity = quantity-(select number from order_content where order_content.goods_specification_id=goods_specification.id and order_id=#{0}) where goods_specification.id in (select goods_specification_id from order_content where order_id=#{0} and status=1)")
	void subtractQuantityByOrder(long orderId);

	// 商品详细列表
	int dataCountContent(GoodsSpecificationQuery goods);

	List<GoodsSpecificationQuery> dataContent(GoodsSpecificationQuery goods);

//	@Select("select goods.price,goods_specification.sku,goods_specification.size,goods_specification.color,(select img_url from goods_img where goods_img.goods_id=goods.id limit 1)"
//			+ " img_url from goods_specification"
//			+ " left join goods on"
//			+ " (goods_specification.goods_id=goods.id) where"
//			+ "	goods.wholesaler_id=#{userId} limit #{start},#{size}")
	List<GoodsSpecificationQuery> scannerSpecificationList(
			@Param("size") int size, @Param("start") int start,
			@Param("userId") long userId,@Param("orderId") String orderId,
			@Param("sku") List<String> sku,@Param("status") int status);

	List<GoodsSpecificationQuery> scannerSpecification(
			@Param("size") int size, @Param("start") int start,
			@Param("userId") long userId,@Param("orderId") String orderId,
			@Param("status") int status);
	@Select("select * from goods_specification where sku=#{0}")
	GoodsSpecification getBySku(String sku);

	List<GoodsSpecificationQuery> specificationBySku(@Param("param") List<String> sku,
			@Param("id") Long id);
//	List<GoodsSpecificationQuery> specificationBySku(
//			@Param("param") List<String> sku);

	// 添加商品的详细信息
	void createGoods(GoodsSpecification goodsSpecification);

	// 修改商品的详细信息
	void updateGoods(GoodsSpecification goodsSpecification);

	// 删除商品的详细信息
	@Delete("delete from goods_specification where goods_id=#{id}")
	void deleteSpecification(long GoodsId);

	/*
	 * @Select("select id from goods") List<Goods> getAll();
	 * 
	 * @Update("update goods set spu = #{spu} where id=#{id}") void
	 * alterSku(Goods goodsSpecification);
	 */

}