package com.sirius.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.User;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.UserMapper;

@Service
public class UserService {

	@Resource
	private UserMapper userMapper;

	//获得该用户的唯一验证
	public User getByToken(String token) {
		return userMapper.getByToken(token);
	}
	
	// 设置库存
	public void updateQuantity(User user) {
		if (user.getEarlyMax() == null) {
			throw new XException("最大库存缺失");
		}
		if (user.getEarlyWarning() == null) {
			throw new XException("最小库存缺失");
		}
		if (user.getId() == null) {
			throw new XException("个人信息缺失");
		}
		userMapper.updateQuantity(user);
	}
}
