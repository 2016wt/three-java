package com.sirius.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sirius.entity.Api;

public interface ApiMapper {

	
	@Insert("insert into api(url) values(#{url})")
	boolean insert(Api api);
	
	
	
	@Select("select * from api order by id desc limit 1")
	Api selectNew();
}
