<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<script type="text/javascript" >
		function exit(){
			bootbox.confirm("您确定退出吗？",function(result){
				if(result){
					startLoding();
					$.post("wholesaler/exit",{},function(result){
						stopLoding();
						location.reload();
					});
				}
			});
		}
	</script>
<header class="navbar" id="header-navbar">
	<div class="container">
		<a href="javascript:;" target="_blank" id="logo" class="navbar-brand">
			<img class=top_logo src="resource/image/S_logo.png">
			<span id="title">北京壹是壹科技</span>
		</a>
		<div class="clearfix">
<!-- 			<button class="navbar-toggle" data-target=".navbar-ex1-collapse" -->
<!-- 				data-toggle="collapse" type="button"> -->
<!-- 				<span class="sr-only">Toggle navigation</span> <span -->
<!-- 					class="fa fa-bars"></span> -->
<!-- 			</button> -->
<!-- 			<div class="nav-no-collapse navbar-left pull-left "> -->
<!-- 				<ul class="nav navbar-nav pull-left"> -->
<!-- 					<li><a class="btn" id="make-small-nav"> <i -->
<!-- 							class="fa fa-outdent"></i> -->
<!-- 					</a></li> -->
<!-- 				</ul> -->
<!-- 			</div> -->
			<div style="float:left;margin-left:20px">
				<ul class="goodsCount">
					<li>全部商品 : ${getAllQuantity }件</li>
					<li>正在销售 : ${getQuantity }件</li>
					<li class="list-none">订单 : ${getAllOrder }单</li>
				</ul>
			</div>
			
			<div style="float:right;height:60px;">
				<div class="nav-no-collapse pull-right" id="header-nav">
					<ul class="nav navbar-nav pull-right">
						<li><a href="wholesaler/messageList">最新消息</a></li>
						<li><a href="javascript:exit();" >退出</a></li>
					</ul>
				</div>
				<div>
					
				</div>
			</div>
			
			<div class="" style="float:right;">
				<button class="btn btn-default dropdown-toggle" type="button"
			id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
			aria-expanded="true">
				功能设置 <span class="caret"></span>
				</button>
				<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
					<li><a href="wholesaler/getCompany">公司信息</a></li>
					<li><a href="wholesaler/getQuantity">库存设置</a></li>
					<li><a href="wholesaler/getDepot">仓库设置</a></li>
					<li><a href="wholesaler/brandList">品牌设置</a></li>							
				</ul>
			</div>
		</div>
	</div>
</header>