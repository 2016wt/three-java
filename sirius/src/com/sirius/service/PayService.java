package com.sirius.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.alipay.config.AlipayConfig;
import com.sirius.alipay.util.AlipaySubmit;
import com.sirius.alipay.util.Keys;
import com.sirius.alipay.util.PayUtil;
import com.sirius.alipay.util.Rsa;
import com.sirius.entity.Order;
import com.sirius.entity.OrderContent;
import com.sirius.entity.PayRecord;
import com.sirius.entity.ShopkeeperVip;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.GoodsMapper;
import com.sirius.mybatis.mapper.GoodsSpecificationMapper;
import com.sirius.mybatis.mapper.OrderContentMapper;
import com.sirius.mybatis.mapper.OrderMapper;
import com.sirius.mybatis.mapper.PayRecordMapper;
import com.sirius.mybatis.mapper.ShopkeeperVipMapper;
import com.sirius.mybatis.mapper.UserMapper;
import com.sirius.po.AlipayCallback;
import com.sirius.po.WechatCallBack;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class PayService {

	@Resource
	private PayRecordMapper payRecordMapper;

	@Resource
	private OrderMapper orderMapper;

	@Resource
	private OrderContentMapper orderContentMapper;

	@Resource
	private GoodsMapper goodsMapper;

	@Resource
	private ShopkeeperVipMapper shopkeeperVipMapper;

	@Resource
	private UserMapper userMapper;

	@Resource
	private GoodsSpecificationMapper goodsSpecificationMapper;

	public String alipay(PayRecord payRecord) {
		// 支付宝type
		payRecord.setType(MyConstants.PayType.ALIPAY);
		// 订单支付
		if (payRecord.getIntention() == MyConstants.PayIntention.ORDER) {
			OrderQuery order = orderMapper.getById(payRecord.getRelevanceId());
			if (order == null
					|| order.getStatus().intValue() != MyConstants.OrderStatus.OBLIGATION) {
				throw new XException("订单无法支付，请刷新！");
			}

			for (OrderContentQuery orderContent : order.getOrderContents()) {
				if (orderContent.getNumber() > orderContent.getQuantity()) {
					throw new XException("订单中存在库存不足商品，请重新下单");
				}
			}

			payRecord.setOrderNo(order.getOrderNo());
			payRecord.setMoney(order.getMoney());
			payRecord.setDescribe("订单");

		} else {
			throw new XException("未知交易类型");
		}

		PayRecord db = payRecordMapper.getByOrderNo(payRecord.getOrderNo());
		if (db != null) {
			if (db.getStatus() == MyConstants.OrderStatus.OBLIGATION) {
				payRecordMapper.clearOrderNo(payRecord.getOrderNo());
			} else {
				throw new XException("订单已支付，请勿重复支付！");
			}
		}

		// if (payRecordMapper.exitOrderNo(payRecord.getOrderNo())) {
		// payRecordMapper.clearOrderNo(payRecord.getOrderNo());
		// }

		payRecordMapper.insert(payRecord);

		String info = PayUtil.getNewOrderInfo(payRecord);
		String sign = Rsa.sign(info, Keys.PRIVATE);
		sign = URLEncoder.encode(sign);
		info += "&sign=\"" + sign + "\"&" + PayUtil.getSignType();

		return info;
	}

	public void alipayCallback(AlipayCallback alipayCallback) {

		PayRecord payRecord = payRecordMapper.getByOrderNo(alipayCallback
				.getOut_trade_no());
		if (payRecord == null
				|| payRecord.getStatus().intValue() != MyConstants.OrderStatus.OBLIGATION
				|| payRecord.getType().intValue() != MyConstants.PayType.ALIPAY) {

			throw new XException("");
		}

		// payRecordMapper.alterStatus(payRecord.getId(), 1);
		payRecord.setTradeNo(alipayCallback.getTrade_no());
		payRecord.setStatus(1);
		payRecordMapper.accomplish(payRecord);

		// 订单
		if (payRecord.getIntention().intValue() == MyConstants.PayIntention.ORDER) {
			Order order = orderMapper.getByNo(payRecord.getOrderNo());
			if (order == null
					|| order.getStatus().intValue() != MyConstants.OrderStatus.OBLIGATION
					|| payRecord.getMoney().doubleValue() != payRecord
							.getMoney().doubleValue()) {
				throw new XException("订单不存在或者订单不是待支付状态,或者钱数不匹配，证明订单非法");
			}

			// 修改订单状态
			orderMapper.alterStatusPay(order.getId(),
					MyConstants.PayType.ALIPAY);
			OrderContent orderContent = new OrderContent();
			orderContent.setOrderId(order.getId());
			orderContent.setStatus(MyConstants.OrderStatus.BEING);
			orderContent.setPayType(MyConstants.PayType.ALIPAY);// 支付宝支付
			orderContentMapper.alterStatusByOrder(orderContent);
			// 支付成功后，减库存
			goodsSpecificationMapper.subtractQuantityByOrder(order.getId());
		} else {
			throw new XException("未知交易类型");
		}

	}

	public void wxpay(PayRecord payRecord) {
		// 微信type
		payRecord.setType(1);
		// 订单支付
		if (payRecord.getIntention() == MyConstants.PayIntention.ORDER) {
			OrderQuery order = orderMapper.getById(payRecord.getRelevanceId());
			if (order == null
					|| order.getStatus().intValue() != MyConstants.OrderStatus.OBLIGATION) {
				throw new XException("订单无法支付，请刷新！");
			}

			for (OrderContentQuery orderContent : order.getOrderContents()) {
				if (orderContent.getNumber() > orderContent.getQuantity()) {
					throw new XException("订单中存在库存不足商品，请重新下单");
				}
			}
			payRecord.setOrderNo(order.getOrderNo());
			payRecord.setMoney(order.getMoney());
			payRecord.setDescribe("订单");

			PayRecord db = payRecordMapper.getByOrderNo(payRecord.getOrderNo());
			if (db == null) {

			} else if (db.getStatus() == 0) {
				payRecordMapper.clearOrderNo(payRecord.getOrderNo());
			} else {
				throw new XException("订单已支付，请勿重复支付！");
			}
			payRecordMapper.insert(payRecord);
		} else {
			throw new XException("未知交易类型");
		}

	}

	public void wxCallback(WechatCallBack callBack) {

		PayRecord payRecord = payRecordMapper.getByOrderNo(callBack
				.getOut_trade_no());
		if (payRecord == null
				|| payRecord.getStatus().intValue() != MyConstants.OrderStatus.OBLIGATION
				|| payRecord.getType().intValue() != MyConstants.PayType.WXPAY) {
			// 订单不存在或者不是未支付状态,或者非微信订单，证明支付非法
			throw new XException("");
		}

		// payRecordMapper.alterStatus(payRecord.getId(), 1);
		payRecord.setTradeNo(callBack.getNonce_str());
		payRecord.setStatus(1);
		payRecordMapper.accomplish(payRecord);

		// 订单
		if (payRecord.getIntention() == MyConstants.PayIntention.ORDER) {
			Order order = orderMapper.getByNo(payRecord.getOrderNo());
			if (order == null || order.getStatus().intValue() != 0) {
				// 订单不存在或者订单不是待支付状态，证明订单非法
				throw new XException("");
			}

			// 修改订单状态
			orderMapper
					.alterStatusPay(order.getId(), MyConstants.PayType.WXPAY);

			OrderContent orderContent = new OrderContent();
			orderContent.setOrderId(order.getId());
			orderContent.setStatus(MyConstants.OrderStatus.OBLIGATION);
			orderContent.setPayType(MyConstants.PayType.WXPAY);// 微信支付
			orderContentMapper.alterStatusByOrder(orderContent);
			// 支付成功后，减库存
			goodsSpecificationMapper.subtractQuantityByOrder(order.getId());

		}

	}

	public String alipayRefund(String batch_no, String batch_num,
			String detail_data, String sonOrderNo) throws IOException {
		// //////////////////////////////////请求参数//////////////////////////////////////

		// 批次号，必填，格式：当天日期[8位]+序列号[3至24位]，如：201603081000001

		// String batch_no = WIDbatch_no;

		// 退款笔数，必填，参数detail_data的值中，“#”字符出现的数量加1，最大支持1000笔（即“#”字符出现的数量999个）

		// String batch_num = WIDbatch_num;

		// 退款详细数据，必填，格式（支付宝交易号^退款金额^备注），多笔请用#隔开
		// String detail_data = WIDdetail_data;

		// ////////////////////////////////////////////////////////////////////////////////

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("notify_url", AlipayConfig.getNotify_url(sonOrderNo));
		sParaTemp.put("seller_user_id", AlipayConfig.seller_user_id);
		sParaTemp.put("refund_date", AlipayConfig.refund_date);
		sParaTemp.put("batch_no", batch_no);
		sParaTemp.put("batch_num", batch_num);
		sParaTemp.put("detail_data", detail_data);
		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
		return sHtmlText;
	}

	public void orderContentRefund(String sonOrderNo) {
		OrderContent orderContent = orderContentMapper.getBySon(sonOrderNo);
		// 标记为已退款
		orderContentMapper.alterStatus(orderContent.getId(),
				MyConstants.OrderStatus.REFUNDED);
		int refund = orderMapper.refundCount(orderContent.getOrderId());
		int contentCount = orderMapper.contentCount(orderContent.getOrderId());

		Order order = new Order();
		order.setId(orderContent.getOrderId());
		order.setStatus(MyConstants.OrderStatus.CANCELED);
		if (refund == contentCount) {
			orderMapper.alterStatus(order);
		}

	}

}
