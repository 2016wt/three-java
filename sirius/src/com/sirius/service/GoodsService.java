package com.sirius.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.sirius.entity.Goods;
import com.sirius.entity.GoodsEvaluate;
import com.sirius.entity.GoodsImg;
import com.sirius.entity.GoodsSpecification;
import com.sirius.entity.Order;
import com.sirius.entity.query.GoodsEvaluateQuery;
import com.sirius.entity.query.GoodsQuery;
import com.sirius.entity.query.GoodsSpecificationQuery;
import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.GoodsEvaluateMapper;
import com.sirius.mybatis.mapper.GoodsImgMapper;
import com.sirius.mybatis.mapper.GoodsMapper;
import com.sirius.mybatis.mapper.GoodsSpecificationMapper;
import com.sirius.mybatis.mapper.LocationProvinceMapper;
import com.sirius.mybatis.mapper.OrderContentMapper;
import com.sirius.mybatis.mapper.OrderContentStripeMapper;
import com.sirius.mybatis.mapper.OrderMapper;
import com.sirius.mybatis.mapper.StockMapper;
import com.sirius.po.BTEntitiy;
import com.sirius.po.BaseInfo;
import com.sirius.util.MyConstants;
import com.sirius.util.StringUtil;

@Service
public class GoodsService {

	@Resource
	private GoodsMapper goodsMapper;
	
	@Resource
	private OrderContentStripeMapper orderContentStripeMapper;

	@Resource
	private LocationProvinceMapper locationProvinceMapper;

	@Resource
	private GoodsImgMapper goodsImgMapper;

	@Resource
	private GoodsSpecificationMapper goodsSpecificationMapper;

	@Resource
	private GoodsEvaluateMapper goodsEvaluateMapper;

	@Resource
	private OrderMapper orderMapper;
	
	@Resource
	private OrderContentMapper orderContentMapper;

	@Resource
	private StockMapper stockMapper;

	public Goods putGoods(Goods goods, List<String> imgs) {
		goodsMapper.insert(goods);

		List<GoodsImg> list = new ArrayList<>();

		for (String img : imgs) {
			GoodsImg goodsImg = new GoodsImg();
			goodsImg.setGoodsId(goods.getId());
			goodsImg.setImgUrl(img);
			list.add(goodsImg);
		}

		goodsImgMapper.insertList(list);
		return goods;

	}

	public List<GoodsQuery> goodsList(long wholesalerId, boolean putaway,
			long markId) {

		if (markId == 0)
			return goodsMapper.mygoodsList(wholesalerId, putaway,
					BaseInfo.pageSize);
		else
			return goodsMapper.mygoodsListMark(wholesalerId, putaway,
					BaseInfo.pageSize, markId);

	}

	public GoodsQuery goodsInfo(long goodsId) {
		GoodsQuery goods = goodsMapper.getById(goodsId);

		if (goods == null) {
			throw new XException("商品不存在");
		}
		if (goods.getGoodsEvaluates() != null
				&& goods.getGoodsEvaluates().size() > 0) {
			for (GoodsEvaluateQuery goodsEvaluate : goods.getGoodsEvaluates()) {
				String name = goodsEvaluate.getShopkeeper().getUserName();
				if (name.length() <= 5) {
					name = name.substring(0, 1) + "****"
							+ name.substring(name.length() - 1, name.length());
				} else if (name.length() <= 7) {
					name = name.substring(0, 2) + "****"
							+ name.substring(name.length() - 2, name.length());
				} else if (name.length() <= 9) {
					name = name.substring(0, 3) + "****"
							+ name.substring(name.length() - 3, name.length());
				} else {
					name = name.substring(0, 3) + "****"
							+ name.substring(name.length() - 4, name.length());
				}
				goodsEvaluate.getShopkeeper().setUserName(name);
			}

		}
		return goods;

	}

	public void putGoodInfo(List<GoodsSpecification> list, long goodsId) {
		// goodsSpecificationMapper.clearGoodsInfo(goodsId);
		// goodsSpecificationMapper.insertList(list);

		for (GoodsSpecification goodsSpecification : list) {
			if (goodsSpecificationMapper.exit(goodsSpecification)) {
				goodsSpecificationMapper.editQquantity(goodsSpecification);
			} else {
				goodsSpecificationMapper.insert(goodsSpecification);
			}
		}

	}

	public List<GoodsQuery> goodsList(GoodsQuery goods) {
		if (StringUtil.isNullOrEmpty(goods.getKeyword())) {
			goods.setKeyword(null);
		} else {
			goods.setKeyword("%" + goods.getKeyword().trim() + "%");
		}
		return goodsMapper.data(goods);
	}

