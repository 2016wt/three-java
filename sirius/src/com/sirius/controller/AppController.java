package com.sirius.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sirius.entity.AndroidApk;
import com.sirius.image.ImgUtils;
import com.sirius.po.JsonEntity;
import com.sirius.service.AppService;

@Controller
@RequestMapping("/app")
public class AppController {

	@Resource
	private AppService appService;

	/**
	 * 安卓版本列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/apkList")
	public String apkList(HttpServletRequest request) {
		request.setAttribute("APKLIST", appService.apkList());
		return "apkList";
	}

	/**
	 * 上传安卓apk文件
	 * 
	 * @param file
	 * @param androidApk
	 * @return
	 */
	@RequestMapping(value = "/uploadApk", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity uploadApk(
			@RequestParam(value = "file", required = false) String file,
			@RequestParam(value = "suffix", required = false) String suffix,
			@ModelAttribute AndroidApk androidApk) {
		System.out.println(suffix + "\n" + file);
		System.out.println(androidApk.getVersion() + "\n"
				+ androidApk.getConstraint());
		if (file == null) {
			return JsonEntity.getError("file空");
		}
		if (suffix == null) {
			return JsonEntity.getError("suffix空");
		}

		if (!suffix.equalsIgnoreCase(".apk")) {
			return JsonEntity.getError("格式不匹配");
		}

		androidApk.setUrl(ImgUtils.uploadFull(file, suffix));
		androidApk.setSize((int) (file.length() / 1024));
		appService.insert(androidApk);
		return JsonEntity.getInstance("上传成功");
	}

}
