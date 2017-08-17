package com.sirius.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sirius.annotation.Scanner;
import com.sirius.entity.AndroidApk;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.Inventory;
import com.sirius.entity.Order;
import com.sirius.entity.OrderContent;
import com.sirius.entity.Out;
import com.sirius.entity.Stock;
import com.sirius.entity.User;
import com.sirius.entity.query.GoodsQuery;
import com.sirius.entity.query.GoodsSpecificationQuery;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderContentStripeQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.entity.query.StockQuery;
import com.sirius.entity.query.UserQuery;
import com.sirius.exception.XException;
import com.sirius.po.JsonEntity;
import com.sirius.service.AppService;
import com.sirius.service.GoodsService;
import com.sirius.service.InventoryService;
import com.sirius.service.OrderContentService;
import com.sirius.service.OrderContentStripeService;
import com.sirius.service.OrderService;
import com.sirius.service.ScannerService;
import com.sirius.service.StockService;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;
import com.sirius.util.Tools;

@Controller
@RequestMapping("/scanner")
public class ScannerController {

	@Resource
	private ScannerService scannerService;

	@Resource
	private OrderContentService orderContentService;

	@Resource
	private GoodsService goodsService;

	@Resource
	private StockService stockService;

	@Resource
	private InventoryService inventoryService;

	@Resource
	private OrderService orderService;
	
	@Resource
	private OrderContentStripeService contentStripeService;

	@Resource
	private AppService appService;

	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/hLogin", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hLogin(@ModelAttribute User user,HttpSession session) {
		if (!StringUtil.phoneCheck(user.getPhone())) {
			return JsonEntity.getInstance("手机号格式错误",new User());
		}
		
		User dbdata = scannerService.login(user);
		
