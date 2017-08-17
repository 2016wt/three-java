package com.sirius.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.alibaba.fastjson.JSONObject;
import com.sirius.exception.NotloginException;
import com.sirius.exception.XException;
import com.sirius.po.JsonEntity;

public class HandlerExceptionResolver extends AbstractHandlerExceptionResolver {

	@Override
	protected final ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {

		JsonEntity jsonEntity = JsonEntity.getError();
		if (exception instanceof NotloginException) {
			jsonEntity.setCode(0);
			jsonEntity.setMsg("未登录");
		} else if (exception instanceof XException)
			jsonEntity.setMsg(exception.getMessage());
		else {
			jsonEntity.setMsg("软件出错，请联系运维人员！");
			exception.printStackTrace();
		}

		if (handler instanceof HandlerMethod) {
			ResponseBody responseBody = ((HandlerMethod) handler)
					.getMethodAnnotation(ResponseBody.class);
			if (responseBody != null) {
				String json = JSONObject.toJSONString(jsonEntity);
				logger.debug(json);
				return new ModelAndView("error/jsonError", "data", json);
			} else {
				logger.debug(jsonEntity.getMsg());
				return new ModelAndView("error/htmlError", "error", jsonEntity);
			}
		} else {
			return new ModelAndView("error/htmlError", "error", jsonEntity);
		}

	}

}
