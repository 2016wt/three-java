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

<title>平台登录</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<link rel="stylesheet" href="resource/css/login.css" />
<style type="text/css">
table {
	border-collapse: separate;
	border-spacing: 30px;
}
</style>
<script type="text/javascript">
	$(function() {
		saveValidate();
		var rem = /phone=\d+/;
		//检查cookie中是否存有用户名
		if(rem.test(document.cookie)){
			var phone = rem.exec(document.cookie);
			phone = phone[0].match(/\d+/);
			$("#phone").val(phone);
			$("#remUser")[0].checked = true;
		}	
		$("#phone").focus();
		$("#login").click(
				function() {
					if (!$("#loginForm").valid()) {
						return;
					}
					var phone = $("#phone").val();
					var reg = /^\d{11}$/g;
					if(!reg.test(phone)){
						alert("手机号格式错误！");
					}
					
					
					startLoding();
					$.post("wholesaler/login", $("#loginForm").serializeArray(),
						function(result) {
							stopLoding();
							if (result.code == 0) {
								//系统当前时间
								var date = new Date();
								//是否勾选记住用户名
								if($("#remUser")[0].checked ){
									if(!rem.test(document.cookie)){
										//设置过期时间（默认10天）
										var expiresDays = 10;
										date.setTime(date.getTime+expiresDays*24*3600*1000);
										//添加cookie;
										document.cookie = "phone="+phone+";expires="+date.toGMTString();
									}
								}else{
									//若用户名已保存在cookie中，则将其删除
									if(rem.test(document.cookie)){
										date.setTime(date.getTime() - 1000);
										//删除cookie
										document.cookie = "phone="+phone+";expires="+date.toGMTString();
									}
								}
								location.href = "<%=basePath%>wholesaler/goodsList";
							}else if(result.code == -1){
									alert(result.message);
							}
						});
					});
	});
	function saveValidate() {
		validate = $('#loginForm').validate({
			unhighlight : function(element, errorClass, validClass) { // 验证通过清除messages
				$(element).removeError(errorClass);
			},
			errorPlacement : function(error, element) { // messages显示位置
				$(element).addError($(error).text());
			},
			rules : {
				password : {
					required : true
				},
				phone : {
					required : true
				}
			},
			messages : {
				password : {
					required : "不能为空",
				},
				phone : {
					required : "不能为空",
				}
			}
		});
	}
</script>
</head>

<body>
	<div class="login-full-page">
		<div class="login-box">
			<header class="login-header"> 
			<div class="login-img">
				<img src="resource/image/S_logo.png" alt="壹是壹科技" />
			</div>
<!-- 				<dl> -->
<!-- 					<dt>壹是壹科技</dt> -->
<!-- 					<dd>isonetech.com</dd> -->
<!-- 				</dl> -->
			</header>
			<div class="login-content">
				<form class="bs-example bs-example-form" role="form" id="loginForm" onsubmit="return false;"
					method="post">
					<div class="input-group input-group-lg">
						<span class="input-group-addon"> <img
							src="resource/img/name.png" />
						</span> <input class="form-control" type="text" maxlength="20"
							style="ime-mode:active" id="phone"
							name="phone" placeholder="请输入手机号">
					</div>
					<div class="input-group input-group-lg">
						<span class="input-group-addon"> <img
							src="resource/img/password.png" />
						</span> <input type="password" maxlength="20" class="form-control"
							name="password" placeholder="请输入密码">
					</div>
					<!-- <div class="input-group input-group-lg">
						<span class="input-group-addon"> <img
							src="resource/img/org_num.jpg" />
						</span> <input type="text" class="form-control" placeholder="请输入组织编码">
					</div> -->
					
					<div class="tip-info">
						<c:if test="${message ne null}">
							<div>
								<span style="color:red;">${message}</span>
							</div>
						</c:if>
						<div class="rem-info">

							<input type="checkbox" id="remUser" /> <label
								for="remPwd">记住用户名</label>
						</div>
						<a href="javascript:void(0)" class="finfPwd">
							<!-- 忘记密码 -->
						</a>
					</div>
					<div class="login-footer">
						<button id="login" class="btn btn-danger">登录</button>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</body>
</html>
