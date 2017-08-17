package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.Goods;
import com.sirius.entity.GoodsImg;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.query.GoodsQuery;

public interface GoodsMapper {

	boolean insert(Goods goods);

	/**
	 * 供应商查看商品列表
	 * 
	 * @param wholesalerId
	 * @param putaway
	 * @param size
	 * @return
	 */
	List<GoodsQuery> mygoodsList(@Param("wholesalerId") long wholesalerId,
			@Param("putaway") boolean putaway, @Param("size") int size);

	List<GoodsQuery> mygoodsListMark(@Param("wholesalerId") long wholesalerId,
			@Param("putaway") boolean putaway, @Param("size") int size,
			@Param("markId") long markId);

	GoodsQuery getById(long goodsId);

	/**
	 * 实体店主查看商品列表
	 * 
	 * @param size
	 * @return
	 */
	// List<GoodsQuery> goodsList(start,int size);
	//
	// List<GoodsQuery> goodsListMark(@Param("size") int size,
	// @Param("markId") long markId);

	/**
	 * 商品批量下架
	 */
	@Update("update goods set putaway=0")
	void goodsSoldOut();

	@Update("update goods set putaway=1 where id=#{id} and wholesaler_id=#{wholesalerId} and putaway=0")
	boolean putaway(Goods goods);

	boolean update(Goods goods);

	// 商品列表
	int dataCount(GoodsQuery goods);

	List<GoodsQuery> data(GoodsQuery goods);

	//获得当前用户的所有商品名称
	@Select("select goods_name from goods where wholesaler_id=#{wholesalerId}")
	List<Goods> getGoodsName(Goods goods);
	
	//获得该商品id的商品名称
	@Select("select * from goods where id=#{id}")
	Goods getGoodsNameId(Goods goods);
	//商品上下架
	@Update("update goods set putaway=#{putaway} where id=#{id} and wholesaler_id=#{wholesalerId}")
	void updatePutaway(Goods goods);
	
	// 删除商品
	@Update("update goods set exist=0 where id=#{id}")
	void deleteData(Long id);

	// 获得删除商品对应的供应商的ID
	@Select("select wholesaler_id from goods where id=#{id}")
	long selectWholesalerId(long id);

	// 查询全部商品数量
	@Select("select ifnull(sum(quantity),0) from goods_specification where goods_id in (select id from goods where wholesaler_id=#{wholesalerId})")
	int getAllQuantity(Long wholesalerId);

	// 查询正在销售数量
	@Select("select ifnull(sum(quantity),0) from goods_specification where goods_id in (select id from goods where putaway=1 and wholesaler_id=#{wholesalerId})")
	int getQuantity(Long wholesalerId);

	// 添加商品
	// @Insert("insert into goods (fashion_id,goods_name) value((select id from dictionary where type='fashion' and dictionary.`name`='牛仔'),'123') ")
	int createGoods(Goods goods);

	// 修改商品
	void updateGoods(Goods goods);

	// 获得需要修改的某个商品的信息
	Goods getUpdateGoods(Goods goods);

	// 根据商品id获得商品详细
	@Select("select * from goods_specification where goods_id=#{goodsId}")
	List<GoodsSpecification> getSpecification(long goodsId);

	// 根据商品id获得商品基本信息
	@Select("select * from goods where id=#{goodsId}")
	Goods selectGoods(long goodsId);

	//根据订单id获得商品信息
	List<GoodsQuery> getGoodsOrderId(GoodsQuery goodsQuery);
	int getGoodsOrderIdContent(GoodsQuery goodsQuery);
	
	//根据orderId获得对应商品的件数
	@Select("select count(1) from goods_specification where goods_id in (select goods_id from order_content where order_id=#{orderId})")
	long getSumQuantity(long orderId);
	
	// //根据商品id获得省
	// @Select("select * from goods where id=#{goodsId}")
	// Goods selectProvince(long goodsId);

	// @Update("update goods set clustered=clustered+#{1} where id=#{0}")
	// void plusClustered(long goodsId, int clustered);
	/**
	 * 获得商品基本信息
	 * 
	 * @param goodsId
	 * @return
	 */
	@Select("select goods.id,goods.clustering,goods.price,goods.putaway,goods.goods_name,(select img_url from goods_img where goods_id=goods.id limit 1) img_url from goods where id=#{0}")
	GoodsQuery getBaseById(long goodsId);

	@Select("select goods.id,goods.price,goods.putaway,goods.goods_name,(select img_url from goods_img where goods_id=goods.id limit 1) img_url from goods where id=(select goods_id from goods_specification where id=#{0})")
	GoodsQuery getBaseBySpecification(long goodsSpecificationId);

	@Select("select price from goods where id=(select goods_id from goods_specification where id=#{0})")
	double getPriceBySpecification(long goodsSpecificationId);

	//供应商商品列表
	@Select("select * from (select goods.spu,goods.price,(select count(1) from order_content where goods_id=goods.id and wholesaler_id=`user`.id) sell,"
			+"user.early_warning,(select sum(quantity) from goods_specification where goods_id=goods.id) quantity"
			+ " from goods"
			+ " left join user on (goods.wholesaler_id=user.id)"
			+ " where wholesaler_id=#{userId})x order by quantity limit #{start},#{size}")
	List<GoodsQuery> scannerGoodsList(@Param("size") int size,
			@Param("start") int start, @Param("userId") long userId);

	@Select("select count(1) from  (select (select sum(quantity) from goods_specification where goods_id=goods.id) count,user.early_warning from goods"
			+ " left join user on goods.wholesaler_id = user.id"
			+ " where wholesaler_id=#{0})x where count<=early_warning")
	int scannerWarning(long userId);

	@Select("select sum(quantity) from goods_specification left join goods on goods.id = goods_specification.goods_id left join user on goods.wholesaler_id = user.id where user.id=#{0}")
	int scannerAmount(long userId);

}
