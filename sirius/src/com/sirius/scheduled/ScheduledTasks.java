package com.sirius.scheduled;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sirius.service.GoodsService;
import com.sirius.service.OrderService;
import com.sirius.service.ShoppingcartService;

/**
 * 定时器
 * 
 * @author dohko
 * 
 */
@Component
@Configurable
@EnableScheduling
public class ScheduledTasks {

	@Resource
	private GoodsService goodsService;

	@Resource
	private OrderService orderService;

	@Resource
	private ShoppingcartService shoppingcartService;

	// 每00:00分执行
	@Scheduled(cron = "0 0 0 * * * ")
	public void goodsSoldOut() {
		// 商品下架
		//goodsService.goodsSoldOut();
		// 取消未支付订单
		//orderService.cancleNotPay();
		// 清空进货车
		//shoppingcartService.clearAll();
	}

}
