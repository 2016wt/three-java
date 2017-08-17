package com.sirius.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sirius.annotation.PlatformLogin;
import com.sirius.annotation.Scanner;
import com.sirius.annotation.ShopkeeperApp;
import com.sirius.annotation.Token;
import com.sirius.annotation.WholesalerApp;
import com.sirius.annotation.WholesalerLogin;
import com.sirius.entity.User;
import com.sirius.exception.NotloginException;
import com.sirius.exception.XException;
import com.sirius.service.BuyerService;
import com.sirius.service.GoodsService;
import com.sirius.service.OrderService;
import com.sirius.service.ShopkeeperService;
import com.sirius.service.UserService;
import com.sirius.service.WholesalerService;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;
import com.sirius.util.Tools;

public class Interceptor extends HandlerInterceptorAdapter {

	@Resource
	private WholesalerService wholesalerService;
	@Resource
	private ShopkeeperService shopkeeperService;
	@Resource
	private BuyerService buyerService;

	@Resource
	private UserService userService;

	@Resource
	private GoodsService goodsService;

	@Resource
	private OrderService orderService;

	private static final Logger log = LoggerFactory
			.getLogger(Interceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURI().toString();
		if (url.indexOf(".") < 0 || url.indexOf(".jsp") > 0
				|| url.indexOf(".htm") > 0) {
			log.debug(url);

		}

		if (handler instanceof HandlerMethod) {

			ResponseBody responseBody = ((HandlerMethod) handler)
					.getMethodAnnotation(ResponseBody.class);
			//平台登录
			PlatformLogin platformLogin = ((HandlerMethod) handler)
					.getMethodAnnotation(PlatformLogin.class);
			// 供应商App登录
			WholesalerApp wholesalerApp = ((HandlerMethod) handler)
					.getMethodAnnotation(WholesalerApp.class);
			// 供应商PC登录
			WholesalerLogin wholesalerLogin = ((HandlerMethod) handler)
					.getMethodAnnotation(WholesalerLogin.class);
			// 实体店主登录
			ShopkeeperApp shopkeeperApp = ((HandlerMethod) handler)
					.getMethodAnnotation(ShopkeeperApp.class);
			// 移动端登录（不拦截）
			Token tokens = ((HandlerMethod) handler)
					.getMethodAnnotation(Token.class);

			Scanner scanner = ((HandlerMethod) handler)
					.getMethodAnnotation(Scanner.class);

			// 供应商App登录验证
			if (wholesalerApp != null) {

			}
			// 实体店主登录验证
			else if (shopkeeperApp != null) {
				String token = request.getHeader("token");
				if (StringUtil.isNullOrEmpty(token)) {
					throw new NotloginException();
				}
				User shopkeeper = shopkeeperService.getByToken(token);
				if (shopkeeper == null)
					throw new NotloginException();
				else if (shopkeeper.getType() != MyConstants.UserType.SHOPKEEPER) {
					throw new XException("此身份非实体店主");
				}
				request.setAttribute(Tools.SHOPKEEPER, shopkeeper);
			}
			// 实体店主登录检查，验证
			else if (tokens != null) {
				String token = request.getHeader("token");
				if (!StringUtil.isNullOrEmpty(token)) {
					User shopkeeper = userService.getByToken(token);
					request.setAttribute(Tools.SHOPKEEPER, shopkeeper);
				}
			}
			// 供应商PC端登录验证
			else if (wholesalerLogin != null) {
				User wholesaler = (User) request.getSession().getAttribute(
						Tools.WHOLESALER);
				if (wholesaler == null) {
					if (responseBody == null) {
						response.sendRedirect("/wholesaler/login");
						return false;
					} else
						throw new NotloginException();
				}
				// 查询全部商品数量
				int getAllQuantity = goodsService.getAllQuantity(wholesaler
						.getId());
				// 查询正在销售数量
				int getQuantity = goodsService.getQuantity(wholesaler.getId());
				// 查询所有订单
				int getAllOrder = orderService.getAllOrder(wholesaler.getId());
				request.setAttribute("getAllQuantity", getAllQuantity);
				request.setAttribute("getQuantity", getQuantity);
				request.setAttribute("getAllOrder", getAllOrder);
			} else 	//验证平台
				if(platformLogin !=null){
					User platform = (User) request.getSession().getAttribute(
							Tools.PLATFORM);
					if(platform ==null){
						response.sendRedirect("/platform/login");
						return false;
					}
				} else if (scanner != null) {
				String token = request.getHeader("token");
				if (StringUtil.isNullOrEmpty(token)) {
					throw new NotloginException();
				}
				User user = userService.getByToken(token);
				if (user == null)
					throw new NotloginException();
				else if (user.getType() != MyConstants.UserType.WHOLESALER && user.getType() != MyConstants.UserType.SHOPKEEPER) {
					throw new XException("身份不符！");
				}
				request.setAttribute(Tools.SCANNER, user);

			}

		}

		return true;
	}
}
