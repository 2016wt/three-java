package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface ImgMapper {

	/**
	 * 首页轮播图
	 * 
	 * @return
	 */
	@Select("select img_url from img_depot where type=0")
	List<String> shufflingFigure();

}
