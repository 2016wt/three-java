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
<title>库存设置</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<script type="text/javascript">
	$(function() {
		saveValidate();

		$("#submit").click(
			function() {
				if (!$("#form").valid()) {
					return;
				}
				startLoding();

				$.post("wholesaler/updateQuantity", $("#form").serialize(),
					function(result) {
							stopLoding();
							bootbox.alert(result.message);
	
					});		
				});		
	});	
	
	
	
	function saveValidate() {
		validate = $('#form').validate({
			unhighlight : function(element, errorClass, validClass) { // 验证通过清除messages
				$(element).removeError(errorClass);
			},
			errorPlacement : function(error, element) { // messages显示位置
				$(element).addError($(error).text());
			},
			rules : {
				earlyWarning : {
					required : true,
					reg : /^[+]{0,1}(\d+)$/,
				},
				earlyMax : {
					required : true,
					reg : /^[+]{0,1}(\d+)$/,
				},
			},
			messages : {
				earlyWarning : {
					required : "最小库存不能为空",
					reg : "格式不对",
				},
				earlyMax : {
					required : "最大库存不能为空",
						reg : "格式不对",
				},
			}
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
					<span class="icon"><i class="fa fa-table"></i></span>
					<h5>库存设置</h5>
				</div>

				<div class="widget-content">
					<form action="" id="form" onsubmit="return false;">
						<div class="labelContent">
							<label for="earlyWarning">最小库存:</label> <input type="text" name="earlyWarning"
								value="${wholesaler.earlyWarning}" maxlength="4" />
						</div>
						<div class="labelContent">
							<label for="earlyMax">最大库存:</label> <input type="text" name="earlyMax"
								value="${wholesaler.earlyMax}" maxlength="4" />
						</div>
						<div class="editInfo-footer">
							<button class="btn btn-primary" id="submit">保存</button>
							<button class="btn btn-danger" onclick="history.go(-1);">取消</button>
						</div>
					</form>
				</div>
			</div>
		</section>
		<!--右侧内容[[-->
	</main>
</body>
</html>




