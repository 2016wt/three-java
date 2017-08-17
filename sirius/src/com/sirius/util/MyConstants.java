package com.sirius.util;

public class MyConstants {

	public static class Dictionary {

		public static final String GOODSGENRE = "goodsGenre";// 商品分类
		public static final String GOODSGENRE_TEXT = "商品分类";// 商品分类

		public static final String STYLEGENRE = "styleGenre";// 风格分类
		public static final String STYLEGENRE_TEXT = "风格分类";// 风格分类

		public static final String TRADEMARK = "trademark";// 品牌
		public static final String TRADEMARK_TEXT = "品牌";// 品牌

		public static final String FASHION = "fashion";// 流行元素
		public static final String FASHION_TEXT = "流行元素";// 流行元素

		public static final String WAREHOUSE = "warehouse";// 仓库
		public static final String WAREHOUSE_TEXT = "仓库";// 仓库

	}

	public static class PayType {

		public static final int ALIPAY = 0;

		public static final int WXPAY = 1;

	}

	public static class PayIntention {

		public static final int ORDER = 0;

	}

	public static class OrderStatus {

		public static final int OBLIGATION = 0;// 待付款

		public static final int BEING = 1;// 处理中

		public static final int ACCOMPLISH = 2;// 已完成
		
		public static final int PAYMENT = 3;// 已付款

		public static final int SHIPMENTED = 5;// 已发货

		public static final int REFUNDED = 6;// 已退款

		public static final int CANCELED = -1;// 已取消

	}
	
	public static class OrderContentStatus {
		
		public static final int OBLIGATION = 0;// 待付款
		
		public static final int BEING = 1;// 处理中
		
		public static final int ACCOMPLISH = 2;// 已完成
		
		public static final int PAYMENT = 3;// 已付款
		
		public static final int SHIPMENTED = 5;// 已发货
		
		public static final int REFUNDED = 6;// 已退款
		
		public static final int CANCELED = -1;// 已取消
		
	}
	public static class OrderError {
		
		public static final int SUCCESS = 0;// 正常
		
		public static final int ERROR = 1;// 已取消
		
	}
	/***
	 * 订单商品异常状态
	 * @author xuelicai
	 *
	 */
	public static class OrderContentError {
		
		public static final int SUCCESS = 0;// 正常
		
		public static final int ERROR = 1;// 已取消
		
	}

	public static class UserType {

		public static final int WHOLESALER = 0;// 供货商

		public static final int SHOPKEEPER = 1;// 实体店主

		public static final int BUYER = 2;// 买手

		public static final int SCANNER = 3;// 手持设备端

	}
	
	/**
	 * 入库表
	 * @author xuelicai
	 *
	 */
	public static class StockError {
		
		public static final int SUCCESS = 0;// 入库正确
		
		public static final int ERROR = 1;// 入库错误
		
		
	}
	/**
	 * 是否出库
	 * @author xuelicai
	 *
	 */
	public static class StockOuted {
		
		public static final Boolean NO = false;// 未出库
		
		public static final Boolean YES = true;// 已出库
		
		
	}

}
