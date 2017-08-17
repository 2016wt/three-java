<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<title>api编辑界面</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<jsp:include page="../../common/importJs.jsp" />
<jsp:include page="../../common/importCss.jsp" />
<style type="text/css">
table {
	border-collapse: separate;
	border-spacing: 30px;
}
</style>
<script type="text/javascript">
	var ue;
	$(function() {
		ue = UE.getEditor('editor');
		ue.ready(function() {
			setContent();
		});
	});
	//实例化编辑器
	//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例

	function getAllHtml() {
		
		startLoding();
		$.post("api/scanner/edit", {content:UE.getEditor('editor').getAllHtml()}, function(
				result) {
			stopLoding();
			bootbox.alert(result.message);
		});
	}

	function setContent() {
		if (${api ne null}) {
			startLoding();
			$.get("${api.url}", {}, function(result) {
				UE.getEditor('editor').execCommand('insertHtml', result);
				stopLoding();
			});
		}
	}


</script>
</head>
<body>




	<div align="center">

		<script id="editor" type="text/plain"
			style="width:90%;height:800px;"></script>
	</div>
	<div id="btns" align="center">
		<div>
			<button class="btn btn-primary" onclick="getAllHtml()">提交</button>
		</div>
	</div>
</body>
</html>