package com.sirius.mybatis.mapper;

import java.util.List;

import com.sirius.entity.User;
import com.sirius.entity.UserDetailed;
import com.sirius.entity.UserShop;
import com.sirius.entity.query.UserQuery;

public interface PlatformMapper {

	//实体店列表
	List<UserQuery> shopkeeperList(UserQuery shopkeeper);
	int shopkeeperContent(User shopkeeper);

	//新增实体店主的个人信息
	void insertShopkeeperContent(UserQuery shopkeeper);
	//新增实体店
	void insertShopkeeper(UserShop userShop);
	
	//修改实体店主个人信息
	void updateShopkeeperContent(User shopkeeper);
	//修改实体店铺的信息
	void updateShopkeeper(UserShop userShop);
	
	//查询实体店的详情
	UserQuery getshopkeeperContent(UserQuery shopkeeper);
	//查询实体店铺信息
	UserShop getshopkeeper(UserQuery shopkeeper);
	
	/**
	 * 供应商列表
	 */
	List<UserQuery> wholesalerList(UserQuery wholesaler);
	int wholesalerContent(UserQuery wholesaler);
	
	//新增供货商的个人信息
	void insertWholesalerContent(UserQuery wholesaler);
	//新增供货商
	void insertWholesaler(UserShop userShop);
	
	//修改供货商个人信息
	void updateWholesalerContent(User wholesaler);
	//修改供货商店铺的信息
	void updateWholesaler(UserShop wholesaler);
	
	//查询供货商的详情
	UserQuery getwholesalerContent(UserQuery wholesaler);
	//查询供货商店铺信息
	UserShop getwholesaler(UserQuery wholesaler);
	
	/**
	 * 会员顾客列表
	 */
	List<UserQuery> customerList(UserQuery customer);
	int customerCount(UserQuery customer);
	
	//会员顾客的详情信息
	UserQuery customerContent(UserQuery customer);
	
	/***
	 * 买手列表
	 */
	List<UserQuery> buyPeopleList(UserQuery buyPeople);
	int buyPeopleCount(UserQuery buyPeople);
	
	//新增买手的基本信息
	void insertBuyPeople(UserQuery userQuery);
	//新增买手的详细信息
	void insertBuyPeopleContent(UserDetailed userDetailed);
}

