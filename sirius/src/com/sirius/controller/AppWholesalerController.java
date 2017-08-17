package com.sirius.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sirius.annotation.WholesalerApp;
import com.sirius.entity.AndroidApk;
import com.sirius.entity.Goods;
import com.sirius.entity.GoodsSpecification;
import com.sirius.image.ImgUtils;
import com.sirius.po.BaseInfo;
import com.sirius.po.JsonEntity;
import com.sirius.service.AddressService;
import com.sirius.service.AppService;
import com.sirius.service.BuyerService;
import com.sirius.service.DictionaryService;
import com.sirius.service.GoodsService;
import com.sirius.service.OrderService;
import com.sirius.service.WholesalerService;
import com.sirius.util.MyConstants;
import com.sirius.util.Tools;

/**
 * 移动端批发商
 * 
 * @author dohko
 * 
 */
@Controller
@RequestMapping("/app/wholesaler")
public class AppWholesalerController {

	@Resource
	private WholesalerService wholesalerService;

	@Resource
	private GoodsService goodsService;

	@Resource
	private AppService appService;

	@Resource
	private DictionaryService genreService;

	@Resource
	private OrderService orderService;

	@Resource
	private AddressService addressService;

	@Resource
	private BuyerService buyerService;

	@Value("${goods.buyTimeStart}")
	private String buyTimeStart;
	@Value("${goods.buyTimeEnd}")
	private String buyTimeEnd;

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
	 * 密码登录
	 * 
	 * @param wholesaler
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity login(HttpServletRequest request) {
		return null;

	}

