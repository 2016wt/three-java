package com.sirius.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.xml.XMLSerializer;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sirius.entity.Order;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.exception.XException;
import com.sirius.po.BTEntitiy;
import com.sirius.po.JsonEntity;
import com.sirius.po.PageBen;
import com.sirius.po.WechatRefundCallback;
import com.sirius.service.OrderService;
import com.sirius.service.PayService;
import com.sirius.util.ConstantUtil;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;
import com.sirius.util.WxUtil;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Resource
	private OrderService orderService;

	@Resource
	private PayService payService;

	/**
	 * 订单列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findByPage", method = RequestMethod.GET)
	public String findByPage() {
		return "order/findByPage";
	}

	/**
	 * 查询订单列表
	 * 
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/findByPage", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy findByPage_(@ModelAttribute OrderQuery order) {
		return orderService.findByPage(order);
	}

	// /**
	// * 退款列表
	// *
	// * @return
	// */
	// @RequestMapping(value = "/refundByPage", method = RequestMethod.GET)
	// public String refundByPage() {
	// return "order/refundByPage";
	// }
	//
	// @RequestMapping(value = "/refundByPage", method = RequestMethod.POST)
	// public @ResponseBody
	// BTEntitiy refundByPage_(@ModelAttribute OrderContentQuery orderContent) {
	// return orderService.refundByPage(orderContent);
	// }

	/**
	 * 货物购买查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/findByGoods")
	public String findByGoods(HttpServletRequest request) {
		PageBen<OrderContentQuery> result = orderService.findByGoods();
		request.setAttribute("result", result);
		return "order/findByGoods";
	}

	/**
	 * 完成订单
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/accomplish/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity accomplish(@PathVariable("orderId") long orderId) {
		orderService.accomplish(orderId);
		return JsonEntity.getInstance("操作成功！");
	}

	/**
	 * 备注
	 * 
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/remark", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity remark(@ModelAttribute Order order) {
		orderService.remark(order);
		return JsonEntity.getInstance("备注成功！");
	}

	@RequestMapping(value = "/shipments", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity shipments(@ModelAttribute Order order) {
		orderService.shipments(order);
		return JsonEntity.getInstance("发货成功！");
	}

	// /**
	// * 无货，申请退款
	// *
	// * @param orderContentId
	// * @return
	// */
	// @RequestMapping(value = "/notHave/{orderContentId}", method =
	// RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity notHave(@PathVariable("orderContentId") long orderContentId) {
	// orderService.notHave(orderContentId);
	// return JsonEntity.getInstance("已申请退款！");
	// }

	/**
	 * 获取退款信息
	 * 
	 * @param sonOrderNo
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/alipayrefund/{sonOrderNo}", method = RequestMethod.GET)
	public String refundInfo(@PathVariable("sonOrderNo") String sonOrderNo,
			HttpServletResponse response) throws IOException {
		OrderContentQuery orderContent = orderService
				.getOrderContentBySon(sonOrderNo);
		if (orderContent.getStatus() != MyConstants.OrderStatus.BEING) {
			// 处理中订单可以退款
			throw new XException("无法退款，请检查状态");
		} else if (orderContent.getPayType() != MyConstants.PayType.ALIPAY) {
			throw new XException("此订单非支付宝支付！");
		}
		Order order = orderService.getBaseById(orderContent.getOrderId());
		if (order.getStatus() != MyConstants.OrderStatus.BEING) {
			throw new XException("无法退款，请检查状态");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String batch_no = sdf.format(new Date())
				+ (new Random().nextInt(100000) + 1000);
		String detail_data = orderContent.getTradeNo() + "^"
				+ orderContent.getMoney() + "^协商退款";

//		Refund refund = new Refund();
//		refund.setMoney(orderContent.getMoney());
//		refund.setOrderNo(order.getOrderNo());
//		refund.setPayType(MyConstants.PayType.ALIPAY);

		String html = payService.alipayRefund(batch_no, "1", detail_data,
				sonOrderNo);
		response.setContentType("text/html");
		response.getWriter().print(html);
		return null;
	}

	@Value("${wechat.refundPassowrd}")
	private String refundPassowrd;
	@Value("${wechat.cert}")
	private String cert;

	@RequestMapping(value = "/wechatrefund/{sonOrderNo}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity wechatrefund(@PathVariable("sonOrderNo") String sonOrderNo,
			@RequestParam String password) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException,
			KeyManagementException, UnrecoverableKeyException {
		if (!DigestUtils.md5Hex(password).equals(refundPassowrd)) {
			return JsonEntity.getError("密码错误");
		}

		OrderContentQuery orderContent = orderService
				.getOrderContentBySon(sonOrderNo);

		if (orderContent.getStatus() != MyConstants.OrderStatus.BEING) {
			return JsonEntity.getError("无法退款，请检查状态");
		} else if (orderContent.getPayType().intValue() != MyConstants.PayType.WXPAY) {
			return JsonEntity.getError("此订单非微信支付！");
		}

		Order order = orderService.getBaseById(orderContent.getOrderId());

		if (order.getStatus() != MyConstants.OrderStatus.BEING) {
			return JsonEntity.getError("无法退款，请检查状态");
		}

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();

		parameters.put("appid", ConstantUtil.APP_ID);
		parameters.put("mch_id", ConstantUtil.PARTNER);
		parameters.put("nonce_str", StringUtil.getUuid());
		parameters.put("out_trade_no", order.getOrderNo());
		parameters.put("out_refund_no", orderContent.getSonOrderNo());
		parameters.put("total_fee", (int) (order.getMoney() * 100) + "");
		parameters
				.put("refund_fee", (int) (orderContent.getMoney() * 100) + "");

		parameters.put("op_user_id", ConstantUtil.PARTNER);

		String sign = WxUtil.createSign("utf-8", parameters);
		parameters.put("sign", sign);

		String reuqestXml = WxUtil.getRequestXml(parameters);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File(cert));// 放退款证书的路径
		try {
			keyStore.load(instream, ConstantUtil.PARTNER.toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslcontext = SSLContexts.custom()
				.loadKeyMaterial(keyStore, ConstantUtil.PARTNER.toCharArray())
				.build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		StringBuffer buffer = new StringBuffer();
		try {
			HttpPost httpPost = new HttpPost(
					"https://api.mch.weixin.qq.com/secapi/pay/refund");// 退款接口
			// System.out.println("executing request" +
			// httpPost.getRequestLine());
			StringEntity reqEntity = new StringEntity(reuqestXml);
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);

			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {

					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						buffer.append(text);
					}

				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}

		XMLSerializer xmlSerializer = new XMLSerializer();
		WechatRefundCallback wechatRefundCallback = JSON.parseObject(
				xmlSerializer.read(buffer.toString()).toString(),
				WechatRefundCallback.class);

		if (wechatRefundCallback.getResult_code().equals("SUCCESS")
				&& wechatRefundCallback.getReturn_code().equals("SUCCESS")) {
			payService.orderContentRefund(sonOrderNo);
			return JsonEntity.getInstance("退款成功");
		} else
			return JsonEntity.getError("退款失败");
	}

	@RequestMapping("/info/{orderId}")
	public String info(@PathVariable("orderId") long orderId,
			HttpServletRequest request) {

		Order order = orderService.getById(orderId);

		request.setAttribute("order", order);
		return "order/info";
	}
}
