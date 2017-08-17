package com.sirius.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sirius.entity.query.GoodsQuery;
import com.sirius.po.BTEntitiy;
import com.sirius.service.GoodsService;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Resource
	private GoodsService goodsService;

	@RequestMapping(value = "/findByPage", method = RequestMethod.GET)
	public String findByPage() {
		return "goods/findByPage";
	}

	@RequestMapping(value = "/findByPage", method = RequestMethod.POST)
	public @ResponseBody
	BTEntitiy findByPage_(@ModelAttribute GoodsQuery goods) {
		return goodsService.findByPage(goods);
	}

}
