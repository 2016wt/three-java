package com.sirius.controller;

import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;

import sun.misc.BASE64Decoder;

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.sirius.annotation.WholesalerLogin;
import com.sirius.entity.Company;
import com.sirius.entity.Dictionary;
import com.sirius.entity.Goods;
import com.sirius.entity.GoodsImg;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.LocationCity;
import com.sirius.entity.LocationProvince;
import com.sirius.entity.Message;
import com.sirius.entity.Order;
import com.sirius.entity.OrderContent;
import com.sirius.entity.User;
import com.sirius.entity.query.GoodsQuery;
import com.sirius.entity.query.GoodsSpecificationQuery;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.exception.XException;
import com.sirius.image.ImgUtils;
import com.sirius.mybatis.mapper.GoodsImgMapper;
import com.sirius.po.BTEntitiy;
import com.sirius.po.JsonEntity;
import com.sirius.service.AddressService;
import com.sirius.service.CompanyService;
import com.sirius.service.DictionaryService;
import com.sirius.service.GoodsImgService;
import com.sirius.service.GoodsService;
import com.sirius.service.MessageService;
import com.sirius.service.OrderService;
import com.sirius.service.UserService;
import com.sirius.service.WholesalerService;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;
import com.sirius.util.Tools;

@Controller
@RequestMapping("/wholesaler")
public class WholesalerController {

	@Resource
	private GoodsImgService goodsImgService;
	
	@Resource
	private AddressService addressService;

	@Resource
	private MessageService messageService;

	// 公司信息service
	@Resource
	private CompanyService companyService;

	// 供应商service
	@Resource
	private WholesalerService wholesalerService;

	// 商品service
	@Resource
	private GoodsService goodsService;

	// 订单service
	@Resource
	private OrderService orderService;

	// //公司service
	// @Resource
	// private CompanyService companyService;
	//
	@Resource
	private DictionaryService dictionaryService;

	@Resource
	private UserService userService;

