package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.Information;
import com.sirius.entity.query.InformationQuery;

public interface InformationMapper {

	void insert(Information information);

	List<Information> informationList(InformationQuery information);

	// List<Information> informationListMark(InformationQuery information);

	// List<Information> informationListType(@Param("size") int size,
	// @Param("type") int type);
	//
	// List<Information> informationListMarkType(@Param("size") int size,
	// @Param("markId") long markId, @Param("type") int type);

	@Update("update information set `read`=`read`+1 where id=#{0}")
	void read(long informationId);

	@Select("select * from information where id=#{0}")
	Information getById(long informationId);

	void update(Information information);
}
