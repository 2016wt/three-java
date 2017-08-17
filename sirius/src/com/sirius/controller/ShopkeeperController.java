package com.sirius.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sirius.entity.AndroidApk;
import com.sirius.entity.User;
import com.sirius.entity.query.UserQuery;
import com.sirius.po.BTEntitiy;
import com.sirius.po.JsonEntity;
import com.sirius.service.AppService;
import com.sirius.service.OrderService;
import com.sirius.service.PayService;
import com.sirius.service.ShopkeeperService;

@Controller
@RequestMapping("/shopkeeper")
public class ShopkeeperController {

	@Resource
	private ShopkeeperService shopkeeperService;

	@Resource
	private AppService appService;

	@Resource
	private OrderService orderService;

	@Resource
	private PayService payService;

	@RequestMapping(value = "/findByPage", method = RequestMethod.GET)
	public String findByPage() {
		return "shopkeeper/findByPage";
	}

	@RequestMapping(value = "/findByPage", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy findByPage_(@ModelAttribute UserQuery shopkeeper) {
		return shopkeeperService.findByPage(shopkeeper);
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
		AndroidApk androidApk = appService.newApk(version, 1);
		if (androidApk == null)
			return JsonEntity.getError();
		else
			return JsonEntity.getInstance(androidApk);
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "shopkeeper/shopkeeperCreate";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity create_(@ModelAttribute User shopkeeper) {
		shopkeeperService.create(shopkeeper);
		return JsonEntity.getInstance("录入成功");
	}

}
