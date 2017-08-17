package com.sirius.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.xml.XMLSerializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sirius.annotation.ShopkeeperApp;
import com.sirius.annotation.Token;
import com.sirius.entity.AndroidApk;
import com.sirius.entity.GoodsEvaluate;
import com.sirius.entity.PayRecord;
import com.sirius.entity.ShopkeeperFootprint;
import com.sirius.entity.ShoppingcartContent;
import com.sirius.entity.User;
import com.sirius.entity.UserAddress;
import com.sirius.entity.query.GoodsQuery;
import com.sirius.entity.query.GoodsSpecificationQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.entity.query.ShoppingcartQuery;
import com.sirius.entity.query.UserQuery;
import com.sirius.image.ImgUtils;
import com.sirius.po.BaseInfo;
import com.sirius.po.JsonEntity;
import com.sirius.po.WechatUnifiedorder;
import com.sirius.service.AddressService;
import com.sirius.service.AppService;
import com.sirius.service.GoodsService;
import com.sirius.service.ImgService;
import com.sirius.service.InformationService;
import com.sirius.service.OrderService;
import com.sirius.service.PayRecordService;
import com.sirius.service.PayService;
import com.sirius.service.ShopkeeperService;
import com.sirius.service.ShoppingcartService;
import com.sirius.util.ConstantUtil;
import com.sirius.util.HttpUtil;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;
import com.sirius.util.Tools;
import com.sirius.util.WxUtil;

/**
 * 移动端供应商访问
 * 
 * @author dohko
 * 
 */
@Controller
@RequestMapping("/app/shopkeeper")
public class AppShopkeeperController {

	@Resource
	private ShopkeeperService shopkeeperService;

	@Resource
	private GoodsService goodsService;

	@Resource
	private AppService appService;

	@Resource
	private ShoppingcartService shoppingcartService;

	@Resource
	private AddressService addressService;

	@Resource
	private OrderService orderService;

	@Resource
	private PayRecordService payRecordService;

	@Resource
	private PayService payService;

	@Resource
	private ImgService imgService;

	@Resource
	private InformationService informationService;

	@Value("${common.defautEvaluate}")
	private String defautEvaluate;

	/**
	 * 获取的基础信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/baseInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity baseInfo(HttpServletRequest request) {
		BaseInfo baseInfo = new BaseInfo();
		
		return JsonEntity.getInstance(baseInfo);
	}

	/**
	 * 登录
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/login/{phone}/{code}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity login(@PathVariable("phone") String phone,
			@PathVariable("code") String code) {

		return JsonEntity.getInstance("登录成功",
				shopkeeperService.login(phone, code));
	}

	@RequestMapping(value = "/pwlogin", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity pwlogin(@ModelAttribute User shopkeeper) {
		return JsonEntity.getInstance("登录成功",
				shopkeeperService.login(shopkeeper));
	}

	/**
	 * 发送验证码
	 * 
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/sendCode/{phone}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity sendCode(@PathVariable("phone") String phone) {
		shopkeeperService.sendCode(phone);
		return JsonEntity.getInstance("验证码发送成功");
	}

	@RequestMapping(value = "/sendCodeFindPassword/{phone}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity sendCodeFindPassword(@PathVariable("phone") String phone) {
		shopkeeperService.sendCodeFindPassword(phone);
		return JsonEntity.getInstance("验证码发送成功");
	}

	@RequestMapping(value = "/sendCodeFindRegister/{phone}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity sendCodeFindRegister(@PathVariable("phone") String phone) {
		shopkeeperService.sendCodeFindRegister(phone);
		return JsonEntity.getInstance("验证码发送成功");
	}

	/**
	 * 验证验证码
	 * 
	 * @param phone
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/checkCode/{phone}/{code}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity checkCode(@PathVariable("phone") String phone,
			@PathVariable("code") String code) {
		shopkeeperService.checkCode(phone, code);
		return JsonEntity.getInstance();
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity register(@ModelAttribute UserQuery shopkeeper) {
		return JsonEntity.getInstance(shopkeeperService.register(shopkeeper));
	}

	/**
	 * 版本更新
	 * 
	 * @param version
	 *            版本号
	 * @return
	 */
	@RequestMapping("/newApk/{version}")
	public @ResponseBody
	JsonEntity newApk(@PathVariable("version") int version) {
		AndroidApk androidApk = appService.newApk(version, MyConstants.UserType.SHOPKEEPER);
		if (androidApk == null)
			return JsonEntity.getError();
		else
			return JsonEntity.getInstance(androidApk);
	}

