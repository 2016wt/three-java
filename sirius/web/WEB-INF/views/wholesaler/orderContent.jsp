<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML >
<html>

<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1" />
<title>订单详情</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<script>
	$(function() {
		initTable();
		/* 导出订单 */
		$("#outXlsx").click(
			function() {
				window.open("wholesaler/outOrderByGoods/"+$("#hidden").val()+"?"
						+ $("#search-form").serialize());
		});
		/* 发货 */
		$("#send").click(
			function(){
				if(${order.status }==0){
					bootbox.alert("订单未付款，禁止发货");
				}else if(${order.status }==5){
					bootbox.alert("订单不可重复发货");
				}else{
					startLoding();
					$.post("wholesaler/sendOrder/"+$("#hidden").val(),{},
						function(){
						stopLoding();
							bootbox.alert("发货成功");
							location.reload();
							//window.open("wholesaler/orderContent"+$("#hidden").val());
					});	
				}	
		});
	});

	function researcth() {
		$("#branchTable").bootstrapTable('refresh', {
			url : 'wholesaler/orderContent'
		});
	}
	function initTable() {
		var opt = {
			locale : 'zh-CN',
			method : 'post',
			url : "wholesaler/goodsList/"+$("#hidden").val(),
			striped : true,
			contentType : "application/x-www-form-urlencoded",
			queryParamsType : "limit",
			queryParams : function(params) {
				var data = bootstrapTableParams("#search-form", params);
				//alert(JSON.stringify(data));
				var x = $(".page-number.active");
				var page = x.find("a").html();
				if (!isNaN(page)) {
					page = parseInt(page);
					if (this.pageNumber != page) {
						this.pageNumber = page;
					}
					for (var int = 0; int < data.length; int++) {
						if (data[int].name == 'offset') {
							data[int].value = (page - 1)
									* parseInt($(".page-size").html());
						}
					}
				}
				return data;
			},
			pagination : true,
			clickToSelect : true,
			pageSize : 10,
			pageList : [ 10, 25, 50, 100, 200 ],
			sidePagination : "server",
			columns : [
					{
						checkbox : true
					},
					{
						field : 'imgUrl',
						title : '图片',
						formatter : function(value, row, index) {
							return "<img src='"+value+"' width='50px' height='75px'/>";
						}
					},
					{
						field : 'spu',
						title : '商品编号',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'goodsName',
						title : '商品名',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'colors',
						title : '颜色',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'quantity',
						title : '数量',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'sizes',
						title : '尺码',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'price',
						title : '单价',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'price',
						title : '总价',
						formatter : function(value, row, index) {
							value*=row.quantity;
							return value;
						}
					}
			]
		};
		$("#branchTable").bootstrapTable(opt);
	}
</script>
</head>
<body>
	<div id="content">
		<form action="" id="search-form" onsubmit="return false;">
			<div class="labelContent label_name">
				<label>订单编号  ：</label> 
				<input type="text" value="${order.orderNo }" readonly="readonly" />
			</div>
			<div class="labelContent label_name">
				<label>订单金额  ：</label> 
				<input type="text" value="${order.money }" readonly="readonly" />
			</div>
			<div class="labelContent label_name">
				<label>付款状态  ：</label> 
				<input type="text" value="${order.status eq 0 ?'待付款':''}${order.status eq 5 ?'已发货':''}" readonly="readonly" />
			</div>
			<div class="labelContent label_name">
				<label>订单归属  ：</label> 
				<input type="text" value="" readonly="readonly" />
			</div>
			<div class="labelContent label_name">
				<label>下单日期  ：</label> 
				<input type="text" value="${order.createTime }" readonly="readonly" />
			</div>
			<div class="labelContent label_name">
				<label>订单状态  ：</label> 
				<input type="text" value="${order.status eq 0 ?'待付款':''}${order.status eq 5 ?'已发货':''}" readonly="readonly" />
			</div>
		</form>
		<h5>商品统计</h5>
		<div>
			<form action="" onsubmit="return false;">
				<input type="hidden" value="${orderId}" name="orderId" id="hidden"/>
			</form>
					
			<div class="query-result">
				<table class="table table-bordered table-hover" id="branchTable" style="width: 100%;height: 90%">

				</table>
			</div>
		</div>
		<div style="float: right;">
			<font>共${quantity }件</font>&nbsp;&nbsp;<font>总价：￥${order.money }</font>
		</div>
		<br>
		<div class="search-groups">
			<button class="btn btn-danger" id="outXlsx">
				导出订单
			</button>
			<button class="btn btn-danger" id="send">
				发货
			</button>
			<a class="btn btn-danger" href="javascript:history.go(-1);" style="background-color:red;color:#fff">取消</a>
		</div>
	</div>		
</body>
</html>
