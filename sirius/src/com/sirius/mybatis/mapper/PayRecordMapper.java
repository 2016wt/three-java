package com.sirius.mybatis.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.PayRecord;

public interface PayRecordMapper {

	@Insert("insert into pay_record(order_no,shopkeeper_id,type,intention,relevance_id,money,`describe`) values(#{orderNo},#{shopkeeperId},#{type},#{intention},#{relevanceId},#{money},#{describe})")
	boolean insert(PayRecord payRecord);

	/**
	 * 
	 * @param orderNo
	 * @return
	 */
	@Select("select count(1) from pay_record where order_no=#{0} and status=0")
	boolean exitOrderNo(String orderNo);

	/**
	 * 只能清除未支付的订单
	 * 
	 * @param orderNo
	 * @return
	 */
	@Delete("delete from pay_record where order_no=#{0} and status=0")
	boolean clearOrderNo(String orderNo);

	@Select("select * from pay_record where order_no=#{0}")
	PayRecord getByOrderNo(String orderNo);

	// @Update("update pay_record set status=#{1} where id=#{0}")
	// void alterStatus(long id, int status);

	@Update("update pay_record set status=#{status},trade_no=#{tradeNo} where id=#{id}")
	void accomplish(PayRecord payRecord);

}
