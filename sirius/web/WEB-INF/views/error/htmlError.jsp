<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

<title>错误</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="shortcut icon" href="resource/image/favicon.ico"
	type="image/x-icon" />
<jsp:include page="../common/importCss.jsp" />
<jsp:include page="../common/importJs.jsp" />

<script type="text/javascript">
	$(function() {
		bootbox.alert('${error.message}');
	});
</script>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
</body>
</html>
