package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.Order;
import com.sirius.entity.query.OrderQuery;

public interface OrderMapper {

	boolean insert(Order order);

	List<OrderQuery> orderListMark(@Param("status") int status,
			@Param("markId") long markId,
			@Param("shopkeeperId") long shopkeeperId, @Param("size") int size);

	List<OrderQuery> orderList(@Param("status") int status,
			@Param("shopkeeperId") long shopkeeperId, @Param("size") int size);

	List<OrderQuery> orderListAllMark(@Param("markId") long markId,
			@Param("shopkeeperId") long shopkeeperId, @Param("size") int size);

	List<OrderQuery> orderListAll(@Param("shopkeeperId") long shopkeeperId,
			@Param("size") int size);

	@Select("select status from `order` where id=#{0}")
	int orderStatus(long orderId);

	@Update("update `order` set status=#{status} where id=#{id}")
	void alterStatus(Order order);

	//根据错误sku查询所在的订单
	//@Select("select * from `order` where shopkeeper_id=#{shopkeeperId} and id in (select order_id from order_content where goods_specification_id in(select id from goods_specification where sku=#{sku}))")
	List<Order> getBySku(@Param("tic")List<String> tic,
						 @Param("shopkeeperId")Long shopkeeperId,
						 @Param("orderId") String orderId);
/*	@Select("select * from `order` where id in (select order_id from order_content where goods_specification_id in(select id from goods_specification where sku=#{sku}))")
	List<Order> getBySku(String sku,long shopkeeperId);
*/	
	OrderQuery getById(long orderId);

	@Select("select * from `order` where id=#{0}")
	Order getBaseById(long orderId);

	@Select("select * from `order` where order_no=#{0}")
	Order getByNo(String orderNo);

	/**
	 * 将所有未支付订单取消
	 */
	@Update("update `order` set status=-1 where status=0")
	void cancleNotPay();

	/**
	 * 完成订单，只有处理中订单可以完成
	 * 
	 * @param orderId
	 * @return
	 */
	@Update("update `order` set status=2 where status=1 and id=#{0}")
	boolean accomplish(long orderId);

	// 订单
	int dataCount(OrderQuery order);
	List<OrderQuery> data(OrderQuery order);

	// 财务
	int financedataCount(OrderQuery order);

	List<OrderQuery> financedata(OrderQuery order);

	@Update("update `order` set remark=#{remark} where id=#{id}")
	boolean remark(Order order);

	/**
	 * 支付成功
	 * 
	 * @param id
	 */
	@Update("update `order` set status=1,pay_time=now(),pay_type=#{1} where id=#{0}")
	void alterStatusPay(long id, int payType);

	/**
	 * 处理中状态才能为发货，切必须要填单号
	 * 
	 * @param order
	 * @return
	 */
	@Update("update `order` set status=5,express_no=#{expressNo} where id=#{id} and status=1")
	boolean shipments(Order order);

	/**
	 * 子订单数量
	 * 
	 * @param orderId
	 * @return
	 */
	@Select("select count(1) from order_content where order_id=#{0}")
	int contentCount(long orderId);

	/**
	 * 子订单退款数量
	 * 
	 * @param orderId
	 * @return
	 */
	@Select("select count(1) from order_content where order_id=#{0} and status = 6")
	int refundCount(long orderId);

	/**
	 * 把已退款完成的订单标记为已退款
	 * 
	 * @param batchNo
	 */
	@Update("update `order` set refunded = 1 where order_no in (select order_no from alipay_refund_content where batch_no = #{0})")
	void refundedByBatchNo(String batchNo);

	@Update("update `order` set evaluated=1 where id=#{0}")
	void evaluated(long orderId);

	@Update("update `order` set exist = 0 where id=#{0} and shopkeeper_id=#{1} and status=2")
	boolean deleteOrder(long orderId, long shopkeeperId);

	// 查询所有订单
	int getAllOrder(Long wholesalerId);

	//查询制定订单
	@Select("select * from `order` where id=#{orderId}")
	Order getOrder(long orderId);
	
	//订单发货
	@Update("update `order` set `status`=#{status} where id=#{id}")
	void sendOrder(Order order);
	
	//根据orderId查询订单状态
	@Select("select `status` from `order` where id=#{id}")
	int getStatusByOrderId(Order order);
	
	List<OrderQuery> getByKeyword(OrderQuery order);

	OrderQuery getByWholesaler(OrderQuery order);

	//入库查询订单
	OrderQuery getByShopkeeper(OrderQuery order);
	
	//根据order_no获取orderId
	@Select("select * from `order` where order_no=#{orderNo}")
	Order getByOrderNo(String orderNo);
	
	//磁条绑定之后改变订单状态，为已发货。
	@Update("update `order` set `status`=#{status} where order_no=#{orderNo}")
	void updateOrderStatus(Order order);
	
	//
	@Update("update `order` set error=#{orderError} where order_no=#{orderNo}")
	void updateError(@Param("orderNo")String orderNo,@Param("orderError")Integer orderError);
	
	//根据orderNo获得orderId
	/**
	 * @param OrderNo
	 * @return
	 */
	@Select("select id from `order` where order_no=#{orderNo}")
	Long getOrderIdByOrderNo(String orderNo);

	//查询该订单
	@Select("select * from `order` where exist and order_no=#{orderNo}")
	Order getOrderByOrderNo(Order order);
}

