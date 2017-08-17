package com.sirius.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sirius.entity.Information;
import com.sirius.entity.query.InformationQuery;
import com.sirius.image.ImgUtils;
import com.sirius.po.JsonEntity;
import com.sirius.service.InformationService;
import com.sirius.util.StringUtil;

@Controller
@RequestMapping("/information")
public class InformationContruller {

	@Resource
	private InformationService informationService;

	@Value("${common.imgHost}")
	private String imgHost;
	@Value("${common.imgLength}")
	private int imgLength;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity add(@ModelAttribute InformationQuery information)
			throws UnsupportedEncodingException {
		information
				.setContent(information
						.getContent()
						.replaceFirst(
								"<head>",
								"<head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />"));
		String x = information.getContent();
		List<String> imgs = new ArrayList<String>();
		int i = 0;
		while ((i = x.indexOf(imgHost)) > 0) {
			imgs.add(x.substring(i, i + imgLength));
			x = x.substring(i + imgLength);
		}
		information.setImgs(imgs);
		byte[] bs = information.getContent().getBytes("UTF-8");
		String url = ImgUtils.uploadImgFull(bs, StringUtil.getUuid() + ".html");
		information.setContent(url);
		informationService.add(information);
		return JsonEntity.getInstance("保存成功");
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add_() {
		return "ueditor/index";
	}

	@RequestMapping(value = "/add/{informationId}", method = RequestMethod.GET)
	public String editor(@PathVariable("informationId") long informationId,
			HttpServletRequest request) {
		Information information = informationService.getById(informationId);
		request.setAttribute("information", information);
		return "ueditor/index";
	}

}
