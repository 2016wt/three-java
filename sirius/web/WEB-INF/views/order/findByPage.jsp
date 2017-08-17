<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>订单列表</title>

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
<script type="text/javascript">
	$(function() {
		initTable();
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

		/* bootbox.confirm("确定完成此订单？", function(reslut) {
			if (reslut) {
				$.post("order/accomplish/" + orderId, {}, function(result) {
					bootbox.alert(result.message);
					if (result.code == 0) {
						researcth();
					}
				});
			}
		}); */

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
			url : 'order/findByPage'
		});
	}
	function initTable() {
		var opt = {
			height : document.body.clientHeight - 20,
			locale : 'zh-CN',
			method : 'post',
			url : "order/findByPage",
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
						field : 'orderContents',
						title : '订单详情',
						formatter : function(value, row, index) {
							var str = "<pre>";
							for (var i = 0; i < value.length; i++) {
								str += "<img src='";
								str += value[i].imgUrl;
								str += "' width='50px' height='50px'/>";
								str += " " + value[i].goodsName;
								str += " " + value[i].size;
								str += " " + value[i].color;
								str += " " + value[i].price + "元";
								str += " " + value[i].number + "件";
								if (value[i].status == 1 && row.status == 1
										&& row.payType == 0) {
									str += "<a href='javascript:;' class='btn btn-danger' onclick='alipayrefund(\""
											+ value[i].sonOrderNo
											+ "\")'>退款</a>";
								} else if (value[i].status == 1
										&& row.status == 1 && row.payType == 1) {
									str += "<a href='javascript:;' class='btn btn-danger' onclick='wechatrefund(\""
											+ value[i].sonOrderNo
											+ "\")'>退款</a>";
								} else if (value[i].status == 6) {
									str += "  退款完成";
								}
								str += "\n";
							}
							str += "</pre>";
							return str;
						}
					},
					{
						field : 'userName',
						title : '下单人',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'orderNo',
						title : '订单号',
						formatter : function(value, row, index) {
							return "<a title='"+value+"' >"
									+ value.substring(0, 5)
									+ "..."
									+ value.substring(value.length - 5,
											value.length) + "</a>";
						}
					},
					{
						field : 'money',
						title : '钱数',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'name',
						title : '收件人姓名',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'phone',
						title : '收件人手机',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'fullAddress',
						title : '地址',
						formatter : function(value, row, index) {
							return "<pre>" + value.split(" ")[0] + " "
									+ value.split(" ")[1] + " "
									+ value.split(" ")[2] + "\n"
									+ value.split(" ")[3] + "</pre>";
						}
					},
					{
						field : 'status',
						title : '状态',
						formatter : function(value, row, index) {
							if (value == 0) {
								return "待支付";
							} else if (value == 1) {
								return "已支付";
							} else if (value == 2) {
								return "已完成";
							} else if (value == 5) {
								return "已发货";
							} else if (value == -1) {
								return "已取消";
							} else
								return "未知状态";
						}
					},
					{
						field : 'expressNo',
						title : '快递单号',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'remark',
						title : '备注',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'createTime',
						title : '创建时间',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'payTime',
						title : '支付时间',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{

						title : '操作',
						formatter : function(value, row, index) {
							var str = "";

							if (row.status == 1) {

								str += "<a href='javascript:;' class='btn btn-primary' onclick='shipments("
										+ row.id + ")'>发货</a>";
							}
							str += "<a href='javascript:;' class='btn btn-info' onclick='remark("
									+ row.id + ")'>备注</a>";

							return str;
						}
					}

			]
		};
		$("#branchTable").bootstrapTable(opt);
	}
</script>
</head>

<body>
	<div>
		<table class="" id="branchTable">

		</table>
	</div>

</body>
</html>