	/**
	 * 商品列表
	 * 
	 * @param markId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/goodsList/{page}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity goodsList(@PathVariable("page") int page,
			@ModelAttribute GoodsQuery goods, HttpServletRequest request) {
		if (!StringUtil.isNullOrEmpty(goods.getOrder())) {
			goods.setOrder(StringUtil.underscoreToCamelCase(goods.getOrder()));
		} else {
			goods.setOrder(null);
		}
		goods.setOffset((page - 1) * BaseInfo.pageSize);
		goods.setLimit(BaseInfo.pageSize);

		return JsonEntity.getInstance(goodsService.goodsList(goods));
	}

	/**
	 * 单个商品
	 * 
	 * @param goodsId
	 * @param request
	 * @return
	 */
	@Token
	@RequestMapping(value = "/goodsInfo/{goodsId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity goodsInfo(@PathVariable("goodsId") long goodsId,
			HttpServletRequest request) {
		GoodsQuery goods = goodsService.goodsInfo(goodsId);
		User shopkeeper = Tools.getShopkeeper(request);
		if (shopkeeper != null && goods != null) {
			ShopkeeperFootprint shopkeeperFootprint = new ShopkeeperFootprint();
			shopkeeperFootprint.setGoodsId(goodsId);
			shopkeeperFootprint.setShopkeeperId(shopkeeper.getId());
			shopkeeperService.addFootprint(shopkeeperFootprint);
		}
		return JsonEntity.getInstance(goods);
	}

