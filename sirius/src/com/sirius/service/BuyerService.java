package com.sirius.service;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.sirius.entity.User;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.UserMapper;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class BuyerService {

	@Resource
	private UserMapper userMapper;

	public User login(User user) {
		if (!StringUtil.phoneCheck(user.getPhone())) {
			throw new XException("手机号格式错误");
		}
		User dbdata = userMapper.getByPhone(user.getPhone());
		if (dbdata == null) {
			throw new XException("此手机号并未注册成为买手！");
		} else if (dbdata.getType() != MyConstants.UserType.BUYER) {
			throw new XException("此手机号非买手身份，无法在此平台登录！");
		} else if (dbdata.getPassword().equals(
				DigestUtils.md5Hex(user.getPassword()))) {
			dbdata.setPassword(null);
			return dbdata;
		} else {
			throw new XException("密码错误！");
		}
	}

}