	public List<GoodsSpecificationQuery> specificationList(long goodsId) {
		Goods goods = goodsMapper.getBaseById(goodsId);
		if (goods == null) {
			throw new XException("商品未到抢购时间");
		} else if (!goods.getPutaway()) {
			throw new XException("未上架，暂时不能购买");
		}

		return goodsSpecificationMapper.specificationList(goodsId);
	}

	public void goodsSoldOut() {
		goodsMapper.goodsSoldOut();

	}

	public void putaway(Goods goods) {
		if (!goodsMapper.putaway(goods))
			throw new XException("上架失败！");

	}

	public List<GoodsSpecification> goodsSpecification(long goodsId) {
		return goodsSpecificationMapper.getByGoodsId(goodsId);
	}

	public Goods updateGoods(Goods goods, List<String> imgs) {
		if (!goodsMapper.update(goods)) {
			throw new XException("修改失败");
		}

		List<GoodsImg> list = new ArrayList<>();
		if (imgs != null) {
			for (String img : imgs) {
				GoodsImg goodsImg = new GoodsImg();
				goodsImg.setGoodsId(goods.getId());
				goodsImg.setImgUrl(img);
				list.add(goodsImg);
			}
			goodsImgMapper.insertList(list);
		}
		return goods;
	}
	
	/**
	 * 商品上架
	 * @param id
	 */
	public void updatePutaway(Goods goods){
		goodsMapper.updatePutaway(goods);
	}
	
	/**
	 * 删除商品
	 * 
	 * @param id
	 */
	public void deleteGoodsList(Long id) {
		// 删除商品图片
		goodsImgMapper.deleteImg(id);
		// 删除商品的详细信息
		goodsSpecificationMapper.deleteSpecification(id);
		// 删除商品基本信息(标记删除)
		goodsMapper.deleteData(id);
	}

	/***
	 * 获得删除商品对应的供应商的ID
	 * 
	 * @param id
	 */
	public long selectWholesalerId(long id) {
		return goodsMapper.selectWholesalerId(id);
	}

	
	/***
	 * 根据orderId查询商品数据
	 * @param goodsQuery
	 * @return
	 */
	public BTEntitiy getGoodsOrderId(GoodsQuery goodsQuery){
		if(StringUtil.isNullOrEmpty(goodsQuery.getGoodsName())){
			goodsQuery.setGoodsName(null);
		}else {
			goodsQuery.setGoodsName("%" + goodsQuery.getGoodsName() + "%");
		}
		List<GoodsQuery> rows = goodsMapper.getGoodsOrderId(goodsQuery);
		int total = goodsMapper.getGoodsOrderIdContent(goodsQuery);
		
		return new BTEntitiy(total,rows);
	}
	
	/***
	 * 获得orderId对应的商品件数
	 * @param orderId
	 * @return
	 */
	public long getSumQuantity(long orderId){
		
		return goodsMapper.getSumQuantity(orderId);
	}
	
	/**
	 * 商品列表
	 * 
	 * @param goods
	 * @return
	 */
	public BTEntitiy findByPage(GoodsQuery goods) {
		if (StringUtil.isNullOrEmpty(goods.getGoodsName())) {
			goods.setGoodsName(null);
		} else {
			goods.setGoodsName("%" + goods.getGoodsName() + "%");
		}
		if (StringUtil.isNullOrEmpty(goods.getSize())) {
			goods.setSize(null);
		}
		if (StringUtil.isNullOrEmpty(goods.getSpu())) {
			goods.setSpu(null);
		}
		if (StringUtil.isNullOrEmpty(goods.getCreateTimeStart())) {
			goods.setCreateTimeStart(null);
		}
		if (StringUtil.isNullOrEmpty(goods.getCreateTimeEnd())) {
			goods.setCreateTimeEnd(null);
		} else {
			goods.setCreateTimeEnd(goods.getCreateTimeEnd() + " 23:59:59");
		}
		int total = goodsMapper.dataCount(goods);
		List<GoodsQuery> rows = goodsMapper.data(goods);
		return new BTEntitiy(total, rows);
	}

	/**
	 * 需要修改的某个商品信息
	 * 
	 * @param goods
	 * @return
	 */
	public Goods getUpdateGoods(Goods goods) {
		return goodsMapper.getUpdateGoods(goods);
	}