	/**
	 * 规格列表
	 * 
	 * @param goodsId
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping("/specificationList/{goodsId}")
	public @ResponseBody
	JsonEntity specificationList(@PathVariable("goodsId") long goodsId) {
		return JsonEntity.getInstance(goodsService.specificationList(goodsId));
	}

	/**
	 * 放进进货车
	 * 
	 * @param jsonData
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/toShoppingcart/{goodsId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity toShoppingcart(@RequestParam("jsonData") String jsonData,
			@PathVariable("goodsId") long goodsId, HttpServletRequest request) {
		List<ShoppingcartContent> list = JSONObject.parseArray(jsonData,
				ShoppingcartContent.class);
		if (list.size() == 0) {
			return JsonEntity.getError("请选择商品");
		}
		shoppingcartService.toShoppingcart(list, goodsId,
				Tools.getShopkeeper(request).getId());
		return JsonEntity.getInstance("添加进进货车成功");
	}

	/**
	 * 进货车列表
	 * 
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/shoppingcartList", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity shoppingcartList(HttpServletRequest request) {
		return JsonEntity.getInstance(shoppingcartService
				.shoppingcartByGoods(Tools.getShopkeeper(request).getId()));
	}

	/**
	 * 添加地址信息
	 * 
	 * @param request
	 * @param shopkeeperAddress
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/addAddress", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity addAddress(HttpServletRequest request,
			@ModelAttribute UserAddress shopkeeperAddress) {
		shopkeeperAddress.setUserId(Tools.getShopkeeper(request).getId());
		addressService.addAddress(shopkeeperAddress);
		return JsonEntity.getInstance("添加地址成功");
	}

	/**
	 * 收货地址列表
	 * 
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/addressList", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity addressList(HttpServletRequest request) {
		return JsonEntity.getInstance(addressService.addressList(Tools
				.getShopkeeper(request).getId()));
	}

	/**
	 * 删除地址
	 * 
	 * @param addressId
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/removeAddress/{addressId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity removeAddress(@PathVariable("addressId") long addressId,
			HttpServletRequest request) {
		UserAddress address = new UserAddress();
		address.setUserId(Tools.getShopkeeper(request).getId());
		address.setId(addressId);
		addressService.removeAddress(address);
		return JsonEntity.getInstance("已删除");
	}

	/**
	 * 提交订单
	 * 
	 * @param request
	 * @param shoppingcartIds
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/commitOrder/{addressId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity commitOrder(
			HttpServletRequest request,
			@PathVariable("addressId") long addressId,
			@RequestParam("shoppingcartContentIds") List<Long> shoppingcartContentIds) {
		if (addressId == 0) {
			return JsonEntity.getError("请选择地址");
		}
		return JsonEntity.getInstance(orderService.commitOrder(Tools
				.getShopkeeper(request).getId(), addressId,
				shoppingcartContentIds));
	}

	/**
	 * 获得支付宝支付信息
	 * 
	 * @param payRecord
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/alipay", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity alipay(@ModelAttribute PayRecord payRecord,
			HttpServletRequest request) {
		payRecord.setShopkeeperId(Tools.getShopkeeper(request).getId());
		return JsonEntity.getInstance((Object) payService.alipay(payRecord));
	}

	/**
	 * 进货车下单信息
	 * 
	 * 返回值：进货车列表，总价，默认地址
	 * 
	 * @param request
	 * @param shoppingcartIds
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/commitOrderInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity commitOrderInfo(
			HttpServletRequest request,
			@RequestParam("shoppingcartContentIds") List<Long> shoppingcartContentIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dafaultAddress", addressService.dafaultAddres(Tools
				.getShopkeeper(request).getId()));
		map.put("shoppingcartContents", shoppingcartService
				.shoppingcartContents(shoppingcartContentIds));
		map.put("money",
				shoppingcartService.moneyByCarts(shoppingcartContentIds));
		map.put("freight", 0);
		return JsonEntity.getInstance(map);
	}

	/**
	 * 
	 * 订单列表，分类获取
	 * 
	 * @param status
	 * @param markId
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/orderList/{markId}/{status}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity orderList(@PathVariable("status") int status,
			@PathVariable("markId") long markId, HttpServletRequest request) {
		List<OrderQuery> list = orderService.orderList(status, markId, Tools
				.getShopkeeper(request).getId());
		return JsonEntity.getInstance(list);
	}

	/**
	 * 订单列表，全部
	 * 
	 * @param markId
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/orderList/{markId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity orderList_(@PathVariable("markId") long markId,
			HttpServletRequest request) {
		List<OrderQuery> list = orderService.orderList(markId, Tools
				.getShopkeeper(request).getId());
		return JsonEntity.getInstance(list);
	}

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/cancleOrder/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity cancleOrder(@PathVariable("orderId") long orderId) {
		orderService.cancleOrder(orderId);
		return JsonEntity.getInstance("取消订单成功");
	}

	/**
	 * 根据id获取订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/getOrderById/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity getOrderById(@PathVariable("orderId") long orderId) {
		return JsonEntity.getInstance(orderService.getById(orderId));
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity changePassword(HttpServletRequest request) {
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		if (StringUtil.isNullOrEmpty(newpassword)
				|| StringUtil.isNullOrEmpty(oldpassword)) {
			return JsonEntity.getError("不能为空！");
		}

		return JsonEntity.getInstance("修改密码成功！", shopkeeperService
				.changePassword(Tools.getShopkeeper(request).getId(),
						oldpassword, newpassword));
	}

	@RequestMapping(value = "/codeChangePassword", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity codeChangePassword(@ModelAttribute UserQuery shopkeeper) {
		if (StringUtil.isNullOrEmpty(shopkeeper.getCode())) {
			return JsonEntity.getError("验证码缺失");
		}
		if (StringUtil.isNullOrEmpty(shopkeeper.getPassword())) {
			return JsonEntity.getError("密码缺失");
		}
		if (StringUtil.isNullOrEmpty(shopkeeper.getPhone())) {
			return JsonEntity.getError("手机号缺失");
		}
		return JsonEntity.getInstance("修改密码成功！",
				shopkeeperService.codeChangePassword(shopkeeper));
	}

	/**
	 * 首页轮播图
	 * 
	 * @return
	 */
	@RequestMapping(value = "/homeInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity homeInfo() {
		Map<String, Object> map = new HashMap<>();
		map.put("shufflingFigure", imgService.shufflingFigure());
		map.put("goodsList", goodsService.findByPage(1));
		return JsonEntity.getInstance(map);
	}

