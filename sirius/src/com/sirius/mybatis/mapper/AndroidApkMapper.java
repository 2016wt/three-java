package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sirius.entity.AndroidApk;

public interface AndroidApkMapper {

	/*public List<AndroidApk> findPage(AndroidApk androidApk);

	public int findPageCount(AndroidApk androidApk);*/

	@Insert("insert into android_apk(version,url,`constraint`,size,type) values(#{version},#{url},#{constraint},#{size},#{type})")
	public void insert(AndroidApk androidApk);

	@Select("select x.*,(select count(1) from android_apk where `constraint` and version>#{0} and type=#{1})>0 `constraint` from (select version,url,size from android_apk where version>#{0} and type=#{1} order by version desc limit 1)x")
	public AndroidApk newApk(int version,int type);

	@Select("select * from android_apk order by version desc limit 20")
	public List<AndroidApk> list();
}
