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
<title>鲜资讯</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
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
		saveValidate();
		ue.ready(function() {
			setContent();
		});
	});
	//实例化编辑器
	//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例

	function getAllHtml() {
		if (!$("#information").valid()) {
			return;
		}
		startLoding();
		$("#content").val(UE.getEditor('editor').getAllHtml());
		$.post("information/add", $("#information").serializeArray(), function(
				result) {
			stopLoding();
			bootbox.alert(result.message);
		});
	}

	function setContent() {
		if (${information ne null}) {
			startLoding();
			$.get("${information.content}", {}, function(result) {
				UE.getEditor('editor').execCommand('insertHtml', result);
				stopLoding();
			});
		}
	}

	function saveValidate() {
		validate = $('#information').validate({
			unhighlight : function(element, errorClass, validClass) { // 验证通过清除messages
				$(element).removeError(errorClass);
			},
			errorPlacement : function(error, element) { // messages显示位置
				$(element).addError($(error).text());
			},
			rules : {
				title : {
					required : true
				},
				subheading : {
					required : true
				},
				type : {
					required : true
				},

			},
			messages : {
				title : {
					required : "标题不能为空"
				},
				subheading : {
					required : "副标题不能为空"
				},
				type : {
					required : "请选择类型"
				},

			}
		});
	}
</script>
</head>
<body>


	<form onsubmit="return false;" id="information"
		style="margin-top: 50px">
		<input name="id" value="${information.id}" type="hidden">
		<table align="center">
			<tr>
				<td><span>标题：</span></td>
				<td><input name="title" value="${information.title}"></td>
			</tr>
			<tr>
				<td><span> 副标题：</span></td>
				<td><input name="subheading" value="${information.subheading}"></td>
			</tr>
			<tr>
				<td><span>类型：</span></td>
				<td><select name="type">
						<option value="">--请选择类型--</option>
						<option value="0" ${information.type eq 0 ? 'selected' :''}>开店</option>
						<option value="1" ${information.type eq 1 ? 'selected' :''}>进货</option>
						<option value="2" ${information.type eq 2 ? 'selected' :''}>潮流</option>
				</select></td>
			</tr>
		</table>

		<input type="hidden" name="content" id="content">
	</form>

	<div align="center">

		<script id="editor" type="text/plain"
			style="width:1024px;height:500px;"></script>
	</div>
	<div id="btns" align="center">
		<div>
			<button class="btn btn-primary" onclick="getAllHtml()">提交</button>
		</div>
	</div>
</body>
</html>