	/**
	 * 商品详细列表
	 * 
	 * @param goods
	 * @return
	 */
	public BTEntitiy goodsContentList(GoodsSpecificationQuery goods) {
		if (StringUtil.isNullOrEmpty(goods.getGoodsName())) {
			goods.setGoodsName(null);
		} else {
			goods.setGoodsName("%" + goods.getGoodsName() + "%");
		}
		int total = goodsSpecificationMapper.dataCountContent(goods);
		List<GoodsSpecificationQuery> rows = goodsSpecificationMapper
				.dataContent(goods);
		return new BTEntitiy(total, rows);
	}

	/**
	 * 根据商品id获得图片
	 * 
	 * @param goodsId
	 * @return
	 */
	public List<GoodsImg> getImgUrl(long goodsId) {
		return goodsImgMapper.getImgUrl(goodsId);
	}

	/**
	 * 根据商品id获得季节
	 * 
	 * @param goodsId
	 * @return
	 */
	public Goods getSeason(long goodsId) {
		return goodsMapper.selectGoods(goodsId);
	}

	/**
	 * 根据商品id获得省
	 * 
	 * @param goodsId
	 * @return
	 */
	public Goods selectProvince(long goodsId) {
		return goodsMapper.selectGoods(goodsId);
	}

	/**
	 * 添加商品
	 * 有图片
	 * @param goods
	 */
	public void createGoodsImg(GoodsQuery goods) {
		// 查询已有的商品名称
		List<Goods> goodsList = goodsMapper.getGoodsName(goods);
		for (Goods g : goodsList) {
			// 判断商品名称是否重复
			if (goods.getGoodsName().equals(g.getGoodsName())) {
				throw new XException("商品名称不可重复");
			}
		}
		// 添加商品的基本信息
		goodsMapper.createGoods(goods);
		// 商品的详细信息
		for (GoodsSpecification specification : goods.getSpecifications()) {
			specification.setSku(StringUtil.getUuid().substring(0, 12));
			specification.setGoodsId(goods.getId());
			goodsSpecificationMapper.createGoods(specification);
		}
		
		// 商品的图片
		goods.getGoodsImg().setGoodsId(goods.getId());
		goodsImgMapper.createGoods(goods.getGoodsImg());
	}
	/**
	 * 添加商品
	 * 没有图片
	 * @param goods
	 */
	public void createGoodsNoImg(GoodsQuery goods) {

		// 查询已有的商品名称
		List<Goods> goodsList = goodsMapper.getGoodsName(goods);
		for (Goods g : goodsList) {
			// 判断商品名称是否重复
			if (goods.getGoodsName().equals(g.getGoodsName())) {
				throw new XException("商品名称不可重复");
			}
		}
		// 添加商品的基本信息
		goodsMapper.createGoods(goods);
		// 商品的详细信息
		for (GoodsSpecification specification : goods.getSpecifications()) {
			specification.setSku(StringUtil.getUuid().substring(0, 12));
			specification.setGoodsId(goods.getId());
			goodsSpecificationMapper.createGoods(specification);
		}
	}

	/**
	 * 修改商品1
	 * 没有图片
	 * 
	 * @param goods
	 */
	public void updateGoodsNoImg(GoodsQuery goods) {
		if(StringUtil.isNullOrEmpty(goods.getProductionPlace())){
			goods.setProductionPlace(null);
		}
		//根据id获得商品的信息，判断修改时如果是当前名称则修改成功，不报异常
		Goods name = goodsMapper.getGoodsNameId(goods);
		// 查询已有的商品名称
		List<Goods> goodsList = goodsMapper.getGoodsName(goods);
		for (Goods g : goodsList) {
			if(name.getGoodsName().equals(goods.getGoodsName())){
				// 修改商品的基本信息
				goodsMapper.updateGoods(goods);
				return ;
			}else 
			// 判断商品名称是否重复
			if (goods.getGoodsName().equals(g.getGoodsName())) {
				throw new XException("商品名称不可重复");
			}
		}
		// 修改商品的基本信息
		goodsMapper.updateGoods(goods);

		for (GoodsSpecification specification : goods.getSpecifications()) {
			// 修改商品的详细信息
			specification.setSku(StringUtil.getUuid().substring(0, 12));
			specification.setGoodsId(goods.getId());
			if (specification.getId() == null) {

				goodsSpecificationMapper.createGoods(specification);
			}
			goodsSpecificationMapper.updateGoods(specification);
		}
	}

