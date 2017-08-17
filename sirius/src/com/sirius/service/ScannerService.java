package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.sirius.entity.User;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.entity.query.UserQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.ScannerMapper;
import com.sirius.mybatis.mapper.UserMapper;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class ScannerService {

	@Resource
	private UserMapper userMapper;
	
	@Resource
	private ScannerMapper scannerMapper;

	/***
	 * 登录
	 * @param user
	 * @return
	 */
	public User login(User user) {
		User dbdata = userMapper.getByPhone(user.getPhone());
		return dbdata;
	}
	
	/****
	 * 根据token查到总金额
	 * @param token
	 * @return
	 */
	public String getByToken(String token,int statusOrderContent){
		return scannerMapper.getByToken(token,statusOrderContent);
	}
	
	/****
	 * 根据token查到付款日期
	 * @param token
	 * @return
	 */
	public List<String> timeByToken(String token){
		return scannerMapper.timeByToken(token);
	}
	
	/****
	 * 根据日期查询销售的金额和数量
	 * @param token
	 * @return
	 */
	public OrderContentQuery getByPayTime(List<String> date,String token){
		if(date.size()==0){
			date=null;
		}
			OrderContentQuery orderQuery = scannerMapper.getByPayTime(date,token);
			return orderQuery;
	}
}
