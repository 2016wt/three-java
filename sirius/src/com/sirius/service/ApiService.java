package com.sirius.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.Api;
import com.sirius.mybatis.mapper.ApiMapper;

@Service
public class ApiService {

	@Resource
	private ApiMapper apiMapper;

	public void insert(Api api) {
		apiMapper.insert(api);
	}

	public Api selectNew() {
		return apiMapper.selectNew();
	}

}
