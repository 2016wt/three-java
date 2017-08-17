package com.sirius.mybatis.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import com.sirius.entity.InformationImg;

public interface InformationImgMapper {

	@Insert("insert into information_img(information_id,url) values(#{informationId},#{url})")
	void insert(InformationImg informationImg);

	@Delete("delete from information_img where information_id=#{0}")
	void clearByInformation(long informationId);

}
