package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.Shoppingcart;
import com.sirius.entity.ShoppingcartContent;
import com.sirius.entity.query.ShoppingcartContentQuery;
import com.sirius.entity.query.ShoppingcartQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.ShoppingcartContentMapper;
import com.sirius.mybatis.mapper.ShoppingcartMapper;

@Service
public class ShoppingcartService {

	@Resource
	private ShoppingcartMapper shoppingcartMapper;

	@Resource
	private ShoppingcartContentMapper shoppingcartContentMapper;

	public void toShoppingcart(List<ShoppingcartContent> list, long goodsId,
			long shopkeeperId) {

		Shoppingcart shoppingcart = shoppingcartMapper.getByGoodsAndShopkeeper(
				goodsId, shopkeeperId);

		if (shoppingcart == null) {
			shoppingcart = new Shoppingcart();
			shoppingcart.setGoodsId(goodsId);
			shoppingcart.setShopkeeperId(shopkeeperId);
			shoppingcartMapper.insert(shoppingcart);
		}

		for (ShoppingcartContent shoppingcartContent : list) {

			shoppingcartContent.setShoppingcartId(shoppingcart.getId());

			// 检测是否有此购物记录
			if (shoppingcartContentMapper.checkHaving(shoppingcartContent)) {
				// 有的话直接数量相加
				shoppingcartContentMapper.amountPlus(shoppingcartContent);
			} else {
				// 没有的话再插入
				shoppingcartContentMapper.insert(shoppingcartContent);
			}
		}
	}

	public List<ShoppingcartQuery> shoppingcartByGoods(long shopkeeperId) {

		return shoppingcartMapper.shoppingcartByGoods(shopkeeperId);
	}

	public List<ShoppingcartQuery> shoppingcarts(List<Long> shoppingcartIds) {
		List<ShoppingcartQuery> list = shoppingcartMapper
				.shoppingcarts(shoppingcartIds);
		if (list.size() != shoppingcartIds.size()) {
			throw new XException("进货车商品失效，请刷新");
		}
		return list;
	}

	public List<ShoppingcartContentQuery> shoppingcartContents(
			List<Long> shoppingcartContentIds) {
		List<ShoppingcartContentQuery> list = shoppingcartContentMapper
				.shoppingcartContents(shoppingcartContentIds);

		if (list.size() != shoppingcartContentIds.size()) {
			throw new XException("进货车商品失效，请刷新");
		}
		return list;

	}

	public double moneyByCarts(List<Long> shoppingcartContentIds) {
		return shoppingcartContentMapper.moneyByIds(shoppingcartContentIds);
	}

	public void clearAll() {
		shoppingcartMapper.clearAll();
		shoppingcartContentMapper.clearAll();
	}

	public List<ShoppingcartQuery> clearLoseEfficacy(long shopkeeperId) {

		shoppingcartContentMapper.clearLoseEfficacy(shopkeeperId);
		return shoppingcartMapper.shoppingcartByGoods(shopkeeperId);
	}

}
