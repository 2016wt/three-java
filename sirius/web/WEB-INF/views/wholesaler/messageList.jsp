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
<title>最新消息</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<script>
	$(function() {
		initTable();
	});
	function researcth() {
		$("#branchTable").bootstrapTable('refresh', {
			url : 'wholesaler/messageList'
		});
	}
	function initTable() {
		var opt = {
			locale : 'zh-CN',
			method : 'post',
			url : "wholesaler/messageList",
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
						field : 'title',
						title : '标题',
						formatter : function(value, row, index) {
							return "<a href='wholesaler/messageContent/"+row.id+"'><xmp>"+value+"</xmp></a>";
						}
					},{
						field : 'type',
						title : '类型',
						formatter : function(value, row, index) {
							if(value==0){
								value="平台通知";
							}
							return value;
						}
					},{
						field : 'createTime',
						title : '时间',
						formatter : function(value, row, index) {
							return value;
						}
					},
			]
		};
		$("#branchTable").bootstrapTable(opt);
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
					<span class="icon"><i class="fa fa-table"></i></span>
					<h5>最新消息</h5>
				</div>
				<div class="widget-content">

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
