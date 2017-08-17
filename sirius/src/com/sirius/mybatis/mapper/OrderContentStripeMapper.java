package com.sirius.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sirius.entity.OrderContentStripe;

public interface OrderContentStripeMapper {

	@Insert("insert into order_content_stripe(order_content_id,order_id,goods_id,goods_specification_id,magnetic_stripe) "
			+ "values(#{orderContentId},#{orderId},#{goodsId},#{goodsSpecificationId},#{magneticStripe})")
	boolean insert(OrderContentStripe orderContentStripe);
	
	//查询orderContent和磁条绑定表：根据sku获得关系表的数量
	@Select("select count(1) from order_content_stripe where goods_specification_id=(select id from goods_specification where sku=#{sku})")
	int getBySku(String sku);
}
