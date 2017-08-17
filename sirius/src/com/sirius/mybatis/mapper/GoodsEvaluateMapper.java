package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sirius.entity.Goods;
import com.sirius.entity.GoodsEvaluate;
import com.sirius.entity.query.GoodsEvaluateQuery;
import com.sirius.entity.query.GoodsQuery;

public interface GoodsEvaluateMapper {

	void insertList(@Param("list") List<GoodsEvaluate> list);

	List<GoodsEvaluateQuery> goodsEvaluateList(@Param("goodsId") long goodsId,
			@Param("size") int size);

	List<GoodsEvaluateQuery> goodsEvaluateListMark(
			@Param("goodsId") long goodsId, @Param("size") int size,
			@Param("markId") long markId);

	
}
