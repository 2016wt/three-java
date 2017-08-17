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

<title>商品列表</title>

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
		$("#search").click(function(){
			researcth();
		});
	});

	function researcth() {
		$("#branchTable").bootstrapTable('refresh', {
			url : 'goods/findByPage'
		});
	}
	function initTable() {
		var opt = {
				height : document.body.clientHeight - 20,
				locale : 'zh-CN',
				method : 'post',
				url : "goods/findByPage",
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
			}

			, {
				field : 'imgUrl',
				title : '图片',
				formatter : function(value, row, index) {
					return "<img src='"+value+"' width='50px' height='50px'/>";
				}
			}, {
				field : 'goodsName',
				title : '商品名',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'describe',
				title : '描述',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'thickness',
				title : '厚薄',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'quality',
				title : '品质',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'price',
				title : '价格',
				formatter : function(value, row, index) {
					return value;
				}
			},
			{
				field : 'quantity',
				title : '库存',
				formatter : function(value, row, index) {
					return value;
				}
			},{
				field : 'sizes',
				title : '尺码',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'colors',
				title : '颜色',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'buyTimeStart',
				title : '抢购开始时间',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'buyTimeEnd',
				title : '抢购结束时间',
				formatter : function(value, row, index) {
					return value;
				}
			}

			, {
				field : 'putaway',
				title : '上架情况',
				formatter : function(value, row, index) {
					if (value)
						return "已上架";
					if (value)
						return "未上架";
				}
			}

			, {
				field : 'createTime',
				title : '商品创建时间',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'clustered',
				title : '已成团数量',
				formatter : function(value, row, index) {
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
	
	<div style="padding-left: 50px;padding-right: 50px;padding-top: 20px;">
	
		<form onsubmit="return false" id="search-form">
			<!-- 库存：<input type="text" name="quantity"/> -->
			尺码：<input type="text" name="size"/>
			商品编号：<input type="text" name="#"/>
				<input type="button" id="search" value="查询"/>
		</form>
		
		<table class="" id="branchTable">

		</table>
	</div>

</body>
</html>
