package com.sirius.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.sirius.entity.User;
import com.sirius.entity.UserDetailed;
import com.sirius.entity.UserShop;
import com.sirius.entity.query.UserQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.PlatformMapper;
import com.sirius.mybatis.mapper.UserMapper;
import com.sirius.po.BTEntitiy;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class PlatformService {

	@Resource
	private UserMapper userMapper;
	
	@Resource
	private PlatformMapper platformMapper;
	
	private static final Map<String, Object> phoneCode = new HashMap<String, Object>();

	/**
	 * 密码登录
	 * 
	 * @param wholesaler
	 * @return
	 */
	public User login(User platform) {
		if (!StringUtil.phoneCheck(platform.getPhone())) {
			throw new XException("手机号格式错误");
		}
		User dbdata = userMapper.getByPhone(platform.getPhone());
		if (dbdata == null) {
			throw new XException("此手机号并未注册成为供应商！");
		} else if (dbdata.getPassword().equals(
				DigestUtils.md5Hex(platform.getPassword()))) {
			dbdata.setPassword(null);
			return dbdata;
		} else {
			throw new XException("密码错误！");
		}
	}
	
	/***
	 * 实体店列表
	 * @param shopkeeper
	 * @return
	 */
	public BTEntitiy platformList(UserQuery shopkeeper){
		List<UserQuery> rows = platformMapper.shopkeeperList(shopkeeper);
		int total = platformMapper.shopkeeperContent(shopkeeper);
		return new BTEntitiy(total, rows);
	}
	
	/*****
	 * 新增实体店
	 * @param shopkeeoer
	 */
	public void insertShopkeeper(UserQuery shopkeeper){
		shopkeeper.setUserName("用户名");
		shopkeeper.setPassword("123456");;
		shopkeeper.setToken(StringUtil.getUuid().substring(0,10));
		shopkeeper.setName("杨士义");
		shopkeeper.setPhone("123");
		shopkeeper.setPosition("CEO");
		shopkeeper.setQq("qq");
		shopkeeper.setWechat("微信");
		shopkeeper.setEmail("Email");
		shopkeeper.setType(1);
		//新增实体店主的个人信息
		platformMapper.insertShopkeeperContent(shopkeeper);
		
		UserShop userShop = new UserShop();
		userShop.setUserId(shopkeeper.getId());
		userShop.setShopName("麻利布");
		userShop.setLevel(0);
		userShop.setSalesmanId(1);
		userShop.setConventionTime("2017.07.12");
		userShop.setProvinceCode(1);
		userShop.setCityCode(1);
		userShop.setAreaCode(1);
		userShop.setRunStyle(1);
		userShop.setDetailedAddress("河南濮阳");
		userShop.setArea("20");
		//新增实体店的基本信息
		platformMapper.insertShopkeeper(userShop);
	}
	
	/***
	 * 修改实体店
	 * @param shopkeeper
	 */
	public void updateShopkeeper(User shopkeeper){
		long id=12;
		shopkeeper.setId(id);
		shopkeeper.setUserName("用户名1");
		shopkeeper.setPassword("123456777");;
		//shopkeeper.setToken(StringUtil.getUuid().substring(0,10));
		shopkeeper.setName("杨士义1");
		shopkeeper.setPhone("123456777");
		shopkeeper.setPosition("CEO");
		shopkeeper.setQq("qq");
		shopkeeper.setWechat("微信");
		shopkeeper.setEmail("Email");
		//shopkeeper.setType(1);
		//修改实体店主个人信息
		platformMapper.updateShopkeeperContent(shopkeeper);
		
		UserShop userShop = new UserShop();
		userShop.setUserId(id);
		userShop.setShopName("麻利布");
		userShop.setLevel(0);
		userShop.setSalesmanId(1);
		userShop.setConventionTime("2017");
		userShop.setProvinceCode(1);
		userShop.setCityCode(1);
		userShop.setAreaCode(1);
		userShop.setRunStyle(1);
		userShop.setDetailedAddress("河南濮阳");
		userShop.setArea("30");
		//修改实体店的基本信息
		platformMapper.updateShopkeeper(userShop);
	}
	
	/***
	 * 实体店详情
	 * @param shopkeeper
	 * @return
	 */
	public UserQuery shopkeeperContent(UserQuery shopkeeper){
		//查询实体店信息
		UserQuery userQuery = platformMapper.getshopkeeperContent(shopkeeper);
		//查询实体店铺信息
		userQuery.setUserShop(platformMapper.getshopkeeper(shopkeeper));
		return userQuery;
	}
	/*-------------------------------------------------*/
	
	/**
	 * 供应商列表
	 */
	public BTEntitiy wholesalerList(UserQuery wholesaler){
		List<UserQuery> rows = platformMapper.wholesalerList(wholesaler);
		int total = platformMapper.wholesalerContent(wholesaler);
		return new BTEntitiy(total, rows);
	}
	
	/*****
	 * 新增供应商
	 * @param shopkeeoer
	 */
	public void insertWholesaler(UserQuery wholesaler){
		wholesaler.setUserName("用户名");
		wholesaler.setPassword("15063514910");;
		wholesaler.setToken(StringUtil.getUuid().substring(0,10));
		wholesaler.setName("供货商");
		wholesaler.setPhone("15063514910");
		wholesaler.setPosition("CEO");
		wholesaler.setQq("qq");
		wholesaler.setWechat("微信");
		wholesaler.setEmail("Email");
		wholesaler.setType(0);
		//新增供货商的个人信息
		platformMapper.insertWholesalerContent(wholesaler);
		
		UserShop userShop = new UserShop();
		userShop.setUserId(wholesaler.getId());
		userShop.setShopName("麻利布");
		userShop.setLevel(0);
		userShop.setSalesmanId(1);
		userShop.setConventionTime("2017.07.12");
		userShop.setProvinceCode(1);
		userShop.setCityCode(1);
		userShop.setAreaCode(1);
		userShop.setRunStyle(1);
		userShop.setDetailedAddress("河南濮阳");
		userShop.setArea("20");
		//新增供货商的基本信息
		platformMapper.insertWholesaler(userShop);
	}
	
	/***
	 * 修改供货商
	 * @param shopkeeper
	 */
	public void updateWholesaler(User wholesaler){
		long id=11;
		wholesaler.setId(id);
		wholesaler.setUserName("150");
		wholesaler.setPassword("150");;
		//wholesaler.setToken(StringUtil.getUuid().substring(0,10));
		wholesaler.setName("杨士义1");
		wholesaler.setPhone("150");
		wholesaler.setPosition("CEO");
		wholesaler.setQq("qq");
		wholesaler.setWechat("微信");
		wholesaler.setEmail("Email");
		//shopkeeper.setType(1);
		//修改供货商个人信息
		platformMapper.updateWholesalerContent(wholesaler);
		
		UserShop userShop = new UserShop();
		userShop.setUserId(id);
		userShop.setShopName("麻利布");
		userShop.setLevel(0);
		userShop.setSalesmanId(1);
		userShop.setConventionTime("2017");
		userShop.setProvinceCode(1);
		userShop.setCityCode(1);
		userShop.setAreaCode(1);
		userShop.setRunStyle(1);
		userShop.setDetailedAddress("河南濮阳");
		userShop.setArea("30");
		//修改供货商的基本信息
		platformMapper.updateShopkeeper(userShop);
	}
	
	/***
	 * 供应商详情
	 * @param shopkeeper
	 * @return
	 */
	public UserQuery wholesalerContent(UserQuery wholesaler){
		//查询供应商信息
		UserQuery userQuery = platformMapper.getwholesalerContent(wholesaler);
		//查询供应商店铺信息
		userQuery.setUserShop(platformMapper.getwholesaler(wholesaler));
		return userQuery;
	}
	/*--------------------------------------------*/
	/**
	 * 会员顾客列表
	 */
	public BTEntitiy customerList(UserQuery customer){
		List<UserQuery> rows = platformMapper.customerList(customer);
		int total = platformMapper.customerCount(customer);
		return new BTEntitiy(total, rows);
	}
	
	/**
	 * 会员顾客的详细信息
	 * @param customer
	 * @return
	 */
	public UserQuery customerContent(UserQuery customer ){
		UserQuery userQuery = platformMapper.customerContent(customer);
		return userQuery;
	}
	/*------------------------------------*/
	
	/**
	 * 买手列表
	 * @param buyPeople
	 * @return
	 */
	public BTEntitiy buyPeopleList(UserQuery buyPeople){
		List<UserQuery> rows = platformMapper.buyPeopleList(buyPeople);
		int total = platformMapper.buyPeopleCount(buyPeople);
		return new BTEntitiy(total, rows);
	}
	
	/***
	 * 新增买手
	 */
	public void insertBuyPeople(UserQuery buyPeople){
		
		//新增买手的基本信息
		buyPeople.setToken(StringUtil.getUuid().substring(0,10));
		buyPeople.setType(2);
		buyPeople.setUserName("1");
		buyPeople.setPassword("1");
		buyPeople.setName("1");
		buyPeople.setPhone("1");
		buyPeople.setQq("1");
		buyPeople.setWechat("1");
		platformMapper.insertBuyPeople(buyPeople);
		
		//新增买手的详细信息
		UserDetailed u = new UserDetailed();
		u.setUserId(buyPeople.getId());
		u.setLevelCustomer(1);
		platformMapper.insertBuyPeopleContent(u);
	}
}
