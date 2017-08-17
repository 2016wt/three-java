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
<title>仓库设置</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<script>
	$(function() {
		initTable();
	$("#submit").click(function(){
		
		
		startLoding();
		$.post("wholesaler/addDepot",$("#form").serializeArray(),function(result){
			stopLoding();
			bootbox.alert(result.message);
				depotList();
		});
			
	});
	});
	/* 新增仓库 */
	function addDepot(){
		
		bootbox.dialog({
			title : "<h4 class='modal-title'>新增</h4>",
			message : "仓库名：<input type='text' id='name' name='name' maxlength='20'  class='bootbox-input bootbox-input-text form-control'  />"
					+ "编号：<input type='text' id='describe' name='describe' maxlength='10'  class='bootbox-input bootbox-input-text form-control'  />",
			buttons : {
				"success" : {
					"label" : "确定",
					"callback" : function() {
						startLoding();
						$.post("wholesaler/addDepot",
							{
							name:$("#name").val(),
							describe:$("#describe").val(),
							},
							function(result){
								bootbox.alert(result.message);
								depotList();
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
			},
		});

	}
	function depotList(){
		$("#branchTable").bootstrapTable('refresh', {
			url : 'wholesaler/depotList'
		});	
	}
	
	
	/* 删除 */
	function deleteDepot(id){
		startLoding();
		bootbox.confirm("您确定要删除吗？",function(result){
			if(result){
				startLoding();
             	$.post("wholesaler/deleteDepot/"+id,{},function(result){
						stopLoding();
						researcth();
             	});
			}
		});
	}
	
	/* 修改 */
	function edit(id){
		startLoding();
		$.post("wholesaler/depot/"+id,{},function(result){
			stopLoding();
			if(result.code==0){
				bootbox.dialog({
					title : "<h4 class='modal-title'>修改</h4>",
					message : "仓库名：<input type='text' id='name' maxlength='20' name='name' value='"+result.data.name+"' class='bootbox-input bootbox-input-text form-control'  />"
					 +"编号：<input type='text' id='describe' maxlength='10' name='describe' value='"+result.data.describe+"' class='bootbox-input bootbox-input-text form-control'  />",
					buttons : {
						"success" : {
							"label" : "确定",
							"callback" : function() {
								startLoding();
								$.post("wholesaler/updateDepot",
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

			}
		});
		
	}
	
	function researcth() {
		$("#branchTable").bootstrapTable('refresh', {
			url : 'wholesaler/depotList'
		});
	} 
	
	function initTable() {
		var opt = {
			locale : 'zh-CN',
			method : 'post',
			url : "wholesaler/depotList",
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
						title : '仓库名称',
						formatter : function(value, row, index) {
							return value;
						}
					},{
						field : 'describe',
						title : '仓库编码',
						formatter : function(value, row, index) {
							return value;
						}
					},{
						field : 'id',
						title : '操作',
						formatter : function(value, row, index) {
							return "<a href='javascript:edit("+value+")'>修改</a>"
								 + " | "
								 + "<a href='javascript:deleteDepot("+value+")'>删除</a>";
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
					<h5>仓库设置</h5>
				</div>
				<div class="widget-content">
					<div class="search-group">
						<a class="btn btn-primary" href="javascript:addDepot()">新增</a>
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




