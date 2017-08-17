package com.sirius.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sirius.po.JsonEntity;
import com.sirius.service.AddressService;

@Controller
@RequestMapping("/address")
public class AddressController {

	@Resource
	private AddressService addressService;

	@RequestMapping("/provinceList")
	public @ResponseBody
	JsonEntity provinceList() {
		return JsonEntity.getInstance(addressService.provinceList());
	}
	
	@RequestMapping("/cityList/{provinceCode}")
	public @ResponseBody
	JsonEntity cityList(@PathVariable("provinceCode") String provinceCode) {
		return JsonEntity.getInstance(addressService.cityList(provinceCode));
	}

	@RequestMapping("/areaList/{cityCode}")
	public @ResponseBody
	JsonEntity areaList(@PathVariable("cityCode") String cityCode) {
		return JsonEntity.getInstance(addressService.areaList(cityCode));
	}
	
	/**
	 * 根据省code获得省name
	 */
	@RequestMapping("/provinceList/{code}")
	public @ResponseBody
	JsonEntity provinceList(@PathVariable("code") String code) {
		return JsonEntity.getInstance(addressService.provinceName(code));
	}
}
