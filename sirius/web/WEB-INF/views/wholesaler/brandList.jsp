
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
<title>品牌设置</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<script>
	$(function() {
		initTable();
		
		$("#btn_search").click(function(){
			startLoding();
			$.post("wholesaler/addBrand",$("#form").serialize(),function(result){
				bootbox.alert(result.message);
				if(result.code==0){
					//location.reload();
					researcth();
				}
				stopLoding();
			});
		});
	});
	/* 添加 */
	function addBrand(){
		bootbox.dialog({
			title : "<h4 class='model-title'>添加</h4>",
			message :  "品牌名：<input type='text' id='name' name='name' class='bootbox-input bootbox-input-text form-control'  />",
			buttons : {
				"success" : {
					"label" : "确定",
					"callback" : function() {
						startLoding();
						$.post("wholesaler/addBrand",
						{
						 name:$("#name").val(),
						},
						function(result){
							bootbox.alert(result.message);
							stopLoding();
							researcth();
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
	}
	
	/* 删除 */
	function deleteBrand(id){
		bootbox.confirm("您确定要删除吗？",function(result){
			if(result){
				startLoding();
             	$.post("wholesaler/deleteBrand/"+id,{},function(result){
						stopLoding();
						researcth();
             	});
			}
		});
	}
	function initTable() {
		var opt = {
			locale : 'zh-CN',
			method : 'post',
			url : "wholesaler/brandList",
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
						field : 'name',
						title : '品牌',
						formatter : function(value, row, index) {
							return value;
						}
					},
					{
						field : 'id',
						title : '操作',
						formatter : function(value, row, index) {
							return "<a href='javascript:deleteBrand("+value+")'>删除</a>";
						}
					},

			]
		};
		$("#branchTable").bootstrapTable(opt);
	}
	function researcth() {
		$("#branchTable").bootstrapTable('refresh', {
			url : 'wholesaler/brandList'
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
					<h5>品牌设置</h5>
				</div>
				
				<div class="widget-content">

					<div class="search-group">
						<a href="javascript:addBrand();" class="btn btn-primary">添加</a>
					</div>
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




