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

import com.sirius.entity.ShopkeeperFootprint;
import com.sirius.entity.ShoppingcartContent;
import com.sirius.entity.User;
import com.sirius.entity.query.Location;
import com.sirius.entity.query.ShopkeeperFootprintQuery;
import com.sirius.entity.query.ShoppingcartQuery;
import com.sirius.entity.query.UserQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.LocationAreaMapper;
import com.sirius.mybatis.mapper.ShopkeeperFootprintMapper;
import com.sirius.mybatis.mapper.ShoppingcartContentMapper;
import com.sirius.mybatis.mapper.ShoppingcartMapper;
import com.sirius.mybatis.mapper.UserMapper;
import com.sirius.po.BTEntitiy;
import com.sirius.submail.SmsUtil;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class ShopkeeperService {

	private static final Logger logger = LoggerFactory
			.getLogger(ShopkeeperService.class);

	private static final Map<String, Object> phoneCode = new HashMap<String, Object>();

	@Resource
	private UserMapper userMapper;

	@Resource
	private LocationAreaMapper locationAreaMapper;

	@Resource
	private ShoppingcartContentMapper shoppingcartContentMapper;

	@Resource
	private ShopkeeperFootprintMapper shopkeeperFootprintMapper;

	@Resource
	private ShoppingcartMapper shoppingcartMapper;

	public User getByToken(String token) {
		return userMapper.getByToken(token);
	}

	public void sendCode(String phone) {

		Random random = new Random();
		String code = (random.nextInt(899999) + 100000) + "";
		phoneCode.put(phone, code);
		SmsUtil.sendCode(phone, code);
		logger.debug(code);
	}

	public void sendCodeFindPassword(String phone) {
		if (!userMapper.checkPhone(phone)) {
			throw new XException("手机尚未注册");
		}
		sendCode(phone);
	}

	public void sendCodeFindRegister(String phone) {
		if (userMapper.checkPhone(phone)) {
			throw new XException("手机已注册，请直接登录");
		}
		sendCode(phone);
	}

	public User login(String phone, String code) {
		if (!StringUtil.phoneCheck(phone)) {
			throw new XException("手机号格式错误");
		}
		if (!code.equals(phoneCode.get(phone))) {
			throw new XException("验证码错误");
		}
		User dbdata = userMapper.getByPhone(phone);
		if (dbdata == null) {
			throw new XException("该手机号未注册");
		} else if (dbdata.getType() != MyConstants.UserType.SHOPKEEPER) {
			throw new XException("该手机号非实体店主身份,无法在此平台登录");

		}
		dbdata.setPassword(null);
		return dbdata;
	}

	public User login(User shopkeeper) {
		if (!StringUtil.phoneCheck(shopkeeper.getPhone())) {
			throw new XException("手机号格式错误");
		}
		User dbdata = userMapper.getByPhone(shopkeeper.getPhone());
		if (dbdata == null) {
			throw new XException("此手机号并未注册成为店主！");
		} else if (dbdata.getType() != MyConstants.UserType.SHOPKEEPER) {
			throw new XException("该手机号非实体店主身份,无法在此平台登录");
		} else if (dbdata.getPassword().equals(
				DigestUtils.md5Hex(shopkeeper.getPassword()))) {
			dbdata.setPassword(null);
			return dbdata;
		} else {
			throw new XException("密码错误！");
		}
	}

	public String changePassword(long shopkeeperId, String oldpassword,
			String newpassword) {
		User shopkeeper = userMapper.getById(shopkeeperId);

		if (DigestUtils.md5Hex(oldpassword).equals(shopkeeper.getPassword())) {
			String token = StringUtil.getUuid();
			shopkeeper.setToken(token);
			shopkeeper.setPassword(DigestUtils.md5Hex(newpassword));
			if (!userMapper.changePasswordById(shopkeeper)) {
				throw new XException("密码修改失败");
			}
			return token;
		} else {
			throw new XException("密码错误");
		}
	}

	public User changeInfo(User shopkeeper) {
		userMapper.changeInfo(shopkeeper);
		return userMapper.getById(shopkeeper.getId());
	}

	public BTEntitiy findByPage(UserQuery shopkeeper) {
		int total = userMapper.dataCount(shopkeeper);
		List<User> rows = userMapper.data(shopkeeper);
		return new BTEntitiy(total, rows);
	}

	public void create(User shopkeeper) {

		userMapper.insert(shopkeeper);
	}

	public int shoppingcartContentChange(ShoppingcartContent shoppingcartContent) {
		if (shoppingcartContent.getAmount() == -1) {
			ShoppingcartContent db = shoppingcartContentMapper
					.getBaseById(shoppingcartContent.getId());
			if (db.getAmount() <= 0) {
				throw new XException("不可以再减！");
			}
			shoppingcartContentMapper
					.shoppingcartContentChange(shoppingcartContent);
		} else if (shoppingcartContent.getAmount() == 1) {
			shoppingcartContentMapper
					.shoppingcartContentChange(shoppingcartContent);
		} else {
			throw new XException("修改信息有误！");
		}
		ShoppingcartContent db = shoppingcartContentMapper
				.getBaseById(shoppingcartContent.getId());

		return db.getAmount();

	}

	public List<ShopkeeperFootprintQuery> footprintMapperList(
			long shopkeeperId, int size, long markId) {
		if (markId == 0) {
			return shopkeeperFootprintMapper.getList(shopkeeperId, size);
		} else {
			return shopkeeperFootprintMapper.getListMark(shopkeeperId, size,
					markId);
		}
	}

	public void addFootprint(ShopkeeperFootprint shopkeeperFootprint) {
		if (shopkeeperFootprintMapper.checkHad(shopkeeperFootprint)) {
			shopkeeperFootprintMapper.delete(shopkeeperFootprint);
		}
		shopkeeperFootprintMapper.insert(shopkeeperFootprint);
	}

	public void checkCode(String phone, String code) {
		if (!code.equals(phoneCode.get(phone))) {
			throw new XException("验证码错误");
		}
	}

	public User register(UserQuery shopkeeper) {
		User user = userMapper.getByPhone(shopkeeper.getPhone());

		if (user == null) {

		} else if (user.getType() == MyConstants.UserType.SHOPKEEPER) {
			throw new XException("此手机号已注册,请直接登录");
		} else {
			throw new XException("此手机号已有其他身份,无法注册实体店主");
		}

		if (!shopkeeper.getCode().equals(phoneCode.get(shopkeeper.getPhone()))) {
			throw new XException("验证码错误");
		}
		shopkeeper.setUserName(shopkeeper.getPhone());
		shopkeeper.setPassword(DigestUtils.md5Hex(shopkeeper.getPassword()));
		shopkeeper.setToken(StringUtil.getUuid());
		userMapper.insert(shopkeeper);
		return userMapper.getByToken(shopkeeper.getToken());

	}

	public User codeChangePassword(UserQuery shopkeeper) {
		User user = userMapper.getByPhone(shopkeeper.getPhone());
		if (user == null) {
			throw new XException("手机号未注册");
		} else if (user.getType() != MyConstants.UserType.SHOPKEEPER) {
			throw new XException("此手机号非实体店主,无法在此平台修改密码");
		}
		if (!shopkeeper.getCode().equals(phoneCode.get(shopkeeper.getPhone()))) {
			throw new XException("验证码错误");
		}
		shopkeeper.setPassword(DigestUtils.md5Hex(shopkeeper.getPassword()));
		shopkeeper.setToken(StringUtil.getUuid());
		shopkeeper.setId(user.getId());
		userMapper.changeInfo(shopkeeper);
		return userMapper.getById(shopkeeper.getId());
	}

	public List<ShoppingcartQuery> shoppingcartContentDelete(
			long shoppingcartContentId, long shopkeeperId) {
		if (!shoppingcartContentMapper.contentDelete(shoppingcartContentId)) {
			throw new XException("无法删除");
		}
		return shoppingcartMapper.shoppingcartByGoods(shopkeeperId);
	}

}
