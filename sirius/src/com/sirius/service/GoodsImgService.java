package com.sirius.service;

import javax.annotation.Resource;

//import jdk.nashorn.internal.ir.annotations.Reference;

import org.springframework.stereotype.Service;

import com.sirius.mybatis.mapper.GoodsImgMapper;

@Service
public class GoodsImgService {

	@Resource
	private GoodsImgMapper goodsImgMapper;
	
	public void deleteImg(long id){
		goodsImgMapper.deleteImgById(id);
	}
}