	/**
	 * 验证登录
	 * 
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/checkLogin")
	public @ResponseBody
	JsonEntity checkLogin(HttpServletRequest request) {
		return JsonEntity.getInstance(Tools.getShopkeeper(request));
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * 生成app微信预支付
	 * 
	 * @param request
	 * @param userId
	 * @param total_fee
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ShopkeeperApp
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/wechatUnifiedorder", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity wechatUnifiedorder(HttpServletRequest request,
			@ModelAttribute PayRecord payRecord)
			throws UnsupportedEncodingException {
		payRecord.setShopkeeperId(Tools.getShopkeeper(request).getId());
		payService.wxpay(payRecord);
		TreeMap parameters = new TreeMap();
		parameters.put("total_fee",
				String.valueOf((int) (payRecord.getMoney() * 100)));
		parameters.put("appid", ConstantUtil.APP_ID);
		parameters.put("mch_id", ConstantUtil.PARTNER);
		parameters.put("notify_url", ConstantUtil.NOTIFY_URL);

		parameters.put("out_trade_no", payRecord.getOrderNo());
		parameters.put("spbill_create_ip", HttpUtil.getRemortIP(request));
		parameters.put("trade_type", "APP");
		parameters.put("nonce_str", StringUtil.getUuid());
		parameters.put("time_start", sdf.format(new Date()));
		parameters.put("body", payRecord.getDescribe());

		StringBuffer xml = new StringBuffer("<xml>\n");

		for (Object key : parameters.keySet()) {
			xml.append("<");
			xml.append(key.toString());
			xml.append("><![CDATA[");
			xml.append(parameters.get(key));
			xml.append("]]></");
			xml.append(key.toString());
			xml.append(">\n");
		}

		xml.append("<sign><![CDATA[");
		xml.append(WxUtil.createSign(parameters));
		xml.append("]]></sign>\n");

		xml.append("</xml>");

		String string = WxUtil.postXml(ConstantUtil.UNIFIEDORDER_URL,
				xml.toString());

		XMLSerializer xmlSerializer = new XMLSerializer();
		WechatUnifiedorder wechatunifiedorder = JSON.parseObject(xmlSerializer
				.read(string).toString(), WechatUnifiedorder.class);

		if ("SUCCESS".equals(wechatunifiedorder.getReturn_code())
				&& "SUCCESS".equals(wechatunifiedorder.getResult_code())) {
			TreeMap parameters_ = new TreeMap();
			parameters_.put("appid", wechatunifiedorder.getAppid());
			parameters_.put("partnerid", wechatunifiedorder.getMch_id());
			parameters_.put("prepayid", wechatunifiedorder.getPrepay_id());
			parameters_.put("package", wechatunifiedorder.getPackageValue());
			parameters_.put("noncestr", wechatunifiedorder.getNonce_str());
			parameters_.put("timestamp", wechatunifiedorder.getTimestamp());
			wechatunifiedorder.setSign(WxUtil.createSign(parameters_));
			return JsonEntity.getInstance(wechatunifiedorder);
		}
		return JsonEntity.getError("支付异常！");
	}

	/**
	 * 修改资料
	 * 
	 * @param shopkeeper
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/changeInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity changeInfo(@ModelAttribute User shopkeeper,
			HttpServletRequest request) {
		if (StringUtil.isNullOrEmpty(shopkeeper.getUserName()))
			return JsonEntity.getError("无修改信息!");
		if (!StringUtil.isNullOrEmpty(shopkeeper.getAvatar()))
			shopkeeper
					.setAvatar(ImgUtils.uploadImgFull(shopkeeper.getAvatar()));
		shopkeeper.setId(Tools.getShopkeeper(request).getId());

		return JsonEntity.getInstance("修改成功",
				shopkeeperService.changeInfo(shopkeeper));
	}

	// /**
	// * 申请退款
	// *
	// * @param request
	// * @param orderContentId
	// * @return
	// */
	// @ShopkeeperApp
	// @RequestMapping(value = "/refund/{orderContentId}", method =
	// RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity refund(HttpServletRequest request,
	// @PathVariable("orderContentId") long orderContentId) {
	// orderService.refund(orderContentId, Tools.getShopkeeper(request)
	// .getId());
	// return JsonEntity.getInstance("已申请退款");
	// }

