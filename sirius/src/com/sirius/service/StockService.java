package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiniu.util.StringUtils;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.Order;
import com.sirius.entity.OrderContent;
import com.sirius.entity.OrderContentStripe;
import com.sirius.entity.Stock;
import com.sirius.entity.query.StockQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.GoodsSpecificationMapper;
import com.sirius.mybatis.mapper.OrderContentMapper;
import com.sirius.mybatis.mapper.OrderContentStripeMapper;
import com.sirius.mybatis.mapper.OrderMapper;
import com.sirius.mybatis.mapper.StockMapper;
import com.sirius.po.JsonEntity;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class StockService {

	@Resource
	private StockMapper stockMapper;
	
	@Resource
	private OrderContentMapper orderContentMapper;
	
	@Resource
	private OrderMapper orderMapper;
	
	@Resource
	private OrderContentStripeMapper orderContentStripeMapper;

	@Resource
	private GoodsSpecificationMapper goodsSpecificationMapper;

	public void stripeBinding(Stock stock,String orderId) {
		
		OrderContent orderContent = new OrderContent();
		// TODO 添加重复绑定验证
		
		//根据orderNo获得orderId
		Long id = orderMapper.getOrderIdByOrderNo(orderId);
//		if (StringUtil.isNullOrEmpty(stock.getSku())) {
//			throw new XException("绑定失败,商品编号为空");
//		}
//		if (StringUtil.isNullOrEmpty(stock.getMagneticStripe())) {
//			throw new XException("绑定失败,磁条编码为空");
//		}

		GoodsSpecification goodsSpecification = goodsSpecificationMapper
				.getBySku(stock.getSku());
		if (goodsSpecification == null) {
			throw new XException("绑定失败,商品编号不合法");
		}
		stock.setGoodsSpecificationId(goodsSpecification.getId());
		//根据词条编号查询数据库
		Stock b = stockMapper.selec(stock);
		//商品已有该磁条
		if(b!=null){
			throw new XException("绑定失败,磁条已绑定");
		}else{
			stock.setOrderId(id);
			//商品写入磁条
			stockMapper.insert(stock);
			
			//给商品和磁条的关系表添加数据
			//
			//
			//根据order_no获取orderId
			Order order = orderMapper.getByOrderNo(orderId);
			//根据绑定的sku获取商品id和商品规格id、
			GoodsSpecification goods = goodsSpecificationMapper.getBySku(stock.getSku());
			//根据orderId获取orderContent的id
			orderContent.setGoodsSpecificationId(goods.getId());
			orderContent.setOrderId(order.getId());
			Long orderContentId = orderContentMapper.getByOrderId(orderContent);
			OrderContentStripe orderContentStripe = new OrderContentStripe();
			//商品和磁条的关系表绑定
			orderContentStripe.setOrderId(order.getId());
			orderContentStripe.setGoodsId(goods.getGoodsId());
			orderContentStripe.setGoodsSpecificationId(goods.getId());
			orderContentStripe.setMagneticStripe(stock.getMagneticStripe());
			orderContentStripe.setOrderContentId(orderContentId);
			orderContentStripeMapper.insert(orderContentStripe);
			
			//获得count
			int count = orderContentMapper.getCount(orderId,stock.getGoodsSpecificationId());
			if(count<=0){
				throw new XException("该商品已经全部绑定");
			}else{
				//绑定一件商品数据库count减1
				orderContentMapper.updateNumber(orderId,stock.getGoodsSpecificationId());
			}
			
//			Order order = new Order();
//			order.setStatus(MyConstants.OrderStatus.SHIPMENTED);
//			order.setOrderNo(orderId);
//			//磁条绑定之后改变订单状态，为已发货。
//			orderMapper.updateOrderStatus(order);
			return;
		}
	}

	// public void outLibrary(List<Stock> stocks) {
	// for (Stock stock : stocks) {
	//
	// if (StringUtils.isNullOrEmpty(stock.getSku())) {
	// throw new XException("商品编号缺失");
	// }
	// if (StringUtils.isNullOrEmpty(stock.getMagneticStripe())) {
	// throw new XException("磁条编号缺失");
	// }
	//
	// Stock put = stockMapper.getPut(stock);
	//
	// if (put == null) {
	// throw new XException("尚未入库，无法出库");
	// } else if (!put.getSku().equals(stock.getSku())) {
	// throw new XException("入库信息异常");
	// } else if (put.getOuted()) {
	// throw new XException("已出库，无法再次出库");
	// }
	//
	// stockMapper.outLibrary(put.getId());
	//
	// }
	// }
	/**
	 * //入库表给错误商品加标注
	 * @param error
	 * @param sku
	 */
	public void updateError(int error,List<String> sku){
		stockMapper.updateError(error,sku);
	}
	
	/**
	 * //根据sku获得磁条编号
	 * @param sku
	 * @return
	 */
	public List<String> getBysku(Stock stock){
		return stockMapper.getBysku(stock);
	}
	
	
	/**
	 * //根据sku获得商品规格
	 * @param sku
	 * @return
	 */
	public Long getGoodsSpecificationIdBysku(String sku){
		return stockMapper.getGoodsSpecificationIdBysku(sku);
	}
	
	/***
	 * 根据sku进行出库
	 * @param sku
	 */
	public void updateOuted(Stock stock){
		stockMapper.updateOuted(stock);
	}
	
	/***
	 * 根据sku和磁条编号进行出库
	 * @param sku
	 */
	public void updateStatus(StockQuery stock){
		stockMapper.updateStatus(stock);
	}
	
	
	/***
	 * 根据sku查询商品
	 * @param sku
	 */
	public StockQuery getStockBysku(String sku){
		return stockMapper.getStockBysku(sku);
	}
	
}
