<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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

<title>安卓版本管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="shortcut icon" href="resource/image/favicon.ico"
	type="image/x-icon" />

<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>
<jsp:include page="common/importJs.jsp" />
<script type="text/javascript">
	$(function() {
		$("#submit").click(
				function() {
					var file = document.getElementById("file");
					var reader = new FileReader();
					//读取图片
					reader.readAsDataURL(file.files[0]);
					//对图片进行编码
					reader.onload = function(evt) {
						var base64 = reader.result;
						var param = $("#form").serializeArray();

						param[param.length] = {
							name : "suffix",
							value : file.value.substring(file.value
									.lastIndexOf("."))
						};
						param[param.length] = {
							name : "file",
							value : base64.substring(base64.indexOf("base64,")
									+ "base64,".length)
						};
						alert(JSON.stringify(param).length);
						$("#base64").text((JSON.stringify(param)));
						startLoding();
						$.post("app/uploadApk", param, function(result) {
							stopLoding();
							bootbox.alert(result.message);
						});

					};
				});
	});
</script>
<body>
	<div id="base64"></div>
	<form method="post" id="form" enctype="multipart/form-data"
		style="text-align: center;">
		版本号：<input name="version"> 类型:<select name="type"><option></option>
			<option value="0">供应商版</option>
			<option value="1">实体店主版</option>
			<option value="2">采购员版</option>
			<option value="3">手持设备端</option>
		</select> 强制更新：<input name="constraint" value="false" type="radio"
			checked="checked">否<input name="constraint" value="true"
			type="radio">是 <input type="file" id="file" name="file"
			accept=".apk">
		<button type="button" id="submit">上传</button>
	</form>

	<table width="80%" align="center" border="1px"
		style="text-align:center;">
		<tr>
			<th>类型</th>
			<th>版本号</th>
			<th>强制更新</th>
			<th>大小</th>
			<th>上传时间</th>
			<th>下载地址</th>
		</tr>
		<c:forEach items="${APKLIST}" var="item">
			<tr>
				<td><c:if test="${item.type eq 0}">供应商版</c:if> <c:if
						test="${item.type eq 1}">实体店主版</c:if> <c:if
						test="${item.type eq 2}">采购员版</c:if> <c:if
						test="${item.type eq 3}">手持设备端</c:if></td>
				<td>${item.version}</td>
				<td>${item.constraint}</td>
				<td><fmt:formatNumber value="${item.size/1024}" pattern="0.00" />M</td>
				<td>${item.createTime}</td>
				<td><a href="javascript:;"
					onclick="window.open('${item.url}');return false;">下载</a></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