	/**
	 * 修改商品2
	 * 有图片
	 * @param goods
	 */
	public void updateGoodsImg(GoodsQuery goods) {
		if(StringUtil.isNullOrEmpty(goods.getProductionPlace())){
			goods.setProductionPlace(null);
		}
		// 查询已有的商品名称
		List<Goods> goodsList = goodsMapper.getGoodsName(goods);
		for (Goods g : goodsList) {
			//根据id获得商品的信息，判断修改时如果是当前名称则修改成功，不报异常
			Goods name = goodsMapper.getGoodsNameId(goods);
			if(name.getGoodsName().equals(goods.getGoodsName())){
				// 修改商品的基本信息
				goodsMapper.updateGoods(goods);
			}else 
			// 判断商品名称是否重复
			if (goods.getGoodsName().equals(g.getGoodsName())) {
				throw new XException("商品名称不可重复");
			}
		}
		// 修改商品的基本信息
		goodsMapper.updateGoods(goods);

		for (GoodsSpecification specification : goods.getSpecifications()) {
			// 修改商品的详细信息
			specification.setSku(StringUtil.getUuid().substring(0, 12));
			specification.setGoodsId(goods.getId());
			if (specification.getId() == null) {
				goodsSpecificationMapper.createGoods(specification);
			}
			goodsSpecificationMapper.updateGoods(specification);
		}
		if (goods.getGoodsImg()!=null) {
			System.out.println(goods.getGoodsImg().getImgUrl()+"-------");
			// 添加图片
			goods.getGoodsImg().setGoodsId(goods.getId());
			goodsImgMapper.createGoods(goods.getGoodsImg());
		}
	}

	/**
	 * 查询全部商品数量
	 * 
	 * @param wholesalerId
	 * @return
	 */
	public Integer getAllQuantity(Long wholesalerId) {
		return goodsMapper.getAllQuantity(wholesalerId);
	}

	/**
	 * 查询正在销售数量
	 * 
	 * @param wholesalerId
	 * @return
	 */
	public Integer getQuantity(Long wholesalerId) {
		return goodsMapper.getQuantity(wholesalerId);
	}

	/**
	 * 查询当前所有商品数据，用来导出数据
	 * 
	 * @param goods
	 * @return
	 */
	public List<GoodsQuery> allData(GoodsQuery goods) {
		goods.setOffset(null);
		goods.setLimit(null);
		if (StringUtil.isNullOrEmpty(goods.getGoodsName())) {
			goods.setGoodsName(null);
		} else {
			goods.setGoodsName("%" + goods.getGoodsName() + "%");
		}
		if (StringUtil.isNullOrEmpty(goods.getSize())) {
			goods.setSize(null);
		}
		if (StringUtil.isNullOrEmpty(goods.getCreateTimeStart())) {
			goods.setCreateTimeStart(null);
		}
		if (StringUtil.isNullOrEmpty(goods.getCreateTimeEnd())) {
			goods.setCreateTimeEnd(null);
		} else {
			goods.setCreateTimeEnd(goods.getCreateTimeEnd() + " 23:59:59");
		}
		List<GoodsQuery> rows = goodsMapper.data(goods);
		return rows;
	}
	/**
	 * 查询订单下所有商品数据，用来导出数据
	 * 
	 * @param goods
	 * @return
	 */
	public List<GoodsQuery> orderGoods(GoodsQuery goods) {
		goods.setOffset(null);
		goods.setLimit(null);
		if (StringUtil.isNullOrEmpty(goods.getGoodsName())) {
			goods.setGoodsName(null);
		} else {
			goods.setGoodsName("%" + goods.getGoodsName() + "%");
		}
		if (StringUtil.isNullOrEmpty(goods.getSize())) {
			goods.setSize(null);
		}
		if (StringUtil.isNullOrEmpty(goods.getCreateTimeStart())) {
			goods.setCreateTimeStart(null);
		}
		if (StringUtil.isNullOrEmpty(goods.getCreateTimeEnd())) {
			goods.setCreateTimeEnd(null);
		} else {
			goods.setCreateTimeEnd(goods.getCreateTimeEnd() + " 23:59:59");
		}
		List<GoodsQuery> rows = goodsMapper.getGoodsOrderId(goods);
		return rows;
	}

	/**
	 * 根据商品Id获得商品详细
	 * 
	 * @param goodsId
	 * @return
	 */
	public List<GoodsSpecification> getSpecification(long goodsId) {
		return goodsMapper.getSpecification(goodsId);
	}

	public List<GoodsQuery> findByPage(int page) {
		GoodsQuery goods = new GoodsQuery();
		goods.setOffset((page - 1) * BaseInfo.pageSize);
		goods.setLimit(BaseInfo.pageSize);
		return goodsMapper.data(goods);
	}

