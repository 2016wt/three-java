package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.User;
import com.sirius.entity.query.UserQuery;

public interface UserMapper {

	@Select("select `user`.*,(select `name` from company where id=`user`.company_id)company from user where phone=#{0}")
	User getByPhone(String phone);

	@Select("select * from user where token = #{0}")
	User getByToken(String token);

	@Update("update user set password=#{password} where id=#{id}")
	boolean changePasswordById(User user);

	@Update("select count(1) from user where phone=#{0}")
	boolean checkPhone(String phone);

	boolean insert(User user);

	@Select("select * from user where id = #{0}")
	User getById(long userId);

	void changeInfo(User user);

	int dataCount(UserQuery user);

	List<User> data(UserQuery user);

	@Select("select * from user where id=#{0}")
	User getBaseById(long userId);

	@Update("update `user` set early_warning=#{earlyWarning} , early_max=#{earlyMax}  where id=#{id}")
	void updateQuantity(User user);
}
