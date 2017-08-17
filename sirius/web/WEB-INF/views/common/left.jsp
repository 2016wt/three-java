<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<div id="nav-col">
	<div id="user-left-box" class="clearfix">
		<img alt="" src="resource/image/loginer.jpg" />
		<div class="user-box pull-left">
			<span class="name"> ${wholesaler.userName} </span> <br> 

		</div>
	</div>
	<div id="sidebar-nav">
		<ul class="nav nav-pills nav-stacked" id="leftMenu">
			<li class="active">
				<a href="wholesaler/goodsList">

<!-- 					<i class="fa"></i> -->
					<span>商品管理</span>
				</a>
			</li>
			<li class="active">
				<a href="wholesaler/orderList">
<!-- 					<i class="fa"></i> -->
					<span>订单管理</span>
				</a>
			</li>
			
		</ul >
	</div>
</div>

<!-- 左侧导航]] -->

