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
<title>最新消息详情</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<style type="text/css">
	pre{
	white-space: pre-wrap;       
	white-space: -moz-pre-wrap;  
	white-space: -pre-wrap;      
	white-space: -o-pre-wrap;    
	word-wrap: break-word;       
}
</style>
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
					<h5>最新消息详情</h5>
				</div>
				<div class="labelContent">
					<a class="btn btn-danger" href="javascript:history.go(-1);">返回</a>
				</div>
				<div class="widget-content" >
					<font size="5" color="blue"><xmp>${message.title }</xmp></font>
					<h6>${message.type eq '0' ? '平台通知' : '' }</h6>
					<h6>${message.createTime }</h6>
				</div>
				<div>
					<pre size="4"><xmp>${message.content}</xmp></pre>
				</div>
			</div>
		</section>
		<!--右侧内容[[-->
	</main>
</body>
</html>
