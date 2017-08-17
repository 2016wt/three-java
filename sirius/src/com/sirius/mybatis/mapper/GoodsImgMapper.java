package com.sirius.mybatis.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sirius.entity.GoodsImg;

public interface GoodsImgMapper {

	boolean insertList(@Param("list") List<GoodsImg> list);

	//根据商品id获得商品图片的路径
	@Select("select * from goods_img where goods_id=#{goodsId}")
	List<GoodsImg> getImgUrl(long id);
	
	//添加商品的图片
	@Insert("insert into goods_img (goods_id,img_url) value(#{goodsId},#{imgUrl})")
	void createGoods(GoodsImg goodsimg );
	
	//根据商品id删除商品图片的路径
	@Delete("delete from goods_img where goods_id=#{id} ")
	void deleteImg(long id);
	
	//根据图片id删除商品图片的路径
	@Delete("delete from goods_img where id=#{id} ")
	void deleteImgById(long id);
}
