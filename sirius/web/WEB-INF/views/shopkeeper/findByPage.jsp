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

<title>实体店主列表</title>

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
		$("#pass").click(function() {
			pass();
		});
	});

	function pass() {
		var rows = $("#branchTable").bootstrapTable("getSelections");
		if (rows.length < 1) {
			bootbox.alert("请选择要操作的数据");
			return;
		}
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].status == 1) {
				bootbox.alert("勾选的数据存在已通过状态的数据,请重新选择");
				return;
			}
			ids[i] = {
				name : "ids",
				value : rows[i].id
			};
		}
		bootbox.confirm("确定要执行通过操作?", function(result) {
			if (result) {
				startLoding();
				$.post("shopkeeper/pass", ids, function(result) {
					stopLoding();
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
			url : 'shopkeeper/findByPage'
		});
	}
	function initTable() {
		var opt = {
			height : document.body.clientHeight - $("#search-form").height() - 50,
			locale : 'zh-CN',
			method : 'post',
			url : "shopkeeper/findByPage",
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
			columns : [ {
				checkbox : true
			}, {
				field : 'userName',
				title : '用户名',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'avatar',
				title : '头像',
				formatter : function(value, row, index) {
					return "<img src='"+value+"' width='50px' height='50px'/>";
				}
			}, {
				field : 'phone',
				title : '手机号',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'status',
				title : '审核状态',
				formatter : function(value, row, index) {
					if (value == 0) {
						return "审核中";
					} else if (value == 1) {
						return "审核通过";
					} else
						return "未知状态";
				}
			}, {
				field : 'type',
				title : '用户类型',
				formatter : function(value, row, index) {
					if (value == 0) {
						return "普通";
					} else if (value == 1) {
						return "会员";
					} else
						return "未知类型";
				}
			}, {
				field : 'fullAddress',
				title : '地址',
				formatter : function(value, row, index) {
					return value;
				}
			},
			{
				field : 'salesman',
				title : '业务员',
				formatter : function(value, row, index) {
					return value;
				}
			}, 
			{
				field : 'createTime',
				title : '录入时间',
				formatter : function(value, row, index) {
					return value;
				}
			}, 
			/* {
				field : 'status',
				title : '操作',
				formatter : function(value, row, index) {
					var str = "";
					if (value == 0) {
						str += "";
					}
					return str;
				}
			} */

			]
		};
		$("#branchTable").bootstrapTable(opt);
	}
</script>
</head>

<body>

	<div style="padding-left: 50px;padding-right: 50px;padding-top: 20px;">
		<form action="" id="search-form" onsubmit="return false;">
			<button id="pass" class="btn btn-success">通过</button>
		</form>
		<table class="" id="branchTable">

		</table>
	</div>

</body>
</html>
