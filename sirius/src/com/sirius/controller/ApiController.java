package com.sirius.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sirius.entity.Api;
import com.sirius.image.ImgUtils;
import com.sirius.po.JsonEntity;
import com.sirius.service.ApiService;
import com.sirius.util.HttpUtil;
import com.sirius.util.StringUtil;

@Controller
@RequestMapping("/api")
public class ApiController {

	@Resource
	private ApiService apiService;

	@RequestMapping(value = "/scanner/edit", method = RequestMethod.GET)
	public String eidt(HttpServletRequest request) {
		request.setAttribute("api", apiService.selectNew());
		return "api/scanner/edit";
	}

	@RequestMapping(value = "/scanner/edit", method = RequestMethod.POST)
	public @ResponseBody
	JsonEntity eidt_(@RequestParam("content") String content)
			throws UnsupportedEncodingException {
		content = content
				.replaceFirst(
						"<head>",
						"<head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
		byte[] bs = content.getBytes("UTF-8");
		String url = ImgUtils.uploadImgFull(bs, StringUtil.getUuid() + ".html");
		Api api = new Api();
		api.setUrl(url);
		apiService.insert(api);
		return JsonEntity.getInstance("修改成功");
	}

	@RequestMapping(value = "/scanner/list", method = RequestMethod.GET)
	public void list(HttpServletResponse response) throws IOException {
		String content = HttpUtil.post(apiService.selectNew().getUrl());
		response.getWriter().write(content);
	}

}
