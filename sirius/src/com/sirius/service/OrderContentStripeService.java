package com.sirius.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.Order;
import com.sirius.entity.OrderContent;
import com.sirius.entity.OrderContentStripe;
import com.sirius.entity.Stock;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderContentStripeQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.OrderContentMapper;
import com.sirius.mybatis.mapper.OrderContentStripeMapper;
import com.sirius.mybatis.mapper.OrderMapper;
import com.sirius.mybatis.mapper.StockMapper;
import com.sirius.po.JsonEntity;
import com.sirius.util.MyConstants;

@Service
public class OrderContentStripeService {

	@Resource
	private OrderMapper orderMapper;

	@Resource
	private OrderContentMapper orderContentMapper;

	@Resource
	private StockMapper stockMapper;

	@Resource
	private OrderContentStripeMapper orderContentStripeMapper;

//	public void outLibrary(List<OrderContentStripeQuery> outLibraryInfo,
//			long orderId, long wholesalerId) {
//		OrderQuery order = new OrderQuery();
//		order.setId(orderId);
//		order.setWholesalerId(wholesalerId);
//		order.setStatus(MyConstants.OrderStatus.BEING);// 只查出处理中的订单
//
//		// 得到订单
//		order = orderMapper.getByWholesaler(order);
//
//		// 校验信息
//		if (order == null)
//			throw new XException("订单状态异常，无法操作");
//		if (order.getOrderContents() == null
//				|| order.getOrderContents().size() == 0) {
//			throw new XException("子订单无法操作");
//		}
//		if (outLibraryInfo.size() != order.getOrderContents().size()) {
//			throw new XException("子订单数量不对");
//		}
//		// 校验重复数据
//		List<Long> ids = new ArrayList<>();
//		for (OrderContentStripeQuery orderContentStripe : outLibraryInfo) {
//			if (ids.contains(orderContentStripe.getOrderContentId())) {
//				throw new XException("子订单数据重复");
//			}
//			ids.add(orderContentStripe.getOrderContentId());
//		}
//		// 匹配数据
//		for (OrderContentQuery orderContent : order.getOrderContents()) {
//			if (orderContent.getStockup()) {
//				throw new XException("已备货，无需重复备货");
//			}
//			if (!ids.contains(orderContent.getId())) {
//				throw new XException("子订单数据不匹配");
//			}
//
//		}
//
//		for (OrderContentStripeQuery orderContentStripe : outLibraryInfo) {
//			OrderContent orderContent = orderContentMapper
//					.getById(orderContentStripe.getOrderContentId());
//			if (orderContent.getNumber() != orderContentStripe.getStripes()
//					.size()) {
//				throw new XException("磁条编码数不匹配");
//			}
//
//			// 循环插入磁条信息
//			// TDDO 提示语标记词条编号
//			for (String magneticStripe : orderContentStripe.getStripes()) {
//				Stock stock = new Stock();
//				stock.setMagneticStripe(magneticStripe);
//				stock.setUserId(wholesalerId);
//				stock = stockMapper.getPut(stock);
//				if (stock == null) {
//					throw new XException(magneticStripe+" 磁条尚未入库，请先入库");
//				}
//				if (stock.getOuted()) {
//					throw new XException("磁条已出库，请勿重复出库");
//				}
//				if (stock.getGoodsSpecificationId().longValue() != orderContent
//						.getGoodsSpecificationId()) {
//					throw new XException("sku不匹配");
//				}
//				OrderContentStripe ocs = new OrderContentStripe();
//				ocs.setGoodsId(orderContent.getGoodsId());
//				ocs.setGoodsSpecificationId(orderContent
//						.getGoodsSpecificationId());
//				ocs.setMagneticStripe(magneticStripe);
//				ocs.setOrderContentId(orderContent.getId());
//				ocs.setOrderId(orderId);
//				// 插入磁条关联信息
//				orderContentStripeMapper.insert(ocs);
//				// 标记出库
//				stockMapper.outLibrary(stock.getId());
//			}
//			// 子订单标记备货
//
//			orderContentMapper.stockup(orderContent.getId());
//
//		}
//
//	}

	/***
	 * 根据商品的sku，完成商品出库的操作
	 * @param stock
	 * @param orderNo
	 * @param userId
	 */
	public void outLibrary(String orderNo,Long userId){
		Order order = new Order();
		order.setStatus(MyConstants.OrderStatus.SHIPMENTED);//已发货
		order.setOrderNo(orderNo);
		//订单标记已发货
		orderMapper.updateOrderStatus(order);
		
		//根据订单编号获取订单ID
		Order o = orderMapper.getByOrderNo(orderNo);
		
		OrderContent orderContent = new OrderContent();
		orderContent.setOrderId(o.getId());
		orderContent.setStatus(MyConstants.OrderContentStatus.SHIPMENTED);//已发货
		//订单商品标记已发货
		orderContentMapper.updateOrderContentStatus(orderContent);
		
	}
}