	/**
	 * 确认收货
	 * 
	 * @param request
	 * @param orderId
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/countersign/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity countersign(HttpServletRequest request,
/**/			@PathVariable("orderId") String orderId) {
		orderService.countersign(orderId, Tools.getShopkeeper(request).getId());
		return JsonEntity.getInstance("已确认收货");
	}

	/**
	 * 获取评价
	 * 
	 * @param orderId
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/orderGrade/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity orderGrade(@PathVariable("orderId") long orderId) {
		List<GoodsSpecificationQuery> list = orderService.orderGrade(orderId);
		return JsonEntity.getInstance(list);
	}

	@ShopkeeperApp
	@RequestMapping(value = "/commitOrderGrade/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity commitOrderGrade(@PathVariable("orderId") long orderId,
			HttpServletRequest request, @RequestParam("result") String result) {

		List<GoodsEvaluate> list = JSONObject.parseArray(result,
				GoodsEvaluate.class);

		for (GoodsEvaluate goodsEvaluate : list) {
			goodsEvaluate.setShopkeeperId(Tools.getShopkeeper(request).getId());
			if (StringUtil.isNullOrEmpty(goodsEvaluate.getContent())) {
				goodsEvaluate.setContent(defautEvaluate);
			}
		}

		goodsService.commitOrderGrade(list, orderId);

		return JsonEntity.getInstance("评价成功");
	}

	/**
	 * 商品评价列表
	 * 
	 * @param goodsId
	 * @param markId
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/goodsEvaluateList/{goodsId}/{markId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity goodsEvaluateList(@PathVariable("goodsId") long goodsId,
			@PathVariable("markId") long markId) {

		return JsonEntity.getInstance(goodsService.goodsEvaluateList(goodsId,
				markId));
	}

	/**
	 * 修改进货车内容，加一或减一
	 * 
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/shoppingcartContentChange", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity shoppingcartContentChange(
			@ModelAttribute ShoppingcartContent shoppingcartContent) {
		return JsonEntity.getInstance((Object) shopkeeperService
				.shoppingcartContentChange(shoppingcartContent));
	}

	@ShopkeeperApp
	@RequestMapping(value = "/shoppingcartContentDelete/{shoppingcartContentId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity shoppingcartContentDelete(
			@PathVariable("shoppingcartContentId") long shoppingcartContentId,
			HttpServletRequest request) {
		return JsonEntity.getInstance(shopkeeperService
				.shoppingcartContentDelete(shoppingcartContentId, Tools
						.getShopkeeper(request).getId()));
	}

	/**
	 * 足迹记录
	 * 
	 * @param request
	 * @param markId
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/footprintMapperList/{markId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity footprintMapperList(HttpServletRequest request,
			@PathVariable("markId") long markId) {
		return JsonEntity.getInstance(shopkeeperService
				.footprintMapperList(Tools.getShopkeeper(request).getId(),
						BaseInfo.pageSize, markId));

	}

	/**
	 * 鲜资讯列表
	 * 
	 * @param markId
	 * @return
	 */
	@RequestMapping(value = "/informationList/{markId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity informationList(@PathVariable("markId") long markId) {
		return JsonEntity.getInstance(informationService
				.informationList(markId));
	}

	/**
	 * 鲜资讯列表，分类型
	 * 
	 * @param markId
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/informationList/{markId}/{type}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity informationList_(@PathVariable("markId") long markId,
			@PathVariable("type") int type) {
		return JsonEntity.getInstance(informationService.informationList(
				markId, type));
	}

	/**
	 * 根据id获取鲜资讯信息
	 * 
	 * @param informationId
	 * @return
	 */
	@RequestMapping(value = "/informationInfo/{informationId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity informationInfo(@PathVariable("informationId") long informationId) {
		return JsonEntity
				.getInstance(informationService.getById(informationId));
	}

	/**
	 * 
	 * @param informationId
	 * @return
	 */
	@RequestMapping(value = "/informationRead/{informationId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity informationRead(@PathVariable("informationId") long informationId) {
		informationService.read(informationId);
		return JsonEntity.getInstance();
	}

	/**
	 * 再次购买
	 * 
	 * @param orderId
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/restocking/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity restocking(@PathVariable("orderId") long orderId,
			HttpServletRequest request) {
		orderService.restocking(orderId, Tools.getShopkeeper(request).getId());
		return JsonEntity.getInstance("已添加进进货车");
	}

	/**
	 * 删除订单
	 * 
	 * @param orderId
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/deleteOrder/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity deleteOrder(@PathVariable("orderId") long orderId,
			HttpServletRequest request) {
		orderService.deleteOrder(orderId, Tools.getShopkeeper(request).getId());
		return JsonEntity.getInstance("已删除");
	}

	/**
	 * 清空失效商品
	 * 
	 * @param request
	 * @return
	 */
	@ShopkeeperApp
	@RequestMapping(value = "/clearLoseEfficacy", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity clearLoseEfficacy(HttpServletRequest request) {
		List<ShoppingcartQuery> list = shoppingcartService
				.clearLoseEfficacy(Tools.getShopkeeper(request).getId());
		return JsonEntity.getInstance("已清空", list);
	}

	/**
	 * 实体店主收货地址信息
	 * 
	 * @param addressId
	 * @return
	 */
	@RequestMapping(value = "/addressInfo/{addressId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity addressInfo(@PathVariable("addressId") long addressId) {
		return JsonEntity.getInstance(addressService
				.shopkeeperAddress(addressId));
	}

}
