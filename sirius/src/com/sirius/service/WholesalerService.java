package com.sirius.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sirius.entity.User;
import com.sirius.entity.query.GoodsQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.GoodsMapper;
import com.sirius.mybatis.mapper.OrderMapper;
import com.sirius.mybatis.mapper.UserMapper;
import com.sirius.po.BTEntitiy;
import com.sirius.submail.SmsUtil;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class WholesalerService {

	private static final Logger logger = LoggerFactory
			.getLogger(WholesalerService.class);

	@Resource
	private UserMapper userMapper;

	@Resource
	private GoodsMapper goodsMapper;

	@Resource
	private OrderMapper orderMapper;

	private static final Map<String, Object> phoneCode = new HashMap<String, Object>();

	public void sendCode(String phone) {

		if (!userMapper.checkPhone(phone)) {
			throw new XException("此手机号并未注册成为供应商！");
		}

		Random random = new Random();
		String code = (random.nextInt(899999) + 100000) + "";
		phoneCode.put(phone, code);
		SmsUtil.sendCode(phone, code);
		logger.debug(code);
	}

	/****
	 * 登陆
	 * 
	 * @param phone
	 * @param code
	 * @return
	 */
	public User login(String phone, String code) {
		if (!StringUtil.phoneCheck(phone)) {
			throw new XException("手机号格式错误");
		}
		if (!code.equals(phoneCode.get(phone))) {
			throw new XException("验证码错误");
		}
		User dbdata = userMapper.getByPhone(phone);
		if (dbdata.getType() != MyConstants.UserType.WHOLESALER) {
			throw new XException("此手机号非供应商身份，无法在此平台登录");
		}
		dbdata.setPassword(null);
		return dbdata;
	}

	public User getByToken(String token) {
		return userMapper.getByToken(token);
	}

	/**
	 * 密码登录
	 * 
	 * @param wholesaler
	 * @return
	 */
	public User login(User wholesaler) {
		if (!StringUtil.phoneCheck(wholesaler.getPhone())) {
			throw new XException("手机号格式错误");
		}
		User dbdata = userMapper.getByPhone(wholesaler.getPhone());
		if (dbdata == null) {
			throw new XException("此手机号并未注册成为供应商！");
		} else if (dbdata.getPassword().equals(
				DigestUtils.md5Hex(wholesaler.getPassword()))) {
			dbdata.setPassword(null);
			return dbdata;
		} else {
			throw new XException("密码错误！");
		}
	}

	public String changePassword(long wholesalerId, String oldpassword,
			String newpassword) {
		User wholesaler = userMapper.getById(wholesalerId);

		if (DigestUtils.md5Hex(oldpassword).equals(wholesaler.getPassword())) {
			String token = StringUtil.getUuid();
			wholesaler.setToken(token);
			wholesaler.setPassword(DigestUtils.md5Hex(newpassword));
			userMapper.changePasswordById(wholesaler);
			return token;
		} else {
			throw new XException("密码错误");
		}
	}

}
