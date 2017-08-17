package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.mybatis.mapper.ImgMapper;

@Service
public class ImgService {

	@Resource
	private ImgMapper imgMapper;

	public List<String> shufflingFigure() {
		return imgMapper.shufflingFigure();
	}

}