	/**
	 * 上传商品
	 * 
	 * @param goods
	 * @param imgBase64
	 * @param request
	 * @return
	 */
	@WholesalerApp
	@RequestMapping(value = "/uploadGoodsInfo", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity uploadGoodsInfo(
			@ModelAttribute Goods goods,
			@RequestParam(value = "imgBase64", required = false) List<String> imgBase64,
			HttpServletRequest request) {

		if (goods.getId() == null) {
			if (imgBase64 == null || imgBase64.size() == 0) {
				return JsonEntity.getError("未上传图片");
			}

			goods.setBuyTimeStart(buyTimeStart);
			goods.setBuyTimeEnd(buyTimeEnd);
			goods.setPrice(goods.getVipPrice() + 5);
			// goods.setPrice(goods.getVipPrice());
			// goods.setClustering(5);
			imgBase64 = ImgUtils.uploadImgFull(imgBase64);
			return JsonEntity.getInstance("商品上传成功",
					goodsService.putGoods(goods, imgBase64).getId());
		} else {
			goods.setPrice(goods.getVipPrice());
			imgBase64 = ImgUtils.uploadImgFull(imgBase64);
			return JsonEntity.getInstance("商品更新成功",
					goodsService.updateGoods(goods, imgBase64).getId());
		}
	}

	/**
	 * 版本更新
	 * 
	 * @param version
	 *            版本号
	 * @return
	 */
	@RequestMapping(value = "/newApk/{version}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity newApk(@PathVariable("version") int version) {
		AndroidApk androidApk = appService.newApk(version, MyConstants.UserType.WHOLESALER);
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
	@WholesalerApp
	@RequestMapping(value = "/goodsList/{putaway}/{markId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity goodsList(@PathVariable("markId") long markId,
			@PathVariable("putaway") boolean putaway, HttpServletRequest request) {
		return JsonEntity.getInstance(goodsService.goodsList(Tools
				.getWholesaler(request).getId(), putaway, markId));

	}

	/**
	 * 单个商品
	 * 
	 * @param goodsId
	 * @param request
	 * @return
	 */
	@WholesalerApp
	@RequestMapping(value = "/goodsInfo/{goodsId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity goodsInfo(@PathVariable("goodsId") long goodsId) {
		return JsonEntity.getInstance(goodsService.goodsInfo(goodsId));
	}

	/**
	 * 上传goods规格信息
	 * 
	 * @param goodsInfoJson
	 *            商品信息列表以json字符串方式传入
	 * @return
	 */
	@WholesalerApp
	@RequestMapping(value = "/putGoodInfo/{goodsId}", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity putGoodInfo(@PathVariable("goodsId") long goodsId,
			@RequestParam("goodsInfoJson") String goodsInfoJson) {
		List<GoodsSpecification> list = JSONObject.parseArray(goodsInfoJson,
				GoodsSpecification.class);
		for (GoodsSpecification goodsInfo : list) {
			goodsInfo.setGoodsId(goodsId);
		}
		goodsService.putGoodInfo(list, goodsId);
		return JsonEntity.getInstance("规格信息录入成功");
	}

	// /**
	// * 分类列表
	// *
	// * @return
	// */
	// @WholesalerApp
	// @RequestMapping("/goodsBase")
	// public @ResponseBody
	// JsonEntity goodsBase(HttpServletRequest request) {
	// Map<String, Object> map = new HashMap<>();
	// map.put("genreList", genreService.genreList());
	// map.put("addressList",
	// addressService.wholesalerAddressList(Tools.getWholesaler(
	// request).getId()));
	// return JsonEntity.getInstance(map);
	// }
	//
	// /**
	// * 商品上架操作
	// *
	// * @param goodsId
	// * @param request
	// * @return
	// */
	// @WholesalerApp
	// @RequestMapping(value = "/putaway/{goodsId}", method =
	// RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity putaway(@PathVariable("goodsId") long goodsId,
	// HttpServletRequest request) {
	//
	// LocalTime time = LocalTime.now();
	// if (time.getHour() > 19) {
	// return JsonEntity.getError("已过7:30，不可上架");
	// } else if (time.getHour() == 19 && time.getMinute() > 30) {
	// return JsonEntity.getError("已过7:30，不可上架");
	// }
	// Goods goods = new Goods();
	// goods.setWholesalerId(Tools.getWholesaler(request).getId());
	// goods.setId(goodsId);
	// goodsService.putaway(goods);
	// return JsonEntity.getInstance("已上架");
	// }
	//
	// /**
	// * 获取商品规格信息
	// *
	// * @param goodsId
	// * @return
	// */
	// @WholesalerApp
	// @RequestMapping(value = "/goodsSpecificationList/{goodsId}", method =
	// RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity goodsSpecification(@PathVariable("goodsId") long goodsId) {
	// return JsonEntity.getInstance(goodsService.goodsSpecification(goodsId));
	// }
	//
	// /**
	// * 供应商查看订单列表
	// *
	// * @param request
	// * @return
	// */
	// @WholesalerApp
	// @RequestMapping("/findByGoods")
	// public @ResponseBody
	// JsonEntity findByGoods(HttpServletRequest request) {
	//
	// WholesalerAppEntity wholesalerAppEntity = Tools.getWholesaler(request);
	//
	// switch (wholesalerAppEntity.getType()) {
	// case 0:
	// return JsonEntity.getInstance(orderService.findByGoods(Tools
	// .getWholesaler(request).getWholesaler().getId()));
	// case 2:
	// break;
	// }
	// return null;
	//
	// }
	//
	// /**
	// * 修改密码
	// *
	// * @param request
	// * @return
	// */
	// @WholesalerApp
	// @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity changePassword(HttpServletRequest request) {
	// String oldpassword = request.getParameter("oldpassword");
	// String newpassword = request.getParameter("newpassword");
	// if (StringUtil.isNullOrEmpty(newpassword)
	// || StringUtil.isNullOrEmpty(oldpassword)) {
	// return JsonEntity.getError("不能为空！");
	// }
	//
	// return JsonEntity.getInstance("修改密码成功！", wholesalerService
	// .changePassword(Tools.getWholesaler(request).getId(),
	// oldpassword, newpassword));
	// }
	//
	// /**
	// * 添加地址信息
	// *
	// * @param request
	// * @param wholesalerAddress
	// * @return
	// */
	// @WholesalerApp
	// @RequestMapping(value = "/addAddress", method = RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity addAddress(HttpServletRequest request,
	// @ModelAttribute WholesalerAddress wholesalerAddress) {
	// wholesalerAddress.setWholesalerId(Tools.getWholesaler(request).getId());
	// addressService.addAddress(wholesalerAddress);
	// return JsonEntity.getInstance("添加地址成功");
	// }
	//
	// /**
	// * 收货地址列表
	// *
	// * @param request
	// * @return
	// */
	// @WholesalerApp
	// @RequestMapping(value = "/addressList", method = RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity addressList(HttpServletRequest request) {
	// return JsonEntity.getInstance(addressService
	// .wholesalerAddressList(Tools.getWholesaler(request).getId()));
	// }
	//
	// /**
	// * 删除地址
	// *
	// * @param addressId
	// * @param request
	// * @return
	// */
	// @WholesalerApp
	// @RequestMapping(value = "/removeAddress/{addressId}", method =
	// RequestMethod.POST)
	// public @ResponseBody
	// JsonEntity removeAddress(@PathVariable("addressId") long addressId,
	// HttpServletRequest request) {
	// WholesalerAddress address = new WholesalerAddress();
	// address.setWholesalerId(Tools.getWholesaler(request).getId());
	// address.setId(addressId);
	// addressService.removeAddress(address);
	// return JsonEntity.getInstance("已删除");
	// }

}
