package com.sirius.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sirius.entity.Inventory;
import com.sirius.entity.Stock;
import com.sirius.exception.XException;
import com.sirius.mybatis.mapper.InventoryMapper;
import com.sirius.mybatis.mapper.StockMapper;
import com.sirius.util.StringUtil;

@Service
public class InventoryService {

	@Resource
	private StockMapper stockMapper;
	
	@Resource
	private InventoryMapper inventoryMapper;

	public void inventory(List<Inventory> inventorys) {

		for (Inventory inventory : inventorys) {
			if (StringUtil.isNullOrEmpty(inventory.getMagneticStripe())) {
				throw new XException("磁条编码为空");
			}
			if (StringUtil.isNullOrEmpty(inventory.getSku())) {
				throw new XException("商品编号为空");
			}

			Stock stock = new Stock();
			stock.setSku(inventory.getSku());
			stock.setMagneticStripe(inventory.getMagneticStripe());
			stock.setUserId(inventory.getUserId());

			stock = stockMapper.getPut(stock);

			if (stock == null || stock.getOuted())
				break;
			
			
			inventory.setGoodsSpecificationId(stock.getGoodsSpecificationId());
			
			inventoryMapper.insert(inventory);

		}
	}
	
	/**
	 * //存盘库数据
	 * @param inventory
	 */
	public void insertInventory(Inventory inventory){
		inventoryMapper.insertInventory(inventory);
	}

}
