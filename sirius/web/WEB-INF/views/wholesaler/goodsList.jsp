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
<title>商品管理</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<script type="text/javascript"
	src="${basePath }/resource/layer/layer.js"></script>
<script>
	$(function() {
		
		initTable();
		/* 查询按钮 */
		$("#btn_search").click(function() {
			researcth();
		});
		$("#outXlsx").click(
			function() {
				window.open("wholesaler/outGoodsListXlsx?"
						+ $("#search-form").serialize());
			});
		});

	/* 批量删除商品 */
	function deleteGoodsList(id){
		var table = $("#branchTable").bootstrapTable("getSelections");
		if(table.length<1){
			bootbox.alert("请选择要删除的商品");
			return ;
		}
		var goodsList=[];
		for(var a=0;a<table.length;a++){
			goodsList[a]={name:"goodsListId",value:table[a].id};
		}
			bootbox.confirm("您确定要删除吗？",function(result){
				if(result){
					startLoding();
					$.post("wholesaler/deleteGoodsList",goodsList,function(result){
						stopLoding();
						if(result.code==0){
							bootbox.alert(result.message);
							researcth();
						}
					});
				}
			});
		
	}

	/* 删除商品 */
	function deleteGoods(id) {

		bootbox.confirm("您确定要删除吗？", function(result) {
			if (result) {
				startLoding();
             	$.post("wholesaler/deleteGoods/"+id,{},function(result){
						stopLoding();
             		if(result.code==0){
             			bootbox.alert(result.message);
						researcth();
             		}
             	});
			}
		});
	}

	function researcth() {
		$("#branchTable").bootstrapTable('refresh', {
			url : 'wholesaler/goodsList'
		});
	}
	function initTable() {
		var opt = {
			locale : 'zh-CN',
			method : 'post',
			url : "wholesaler/goodsList",
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
						field : 'goodsName',
						title : '商品名',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'quantity',
						title : '库存',
						formatter : function(value, row, index) {
							if($("#quantity").val()>value){
								return "<font color='red'>"+value+"</font>";
							}else{
								return value;
							}
						}
					},
					{
						field : 'salesVolume',
						title : '售出',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'price',
						title : '批货价',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'marketPrice',
						title : '市场价',
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
						field : 'id',
						title : '操作',
						formatter : function(value, row, index) {
							if(row.putaway==true){
								row.putaway="下架";
							}else if(row.putaway==false){
								row.putaway="上架";
							}
							return "<a href='javascript:void(0)' onclick='showInfo("
									+ value
									+ ")'>详情</a>"
									+ " | "
									+ "<a href='javascript:deleteGoods("
									+ value
									+ ")'>删除</a>"
									+ " | "
									+ "<a href='wholesaler/updateGoods/"
									+ value
									+"'>修改</a>"
									+ " | "
									+ "<a href='javascript:edit("
									+ value
									+ ")'>库存</a>"
									+ " | "
									+ "<a href='javascript:"+row.putaway+"("
									+ value
									+ ")'>"+row.putaway+"</a>";
						}
					},
			]
		};
		$("#branchTable").bootstrapTable(opt);
	}
	
	function 上架(id){
		startLoding();
		$.post("wholesaler/putawayU/"+id,{},function(result){
			stopLoding();
			if(result){
				bootbox.alert(result.message);
				researcth();
			}
		});
	}
	
	function 下架(id){
		startLoding();
		$.post("wholesaler/putawayD/"+id,{},function(result){
			stopLoding();
			if(result){
				bootbox.alert(result.message);
				researcth();
			}
		});
	}
	/* 修改库存 */
	function edit(id){
		
		startLoding();
		$.post("wholesaler/goodsQuantity/"+id,{},function(result){
			stopLoding();
				bootbox.dialog({
					title : "<h4 class='modal-title'>库存</h4>",
					message : " 颜色：<input type='text' name='color' id='color' value='1' readonly='readonly' class='bootbox-input bootbox-input-text form-control' />  "
						  	 +" 件数：<input type='text' id='quantity' name='quantity' value='2' maxlength='5' class='bootbox-input bootbox-input-text form-control'  /> "
							,
					buttons : {
						"success" : {
							"label" : "确定",
							"callback" : function() {
								startLoding();
								$.post("wholesaler/updateDepot/"+id,
									{
									name:$("#name").val(),
									describe:$("#describe").val(),
									id:id
									}
								,function(result){
										bootbox.alert(result.message);
										researcth();
										stopLoding();
								});
							}
						},
						"cancel" : {
							"label" : "取消",
							"callback" : function() {
								return true;

							}
						}
					}
				});

		});
		
	}
	//商品详情
	function showInfo(value) {
		//遮罩层访问url
		var url = "wholesaler/goodsContentList/" + value;
		//打开遮罩层
		layer.open({
			type : 2,
			content : url,
			area : [ '800px', '90%' ],
			title : "商品详情"
		});

	}
</script>
</head>
<body>
	<jsp:include page="../common/top.jsp" />
	<main id="page-wrapper">
		<!-- 左侧导航[[ -->
		<jsp:include page="../common/left.jsp" />
		<!-- 左侧导航]] -->
		<!--右侧内容[[-->
		<section id="content-wrapper">
			<div class="widget-box">
				<div class="widget-title">
					<!-- 					<span class="icon"><i class="fa fa-table"></i></span> -->
					<h5>商品管理</h5>
				</div>
				<div class="widget-content">
				<!-- 隐藏域存放设置的最小库存数 -->
				<input type="hidden" id="quantity" value="${earlyWarning}"/>
					<form action="" id="search-form" onsubmit="return false;">
						
						<div style="text-align: center;" class="search-group">
							<label for="createTimeStart">时间</label> 
							<input style="float:left" type="text" name="createTimeStart" id="createTimeStart" onclick="WdatePicker({maxDate:$('#createTimeEnd').val()})" /> 
							<label for="createTimeEnd">至</label> 
							<input  style="float:left" type="text" name="createTimeEnd" onclick="WdatePicker({minDate:$('#createTimeStart').val()})" id="createTimeEnd" />
						</div>
						<div class="search-group">
							<label for="spu">商品编号</label> <input type="text" name="spu" />
						</div>

						<div class="search-group">
							<label for="quantity">商品库存</label> <input type="text"
								name="quantity" />
						</div>
						<div class="search-group">
							<label for="size">商品尺码</label> <input type="text" name="size" />
						</div>
						<div class="search-group" style="float:right;margin-right:-10px">
							<button class="btn btn-danger" id="btn_search"> 查询 </button>
							<button class="btn btn-danger" id="btn_delete" onclick="deleteGoodsList(0);"> 删除 </button>
							<a class="btn btn-primary" id="btn_create"
								href="wholesaler/createGoods"> 新建 </a>
							<button class="btn btn-info" type="reset"> 重置 </button>
							<button class="btn btn-info" type="reset" id="outXlsx">
								导出 </button>

						</div>
					</form>

					<div class="query-result">
						<table class="table table-bordered table-hover" id="branchTable">

						</table>
					</div>
				</div>
			</div>
		</section>
		<!--右侧内容[[-->
	</main>
</body>
</html>




