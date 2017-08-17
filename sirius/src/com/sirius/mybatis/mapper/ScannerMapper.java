package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sirius.entity.query.OrderContentQuery;
import com.sirius.entity.query.OrderQuery;
import com.sirius.entity.query.UserQuery;

public interface ScannerMapper {

	//select sum(money) totalMoney from `order` where `status`=3 and id in(select order_id from order_content where goods_id in(select id from goods where wholesaler_id=(select id from `user` where token=#{token})))
	//获得所有销售额
	@Select("select sum(money) totalMoney from order_content where status=#{statusOrderContent} and wholesaler_id=(select id from `user` where token=#{token})")
	String getByToken(@Param("token")String token,@Param("statusOrderContent")int statusOrderContent);
	
	//获得订单付款时间
	@Select("select pay_time from `order` where `status`=#{status} and id in(select DISTINCT order_id from order_content where wholesaler_id=((select id from `user` where token=#{token})))")
	List<String> timeByToken(String token);
	
	//根据时间获取时间段的销售额和数量
	//@Select("select sum(money),count(1) from order_content where wholesaler_id=(select id from `user` where token='11111111111') and order_id in(select id from `order` where pay_time in('2017-07-17 11:59:54','2017-04-17 17:03:50'))")
	OrderContentQuery getByPayTime(@Param("date") List<String> date,@Param("token")String token); 
}