		if (dbdata == null) {
			return JsonEntity.getInstance("此手机号并未注册！",new User());
		} else if (dbdata.getType() != MyConstants.UserType.WHOLESALER && dbdata.getType() != MyConstants.UserType.SHOPKEEPER) {
			dbdata=null;
			return JsonEntity.getInstance("该手机号非供应商和实体店主身份,无法在此平台登录",new User());
		} else if (dbdata.getPassword().equals(
				DigestUtils.md5Hex(user.getPassword()))) {
			dbdata.setPassword(null);
			return JsonEntity.getInstance("登录成功",dbdata);
		} else {
			return JsonEntity.getInstance("密码错误",new User());
		}
	}
	
	
	/***
	 * 根据token获得用户的总金额、昨天的金额和数量
	 * @param token
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hGetHomeInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hGetHomeInfo(@ModelAttribute User user,HttpServletRequest request){
		user = Tools.getScanner(request);
		OrderContentQuery  o = new OrderContentQuery ();
		//选择已完成的订单
		int statusOrderContent = MyConstants.OrderContentStatus.ACCOMPLISH;//已完成
		//根据token获得总销售额
		String orderQuery = scannerService.getByToken(user.getToken(),statusOrderContent);
		//根据token获得昨天销售额
        //今天的零点零分秒
        long today = Tools.getTodayTime();
        //昨天的零点零分秒
        long yesterday = today-(24*60*60*1000);
        //获得订单付款时间
        List<String> date = scannerService.timeByToken(user.getToken());
        List<String> list = new ArrayList<>();
        for(String str:date){
        	try {
        		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = s.parse(str);
				long time = d.getTime();
				//时间确定在昨天的时间范围
				if(time>=yesterday&&time<today){
					//把时间戳转化为时间格式进行查询
					String dat = s.format(time);
					list.add(dat);
				}else{
					o.setYesterdayGoodsNum("0");
					o.setYesterdaytotalMoney("0");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        o = scannerService.getByPayTime(list,user.getToken());
        if(o.getYesterdaytotalMoney()==null){
        	o.setYesterdaytotalMoney("0");
        }
		//总销售额为空
        if(orderQuery==null){
        	o.setTotalMoney("0");
        	o.setYesterdayGoodsNum("0");
			o.setYesterdaytotalMoney("0");
        }else{
        	o.setTotalMoney(orderQuery);
        }
		return JsonEntity.getInstance("请求成功",o);
	}
	/***
	 * 获得入库、盘点、出库的页面信息
	 * (获取对应的入库、盘点、出库页面的商品总数量和昨日的总数量)
	 * @param token
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hGetInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hGetInfo(@ModelAttribute User user,@RequestParam("type")String type,
			HttpServletRequest request){
		List<String> list = new ArrayList<String>();
		OrderContentQuery  o = new OrderContentQuery ();
		user = Tools.getScanner(request);
		
		//获得商品总数量
		o = scannerService.getByPayTime(list,user.getToken());
		
		switch (type) {
		case "0"://入库页面
			if(user.getType()==0){//供货商
				//总数量和昨天的数量设置为空
				o.setTotalNum("0");
				o.setYesterdayGoodsNum("0");
				return JsonEntity.getInstance("请求成功",o);
			}else if(user.getType()==1){//店主
				return JsonEntity.getInstance("请求成功",o);
			}
		case "1"://盘点页面
			
			break;
		case "2"://出库页面
			
			break;

		default:
			break;
		}
		
//		int statusOrderContent = MyConstants.OrderContentStatus.PAYMENT;//已付款
//		//根据token获得总销售额
//		String orderQuery = scannerService.getByToken(user.getToken(),statusOrderContent);
//		//根据token获得昨天销售额
//		//今天的零点零分秒
//		long da = Tools.getTodayTime();
//		//昨天的零点零分秒
//		long l = da-24*60*60*1000;
		//获得订单付款时间
//		List<String> date = scannerService.timeByToken(user.getToken());
//		List<String> list = new ArrayList<>();
//		for(String str:date){
//			try {
//				SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				Date d = s.parse(str);
//				long time = d.getTime();
//				//时间确定在昨天的时间范围
//				if(time>=l&&time<da){
//					//把时间戳转化为时间格式进行查询
//					String dat = s.format(time);
//					list.add(dat);
//				}else{
//					o.setYesterdayGoodsNum("0");
//					o.setYesterdaytotalMoney("0");
//				}
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		o = scannerService.getByPayTime(list,user.getToken());
//		if(o.getYesterdaytotalMoney()==null){
//			o.setYesterdaytotalMoney("0");
//		}
//		//总销售额为空
//		if(orderQuery==null){
//			o.setTotalMoney("0");
//			o.setYesterdayGoodsNum("0");
//			o.setYesterdaytotalMoney("0");
//		}else{
//			o.setTotalMoney(orderQuery);
//		}
		return JsonEntity.getInstance("请求成功",o);
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
		AndroidApk androidApk = appService.newApk(version,
				MyConstants.UserType.SCANNER);
		if (androidApk == null)
			return JsonEntity.getError();
		else
			return JsonEntity.getInstance(androidApk);
	}

	/**
	 * 商品列表，分页
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hGoodsList/{page}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hGoodsList(@PathVariable("page") int page,
			HttpServletRequest request) {
		User user = Tools.getScanner(request);
		List<String> list = new ArrayList<>();
		switch (user.getType()) {
		// 供应商
		case MyConstants.UserType.WHOLESALER:
			List<GoodsQuery> goodsQueryGoodsQueries = 
					goodsService.scannerGoodsList(page,user.getId());
			return JsonEntity.getInstance("商品展示成功",goodsQueryGoodsQueries);
			// 买手
		case MyConstants.UserType.BUYER:

			return null;
		case MyConstants.UserType.SHOPKEEPER:
			
			return JsonEntity.getInstance("您的身份无法使用此功能",list);
		default:
			return null;

		}

	}

	/**
	 * 入库页面信息
	 * 
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/putInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity putInfo(HttpServletRequest request) {
		User user = Tools.getScanner(request);

		switch (user.getType()) {
		case MyConstants.UserType.WHOLESALER:

			return JsonEntity.getInstance(goodsService.getScannerPutInfo(user
					.getId()));
		case MyConstants.UserType.BUYER:

			return JsonEntity.getError("您的身份无法使用此功能");
		case MyConstants.UserType.SHOPKEEPER:
			return JsonEntity.getError("您的身份无法使用此功能");
		default:
			return null;
		}
	}

	/**
	 * 根据条件搜索未绑定磁条的商品
	 * 订单已付款，未绑定磁条的商品
	 * @param page
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hGoodsSpecificationList/{page}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hGoodsSpecificationList(@PathVariable("page") int page,
			HttpServletRequest request,@RequestParam("orderId") String orderId,
									   @RequestParam("type") String type) {
		User user = Tools.getScanner(request);
		switch (user.getType()) {

		// 供应商
		case MyConstants.UserType.WHOLESALER:
			
			if(type.equals("2")){//订单编号
				
				//查询订单下商品的number
				//List<OrderContentQuery> numbers = orderContentService.getByOrderId(orderId);
				
				List<GoodsSpecificationQuery> goodsSpecificationQueries = 
						goodsService.scannerSpecificationList(page, user.getId(),orderId);
				
				return JsonEntity.getInstance("查询成功",goodsSpecificationQueries);
			}
			// 买手
		case MyConstants.UserType.BUYER:
			return null;
		case MyConstants.UserType.SHOPKEEPER:
			return JsonEntity.getError("您的身份无法使用此功能");
		default:
			return null;

		}

	}

	/**
	 * 绑定
	 * 
	 * @param stock
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hStripeBinding", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hStripeBinding(@RequestParam("orderId") String orderId,
			@RequestParam("sku") String sku,
			@RequestParam("magneticStripe") String magneticStripe,
			HttpServletRequest request) {
		User user = Tools.getScanner(request);
		Stock stock=new Stock();
		//根据订单编号获取订单id
		Order order = orderService.getByOrderNo(orderId);
		switch (user.getType()) {
		// 供应商
		case MyConstants.UserType.WHOLESALER:
			stock.setUserId(user.getId());
			stock.setOrderId(order.getId());
			stock.setSku(sku);
			stock.setMagneticStripe(magneticStripe);
			stockService.stripeBinding(stock,orderId);
			return JsonEntity.getInstance("绑定成功");
			// 买手
		case MyConstants.UserType.BUYER:
			return null;
		case MyConstants.UserType.SHOPKEEPER:
			return JsonEntity.getError("您的身份无法使用此功能");
		default:
			return null;

		}

	}

	/**
	 * 根据sku获取商品信息
	 * 盘点
	 * @param sku
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hSpecificationBySku", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hSpecificationBySku(@RequestParam("sku") String sku,HttpServletRequest request) {
		
		User user = Tools.getScanner(request);
		switch (user.getType()) {
		// 供应商
		case MyConstants.UserType.WHOLESALER:
			return JsonEntity.getError("您的身份无法使用此功能");
			// 买手
		case MyConstants.UserType.BUYER:
			return null;
			//店主
		case MyConstants.UserType.SHOPKEEPER:
			String string = sku;
			String[] s = string.split(",");
			List<String> list = new ArrayList<String>();
			for(int i = 0;i<s.length;i++){
				String sk = s[i];
				list.add(sk);
			}
			return JsonEntity.getInstance("查询成功",goodsService.specificationBySku(list,user.getId()));
		default:
			return null;
			}
	}
//	@Scanner
//	@RequestMapping(value = "/specificationBySku", method = RequestMethod.POST)
//	public @ResponseBody
//	JsonEntity specificationBySku(@RequestParam("sku") List<String> sku) {
//		return JsonEntity.getInstance(goodsService.specificationBySku(sku));
//	}

	// /**
	// * 出库
	// *
	// * @param magneticStripes
	// * @param request
	// * @return
	// */
	// @Scanner
	// @RequestMapping(value = "/outLibrary", method = RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity outLibrary(@RequestParam("stocks") String stocks_,
	// HttpServletRequest request) {
	//
	// List<Stock> stocks = JSONObject.parseArray(stocks_, Stock.class);
	//
	// if (stocks.size() == 0) {
	// return JsonEntity.getError("无数据");
	// }
	//
	// for (Stock stock : stocks) {
	// stock.setUserId(Tools.getScanner(request).getId());
	// }
	//
	// stockService.outLibrary(stocks);
	//
	// return JsonEntity.getInstance("出库成功");
	// }

	/**
	 * 盘点
	 * 生成盘点记录
	 * @param inventorys_
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hCheckRecord", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hCheckRecord(@RequestParam("keyword") String keyword,
			HttpServletRequest request) {
		User user = Tools.getScanner(request);
		Stock stock = new Stock();
		Inventory inventory = new Inventory();
		switch (user.getType()) {
		// 店主
		case MyConstants.UserType.SHOPKEEPER:
//			//获得当前时间戳
//			Long today = Tools.getTodayTime();
//			//获得昨天时间戳
//			Long yesterday = today-(24*60*60*1000);
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");  
		    String dateNowStr = sdf.format(date);  
		    String uuid = (user.getId())+//盘点编号
					s.format(date)+StringUtil.getUuid().substring(0, 2);
			try {
				JSONArray jsonArray = new JSONArray(keyword);
				for(int i=0;i<jsonArray.length();i++){
					String sku = (String) jsonArray.getJSONObject(i).get("sku");
					String num = (String) jsonArray.getJSONObject(i).get("num");
					//根据sku获得商品规格
					Long goodsSpecificationId = stockService.getGoodsSpecificationIdBysku(sku);
					stock.setSku(sku);
					stock.setError(MyConstants.StockError.SUCCESS);
					stock.setOuted(MyConstants.StockOuted.NO);
					//根据sku获得磁条编号
					List<String> magneticStripeList = stockService.getBysku(stock);
					
					for(String magneticStripe:magneticStripeList){
						inventory.setCreateTime(dateNowStr);//盘点时间
						inventory.setUserId(user.getId());
						inventory.setSerialNumber(uuid);
						inventory.setMagneticStripe(magneticStripe);//磁条编号
						inventory.setGoodsSpecificationId(goodsSpecificationId);
						inventory.setSku(sku);
						//存盘库数据
						inventoryService.insertInventory(inventory);
					}
				}
				return JsonEntity.getInstance("已生成盘点记录");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			String skuList = keyword.substring(keyword.indexOf("sku:")+4,keyword.indexOf(",num"));
//			System.out.println(skuList);
//			System.out.println(keyword+"----------------");
			
//			List<Inventory> inventorys = JSONObject.parseArray(inventorys_,
//					Inventory.class);
//			if (inventorys.size() == 0) {
//				return JsonEntity.getError("无数据");
//			}
//			for (Inventory inventory : inventorys) {
//				inventory.setUserId(user.getId());
//			}
//			inventoryService.inventory(inventorys);
//			return JsonEntity.getInstance("盘点成功");
			// 买手
		case MyConstants.UserType.BUYER:
			return null;
		case MyConstants.UserType.WHOLESALER://供货商
			return JsonEntity.getError("您的身份无法使用此功能");
		default:
			return null;

		}
	}
	
	/**
	 * 根据商品的sku，返回商品的相关信息（spu,sku、大小、颜色、图片地址，库存量，type类型（0：本店，1：其他店铺）,店名）
	 * @param sku
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hCheckInventoryWithPlaceBySku", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hCheckInventoryWithPlaceBySku(@RequestParam("sku")String sku){
		
		return JsonEntity.getInstance("获取成功");
	}

	/**
	 * 出库页面显示查找订单信息
	 * 
	 * @param order
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hOutOrderInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hOutOrderInfo(@ModelAttribute OrderQuery order,
			HttpServletRequest request,
			@RequestParam("type")String type,
			@RequestParam("keyword")String keyword) {
		User user = Tools.getScanner(request);
		switch (user.getType()) {

		// 供应商
		case MyConstants.UserType.WHOLESALER:
			if(type.equals("0")){//供货商
				//keyword=订单号(orderNo)
				//订单状态和订单下的状态为已付款
				int status = MyConstants.OrderStatus.PAYMENT;
				//根据orderNo获得商品信息
				List<OrderContent> orderContentList = 
						orderContentService.getOrderContentByOrderNo(keyword,status,
								user.getId());
				return JsonEntity.getInstance("查询成功",orderContentList);
//			order.setWholesalerId(user.getId());
//			order.setStatus(MyConstants.OrderStatus.BEING);
//			return JsonEntity.getInstance(orderService.getByKeyword(order));
			}else if(type.equals("1")){
				return JsonEntity.getInstance("您的身份无法使用此功能");
			}
		//店主
		case MyConstants.UserType.SHOPKEEPER:
			if(type.equals("0")){//供货商
				return JsonEntity.getInstance("您的身份无法使用此功能");
			}else if(type.equals("1")){//店主
				List<String> list = new ArrayList<String>();
				Stock stock = new Stock();
				//keyword=商品编号(sku)
				String[] skus = keyword.split(",");
				for(int i=0;i<skus.length;i++){
					list.add(skus[i]);
				}
				//sku集合查询信息
				List<OrderContent> orderContentList = 
						orderContentService.getOrderContentBySku(list,user.getId());
				if(orderContentList==null){
					return JsonEntity.getInstance("无出库数据",new OrderContent());
				}
				//店主商品出库（卖出）
				return JsonEntity.getInstance("查询成功", orderContentList);
			}
		default:
			return null;
		}
	}

	/**
	 * 出库操作（备货）
	 * 
	 * @param orderId
	 * @param outLibraryInfo_
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hOutLibrary", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hOutLibrary(@RequestParam("keyword") String keyword,
			@RequestParam("type") String type,
			HttpServletRequest request) {
		Order order = new Order();
		User user = Tools.getScanner(request);
		
		if(keyword == null || keyword.equals("")){
			return JsonEntity.getInstance("无数据");
		}
		switch (user.getType()) {
		// 供应商
		case MyConstants.UserType.WHOLESALER:
			if(type.equals("0")){
				//int status = MyConstants.OrderStatus.PAYMENT;//已付款
				//根据订单编号查询商品
				List<OrderContent> orderContentList = 
						orderContentService.getOrderContent(keyword, user.getId());
//				contentStripeService.outLibrary(outLibraryInfo, orderId,
//						user.getId());
				//order.setStatus(MyConstants.OrderStatus.PAYMENT);//已付款
				order.setOrderNo(keyword);
				//查询该订单
				Order o = orderService.getOrderByOrderNo(order);
				if(orderContentList.size()==0){
					return JsonEntity.getInstance("无出库数据");
				}
				if(o.getStatus()==MyConstants.OrderStatus.SHIPMENTED){//已发货
					return JsonEntity.getInstance("订单已发货，不可重复发货");
				}
				if(o==null || o.equals(null)){
					return JsonEntity.getInstance("没有数据");
				}
				if(o.getStatus()!=MyConstants.OrderStatus.PAYMENT){
					return JsonEntity.getInstance("订单未付款");
				}
				order.setStatus(MyConstants.OrderStatus.SHIPMENTED);//已发货
				if(order.getStatus()!=MyConstants.OrderStatus.SHIPMENTED){
					return JsonEntity.getInstance("订单已发货，请勿重复发货");
				}
				//根据商品的sku，完成商品出库的操作
				contentStripeService.outLibrary(keyword,user.getId());
				return JsonEntity.getInstance("出库成功",new Order());
			}else if(type.equals("1")){//店主
				return JsonEntity.getError("您的身份无法使用此功能");
			}
		//门店
		case MyConstants.UserType.SHOPKEEPER://店主
			if(type.equals("0")){//供货商
				return JsonEntity.getError("您的身份无法使用此功能");
			}else if(type.equals("1")){//店主 keyword={magneticStripe:"5564486954685",sku:"1"}]
				Out out = new Out();
				Date date = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				List<StockQuery> stockList = JSONObject.parseArray(keyword,StockQuery.class);
				for(StockQuery s:stockList){
					//根据sku查询数量
					StockQuery stockQuery = stockService.getStockBysku(s.getSku());
					if(stockQuery==null){
						return JsonEntity.getInstance("无出库数据");
					}
					if(stockQuery.getNum()<s.getNum()){
						return JsonEntity.getInstance("出库失败,该商品的数量不足:"+s.getSku());
					}
					if(stockQuery.getError()==MyConstants.StockError.ERROR){
						return JsonEntity.getInstance("出库失败,该商品入库不正确:"+s.getSku());
					}
					if(stockQuery.getOuted()==MyConstants.StockOuted.YES){
						return JsonEntity.getInstance("出库失败,该商品已出库:"+s.getSku());
					}
					s.setOuted(MyConstants.StockOuted.YES);//已出库
					s.setError(MyConstants.StockError.SUCCESS);//入库正常的商品
					//根据sku和磁条售出商品（出库标记）
					stockService.updateStatus(s);
					//将出库商品添加到出库表
					out.setUserId(user.getId());
					out.setOutNo(simpleDateFormat+StringUtil.getUuid().substring(0,4));
					out.setSku(s.getSku());
					//out.setMagneticStripe();
					
				}
				return JsonEntity.getInstance("出库成功",new StockQuery());
			}
		default:
			return null;
		}
	}

	/***
	 * 入库查询订单
	 * @param order
	 * @param type
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hOrderByShopkeeper", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hOrderByShopkeeper(@ModelAttribute OrderQuery order,
				@RequestParam("type") String type,@RequestParam("keyword") String keyword,
								  HttpServletRequest request) {
		User user = Tools.getScanner(request);
		switch (user.getType()) {

		// 供应商
		case MyConstants.UserType.WHOLESALER:
			return JsonEntity.getError("您的身份无法使用此功能");
			// 买手
		case MyConstants.UserType.BUYER:
			return null;
		case MyConstants.UserType.SHOPKEEPER:
			if(type.equals("1")){//type=订单编号
				order.setShopkeeperId(user.getId());
				order.setStatus(MyConstants.OrderStatus.SHIPMENTED);
				order.setShopkeeperId(Tools.getScanner(request).getId());//实体店主
				order.setKeyword(keyword);
				//判断该订单是否有数据
				OrderQuery orderQuery = orderService.getByShopkeeper(order);
				if(orderQuery==null){
					return JsonEntity.getInstance("无数据",new OrderQuery());
				}
				return JsonEntity.getInstance("查询成功",orderQuery);
			}else if(type.equals("0")){
				return JsonEntity.getError("不支持物流单号");
			}
		default:
			return null;

		}
	}

	/**
	 * 确认收货
	 * 
	 * @param request
	 * @param orderId
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hCountersign", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hCountersign(HttpServletRequest request,
			@RequestParam("keyword") List<String> keyword,
			@RequestParam("orderId") String orderId) {
		User user = Tools.getScanner(request);
		List<String> list = new ArrayList<String>();
		switch (user.getType()) {
		// 供应商
		case MyConstants.UserType.WHOLESALER:
			return JsonEntity.getError("您的身份无法使用此功能");
			// 买手
		case MyConstants.UserType.BUYER:
			return JsonEntity.getError("您的身份无法使用此功能");
		case MyConstants.UserType.SHOPKEEPER://店主
			if(keyword==null){
					return JsonEntity.getInstance("参数为空",new Order());
			}else{//!=null//没有错误
				if(keyword.toString().contains("正确")) {
					
					orderService.countersign(orderId,user.getId());
					return JsonEntity.getInstance("已入库",new Order());
				}else{//不正确:sku
					//查询订单下的所有sku商品
					List<OrderContentQuery> orderContentList=
							orderContentService.getOrderContent(orderId);
					//遍历keyword
					for(String sku:keyword){
						for(OrderContentQuery orderContentQuery:orderContentList){
							if(sku.equals(orderContentQuery.getSku())){
								list.add(sku);
							}
						}
					}
					if(list.size()!=keyword.size()){
						throw new XException("不可操作商品");
					}
					//订单入库,标记已完成
					orderService.countersign(orderId,user.getId());
					//订单异常//该订单显示错误的已完成
					int orderError = MyConstants.OrderError.ERROR;
					orderService.updateError(orderId,orderError);
					//商品标注异常
					int orderContentError = MyConstants.OrderContentError.ERROR;
					orderContentService.updateError(list,orderContentError);
						
					//入库表给错误商品加标注
					int stockError = MyConstants.StockError.ERROR;
					stockService.updateError(stockError,list);
					
					return JsonEntity.getInstance("已入库,但入库有误",new Order());
				}
			}
		default:
			return null;
		}

	}

	// @RequestMapping(value = "/x", method = RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity x() {
	// goodsService.x();
	// return JsonEntity.getInstance("成");
	// }
	
	/***
	 * 获取用户的全部订单（已付款，即将发货）
	 * @param request
	 * @return
	 */
	@Scanner
	@RequestMapping(value = "/hGetAllOutOrder", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity hGetAllOutOrder(HttpServletRequest request,OrderQuery order){
		List<String> list = new ArrayList<>();
		User user = Tools.getScanner(request);
		OrderContent orderContent = new OrderContent();
		orderContent.setWholesalerId(user.getId());
		orderContent.setStatus(MyConstants.OrderContentStatus.PAYMENT);//已付款
		//获得该用户的所有订单下单商品，状态为以付款
		List<String> orderIdList = orderContentService.getByUserId(orderContent);
		for(String orderId:orderIdList){
			list.add(orderId);
		}
		if(orderIdList.size()==0 ||orderIdList==null){
			return JsonEntity.getInstance("无数据",new OrderContent());
		}
		String o = orderIdList.toString();
		String idString = o.substring(1,o.length()-1);
		order.setOrderNo(idString);
		return JsonEntity.getInstance("查询成功",order);
	}
}
