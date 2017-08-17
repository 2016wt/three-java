package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.sirius.entity.OrderContent;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.mybatis.mapper.OrderContentMapper;
import com.sirius.mybatis.mapper.UserAddressMapper;

@Service
public class OrderContentService {

	@Resource
	private OrderContentMapper orderContentMapper;

	/**
	 * 查询订单下商品的数量
	 * @param orderId
	 * @return
	 */
	public List<OrderContentQuery> getByOrderId(String orderId){
		return orderContentMapper.getNumberByOrderId(orderId);
	}
	/**
	 * 查询订单下商品
	 * @param orderId
	 * @return
	 */
	public List<OrderContentQuery> getOrderContent(String orderId){
		return orderContentMapper.getByOrderNo(orderId);
	}
	
	/**
	 * 错误商品标注异常
	 * @param sku
	 */
	public void updateError(List<String> sku,int error){
		orderContentMapper.updateError(sku,error);
	}
	
	/***
	 * 获得该用户的所有订单下单商品，状态为以付款
	 * @param userId
	 * @return
	 */
	public List<String> getByUserId(OrderContent orderContent){
		return orderContentMapper.getByUserId(orderContent);
	}
	
	/***
	 * 根据orderNo获得商品信息
	 * @param orderNo
	 * @return
	 */
	public List<OrderContent> getOrderContentByOrderNo(String orderNo,int status,Long userId){
		return orderContentMapper.getOrderContentByOrderNo(orderNo,status,userId);
	}
	
	/***
	 * 根据sku集合获得商品信息
	 * @param orderNo
	 * @return
	 */
	public List<OrderContent> getOrderContentBySku(List<String> sku,Long userId){
		return orderContentMapper.getOrderContentBySku(sku,userId);
	}
	
	/***
	 * 根据订单编号查询商品
	 * @param orderNo
	 * @return
	 */
	public List<OrderContent> getOrderContent(String orderNo,Long userId){
		return orderContentMapper.getOrderContent(orderNo,userId);
	}
	
}
