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
		$(function(){
			order();
		});
		
		function order(){
			var opt = {
					columns : [
								{
									checkbox : true
								},
								{
									field : 'orderContents',
									title : '图片',
									formatter : function(value, row, index) {
										
										return value;
									}
								},
								{
									field : 'userName',
									title : '货号',
									formatter : function(value, row, index) {
										return value;
									}
								},
								{
									field : 'orderNo',
									title : '库存',
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
									title : '售出',
									formatter : function(value, row, index) {
										return value;
									}
								},
								{
									field : 'name',
									title : '批货价',
									formatter : function(value, row, index) {
										return value;
									}
								},
								{
									field : 'phone',
									title : '市场价',
									formatter : function(value, row, index) {
										return value;
									}
								},
								{

									title : '尺码',
									formatter : function(value, row, index) {
										return str;
									}
								},
								{

									title : '操作',
									formatter : function(value, row, index) {
										return str;
									}
								}
						]
			}
			$("#goodsTable").bootstrapTable(opt);
		}
	</script>
</head>

<body>

	<table id="goodsTable">
	
	</table>
	<%-- <table width="80%" align="center" border="1px"
		style="text-align:center;">
		<tr>
			<th>商品名</th>
			<th>图片</th>
			<th>颜色</th>
			<th>尺寸</th>
			<th>购买量</th>

		</tr>
		<c:forEach items="${result.list}" var="item">
			<tr>
				<td>${item.goods.goodsName}</td>
				<td><img alt="" src="${item.goods.imgUrls[0]}" width="100px"
					height="100px"></td>
				<td>${item.color}</td>
				<td>${item.size}</td>
				<td>${item.gross}</td>
			</tr>
		</c:forEach>
	</table> --%>
</body>
</html>
