package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.OrderContent;
import com.sirius.entity.query.GoodsSpecificationQuery;
import com.sirius.entity.query.OrderContentQuery;

public interface OrderContentMapper {

	void insertList(@Param("list") List<OrderContent> list);

	@Select("select count(1) from (select count(1) from order_content group by goods_specification_id)x")
	int findByGoodsCount();

	List<OrderContentQuery> findByGoods();

	List<OrderContentQuery> findByGoodsWholesaler(long wholesalerId);

	@Update("update order_content set status=#{status},pay_type=#{payType} where order_id=#{orderId}")
	// void alterStatusByOrder(long orderId, int status, int payType);
	void alterStatusByOrder(OrderContent orderContent);

	@Update("update order_content set `status`=-1 where order_id in (select id from `order` where status=0)")
	void cancleNotPay();

	@Select("select * from order_content where id = #{0}")
	OrderContent getById(long orderContentId);

	@Update("update order_content set status=#{1} where id=#{0}")
	boolean alterStatus(long orderContentId, int status);

	@Update("update order_content set status=-1 where order_id=#{0}")
	void cancleByOrderId(long orderId);

	/**
	 * 标记为完成,只有已发货可以
	 * 
	 * @param orderId
	 */
	@Update("update order_content set status=2 where order_id=#{0} and status=5")
	void countersign(long orderId);

	/**
	 * 标记为发货
	 * 
	 * @param orderId
	 */
	@Update("update order_content set status=5 where order_id=#{0} and status=1")
	void shipments(long orderId);

	List<OrderContentQuery> refundByPage(OrderContentQuery orderContent);

	int refundByPageCount(OrderContentQuery orderContent);

	List<OrderContentQuery> refundOrderNo(List<String> orderNos);

	// @Update("update order_content set status=6 where status=4 and order_id in (select id from `order` where order_no in (select order_no from alipay_refund_content where batch_no=#{0}))")
	// void refundedByBatchNo(String batchNo);

	@Select("select goods_specification.*,goods.goods_name,(select img_url from goods_img where goods_id=goods_specification.goods_id limit 1) img_url from goods_specification left join goods on goods_specification.goods_id=goods.id where goods_specification.id in (select goods_specification_id from order_content where order_id=#{0} and status=2)")
	List<GoodsSpecificationQuery> orderGrade(long orderId);

	@Select("select order_content.*,(select trade_no from pay_record where relevance_id=order_content.order_id and intention=0) trade_no from order_content where son_order_no=#{0}")
	OrderContentQuery getBySon(String sonOrderNo);

	@Update("update order_content set status=6 where son_order_no=#{0}")
	void orderContentRefund(String sonOrderNo);

	// @Select("select goods_id,sum(number) number from order_content where order_id=#{0} group by goods_id")
	// List<OrderContent> getClusteringByOrder(Long id);

	// 订单详情
	int dataCountContent(OrderContentQuery order);
	List<OrderContentQuery> dataContent(OrderContentQuery order);

	@Update("update order_content set stockup=1 where id=#{0}")
	boolean stockup(long orderContentId);
	
	//根据orderId获取orderContent的id
	@Select("select id from order_content where order_id=#{orderId} and goods_specification_id=#{goodsSpecificationId}")
	Long getByOrderId(OrderContent orderContent);
	
	//查询订单下商品的number
	@Select("select (select sku from goods_specification where id in (goods_specification_id))sku,number from order_content where order_id =(select id from `order` where order_no=#{orderId}) ")
	List<OrderContentQuery> getNumberByOrderId(String orderId);
	
	//查询订单下商品
	@Select("select order_content.*,(select sku from goods_specification where id=goods_specification_id)sku from order_content where order_id = (select id from `order` where order_no=#{orderId})")
	List<OrderContentQuery> getByOrderNo(String orderId);
	
	//绑定磁条时修改number的数量
	@Update("update order_content set count=(count-1) where order_id=(select id from `order` where order_no=#{orderId})  and goods_specification_id=#{goodsSpecificationId}")
	void updateNumber(@Param("orderId")String orderId,@Param("goodsSpecificationId")Long goodsSpecificationId);
	
	@Select("select number from order_content where order_id=(select id from `order` where order_no=#{orderId}) and goods_specification_id=#{goodsSpecificationId}")
	int getCount(@Param("orderId")String orderId,@Param("goodsSpecificationId")Long goodsSpecificationId);
	
	//订单商品标注异常
	//@Update("update order_content set error=#{error} where goods_specification_id=(select id from goods_specification where sku=#{sku}) ")
	void updateError(@Param("sku")List<String> sku,@Param("error")int error);
	
	//获得该用户的所有订单下单商品，状态为以付款
	//@Select("select order_no from `order` where id in (select order_id from order_content where wholesaler_id=#{wholesalerId} and `status`=#{status})")
	@Select("select order_no from `order` where id in (select order_id from order_content where wholesaler_id=#{wholesalerId} and `status`=#{status})")
	List<String> getByUserId(OrderContent orderContent);
	
	//根据orderNo获得商品信息
	List<OrderContent> getOrderContentByOrderNo(@Param("orderNo")String orderNo,@Param("status")int status,@Param("userId")Long userId);
	
	//根据sku集合获得商品信息
	List<OrderContent> getOrderContentBySku(@Param("sku")List<String> sku,@Param("userId")Long userId);
	
	//根据订单编号查询商品
	@Select("select * from order_content where order_id=(select id from `order` where order_no=#{orderNo} )")
	List<OrderContent> getOrderContent(@Param("orderNo")String orderNo,@Param("userId")Long userId);
	
	//订单商品标记已发货
	@Update("update order_content set `status`=#{status} where order_id=#{orderId}")
	void updateOrderContentStatus(OrderContent orderContent);
	
}