	public void commitOrderGrade(List<GoodsEvaluate> list, long orderId) {

		Order order = orderMapper.getBaseById(orderId);

		if (order.getEvaluated()) {
			throw new XException("已评价过,请勿重复评价");
		}
		orderMapper.evaluated(orderId);

		for (GoodsEvaluate goodsEvaluate : list) {
			goodsEvaluate.setOrderId(orderId);
		}

		goodsEvaluateMapper.insertList(list);

	}

	public List<GoodsEvaluateQuery> goodsEvaluateList(long goodsId, long markId) {
		List<GoodsEvaluateQuery> list;
		if (markId == 0)
			list = goodsEvaluateMapper.goodsEvaluateList(goodsId,
					BaseInfo.pageSize);
		else
			list = goodsEvaluateMapper.goodsEvaluateListMark(goodsId,
					BaseInfo.pageSize, markId);
		if (list != null && list.size() > 0) {
			for (GoodsEvaluateQuery goodsEvaluate : list) {
				String name = goodsEvaluate.getShopkeeper().getUserName();
				if (name.length() <= 5) {
					name = name.substring(0, 1) + "****"
							+ name.substring(name.length() - 1, name.length());
				} else if (name.length() <= 7) {
					name = name.substring(0, 2) + "****"
							+ name.substring(name.length() - 2, name.length());
				} else if (name.length() <= 9) {
					name = name.substring(0, 3) + "****"
							+ name.substring(name.length() - 3, name.length());
				} else {
					name = name.substring(0, 3) + "****"
							+ name.substring(name.length() - 4, name.length());
				}
				goodsEvaluate.getShopkeeper().setUserName(name);
			}

		}
		return list;
	}

	/****
	 * 供应商商品列表
	 * @param page
	 * @param userId
	 * @return
	 */
	public List<GoodsQuery> scannerGoodsList(int page, long userId) {
		List<GoodsQuery> goods = goodsMapper.scannerGoodsList(BaseInfo.pageSize, (page - 1)
				* BaseInfo.pageSize, userId);
		
		return goods;
	}

	/***
	 * 根据条件搜索未绑定磁条的商品
	 * @param page
	 * @param userId
	 * @param sku
	 * @return
	 */
	public List<GoodsSpecificationQuery> scannerSpecificationList(int page,
			long userId,String orderId) {
		if(StringUtil.isNullOrEmpty(orderId)){
			orderId=null;
		}
		Map<String, Integer> map = new HashMap<>();
		//查询订单下商品的number
		List<OrderContentQuery> orderContentQuery = orderContentMapper.getByOrderNo(orderId);
		OrderContentQuery o = new OrderContentQuery();
		List<String> list = new ArrayList<>();
		int status = MyConstants.OrderStatus.PAYMENT;
		for(OrderContentQuery number:orderContentQuery ){
			//查询orderContent和磁条绑定表：根据sku获得关系表的数量
			int count = orderContentStripeMapper.getBySku(number.getSku());
			if(count==number.getNumber()){
				list.add(number.getSku());//存放的sku是绑定好不需要展示的sku
			}
		}
		if(list.size()==0){//没有磁条绑定好不需要展示的sku商品
			return goodsSpecificationMapper.scannerSpecification(
					BaseInfo.pageSize, (page - 1) * BaseInfo.pageSize, userId,orderId,status);
		}else{//绑定好的sku商品不展示
			return goodsSpecificationMapper.scannerSpecificationList(
					BaseInfo.pageSize, (page - 1) * BaseInfo.pageSize,userId,orderId,list,status);
		}
	}
	

	public List<GoodsSpecificationQuery> specificationBySku(List<String> sku,Long id) {
		return goodsSpecificationMapper.specificationBySku(sku,id);
	}
//	public List<GoodsSpecificationQuery> specificationBySku(List<String> sku) {
//		return goodsSpecificationMapper.specificationBySku(sku);
//	}

	public Map<String, Integer> getScannerPutInfo(long userId) {
		Map<String, Integer> map = new HashMap<>();
		map.put("warning", goodsMapper.scannerWarning(userId));
		map.put("amount", goodsMapper.scannerAmount(userId));
		map.put("putGoods", stockMapper.scannerPutGoods(userId));
		map.put("outGoods", stockMapper.scannerOutGoods(userId));
		return map;
	}

	// public void x() {
	// List<Goods> list = goodsSpecificationMapper.getAll();
	// for (Goods goodsSpecification : list) {
	// goodsSpecification.setSpu(StringUtil.getUuid().substring(0, 12));
	// goodsSpecificationMapper.alterSku(goodsSpecification);
	// }
	// }

}