	// 可以购买开始时间
	@Value("${goods.buyTimeStart}")
	private String buyTimeStart;
	// 可以购买结束时间
	@Value("${goods.buyTimeEnd}")
	private String buyTimeEnd;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		if (request.getSession().getAttribute("wholesaler") != null) {
			return "redirect:/wholesaler/goodsList";
		}
		return "wholesaler/login";
	}

	/**
	 * 登陆
	 * 
	 * @param wholesaler
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity login_(@ModelAttribute User wholesaler, HttpSession session,
			HttpServletResponse response) {
		wholesaler = wholesalerService.login(wholesaler);
		session.setAttribute("wholesaler", wholesaler);

		return JsonEntity.getInstance();
	}

	/**
	 * 打开订单列表
	 * 
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/orderList", method = RequestMethod.GET)
	public String orderList() {
		return "wholesaler/orderList";
	}

	/**
	 * 订单信息
	 * 
	 * @param session
	 * @param order
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/orderList", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy orderList(HttpServletRequest req, @ModelAttribute OrderQuery order) {
		order.setWholesalerId(Tools.getPCWholesaler(req).getId());
		order.setOrder("id desc");
		return orderService.findByPage(order);
	}

	/**
	 * 打开订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/orderContent/{orderId}", method = RequestMethod.GET)
	public String orderContent(@PathVariable("orderId") long orderId,HttpServletRequest req) {
		
		
		//获得订单的基本信息
		Order order = orderService.getOrder(orderId);
		req.setAttribute("order", order);
		
		//获得订单对应的所有商品总数
		long quantity = goodsService.getSumQuantity(orderId);
		req.setAttribute("quantity", quantity);
				
		return "wholesaler/orderContent";
	}

	/**
	 * 订单详细列表
	 * 订单下的商品信息
	 * @param req
	 * @param order
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/goodsList/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy goodsList(@PathVariable("orderId") long orderId,HttpServletRequest req,
			@ModelAttribute GoodsQuery goodsQuery) {
		
		goodsQuery.setOrderId(orderId);
		goodsQuery.setWholesalerId(Tools.getPCWholesaler(req).getId());
		//根据订单id查询商品信息
		
		return goodsService.getGoodsOrderId(goodsQuery);
	}
	
	
	/**
	 * 打开商品列表
	 * 
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/goodsList", method = RequestMethod.GET)
	public String goodsList(HttpServletRequest req) {
		//获得供应商设置的最小库存数
		User wholesaler = Tools.getPCWholesaler(req);
		req.setAttribute("earlyWarning",wholesaler.getEarlyWarning());
		return "wholesaler/goodsList";
	}
	
	/****
	 * 订单发货
	 * @param orderId
	 * @param order
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/sendOrder/{orderId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity sendOrder(@PathVariable("orderId") long orderId,@ModelAttribute Order order){
		order.setId(orderId);
		orderService.sendOrder(order);
		return JsonEntity.getInstance("发货成功");
	}
	/**
	 * 商品信息
	 * @param session
	 * @param goods
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/goodsList", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy goodsList(HttpServletRequest req, @ModelAttribute GoodsQuery goods) {
		goods.setWholesalerId(Tools.getPCWholesaler(req).getId());
		goods.setOrder("id desc");
		
		return goodsService.findByPage(goods);
	}
	
	/**
	 * 商品上架
	 * @param req
	 * @param goods
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/putawayU/{id}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity putawayU(@PathVariable("id") long id,@ModelAttribute Goods goods,HttpServletRequest req){
		goods.setId(id);
		goods.setPutaway(true);
		goods.setWholesalerId(Tools.getPCWholesaler(req).getId());
		goodsService.updatePutaway(goods);
		return JsonEntity.getInstance("上架成功");
	}
	
	/**
	 * 商品下架
	 * @param req
	 * @param goods
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/putawayD/{id}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity putawayD(@PathVariable("id") long id,@ModelAttribute Goods goods,HttpServletRequest req){
		goods.setId(id);
		goods.setPutaway(false);
		goods.setWholesalerId(Tools.getPCWholesaler(req).getId());
		goodsService.updatePutaway(goods);
		return JsonEntity.getInstance("下架成功");
	}
	
	
	/**
	 * 获得某商品的详细库存
	 * @param session
	 * @param goods
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/goodsQuantity/{goodsId}", method = RequestMethod.POST)
	public List<GoodsSpecification> goodsQuantity(@PathVariable("goodsId") long goodsId,HttpServletRequest req) {
		// 根据商品id获得商品详细
		List<GoodsSpecification> goodsSpecificationList = goodsService
				.getSpecification(goodsId);
		for(GoodsSpecification goodsSpecification:goodsSpecificationList){
			req.setAttribute("goodsSpecification", goodsSpecification);
			System.out.println(goodsSpecification.getSize());
		}
		return goodsSpecificationList;
	}
	
	/**
	 * 打开商品详细列表
	 * 
	 * @param goodsId
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/goodsContentList/{goodsId}", method = RequestMethod.GET)
	public String goodsContentList(@PathVariable("goodsId") long goodsId,
			Dictionary dictionary, HttpServletRequest req) {

		// 根据商品id获得商品的图片
		List<GoodsImg> imgList = goodsService.getImgUrl(goodsId);
		req.setAttribute("imgList", imgList);

		// 根据商品的id获得商品的产地（省市）
		Goods g = goodsService.selectProvince(goodsId);
		// 省
		LocationProvince province = addressService.provinceName(g
				.getProductionProvince());
		// 市
		LocationCity city = addressService.cityName(g.getProductionPlace());

		req.setAttribute("province", province);
		req.setAttribute("city", city);

		// 流行元素
		dictionary.setType(MyConstants.Dictionary.FASHION);
		List<Dictionary> fashionList = dictionaryService
				.fashionData(dictionary);
		for (Dictionary fashion : fashionList) {
			req.setAttribute("fashion", fashion);
		}

		// 查询品牌
		dictionary.setType(MyConstants.Dictionary.TRADEMARK);
		List<Dictionary> trademarkList = dictionaryService
				.trademarkData(dictionary);
		for (Dictionary trademark : trademarkList) {
			req.setAttribute("trademark", trademark);
		}

		// 商品分类
		dictionary.setType(MyConstants.Dictionary.GOODSGENRE);
		List<Dictionary> goodsgenreList = dictionaryService
				.goodsgenreData(dictionary);
		for (Dictionary goodsgenre : goodsgenreList) {
			req.setAttribute("goodsgenre", goodsgenre);
		}

		// 风格分类
		dictionary.setType(MyConstants.Dictionary.STYLEGENRE);
		List<Dictionary> stylegenreList = dictionaryService
				.stylegenreList(dictionary);
		for (Dictionary stylegenre : stylegenreList) {
			req.setAttribute("stylegenre", stylegenre);
		}

		// 根据商品id获得商品详细
		List<GoodsSpecification> goodsSpecificationList = goodsService
				.getSpecification(goodsId);

		req.setAttribute("goodsSpecificationList", goodsSpecificationList);

		Goods goods = new Goods();
		goods.setId(goodsId);
		// 获取需要修改的商品信息
		goods = goodsService.getUpdateGoods(goods);
		req.setAttribute("goods", goods);
		return "wholesaler/goodsContent";
	}

	/**
	 * 商品详细列表
	 * 
	 * @param req
	 * @param goods
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/goodsContentList", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy goodsContentList(@ModelAttribute GoodsSpecificationQuery goods) {
		return goodsService.goodsContentList(goods);
	}

	/**
	 * 删除商品信息
	 * 
	 * @param id
	 * @param goods
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/deleteGoods/{id}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity deleteGoods(@PathVariable("id") long id,
			@ModelAttribute GoodsQuery goods, HttpServletRequest req) {
		// 获得删除的商品的供应商ID
		long ID = goodsService.selectWholesalerId(id);
		// 获得当前用户id
		long wholesalerId = Tools.getPCWholesaler(req).getId();
		if (ID == wholesalerId) {
			goodsService.deleteGoodsList(id);
		}
		return JsonEntity.getInstance("删除商品成功");
	}

	/**
	 * 批量删除
	 * @param id
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/deleteGoodsList", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity deleteGoodsList(@RequestParam("goodsListId") long[] goodsListId){
		for(long id:goodsListId){
			System.out.println(id);
			//根据id删除商品
			goodsService.deleteGoodsList(id);
		}
		return JsonEntity.getInstance("删除商品成功");
	}
	
	/**
	 * 打开商品管理
	 */
	@WholesalerLogin
	@RequestMapping(value = "/goods/{goodsId}", method = RequestMethod.GET)
	public String goods(Dictionary dictionary, HttpServletRequest req,
			@PathVariable("goodsId") long goodsId) {
		return "wholesaler/goods";
	}

	/**
	 * 打开添加商品页面
	 * 
	 * @param dictionary
	 * @param goodsQuery
	 * @param req
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/createGoods" + "", method = RequestMethod.GET)
	public String createGoods(@ModelAttribute Dictionary dictionary,
			HttpServletRequest req) {
		req.setAttribute("title", "商品添加");

		dictionary.setUserId(Tools.getPCWholesaler(req).getId());
		// 流行元素
		dictionary.setType(MyConstants.Dictionary.FASHION);
		// 查询流行元素
		List<Dictionary> fashionList = dictionaryService
				.fashionData(dictionary);

		dictionary.setType(MyConstants.Dictionary.TRADEMARK);
		// 查询品牌
		List<Dictionary> trademarkList = dictionaryService
				.trademarkData(dictionary);

		// 商品分类
		dictionary.setType(MyConstants.Dictionary.GOODSGENRE);
		// 查询商品分类
		List<Dictionary> goodsgenreList = dictionaryService
				.goodsgenreData(dictionary);

		// 风格分类
		dictionary.setType(MyConstants.Dictionary.STYLEGENRE);
		// 查询风格分类
		List<Dictionary> stylegenreList = dictionaryService
				.stylegenreList(dictionary);

		req.setAttribute("fashionList", fashionList);
		req.setAttribute("trademarkList", trademarkList);
		req.setAttribute("goodsgenreList", goodsgenreList);
		req.setAttribute("stylegenreList", stylegenreList);

		return "wholesaler/goods";
	}

	/**
	 * 打开修改商品页面
	 * 
	 * @param req
	 * @param dictionary
	 * @param goodsId
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/updateGoods/{goodsId}",method = RequestMethod.GET)
	public String updateGoods(HttpServletRequest req, Dictionary dictionary,
			@PathVariable("goodsId") long goodsId) {

		// 根据商品id获得商品的图片
		List<GoodsImg> imgList = goodsService.getImgUrl(goodsId);
		req.setAttribute("imgList", imgList);

		req.setAttribute("title", "商品修改");
		// 流行元素
		dictionary.setType(MyConstants.Dictionary.FASHION);
		// 查询流行元素
		List<Dictionary> fashionList = dictionaryService
				.fashionData(dictionary);

		dictionary.setType(MyConstants.Dictionary.TRADEMARK);
		// 查询品牌
		List<Dictionary> trademarkList = dictionaryService
				.trademarkData(dictionary);

		// 商品分类
		dictionary.setType(MyConstants.Dictionary.GOODSGENRE);
		// 查询商品分类
		List<Dictionary> goodsgenreList = dictionaryService
				.goodsgenreData(dictionary);

		// 风格分类
		dictionary.setType(MyConstants.Dictionary.STYLEGENRE);
		// 查询风格分类
		List<Dictionary> stylegenreList = dictionaryService
				.stylegenreList(dictionary);

		// 根据商品id获得商品详细
		List<GoodsSpecification> goodsSpecificationList = goodsService
				.getSpecification(goodsId);

		req.setAttribute("goodsSpecificationList", goodsSpecificationList);
		req.setAttribute("fashionList", fashionList);
		req.setAttribute("trademarkList", trademarkList);
		req.setAttribute("goodsgenreList", goodsgenreList);
		req.setAttribute("stylegenreList", stylegenreList);

		Goods goods = new Goods();
		goods.setId(goodsId);
		// 获取需要修改的商品信息
		goods = goodsService.getUpdateGoods(goods);
		req.setAttribute("goods", goods);
		return "wholesaler/goods";
	}

	/**
	 * 商品统一管理
	 * 
	 * @param goods
	 * @param specification
	 * @param req
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/goods", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity goods(@ModelAttribute GoodsQuery goods,
			@RequestParam("specification") String specification,
			@RequestParam(value="base64",required=false) String base64,HttpServletRequest req) {
		
		if (goods.getId() == null) {// 商品添加

			if(base64!=null){//添加有图片
				//获得图片bas64编码
				String newBase64 = base64.substring(23);
				//图片上传服务器
				String imgUrl = ImgUtils.uploadImgFull(newBase64);
				
				// 设置该供应商的id
				goods.setWholesalerId(Tools.getPCWholesaler(req).getId());
				List<GoodsSpecification> specifications = JSONObject.parseArray(
						specification, GoodsSpecification.class);
				// 设置该商品的详情
				goods.setSpecifications(specifications);
				// 设置spu
				goods.setSpu(StringUtil.getUuid().substring(0, 12));
				// 设置可以购买开始时间
				goods.setBuyTimeStart(buyTimeStart);
				// 设置可以购买结束时间
				goods.setBuyTimeEnd(buyTimeEnd);
				// 成团数量
				goods.setClustering(5);


				GoodsImg goodsImg = new GoodsImg();
				goodsImg.setImgUrl(imgUrl);
				goods.setGoodsImg(goodsImg);
				goodsService.createGoodsImg(goods);
				return JsonEntity.getInstance("添加成功");
				
			}else{//添加没有图片
				
				// 设置该供应商的id
				goods.setWholesalerId(Tools.getPCWholesaler(req).getId());
				List<GoodsSpecification> specifications = JSONObject.parseArray(
						specification, GoodsSpecification.class);
				// 设置该商品的详情
				goods.setSpecifications(specifications);
				// 设置spu
				goods.setSpu(StringUtil.getUuid().substring(0, 12));
				// 设置可以购买开始时间
				goods.setBuyTimeStart(buyTimeStart);
				// 设置可以购买结束时间
				goods.setBuyTimeEnd(buyTimeEnd);
				// 成团数量
				goods.setClustering(5);

				goodsService.createGoodsNoImg(goods);
				return JsonEntity.getInstance("添加成功");
			}

		} else {// 商品修改

			//判断是否有图片
			if(base64!=null){//修改有图片
				List<GoodsSpecification> specifications = JSONObject.parseArray(
						specification, GoodsSpecification.class);
				
				// 设置该商品的详情
				goods.setSpecifications(specifications);
				goods.setWholesalerId(Tools.getPCWholesaler(req).getId());
				
				//获得图片bas64编码
				String newBase64 = base64.substring(23);
				//图片上传服务器
				String imgUrl = ImgUtils.uploadImgFull(newBase64);
				
				GoodsImg goodsImg = new GoodsImg();
				goodsImg.setImgUrl(imgUrl);
				goods.setGoodsImg(goodsImg);
				goodsService.updateGoodsImg(goods);
				return JsonEntity.getInstance("修改成功");
			}else{//修改没有图片

				List<GoodsSpecification> specifications = JSONObject.parseArray(
						specification, GoodsSpecification.class);
				
				// 设置该商品的详情
				goods.setSpecifications(specifications);
				goods.setWholesalerId(Tools.getPCWholesaler(req).getId());
				
				goodsService.updateGoodsNoImg(goods);
				return JsonEntity.getInstance("修改成功");
			}
			
		}
	}
	
	/**
	 * 点击图片进行删除
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value="deleteImg/{id}",method = RequestMethod.POST)
	public void deleteImg(@PathVariable("id") long id){
		System.out.println("------");
		goodsImgService.deleteImg(id);
	}

	/**
	 * 退出
	 * 
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exit")
	public String exit(HttpSession session, HttpServletResponse response) {
		session.removeAttribute(Tools.WHOLESALER);
		Cookie cookie = new Cookie("wholesalerToken", null);
		response.addCookie(cookie);
		return "redirect:/wholesaler/login";
	}

	/**
	 * 打开最新消息
	 * 
	 * @param session
	 * @param response
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/messageList", method = RequestMethod.GET)
	public String messageList() {
		return "wholesaler/messageList";
	}

	/**
	 * 最新消息
	 */
	@WholesalerLogin
	@RequestMapping(value = "/messageList", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy messageList(@ModelAttribute Message message) {
		return messageService.messageList(message);
	}

	/**
	 * 最新消息详情
	 */
	@WholesalerLogin
	@RequestMapping(value = "/messageContent/{id}", method = RequestMethod.GET)
	public String messageContent(@PathVariable("id") long id,
			HttpServletRequest req) {
		// 根据id获得消息的内容
		Message message = messageService.messageContent(id);
		req.setAttribute("message", message);
		return "wholesaler/messageContent";
	}

	/**
	 * 打开品牌
	 * 
	 * @param session
	 * @param response
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/brandList", method = RequestMethod.GET)
	public String brandList(HttpSession session, HttpServletResponse response) {

		return "wholesaler/brandList";
	}

	/**
	 * 品牌列表
	 * 
	 * @param req
	 * @param dictionary
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/brandList", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy brandList(HttpServletRequest req,
			@ModelAttribute Dictionary dictionary) {

		dictionary.setUserId(Tools.getPCWholesaler(req).getId());
		dictionary.setType(MyConstants.Dictionary.TRADEMARK);
		return dictionaryService.brandList(dictionary);
	}

	/**
	 * 添加品牌
	 * 
	 * @param dictionary
	 * @param req
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/addBrand", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity addBrand(@ModelAttribute Dictionary dictionary,
			HttpServletRequest req) {

		dictionary.setDescribe(MyConstants.Dictionary.TRADEMARK_TEXT);
		dictionary.setType(MyConstants.Dictionary.TRADEMARK);
		dictionary.setUserId(Tools.getPCWholesaler(req).getId());

		dictionaryService.addBrand(dictionary);

		return JsonEntity.getInstance("添加品牌成功");
	}

	/**
	 * 删除品牌
	 * 
	 * @param id
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/deleteBrand/{id}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity deleteBrand(@PathVariable("id") int id, HttpServletRequest req) {
		// 根据id获得对应的供应商id
		Dictionary d = dictionaryService.selectUserId(id);
		// 当前供应商id
		long userId = Tools.getPCWholesaler(req).getId();
		if (d.getUserId() == userId) {
			dictionaryService.deleteBrand(id);
		}
		return JsonEntity.getInstance();
	}

	/**
	 * 打开公司信息
	 * 
	 * @param req
	 * @param company
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/getCompany", method = RequestMethod.GET)
	public String getCompany(HttpServletRequest req,
			@ModelAttribute Company company) {

		// 获得当前登陆用户的Id
		company.setUserId(Tools.getPCWholesaler(req).getId());
		Company compan = companyService.getCompany(company);

		req.setAttribute("compan", compan);
		return "wholesaler/company";
	}

	/**
	 * 修改公司信息请求
	 * 
	 * @param company
	 * @param req
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/updateCompany", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity updateCompany(@ModelAttribute Company company,
			HttpServletRequest req) {
		//
		company.setUserId(Tools.getPCWholesaler(req).getId());
		companyService.updateCompany(company);

		return JsonEntity.getInstance("修改公司信息成功");
	}

	/**
	 * 仓库
	 * 
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/getDepot", method = RequestMethod.GET)
	public String getDepot() {
		return "wholesaler/depot";
	}

	/**
	 * 仓库列表请求
	 * 
	 * @param dictionary
	 * @param req
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/depotList", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy depotList(Dictionary dictionary, HttpServletRequest req) {
		dictionary.setType(Dictionary.DEPOT);
		dictionary.setUserId(Tools.getPCWholesaler(req).getId());
		return dictionaryService.depotList(dictionary);
	}

	/**
	 * /新增仓库请求
	 * 
	 * @param dictionary
	 * @param req
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/addDepot", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity addDepot(@ModelAttribute Dictionary dictionary,
			HttpServletRequest req) {
		dictionary.setUserId(Tools.getPCWholesaler(req).getId());
		dictionary.setType(Dictionary.DEPOT);
		dictionary.setExist(true);
		dictionaryService.addDepot(dictionary);

		return JsonEntity.getInstance("新增仓库成功");
	}

	/**
	 * 打开修改仓库页面
	 * 
	 * @param id
	 * @return
	 */

	@WholesalerLogin
	@RequestMapping(value = "/depot/{id}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity depot(@PathVariable("id") long id) {

		// 显示某仓库的信息
		return JsonEntity.getInstance(dictionaryService.depot(id));
	}

	/**
	 * 修改仓库请求
	 * 
	 * @param dictionary
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/updateDepot", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity updateDepot(@ModelAttribute Dictionary dictionary) {
		dictionaryService.updateDepot(dictionary);
		return JsonEntity.getInstance("仓库修改成功");
	}

	/**
	 * 删除仓库
	 * 
	 * @param id
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/deleteDepot/{id}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity deleteDepot(@PathVariable("id") long id, HttpServletRequest req) {
		// 该仓库的对应的供应商id
		Dictionary d = dictionaryService.selectUserId(id);
		// 当前供应商id
		long userId = Tools.getPCWholesaler(req).getId();
		// 如果一致进行删除
		if (d.getUserId() == userId) {
			// 删除
			dictionaryService.deleteDepot(id);
		} else {
			throw new XException("该仓库不属于本供应商所有，您不能删除");
		}
		// 根据id查询该仓库对应的供应商
		return JsonEntity.getInstance("删除仓库成功");
	}

	/***
	 * 库存
	 * 
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/getQuantity", method = RequestMethod.GET)
	public String getQuantity() {

		return "wholesaler/quantity";
	}

	/**
	 * 设置库存
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@WholesalerLogin
	@RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity updateQuantity(@ModelAttribute User user, HttpServletRequest req) {

		User wholesaler = Tools.getPCWholesaler(req);

		user.setId(wholesaler.getId());

		userService.updateQuantity(user);

		Tools.setPCWholesaler(req.getSession(),
				userService.getByToken(wholesaler.getToken()));

		return JsonEntity.getInstance("修改库存成功");
	}

	/**
	 * 导出商品
	 * 
	 * @param response
	 * @param request
	 * @param goods
	 */
	@WholesalerLogin
	@RequestMapping("/outGoodsListXlsx")
	public void outGoodsListXlsx(HttpServletResponse response,
			HttpServletRequest request, @ModelAttribute GoodsQuery goods)
			throws IOException {
		goods.setWholesalerId(Tools.getPCWholesaler(request).getId());
		goods.setOrder("id desc");
		List<GoodsQuery> list = goodsService.allData(goods);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet();
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("商品名");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("库存");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("售出");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("批货价");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("市场价");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("尺码");
		cell.setCellStyle(style);

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			GoodsQuery goods1 = list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(goods1.getGoodsName());
			row.createCell(1).setCellValue(goods1.getQuality() + "");
			row.createCell(2).setCellValue(goods1.getSalesVolume());
			row.createCell(3).setCellValue(goods1.getPrice() + "");
			row.createCell(4).setCellValue(goods1.getMarketPrice() + "");
			row.createCell(5).setCellValue(goods1.getSizes().toString());

			// cell = row.createCell( 8);
			/*
			 * cell.setCellValue(new SimpleDateFormat("yyyy-mm-dd").format(stu
			 * .getBirth()));
			 */
		}
		for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
			sheet.setColumnWidth(i, 30 * 256);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		String filename = new String(
				(sdf.format(new Date()) + "-商品列表.xls").getBytes("gbk"),
				"iso-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ filename);
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();

	}

	/**
	 * 导出订单
	 * 
	 * @param response
	 * @param request
	 * @param order
	 */
	@WholesalerLogin
	@RequestMapping("/outOrderListXlsx")
	public void outOrderListXlsx(HttpServletResponse response,
			HttpServletRequest request, @ModelAttribute OrderQuery order)
			throws IOException {
		order.setWholesalerId(Tools.getPCWholesaler(request).getId());
		order.setOrder("id desc");
		List<OrderQuery> list = orderService.allData(order);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet();
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("订单编号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("商品编号");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("日期");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("总价");
		cell.setCellStyle(style);

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			OrderQuery order1 = list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(order1.getOrderNo());
			row.createCell(1).setCellValue(order1.getGoodsid());
			row.createCell(2).setCellValue(order1.getCreateTime());
			row.createCell(3).setCellValue(order1.getMoney());

			// cell = row.createCell( 8);
			/*
			 * cell.setCellValue(new SimpleDateFormat("yyyy-mm-dd").format(stu
			 * .getBirth()));
			 */
		}
		for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
			sheet.setColumnWidth(i, 30 * 256);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		String filename = new String(
				(sdf.format(new Date()) + "-订单列表.xls").getBytes("gbk"),
				"iso-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ filename);
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();

	}
	/**
	 * 导出订单详情的商品列表
	 * 
	 * @param response
	 * @param request
	 * @param order
	 */
	@WholesalerLogin
	@RequestMapping("/outOrderByGoods/{orderId}")
	public void outOrderByGoods(HttpServletResponse response,
			HttpServletRequest request, @ModelAttribute GoodsQuery goods,
			@PathVariable("orderId") long orderId)
			throws IOException {
		goods.setWholesalerId(Tools.getPCWholesaler(request).getId());
		goods.setOrder("id desc");
		goods.setOrderId(orderId);
		//订单下的商品列表
		List<GoodsQuery> list = goodsService.orderGoods(goods);
		//订单基本信息
		Order order = orderService.getOrder(orderId);
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet();
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("订单编号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("订单金额");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("付款状态");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("订单归属");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("下单日期");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("订单状态");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("商品编号");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("商品名");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("颜色");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("数量");
		cell.setCellStyle(style);
		cell = row.createCell(10);
		cell.setCellValue("尺寸");
		cell.setCellStyle(style);
		cell = row.createCell(11);
		cell.setCellValue("单价");
		cell.setCellStyle(style);
		cell = row.createCell(12);
		cell.setCellValue("总价");
		cell.setCellStyle(style);
		

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			GoodsQuery goods1 = list.get(i);
			//单价
			Double price = goods1.getPrice();
			//数量
			long quantity = goods1.getQuantity();
			//总价
			long money = (long) (price*quantity);
			//状态
			String status = null ;
			if(order.getStatus()==0){
				status = "未付款";
			}else if(order.getStatus()==5){
				status = "已发货";
			}
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(order.getOrderNo());
			row.createCell(1).setCellValue(order.getMoney());
			row.createCell(2).setCellValue(status);
			row.createCell(3).setCellValue(order.getOrderNo());
			row.createCell(4).setCellValue(order.getCreateTime());
			row.createCell(5).setCellValue(status);
			row.createCell(6).setCellValue(goods1.getSpu());
			row.createCell(7).setCellValue(goods1.getGoodsName());
			row.createCell(8).setCellValue(goods1.getColors()+"");
			row.createCell(9).setCellValue(goods1.getQuantity());
			row.createCell(10).setCellValue(goods1.getSizes()+"");
			row.createCell(11).setCellValue(goods1.getPrice());
			row.createCell(12).setCellValue(money);
			

		}
		for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
			sheet.setColumnWidth(i, 30 * 256);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		String filename = new String(
				(sdf.format(new Date()) + "-商品列表.xls").getBytes("gbk"),
				"iso-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ filename);
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();

	}
}
