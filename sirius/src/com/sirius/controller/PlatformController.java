package com.sirius.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sirius.annotation.PlatformLogin;
import com.sirius.entity.Dictionary;
import com.sirius.entity.Goods;
import com.sirius.entity.GoodsImg;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.LocationCity;
import com.sirius.entity.LocationProvince;
import com.sirius.entity.User;
import com.sirius.entity.query.UserQuery;
import com.sirius.po.BTEntitiy;
import com.sirius.po.JsonEntity;
import com.sirius.service.PlatformService;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;
import com.sirius.util.Tools;

@Controller
@RequestMapping("/platform")
public class PlatformController {

	@Resource
	private PlatformService platformService;
	
	/**
	 * 平台登录页面
	 * @param request
	 * @return
	 */
//	@RequestMapping(value = "/pLogin", method = RequestMethod.GET)
//	public String login(HttpServletRequest request) {
//		if (request.getSession().getAttribute("platform") != null) {
//			return "redirect:/platform/shopkeeperList";
//		}
//		return "platform/login";
//	}
	
	/****
	 * 登录
	 * @param platform
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pLogin", method = RequestMethod.GET)
	public @ResponseBody
	JsonEntity pLogin_(@ModelAttribute User platform, HttpSession session,
			HttpServletResponse response) {
		platform = platformService.login(platform);
		session.setAttribute("platform", platform);
		return JsonEntity.getInstance();
	}
	
	/***
	 * 实体店列表
	 * @param platform
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="/pShopkeeperList",method=RequestMethod.GET)
	public @ResponseBody String pShopkeeperList(@ModelAttribute UserQuery shopkeeper,HttpServletRequest req){
		shopkeeper.setType(1);
//		shopkeeper.setUserName("11111111111");
//		shopkeeper.setName("扬士义");
//		shopkeeper.setPhone("11111111111");
		String jsonString = "{\"total\":2,\"rows\":[{\"userName\":\"11111111111\",\"name\":\"扬士义\",\"phone\":\"11111111111\",\"levelShop\":0,\"levelCustomer\":0,\"conventionTime\":\"2017.0\",\"shopid\":0,\"provinceCode\":1,\"cityCode\":1,\"age\":0},{\"userName\":\"用户名1\",\"name\":\"杨士义1\",\"phone\":\"123456777\",\"levelShop\":0,\"levelCustomer\":0,\"conventionTime\":\"2017\",\"shopid\":0,\"provinceCode\":1,\"cityCode\":1,\"age\":0}]}";
		jsonString = "jsonpCallback"+"("+jsonString+")";
		return jsonString;
	}
	//String jsonString = JSONObject.fromObject(map).toString();
	
	/***
	 * 新增实体店 
	 * @param shopkeeper
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="/pInsertShopkeeper",method=RequestMethod.GET)
	public @ResponseBody
	JsonEntity pInsertShopkeeper(@ModelAttribute UserQuery shopkeeper){
		platformService.insertShopkeeper(shopkeeper);
		return JsonEntity.getInstance();
	}
	
	/**
	 * 修改实体店
	 * @param shopkeeper
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="pUpdateShopkeeper",method=RequestMethod.GET)
	public @ResponseBody
	JsonEntity pUpdateShopkeeper(@ModelAttribute UserQuery shopkeeper){
		platformService.updateShopkeeper(shopkeeper);
		return JsonEntity.getInstance();
	}
	
	/****
	 * 打开实体店的详细信息
	 * @param goodsId
	 * @param dictionary
	 * @param req
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value = "/pShopkeeperContent", method = RequestMethod.GET)
	public String pShopkeeperContent(UserQuery shopkeeper,HttpServletRequest req) {
		String phone="18612415766";
		shopkeeper.setPhone(phone);
		//获取实体店主的信息
		UserQuery userQuery = platformService.shopkeeperContent(shopkeeper);
		req.setAttribute("userQuery", userQuery);
		return "platform/login";
	}
	
	/*---------------------------------------------------------*/
	
	/***
	 * 供应商列表
	 * @param wholesaler
	 * @param req
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="/pWholesalerList",method=RequestMethod.GET)
	public @ResponseBody
	BTEntitiy pWholesalerList(@ModelAttribute UserQuery wholesaler,HttpServletRequest req){
		wholesaler.setType(0);
		return platformService.wholesalerList(wholesaler);
	}
	
	/***
	 * 新增供应商
	 * @param shopkeeper
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="/pInsertWholesaler",method=RequestMethod.GET)
	public @ResponseBody
	JsonEntity pInsertWholesaler(@ModelAttribute UserQuery wholesaler){
		platformService.insertWholesaler(wholesaler);
		return JsonEntity.getInstance();
	}
	

	/**
	 * 修改供货商
	 * @param shopkeeper
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="pUpdateWholesaler",method=RequestMethod.GET)
	public @ResponseBody
	JsonEntity pUpdateWholesaler(@ModelAttribute UserQuery wholesaler){
		platformService.updateWholesaler(wholesaler);
		return JsonEntity.getInstance();
	}
	
	/****
	 * 打开供货商的详细信息
	 * @param goodsId
	 * @param dictionary
	 * @param req
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value = "/pWholesalerContent", method = RequestMethod.GET)
	public void pWholesalerContent(UserQuery wholesaler,HttpServletRequest req) {
		String phone="15063514910";
		wholesaler.setPhone(phone);
		//获取实体店主的信息
		UserQuery userQuery = platformService.wholesalerContent(wholesaler);
		req.setAttribute("userQuery", userQuery);
	}
	/*---------------------------------------------------------------*/
	
	/***
	 * 会员顾客列表
	 * @param wholesaler
	 * @param req
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="/pCustomerList",method=RequestMethod.GET)
	public @ResponseBody
	BTEntitiy pCustomerList(@ModelAttribute UserQuery customer,HttpServletRequest req){
		//会员顾客
		customer.setType(4);
		//customer.setPhone("1231");
		return platformService.customerList(customer);
	}
	
	/****
	 * 打开供货商的详细信息
	 * @param goodsId
	 * @param dictionary
	 * @param req
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value = "/customerContent", method = RequestMethod.GET)
	public void customerContent(UserQuery customer,HttpServletRequest req) {
		long id=21;
		customer.setId(id);
		customer.setType(4);
		//获取实体店主的信息
		UserQuery userQuery = platformService.customerContent(customer);
		req.setAttribute("userQuery", userQuery);
	}
	
	/*-------------------------------------------------------------------*/
	
	/***
	 * 买手列表
	 * @param buyPeople
	 * @param req
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="/buyPeopleList",method=RequestMethod.GET)
	public @ResponseBody
	BTEntitiy buyPeopleList(@ModelAttribute UserQuery buyPeople,HttpServletRequest req){
		//会员顾客
		buyPeople.setType(2);
		//buyPeople.setNumber("1111");
		//buyPeople.setName("杨士义");
		return platformService.buyPeopleList(buyPeople);
	}
	/***
	 * 新增买手
	 * @param shopkeeper
	 * @return
	 */
	//@PlatformLogin
	@RequestMapping(value="/insertBuyPeople",method=RequestMethod.GET)
	public @ResponseBody
	JsonEntity insertBuyPeople(@ModelAttribute UserQuery buyPeople){
		platformService.insertBuyPeople(buyPeople);
		return JsonEntity.getInstance();
	}
	
}
