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

<title>订单管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<jsp:include page="../common/importCss.jsp" />
<jsp:include page="../common/importJs.jsp" />
<script type="text/javascript"
	src="${basePath }/resource/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		initTable();
		$("#btn_search").click(function(){
			researcth();
		});
		$("#outXlsx").click(
				function() {
					window.open("wholesaler/outOrderListXlsx?"
							+ $("#search-form").serialize());
				});
	});

	function alipayrefund(sonOrderNo) {
		window.open("order/alipayrefund/" + sonOrderNo);
	}
	function wechatrefund(sonOrderNo) {
		bootbox
				.dialog({
					title : "<h4 class='modal-title'>请输入退款密码</h4>",
					message : "<input type='password' id='password' class='bootbox-input bootbox-input-text form-control'  />",
					buttons : {
						"success" : {
							"label" : "退款",
							"callback" : function() {
								var password = $("#password").val();
								if (password == "") {
									bootbox.alert("密码不能为空");
									return false;
								}
								startLoding();
								$.post("order/wechatrefund/" + sonOrderNo, {
									password : password
								}, function(result) {
									stopLoding();
									bootbox.alert(result.message, function() {
										if (result.code == 0) {
											researcth();
										}
									});
								});

							}
						},
						"cancel" : {
							"label" : "取消",
							"callback" : function() {
							}
						}
					}
				});

	}

	function shipments(orderId) {
		bootbox.prompt("请输入单号", function(msg) {
			if (msg != null) {
				if (msg.trim() == "") {
					bootbox.alert("单号不能为空！");
					return false;
				}
				$.post("order/shipments", {
					id : orderId,
					expressNo : msg
				}, function(result) {
					bootbox.alert(result.message);
					if (result.code == 0) {
						researcth();
					}
				});

			}
		});

		bootbox.confirm("确定完成此订单？", function(reslut) {
			if (reslut) {
				$.post("order/accomplish/" + orderId, {}, function(result) {
					bootbox.alert(result.message);
					if (result.code == 0) {
						researcth();
					}
				});
			}
		});

	}

	function remark(orderId) {
		bootbox.prompt("请输入备注", function(msg) {
			if (msg != null) {
				if (msg.trim() == "") {
					bootbox.alert("请输入内容");
					return false;
				}
				$.post("order/remark", {
					id : orderId,
					remark : msg
				}, function(result) {
					bootbox.alert(result.message);
					if (result.code == 0) {
						researcth();
					}
				});

			}
		});
	}

	function researcth() {
		$("#branchTable").bootstrapTable('refresh', {
			url : 'wholesaler/orderList'
		});
	}
	function initTable() {
		var opt = {
			locale : 'zh-CN',
			method : 'post',
			url : "wholesaler/orderList",
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
					},{
						field : 'orderNo',
						title : '订单编号',
						formatter : function(value, row, index) {
							return value;
						}
					},{
						field : 'spu',
						title : '货号',
						formatter : function(value, row, index) {
							return value;
						}
					},{
						field : 'createTime',
						title : '日期',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'money',
						title : '总价',
						formatter : function(value, row, index) {
							return value;
						}
					},{
						field : 'id',
						title : '操作',
						formatter : function(value, row, index) {
								
								return "<a href='javascript:void(0)' onclick='showInfo("+ value + ")' >详情</a>"
									 + " | "
									 + "<a href='javascript:void(0)' onclick='sendOrder("+value+")' >发货</a>";
						}
					}
			]
		};
		$("#branchTable").bootstrapTable(opt);
	}
	
	/* 订单详情 */
	function showInfo(value) {
		//遮罩层访问url
		var url = "wholesaler/orderContent/" + value;
		//打开遮罩层
		layer.open({
			type : 2,
			content : url,
			area : [ '800px', '90%' ],
			title : "订单详情"
		});

	}
	/* 发货 */
	function sendOrder(id){
		startLoding();
		$.post("wholesaler/sendOrder/"+id,{},function(result){
			stopLoding();
			bootbox.alert(result.message);
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
					<h5>订单管理</h5>
				</div>
				<div class="widget-content">
					<form action="" id="search-form" onsubmit="return false;">
					
						<div style="text-align: center;" class="search-group left">
							<label for="paytimeStart">时间:</label> <input style="float:left" type="text"
								name="paytimeStart" id="paytimeStart"
								onclick="WdatePicker({maxDate:$('#paytimeEnd').val()})" /> <label
								for="paytimeEnd" style="margin-left: 10px">至</label> <input style="float:left" type="text"
								name="paytimeEnd"
								onclick="WdatePicker({minDate:$('#paytimeStart').val()})"
								id="paytimeEnd" />
						</div>
						
						<div class="search-group left">
							<label for="orderNo">订单编号:</label> <input type="text"
								name="orderNo" />
						</div>
						
						<div class="search-group left">
							<label for="goodsid">货号:</label> <input type="text" 
								name="goodsid" />
						</div>
						
						<div class="search-groups">
							<button class="btn btn-danger" id="btn_search">
								查询
							</button>
							
							<button class="btn btn-info" type="reset">
								重置
							</button>
							<button class="btn btn-info" type="reset" id="outXlsx">
								导出
							</button>
						</div>
					</form>

					<div >
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
