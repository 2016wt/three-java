package com.sirius.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sirius.alipay.util.AlipayNotify;
import com.sirius.exception.XException;
import com.sirius.po.AlipayCallback;
import com.sirius.po.AlipayRefundCallback;
import com.sirius.po.WechatCallBack;
import com.sirius.service.PayService;
import com.sirius.util.ConstantUtil;
import com.sirius.util.WxUtil;

@Controller
@RequestMapping("/pay")
public class PayController {

	@Resource
	private PayService payService;

	@RequestMapping(value = "/alipayCallback", method = RequestMethod.POST)
	@ResponseBody
	public String alipayCallback(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute AlipayCallback alipayCallback) throws IOException {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no")
				.getBytes("ISO-8859-1"), "UTF-8");

		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes(
				"ISO-8859-1"), "UTF-8");

		// 交易状态
		String trade_status = new String(request.getParameter("trade_status")
				.getBytes("ISO-8859-1"), "UTF-8");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		//PrintWriter out = response.getWriter();

		if (AlipayNotify.verify(params)) {// 验证成功
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if (trade_status.equals("TRADE_FINISHED")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 该种交易状态只在两种情况下出现
				// 1、开通了普通即时到账，买家付款成功后。
				// 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				payService.alipayCallback(alipayCallback);
			}

			return "success"; // 请不要修改或删除

			// ////////////////////////////////////////////////////////////////////////////////////////
		} else {// 验证失败
			return "fail";
		}

	}

	@RequestMapping(value = "/alipayRefund/{sonOrderNo}", method = RequestMethod.POST)
	@ResponseBody
	public String alipayRefund(HttpServletRequest request,
			@PathVariable("sonOrderNo") String sonOrderNo,
			HttpServletResponse response,
			@ModelAttribute AlipayRefundCallback alipayaRefundCallback)
			throws IOException { // 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 批次号

		// String batch_no = new
		// String(request.getParameter("batch_no").getBytes(
		// "ISO-8859-1"), "UTF-8");
		//
		// // 批量退款数据中转账成功的笔数
		//
		// String success_num = new String(request.getParameter("success_num")
		// .getBytes("ISO-8859-1"), "UTF-8");
		//
		// // 批量退款数据中的详细信息
		// String result_details = new String(request.getParameter(
		// "result_details").getBytes("ISO-8859-1"), "UTF-8");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if (AlipayNotify.verify(params)) {// 验证成功
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			// 判断是否在商户网站中已经做过了这次通知返回的处理
			// 如果没有做过处理，那么执行商户的业务程序
			// 如果有做过处理，那么不执行商户的业务程序

			// payService.alipayRefundCallback(alipayaRefundCallback);

			payService.orderContentRefund(sonOrderNo);

			return "success"; // 请不要修改或删除

			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			// ////////////////////////////////////////////////////////////////////////////////////////
		} else {// 验证失败
			return "fail";
		}
	}

	/**
	 * 微信APP支付回调
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws XException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/wechatCallBack", method = RequestMethod.POST)
	@ResponseBody
	public String wechatCallBack(HttpServletResponse response,
			HttpServletRequest request) throws IOException {
		response.setContentType("text/xml;charset=UTF-8");
		try {
			InputStream is;
			is = request.getInputStream();
			// 已HTTP请求输入流建立一个BufferedReader对象
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			// 读取HTTP请求内容
			String buffer = null;
			StringBuffer sb = new StringBuffer();
			while ((buffer = br.readLine()) != null) {
				// 在页面中显示读取到的请求参数
				sb.append(buffer);
			}
			XMLSerializer xmlSerializer = new XMLSerializer();
			WechatCallBack callBack = JSON.parseObject(
					xmlSerializer.read(sb.toString()).toString(),
					WechatCallBack.class);
			if (!ConstantUtil.PARTNER.equals(callBack.getMch_id())) {
				throw new XException("商户号验证失败");
			}
			TreeMap parameters = new TreeMap();
			JSONObject jobj = JSONObject
					.fromObject(xmlSerializer.read(sb.toString()).toString());
			Iterator it = jobj.keys();
			while (it.hasNext()) {
				Object key = it.next();
				if ("sign".equals(key)) {
					continue;
				}
				parameters.put(key, jobj.get(key));
			}
			if (!WxUtil.createSign(parameters).equals(callBack.getSign())) {
				throw new XException("签名验证失败");
			}
			if ("SUCCESS".equals(callBack.getReturn_code())) {
				payService.wxCallback(callBack);
			}
			return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		} catch (Exception e) {
			e.printStackTrace();
			return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA["
					+ e.getMessage() + "]]></return_msg></xml>";
		}

	}

}
