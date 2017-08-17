package com.sirius.mybatis.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import com.sirius.entity.Company;

public interface CompanyMapper {

	//公司信息
	@Select("select * from company where user_id=#{userId}")
	Company getCompany(Company company);
	
	//修改公司信息
	void updateCompany(Company company);
}
