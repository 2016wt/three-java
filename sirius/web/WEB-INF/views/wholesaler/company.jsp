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
<title>公司信息</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<script>
	$(function() {

		saveValidate();

		$("#submit").click(
				function() {
					if (!$("#form").valid()) {
						return;
					}
					startLoding();
					$.post("wholesaler/updateCompany", $("#form").serialize(),
							function(result) {
								bootbox.alert(result.message);
								stopLoding();
							});
				});
		$.post("address/provinceList",{},
				function(result) {
							if (result.code == 0) {
								var str = "<option value=''>请选择</option>";

								for (var i = 0; i < result.data.length; i++) {

									if ("${compan.province}" == result.data[i].code)
										str += "<option selected value='"+result.data[i].code+"'>"
												+ result.data[i].provinceName
												+ "</option>";

									else
										str += "<option value='"+result.data[i].code+"'>"
												+ result.data[i].provinceName
												+ "</option>";
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
	});
	function cityList() {
		if ($("#provinceCode").val() == ''){
			//原有数据清掉
			$("#cityCode").html("<option value=''>请选择</option>");
			$("#areaCode").html("<option value=''>请选择</option>");
			return;
		}
		startLoding();
		$
				.post(
						"address/cityList/" + $("#provinceCode").val(),
						{},
						function(result) {

							if (result.code == 0) {
								var str = "<option value=''>请选择</option>";
								for (var i = 0; i < result.data.length; i++) {
									if ("${compan.city}" == result.data[i].code)
										str += "<option selected value='"+result.data[i].code+"'>"
												+ result.data[i].cityName
												+ "</option>";
									else
										str += "<option value='"+result.data[i].code+"'>"
												+ result.data[i].cityName
												+ "</option>";
								}
								$("#cityCode").html(str);
								stopLoding();
								areaList();

							}
						});
	}

	function areaList() {
		if ($("#cityCode").val() == ''){
			$("#areaCode").html("<option value=''>请选择</option>");
			return;
		}
		startLoding();
		$
				.post(
						"address/areaList/" + $("#cityCode").val(),
						{},
						function(result) {

							if (result.code == 0) {
								var str = "<option value=''>请选择</option>";
								for (var i = 0; i < result.data.length; i++) {
									if ("${compan.area}" == result.data[i].code)
										str += "<option selected value='"+result.data[i].code+"'>"
												+ result.data[i].areaName
												+ "</option>";
									else
										str += "<option value='"+result.data[i].code+"'>"
												+ result.data[i].areaName
												+ "</option>";

								}
								$("#areaCode").html(str);
								stopLoding();

							}
						});
	}

	function saveValidate() {
		validate = $('#form').validate({
			unhighlight : function(element, errorClass, validClass) { // 验证通过清除messages
				$(element).removeError(errorClass);
			},
			errorPlacement : function(error, element) { // messages显示位置
				$(element).addError($(error).text());
				stopLoding();
			},
			rules : {
				name : {
					required : true,
				},
				province : {
					required : true,
				},
				city : {
					required : true,
				},
				area : {
					required : true,
				},
				detailed : {
					required : true,
				},
				phone : {
					required : true,
					reg : /^[0-9]*$/
				},
				email : {
					email : true,
				},
				url : {
					url : true,
				},
				intro : {
				},
			},
			messages : {
				name : {
					required : "公司名称不能为空",
				},
				province : {
					required : "公司所在省份不能为空",
				},
				city : {
					required : "公司所在城市不能为空",
				},
				area : {
					required : "公司所在县不能为空",
				},
				detailed : {
					required : "公司详细地址不能为空",
				},
				phone : {
					required : "公司手机号不能为空",
					reg : "电话号码格式不正确",
				},
				email : {
					email : "邮箱格式错误"
				},
				url : {
					url : "网址格式错误"
				},
				intro : {
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
<!-- 					<span class="icon"><i class="fa fa-table"></i></span> -->
					<h5>公司信息</h5>
				</div>
				<form action="" id="form" onsubmit="return false;">
					<div>
						<div class="labelContent">
							<label for="name">公司名称:</label> <input type="text" name="name"
								value="${compan.name}" maxlength="50" />
						</div>
						<div class="labelContent">
							<label for="province">公司区域:</label> 
							<select id="provinceCode" name="province"></select> 
							<select id="cityCode" name="city"></select>
							<select id="areaCode" name="area"></select>
						</div>
						<div class="labelContent">
							<label for="detailed">详细地址:</label> <input type="text" maxlength="100"
								name="detailed" value="${compan.detailed }"/>
						</div>
						<div class="labelContent">
							<label for="brand">主营品牌:</label> <input type="text" name="brand"
								value="${compan.brand }" maxlength="10" />
						</div>
						<div class="labelContent">
							<label for="url">公司网址:</label> <input type="text" name="url"
								value="${compan.url }" maxlength="100" />
						</div>
						<div class="labelContent">
							<label for="phone">公司电话:</label> <input type="text" name="phone"
								value="${compan.phone }" maxlength="15" />
						</div>
						<div class="labelContent">
							<label for="email">公司邮箱:</label> <input type="text" name="email"
								value="${compan.email }" maxlength="50" />
						</div>
						<div class="labelContent">
							<label for="email">公司简介:</label> <input type="text" name="intro" maxlength="200"
								value="${compan.intro }" />
						</div>


						<div class="editInfo-footer">
							<button class="btn btn-primary" id="submit">保存</button>
							<button class="btn btn-danger" onclick="history.go(-1);">取消</button>
						</div>
					</div>
				</form>
			</div>
		</section>
		<!--右侧内容[[-->
	</main>
</body>
</html>




