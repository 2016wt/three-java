package com.sirius.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.Company;
import com.sirius.entity.LocationCity;
import com.sirius.entity.LocationProvince;
import com.sirius.mybatis.mapper.CompanyMapper;
import com.sirius.mybatis.mapper.LocationCityMapper;
import com.sirius.mybatis.mapper.LocationProvinceMapper;
@Service
public class CompanyService {

	//省
	@Resource
	private LocationProvinceMapper locationProvinceMapper;
	//市
	@Resource
	private LocationCityMapper locationCityMapper;
	
	@Resource
	private CompanyMapper companyMapper;
	
	//查询公司信息
	public Company getCompany(Company company){
		return companyMapper.getCompany(company);
	}
	
	//修改公司信息
	public void updateCompany(Company company){

		companyMapper.updateCompany(company);
	}
}
