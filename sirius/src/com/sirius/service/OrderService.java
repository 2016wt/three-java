package com.sirius.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.Order;
import com.sirius.entity.OrderContent;
import com.sirius.entity.Shoppingcart;
import com.sirius.entity.ShoppingcartContent;
import com.sirius.entity.User;
import com.sirius.entity.UserAddress;
import com.sirius.entity.query.GoodsSpecificationQuery;
import com.sirius.entity.query.Location;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.GoodsMapper;
import com.sirius.mybatis.mapper.LocationAreaMapper;
import com.sirius.mybatis.mapper.OrderContentMapper;
import com.sirius.mybatis.mapper.OrderMapper;
import com.sirius.mybatis.mapper.ShoppingcartContentMapper;
import com.sirius.mybatis.mapper.ShoppingcartMapper;
import com.sirius.mybatis.mapper.UserAddressMapper;
import com.sirius.mybatis.mapper.UserMapper;
import com.sirius.po.BTEntitiy;
import com.sirius.po.BaseInfo;
import com.sirius.po.PageBen;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class OrderService {

	@Resource
	private UserAddressMapper shopkeeperAddressMapper;

	@Resource
	private OrderMapper orderMapper;

	@Resource
	private ShoppingcartMapper shoppingcartMapper;

	@Resource
	private ShoppingcartContentMapper shoppingcartContentMapper;

	@Resource
	private OrderContentMapper orderContentMapper;

	@Resource
	private GoodsMapper goodsMapper;

	@Resource
	private LocationAreaMapper locationAreaMapper;

	@Resource
	private UserMapper userMapper;

	/**
	 * 下单
	 * 
	 * @param shopkeeperId
	 * @param addressId
	 * @param shoppingcartIds
	 */
	public long commitOrder(long shopkeeperId, long addressId,
			List<Long> shoppingcartContentIds) {

		// 生成订单内容

		List<ShoppingcartContent> contents = shoppingcartContentMapper
				.getByIds(shoppingcartContentIds);

		if (contents.size() < shoppingcartContentIds.size()) {
			throw new XException("进货车内容失效，请刷新");
		}

		// 生成订单
		UserAddress shopkeeperAddress = shopkeeperAddressMapper
				.getBaseById(addressId);
		if (!"410900".equals(shopkeeperAddress.getCityCode())) {
			throw new XException("收货地暂时只支持 河南省 濮阳市");
		}

		User shopkeeper = userMapper.getById(shopkeeperId);

		Location location = locationAreaMapper.getLocation(shopkeeperAddress
				.getAreaCode());

		Order order = new Order();
		order.setName(shopkeeperAddress.getName());
		order.setPhone(shopkeeperAddress.getPhone());
		order.setAreaCode(location.getAreaCode());
		order.setCityCode(location.getCityCode());
		order.setProvinceCode(location.getProvinceCode());
		// 存全路径 包括省市县信息
		order.setFullAddress(location.getProvinceName() + " "
				+ location.getCityName() + " " + location.getAreaName() + " "
				+ shopkeeperAddress.getDetailed());
		order.setMoney(shoppingcartContentMapper
				.moneyByIds(shoppingcartContentIds));
		order.setOrderNo(StringUtil.getUuid());
		order.setShopkeeperId(shopkeeperId);
		orderMapper.insert(order);

		/**
		 * 生成订单下的信息
		 */
		List<OrderContent> orderContents = new ArrayList<>();
		for (int i = 0; i < contents.size(); i++) {

			OrderContent orderContent = new OrderContent();

			orderContent.setGoodsSpecificationId(contents.get(i)
					.getGoodsSpecificationId());
			orderContent.setNumber(contents.get(i).getAmount());
			orderContent.setOrderId(order.getId());
			orderContent.setMoney(goodsMapper.getPriceBySpecification(contents
					.get(i).getGoodsSpecificationId())
					* contents.get(i).getAmount());
			orderContent.setCount(contents.get(i).getAmount());
			orderContent.setSonOrderNo(StringUtil.getUuid());
			orderContents.add(orderContent);
		}
		orderContentMapper.insertList(orderContents);

		// 清空进货车

		// shoppingcartMapper.clearByIds(shoppingcartIds);

		shoppingcartContentMapper.clearByIds(shoppingcartContentIds);

		return order.getId();

	}

	public List<OrderQuery> orderList(int status, long markId, long shopkeeperId) {
		if (markId == 0) {
			return orderMapper.orderList(status, shopkeeperId,
					BaseInfo.pageSize);
		} else
			return orderMapper.orderListMark(status, markId, shopkeeperId,
					BaseInfo.pageSize);
	}

	public List<OrderQuery> orderList(long markId, long shopkeeperId) {
		if (markId == 0) {
			return orderMapper.orderListAll(shopkeeperId, BaseInfo.pageSize);
		} else
			return orderMapper.orderListAllMark(markId, shopkeeperId,
					BaseInfo.pageSize);
	}

	public void cancleOrder(long orderId) {
		int status = orderMapper.orderStatus(orderId);
		// 只有待付款订单可以取消
		if (status != MyConstants.OrderStatus.OBLIGATION) {
			throw new XException("订单无法取消！");
		}

		Order order = new Order();
		order.setId(orderId);
		order.setStatus(MyConstants.OrderStatus.CANCELED);
		orderMapper.alterStatus(order);
		orderContentMapper.cancleByOrderId(orderId);
	}

	public Order getById(long orderId) {
		return orderMapper.getById(orderId);

	}

	public Order getBaseById(long orderId) {
		return orderMapper.getBaseById(orderId);

	}
	
	/***
	 * 订单的基本信息
	 */
	public Order getOrder(long orderId){
		return orderMapper.getOrder(orderId);
	}

	/**
	 * 订单发货
	 * @param order
	 */
	public void sendOrder(Order order){
		//根据orderId查询订单状态
		int status = orderMapper.getStatusByOrderId(order);
		if(status==5){
			throw new XException("订单不可重复发货");
		}else if(status==0){
			throw new XException("订单未付款，禁止发货");
		}
		//发货
		order.setStatus(5);
		orderMapper.sendOrder(order);
	}
	/**
	 * 订单列表
	 * @param order
	 * @return
	 */
	public BTEntitiy findByPage(OrderQuery order) {
		if (StringUtil.isNullOrEmpty(order.getOrderNo())) {
			order.setOrderNo(null);
		} else {
			order.setOrderNo("%" + order.getOrderNo() + "%");
		}
		if (StringUtil.isNullOrEmpty(order.getGoodsid())) {
			order.setGoodsid(null);
		}
		if (StringUtil.isNullOrEmpty(order.getPaytimeStart())) {
			order.setPaytimeStart(null);
		}
		if (StringUtil.isNullOrEmpty(order.getPaytimeEnd())) {
			order.setPaytimeEnd(null);
		} else {
			order.setPaytimeEnd(order.getPaytimeEnd() + " 23:59:59");
		}
		int total = orderMapper.dataCount(order);
		List<OrderQuery> rows = orderMapper.data(order);
		return new BTEntitiy(total, rows);
	}

	/**
	 * 订单详细列表
	 * @param order
	 * @return
	 */
	public BTEntitiy orderContentList(OrderContentQuery order) {
		
		List<OrderContentQuery> rows = orderContentMapper.dataContent(order);
		int total = orderContentMapper.dataCountContent(order);
		return new BTEntitiy(total, rows);
	}

	/**
	 * 根据订单编号修改订单状态
	 * @param orderNo
	 */
	public void updateError(String orderNo,Integer orderError){
		orderMapper.updateError(orderNo,orderError);
	}
	/**
	 * 查询所有订单数量
	 * @param wholesalerId
	 * @return
	 */
	public Integer getAllOrder(Long wholesalerId) {
		Integer count = orderMapper.getAllOrder(wholesalerId);
		return count;
	}

	/**
	 * 查询全部订单列表
	 * @param order
	 * @return
	 */
	public List<OrderQuery> allData(OrderQuery order) {
		order.setOffset(null);
		order.setLimit(null);
		if (StringUtil.isNullOrEmpty(order.getOrderNo())) {
			order.setOrderNo(null);
		} else {
			order.setOrderNo("%" + order.getOrderNo() + "%");
		}
		if (StringUtil.isNullOrEmpty(order.getGoodsid())) {
			order.setGoodsid(null);
		}
		if (StringUtil.isNullOrEmpty(order.getPaytimeStart())) {
			order.setPaytimeStart(null);
		}
		if (StringUtil.isNullOrEmpty(order.getPaytimeEnd())) {
			order.setPaytimeEnd(null);
		} else {
			order.setPaytimeEnd(order.getPaytimeEnd() + " 23:59:59");
		}
		List<OrderQuery> rows = orderMapper.data(order);
		return rows;
	}


	public PageBen<OrderContentQuery> findByGoods() {
		PageBen<OrderContentQuery> result = new PageBen<>();
		result.setPage(1);
		List<OrderContentQuery> list = orderContentMapper.findByGoods();
		result.setList(list);
		result.setPageSize(list.size());
		result.setCount(list.size());
		return result;
	}

	public List<OrderContentQuery> findByGoods(long wholesalerId) {
		List<OrderContentQuery> list = orderContentMapper
				.findByGoodsWholesaler(wholesalerId);
		return list;
	}

	public void cancleNotPay() {
		orderContentMapper.cancleNotPay();
		orderMapper.cancleNotPay();

	}

	public void accomplish(long orderId) {
		if (!orderMapper.accomplish(orderId))
			throw new XException("无法完成！请检查订单是否合法");

	}

	public void remark(Order order) {
		if (!orderMapper.remark(order))
			throw new XException("备注失败");

	}

	

	public void shipments(Order order) {
		if (!orderMapper.shipments(order))
			throw new XException("发货失败");
		orderContentMapper.shipments(order.getId());

	}

	/***
	 * 确认入库
	 * @param orderId
	 * @param shopkeeperId
	 */
	public void countersign(String orderId, long shopkeeperId) {
		Order order = orderMapper.getByOrderNo(orderId);
		if(order.equals(null)){
			throw new XException("数据异常");
		}
		if (order.getStatus() != MyConstants.OrderStatus.SHIPMENTED
				|| order.getShopkeeperId() != shopkeeperId) {
			throw new XException("无法确认收货");
		}
		// 标记为已完成
		order.setStatus(MyConstants.OrderStatus.ACCOMPLISH);
		orderMapper.alterStatus(order);
		//orderContentMapper.countersign(orderId);
	}
	
	/***
	 * 根据错误磁条编号查询所在的订单
	 * @param sku
	 * @return
	 */
	public List<Order> getBySku(List<String> tic,Long shopkeeperId,String orderId){
		return orderMapper.getBySku(tic,shopkeeperId,orderId);
	}

	// public BTEntitiy refundByPage(OrderContentQuery orderContent) {
	// List<OrderContentQuery> rows = orderContentMapper
	// .refundByPage(orderContent);
	// int total = orderContentMapper.refundByPageCount(orderContent);
	// return new BTEntitiy(total, rows);
	// }

	public List<OrderContentQuery> refundOrderNo(List<String> orderNos) {
		List<OrderContentQuery> rows = orderContentMapper
				.refundOrderNo(orderNos);
		return rows;
	}

	public List<GoodsSpecificationQuery> orderGrade(long orderId) {
		Order order = orderMapper.getBaseById(orderId);
		if (order.getEvaluated()) {
			throw new XException("已评价过,请勿重复评价");
		}
		return orderContentMapper.orderGrade(orderId);
	}

	public OrderContentQuery getOrderContentBySon(String sonOrderNo) {
		return orderContentMapper.getBySon(sonOrderNo);
	}

	public void restocking(long orderId, long shopkeeperId) {
		OrderQuery order = orderMapper.getById(orderId);
		for (OrderContentQuery content : order.getOrderContents()) {
			Shoppingcart shoppingcart = shoppingcartMapper
					.getByGoodsAndShopkeeper(content.getGoodsId(), shopkeeperId);
			if (shoppingcart == null) {
				shoppingcart = new Shoppingcart();
				shoppingcart.setShopkeeperId(shopkeeperId);
				shoppingcart.setGoodsId(content.getGoodsId());
				shoppingcartMapper.insert(shoppingcart);
			}

			ShoppingcartContent shoppingcartContent = new ShoppingcartContent();
			shoppingcartContent.setGoodsSpecificationId(content
					.getGoodsSpecificationId());
			shoppingcartContent.setAmount(content.getNumber());
			shoppingcartContent.setShoppingcartId(shoppingcart.getId());
			// 检测是否有此购物记录
			if (shoppingcartContentMapper.checkHaving(shoppingcartContent)) {
				// 有的话直接数量相加
				shoppingcartContentMapper.amountPlus(shoppingcartContent);
			} else {
				// 没有的话再插入
				shoppingcartContentMapper.insert(shoppingcartContent);
			}
		}
	}

	public void deleteOrder(long orderId, long shopkeeperId) {
		if (!orderMapper.deleteOrder(orderId, shopkeeperId)) {
			throw new XException("无法删除");
		}

	}

	public List<OrderQuery> getByKeyword(OrderQuery order) {
		return orderMapper.getByKeyword(order);
	}

	/***
	 * 入库查询订单
	 * @param order
	 * @return
	 */
	public OrderQuery getByShopkeeper(OrderQuery order) {
		return orderMapper.getByShopkeeper(order);
	}
	
	public Order getByOrderNo(String orderNo){
		return orderMapper.getByOrderNo(orderNo);
	}
	
//	public List<OrderQuery> getByUserId(Long id){
//		return orderMapper.getByUserId(id);
//	}
	
	/***
	 * 查询该订单
	 * @param order
	 * @return
	 */
	public Order getOrderByOrderNo(Order order){
		return orderMapper.getOrderByOrderNo(order);
	}
}
