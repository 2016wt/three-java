package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.taglibs.standard.lang.jstl.test.beans.PublicBean1;
import org.springframework.stereotype.Service;

import sun.util.logging.resources.logging;

import com.sirius.entity.Dictionary;
import com.sirius.entity.Goods;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.DictionaryMapper;
import com.sirius.po.BTEntitiy;
import com.sirius.po.JsonEntity;
import com.sirius.util.Tools;

@Service
public class DictionaryService {

	@Resource
	private DictionaryMapper dictionaryMapper;

	public List<Dictionary> genreList() {
		return dictionaryMapper.genreList();
	}

	/**
	 * 品牌列表
	 * @param dictionary
	 * @return
	 */
	public BTEntitiy brandList(Dictionary dictionary) {

		int total = dictionaryMapper.dataCount(dictionary);
		List<Dictionary> rows = dictionaryMapper.data(dictionary);
		return new BTEntitiy(total, rows);
	}

	/**
	 * 添加品牌
	 * @param dictionary
	 */
	public void addBrand(Dictionary dictionary) {
		//判断输入品牌是否为空
		if(dictionary.getName()==""){
			throw new XException("品牌缺失");
		}
		//判断品牌的名称是否重复
		List<Dictionary> nameString = dictionaryMapper.data(dictionary);
		for(Dictionary d : nameString){
			if(dictionary.getName().equals(d.getName())){
				throw new XException("品牌名称不可以重复");
			}
		}
		
		//判断品牌名称重复的是否被删除，如果删除就恢复
		List<Dictionary> exist = dictionaryMapper.allBrand(dictionary);
		for(Dictionary d : exist){
			if(dictionary.getName().equals(d.getName())){
				if(d.getExist()==false){
					//恢复已经删除的品牌
					dictionary.setExist(true);
					dictionary.setName(d.getName());
					dictionaryMapper.updateExist(dictionary);
				}else{
					dictionaryMapper.addBrand(dictionary);
				}
			}
		}
		//判断品牌数量
		if(dictionaryMapper.dataCount(dictionary)>9){
			throw new XException("品牌超出10个,添加失败。");
		}
		dictionaryMapper.addBrand(dictionary);
		
	}
	
	/**
	 * 删除品牌
	 * @param id
	 */
	public void deleteBrand(int id){
		dictionaryMapper.deleteBrand(id);
	}
	
	/**
	 * 品牌数量
	 * @param dictionary
	 * @return
	 */
	public int dataCount(Dictionary dictionary){
		
		return dictionaryMapper.dataCount(dictionary);
	}
	
	/**
	 * 查询流行元素
	 * @param dictionary
	 * @return
	 */
	public List<Dictionary> fashionData(Dictionary dictionary){
		return dictionaryMapper.fashionData(dictionary);
	}
	
	/**
	 * 查询品牌
	 * @param dictionary
	 * @return
	 */
	public List<Dictionary> trademarkData(Dictionary dictionary){
		return dictionaryMapper.trademarkData(dictionary);
	}
	
	/**
	 * 查询商品分类
	 * @param dictionary
	 * @return
	 */
	public List<Dictionary> goodsgenreData(Dictionary dictionary){
		return dictionaryMapper.goodsgenreData(dictionary);
	}
	/**
	 * 查询商品分类
	 * @param dictionary
	 * @return
	 */
	public List<Dictionary> stylegenreList(Dictionary dictionary){
		return dictionaryMapper.stylegenreList(dictionary);
	}
	
	/**
	 * 仓库列表
	 * @param dictionary
	 * @return
	 */
	public BTEntitiy depotList(Dictionary dictionary){
		int total = dictionaryMapper.depotListCount(dictionary);
		List<Dictionary> rows = dictionaryMapper.depotList(dictionary);
		return new BTEntitiy(total, rows);
	}
	
	/**
	 * 根据id显示仓库信息
	 * @param id
	 * @return
	 */
	public Dictionary depot(long id){
		return dictionaryMapper.depot(id);
	}
	
	/**
	 * 新增仓库
	 * @param dictionary
	 */
	public void addDepot(Dictionary dictionary){
		//验证仓库名称不能为空
		if(dictionary.getName()==""){
			throw new XException("仓库名称不不可为空");
		}
		//验证仓库编号不能为空
		if(dictionary.getDescribe()==""){
			throw new XException("仓库编号不不可为空");
		}
		//验证新增仓库名称是否和其他仓库名称相同,根据新增信息查询数据库
		dictionary.setType(Dictionary.DEPOT);
		Dictionary dic = dictionaryMapper.selectDepot(dictionary);
		
		if(dic==null){
			dictionaryMapper.addDepot(dictionary);
		}else{
			throw new XException("仓库名称+编号不可重复.");
		}
	}
	
	/**
	 * 修改仓库
	 * @param dictionary
	 */
	public void updateDepot(Dictionary dictionary){
		dictionary.setType(Dictionary.DEPOT);
		//exist存在的数据
		Dictionary dic = dictionaryMapper.selectDepot(dictionary);
		
		//根据id获得当前需要修改的仓库名称和编号
		Dictionary d = dictionaryMapper.depot(dictionary.getId());
		
		if(dic!=null){
			if(d.getName().equals(dictionary.getName()) || d.getDescribe().equals(dictionary.getDescribe())){
				//修改仓库
				dictionaryMapper.updateDepot(dictionary);
				return;
			}else {
				throw new XException("仓库名称+编号不可重复.");
			}
		}else{
			dictionaryMapper.updateDepot(dictionary);
		}
	}
	
	/**
	 * 删除仓库
	 * @param id
	 */
	public void deleteDepot(long id){
		dictionaryMapper.deleteDepot(id);
	}
	
	/****
	 * 根据仓库id判断该仓库对应的供应商
	 */
	public Dictionary selectUserId(long id){
		return  dictionaryMapper.selectUserId(id);
	}
}