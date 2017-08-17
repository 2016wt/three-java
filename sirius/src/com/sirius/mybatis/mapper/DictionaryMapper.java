package com.sirius.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sirius.entity.Dictionary;
import com.sirius.entity.Goods;

public interface DictionaryMapper {

	@Select("select * from dictionary")
	List<Dictionary> genreList();

	@Select("select * from dictionary where id=#{0}")
	Dictionary getById(int id);

	// 品牌列表
	List<Dictionary> data(Dictionary dictionary);
	int dataCount(Dictionary dictionary);

	//查询数据库已经删除的品牌
	@Select("select * from dictionary where user_id=#{userId} and type=#{type} ")
	List<Dictionary> allBrand(Dictionary dictionary);
	
	// 添加品牌
	@Insert("insert into dictionary (`name`,type,`describe`,user_id) value(#{name},#{type},#{describe},#{userId})")
	boolean addBrand(Dictionary dictionary);
	
	//如果品牌名称相同且品牌已经标记删除即恢复已删除的品牌
	@Update("update dictionary set exist=#{exist} where user_id=#{userId} and `name`=#{name} and type=#{type}")
	boolean updateExist(Dictionary dictionary);
	
	//如果仓库名称相同且仓库已经标记删除即恢复已删除的仓库
	@Update("update dictionary set exist=#{exist} where user_id=#{userId} and `name`=#{name} and type=#{type}")
	boolean updateDepotExist(Dictionary dictionary);
	
	//删除品牌
	@Delete("update dictionary set exist=0 where id=#{id}")
	void deleteBrand(int id);
	
	//查询流行元素
	List<Dictionary> fashionData(Dictionary dictionary);
	
	//查询品牌
	List<Dictionary> trademarkData(Dictionary dictionary);
	
	//查询商品分类
	List<Dictionary> goodsgenreData(Dictionary dictionary);
	
	//查询商品分类
	List<Dictionary> stylegenreList(Dictionary dictionary);
	
	//仓库列表
	@Select("select * from dictionary where type=#{type} and user_id=#{userId} and exist")
	List<Dictionary> depotList(Dictionary dictionary);
	
	//根据添加的仓库名称和仓库编号查询，查看是否有重复
	@Select("select * from dictionary where `name`=#{name} and `describe` = #{describe} and type=#{type}")
	Dictionary selectDepot(Dictionary dictionary);
	
	//根据添加的仓库名称和仓库编号查询，查看是否有重复
	@Select("select * from dictionary where `name`=#{name} and `describe` = #{describe} and type=#{type} and exist=#{exist}")
	Dictionary selectDepot2(Dictionary dictionary);
	
	//仓库数量
	@Select("select count(1) from dictionary where type=#{type} and user_id=#{userId} and exist")
	int depotListCount(Dictionary dictionary);
	
	//新增仓库
	@Insert("insert into dictionary(`name`,type,`describe`,user_id) value(#{name},#{type},#{describe},#{userId})")
	void addDepot(Dictionary dictionary);
	
	//根据id显示仓库信息
	@Select("select `name`,`describe` from dictionary where id=#{id} ")
	Dictionary depot(long id);
	
	//修改仓库信息
	@Update("update dictionary set `name`=#{name},`describe`=#{describe} where id=#{id}")
	void updateDepot(Dictionary dictionary);
	
	//删除仓库
	@Delete("delete from dictionary where id=#{id}")
	void deleteDepot(long id);
	
	//根据id获得供应商id
	@Select("select * from dictionary where id=#{id}")
	Dictionary selectUserId(long id);

	@Insert("insert into x(x) value(#{0})")
	void x(String x);
}
