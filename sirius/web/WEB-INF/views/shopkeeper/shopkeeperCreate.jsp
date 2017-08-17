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

<title>录入</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<jsp:include page="../common/importCss.jsp" />
<jsp:include page="../common/importJs.jsp" />
<script type="text/javascript">
	$(function() {
		saveValidate();
		startLoding();
		$.post("address/provinceList", {}, function(result) {
			if (result.code == 0) {
				var str = "";
				for (var i = 0; i < result.data.length; i++) {
					str += "<option value='"+result.data[i].code+"'>"
							+ result.data[i].provinceName + "</option>";
				}
				$("#provinceCode").html(str);
				stopLoding();
				cityList();
			}
		});

		$("#provinceCode").change(function() {
			cityList();
		});
		$("#cityCode").change(function() {
			areaList();
		});

		$("#submit").click(
				function() {
					if (!$("#table").valid()) {
						return;
					}
					startLoding();
					$.post("shopkeeper/create", $("#table").serializeArray(),
							function(result) {
								stopLoding();
								bootbox.alert(result.message);
								if (result.code == 0) {

								}
							});
				});

	});

	function cityList() {
		startLoding();
		$.post("address/cityList/" + $("#provinceCode").val(), {}, function(
				result) {
			if (result.code == 0) {
				var str = "";
				for (var i = 0; i < result.data.length; i++) {
					str += "<option value='"+result.data[i].code+"'>"
							+ result.data[i].cityName + "</option>";
				}
				$("#cityCode").html(str);
				stopLoding();
				areaList();
			}
		});
	}
	function areaList() {
		startLoding();
		$.post("address/areaList/" + $("#cityCode").val(), {},
				function(result) {
			alert(result.data.areaName);
					if (result.code == 0) {
						var str = "";
						for (var i = 0; i < result.data.length; i++) {
							str += "<option value='"+result.data[i].code+"'>"
									+ result.data[i].areaName + "</option>";
						}
						$("#areaCode").html(str);
						stopLoding();

					}
				});
	}

	function saveValidate() {
		validate = $('#table').validate({
			unhighlight : function(element, errorClass, validClass) { // 验证通过清除messages
				$(element).removeError(errorClass);
			},
			errorPlacement : function(error, element) { // messages显示位置
				$(element).addError($(error).text());
			},
			rules : {
				userName : {
					required : true
				},
				phone : {
					required : true,
					minlength : 11
				},
				fullAddress : {
					required : true
				},
				areaCode : {
					required : true
				},
				salesman : {
					required : true
				},

			},
			messages : {
				userName : {
					required : "用户名不能为空"
				},
				phone : {
					required : "请输入合法手机号",
					minlength : "请输入合法手机号"
				},
				fullAddress : {
					required : "详细地址不能为空"
				},
				areaCode : {
					required : "县不能为空"
				},
				salesman : {
					required : "业务员不能为空"
				},

			}
		});
	}
</script>
<style type="text/css">
table {
	border-collapse: separate;
	border-spacing: 30px;
}
</style>
</head>

<body>
	<h1 align="center">录入实体店主</h1>
	<form action="" onsubmit="return false;" id="table">
		<table align="center">
			<tr>
				<td>用户名</td>
				<td><input name="userName" /></td>
			</tr>
			<tr>
				<td>手机号</td>
				<td><input name="phone" maxlength="11" /></td>
			</tr>
			<tr>
				<td>省</td>
				<td><select name="provinceCode" id="provinceCode"></select></td>
			</tr>
			<tr>
				<td>市</td>
				<td><select name="cityCode" id="cityCode"></select></td>
			</tr>
			<tr>
				<td>县</td>
				<td><select name="areaCode" id="areaCode"></select></td>
			</tr>
			<tr>
				<td>详细地址</td>
				<td><input name="fullAddress" /></td>
			</tr>
			<tr>
				<td>业务员</td>
				<td><input name="salesman" maxlength="10" /></td>
			</tr>
			<tr>
				<td align="center" colspan="2"><button id="submit">提交</button></td>

			</tr>
		</table>

	</form>
</body>
</html>
