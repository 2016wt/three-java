
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>

<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1" />
<title>${title }</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
<script type="text/javascript" src="/resource/cityPicker/js/cityPicker.js"></script>
<link rel="stylesheet" href="/resource/cityPicker/css/cityPicker.css">

<script type="text/javascript">

	/* 点击图片进行预览 */
	$(document).ready(function(){
		$("#img").change(function(){

			
			$("#userdefinedFile").val($("#img").val());
			//获得图片
			var img = document.getElementById("img");
			var reader = new FileReader();
			//读取图片
			reader.readAsDataURL(img.files[0]);
			
			reader.onload = function(){
				document.getElementById("imgDiv").innerHTML+="<img src='"+reader.result+"' onclick='removeImg(this)'/>";
			};			
		});
	});


	$(function() {
		
		saveValidate() ;
		
		$("#add").click(function() {
			$(".goods-attr").append(
					"<div class='specification'>"
						+"<span>尺&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码 </span>"
						+"<select name='size' id='size'>"
						+ "<option value=''>请选择</option>"
						+ "<option value='S'>S</option>"
						+ "<option value='M'>M</option>"
						+ "<option value='L'>L</option>"
						+ "<option value='XL'>XL</option>"
						+ "</select> <span>颜&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色</span>"
						+"<input type='text' id='color' name='color' /> "
						+ "<span>件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数 </span>"
						+"<input type='text'name='quantity' id='quantity' /> "
				 + "</div>");						
		});
		
		
		$("#submit").click(
				function() {

					var boo = true;
					
					
					$("#city").val(CityPicker.cityCode);
					//获得img
					var imgs = document.getElementById("imgDiv").getElementsByTagName("img");
					//var imgs = $(".goods-images");
					for(var a = 0;a<imgs.length;a++){
						if(imgs[a].style.display=="none"){
							if(imgs[a].id==""){
								var img = document.getElementById("img");
								img.value="";
							}
							//发送请求把display=none的图片实现提交删除
							$.post("wholesaler/deleteImg/"+imgs[a].id,{},function(result){

							});
						}else{
							
						}
					}
					//获得图片
					var img = document.getElementById("img");
					/* 判断是否有图片 */
					if(img.value==""){//操作没有图片
							var list = [];
							
							$(".specification").each(function(index){
								var o = {
										size:$(this).find("#size").val(),
										color:$(this).find("#color").val(),
										quantity:$(this).find("#quantity").val(),
										id:$(this).find("#id").val(),
										};
								list[index] = o;
							//	alert(list[index].size);
							
							var r = new RegExp(/^[+]{0,1}(\d+)$/);
								/* 验证尺码+颜色+库存不可为空 */
								if(list[index].size==""){
									bootbox.alert("尺码不可为空");
									boo = false;
									return false;
								}else if(list[index].color==""){
									bootbox.alert("颜色不可为空");
									boo = false;
									return false;
								}else if(list[index].quantity==""){
									bootbox.alert("库存不可为空");
									boo = false;
									return false;
								}else if(!r.test(list[index].quantity)){
									bootbox.alert("库存格式错误");
									boo = false;
									return false;
								}else{
									/* 验证尺码+颜色是否重复 */
									for(var a = 0;a<list.length-1;a++){
											
										for(var b = a+1;b<list.length;b++){
											
											var listA = list[a].size+list[a].color;//第一个
											var listB = list[b].size+list[b].color;//第二个以上
											//alert(listA+"--"+listB);
											if(listA==listB){
												bootbox.alert("尺寸+颜色不可重复");
												boo = false;
												return false;
											}
										}	
									}
								}
							});
							
							
							if(!boo){
								return ;
							}
							
							/* 开始验证 */
							if (!$("#form").valid()) {
								return;
							}
							
							var param = $("#form").serializeArray(); 

							param[param.length]  = {name:"specification",value:JSON.stringify(list)};

							if (!$("#form").valid()) {
								return;
							}
							startLoding();
							$.post("wholesaler/goods", param,
									function(result) {
										stopLoding();
											bootbox.alert(result.message,function(){
												if(result.code==0){
													window.location.href="wholesaler/goodsList";
												}
											});
									});
							return false;
					}else{//操作有图片
							//图片大小
							var imgSize = document.getElementById("img").files[0].size;
							if(imgSize>1024*1024*2){
								bootbox.alert("图片不可超出2M");
								return false;
							}
							var reader = new FileReader();
							//读取图片
							reader.readAsDataURL(img.files[0]);
							//对图片进行编码
							reader.onload = function(evt){
							var base64 = reader.result;
						
						var list = [];
						
						$(".specification").each(function(index){
							var o = {
									size:$(this).find("#size").val(),
									color:$(this).find("#color").val(),
									quantity:$(this).find("#quantity").val(),
									id:$(this).find("#id").val(),
									};
							list[index] = o;
						
						var r = new RegExp(/^[+]{0,1}(\d+)$/);
							/* 验证尺码+颜色+库存不可为空 */
							if(list[index].size==""){
								bootbox.alert("尺码不可为空");
								boo = false;
								return false;
							}else if(list[index].color==""){
								bootbox.alert("颜色不可为空");
								boo = false;
								return false;
							}else if(list[index].quantity==""){
								bootbox.alert("库存不可为空");
								boo = false;
								return false;
							}else if(!r.test(list[index].quantity)){
								bootbox.alert("库存格式错误");
								boo = false;
								return false;
							}else{
								/* 验证尺码+颜色是否重复 */
								for(var a = 0;a<list.length-1;a++){
										
									for(var b = a+1;b<list.length;b++){
										
										var listA = list[a].size+list[a].color;//第一个
										var listB = list[b].size+list[b].color;//第二个以上
										//alert(listA+"--"+listB);
										if(listA==listB){
											bootbox.alert("尺寸+颜色不可重复");
											boo = false;
											return false;
										}
									}	
								}
							}
						});
						
						
						if(!boo){
							return ;
						}
						
						/* 开始验证 */
						if (!$("#form").valid()) {
							return;
						}
						
						var param = $("#form").serializeArray(); 
	
						param[param.length] = {name:"specification",value:JSON.stringify(list)};
						param[param.length] = {name:"base64",value:base64};
					
						if (!$("#form").valid()) {
							return;
						}
						startLoding();
						$.post("wholesaler/goods", param,
								function(result) {
									stopLoding();
									bootbox.alert(result.message,function(){
										if (result.code == 0) {
											window.location.href="wholesaler/goodsList";
										}
									});
								});
						return false;
						}
					}
				});
	});

	/* 点击图片删除预览 */
	function removeImg(img){
		bootbox.confirm("您确定要删除吗？",function(result){
			if(result){
				img.style.display="none";
				$("#userdefinedFile").val("");
			}	
		});
	}
	
	/* 点击图片删除 */
	function deleteImg(img){
		bootbox.confirm("您确定要删除吗？",function(result){
			if(result){
				img.style.display="none";
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
			},
			rules : {
				goodsName : {
					required : true,
					reg : /^\S{0,50}$/,
				},
				describe : {
					required : true,
					reg : /^\S{0,100}$/,
				},
				fashionId : {
					required : true
				},
				productionPlace : {
					required : true
				},
				goodsgenre : {
					required : true
				},
				stylegenre : {
					required : true
				},
				quality : {
					required : true,
					reg : /^\S{0,10}$/,
				},
				season : {
					required : true
				},
				price : {
					required : true
				},
				marketYear : {
					required : true
				},
				marketPrice : {
					required : true
				},
				factoryDate : {
					required : true
				},
				model : {
					required : true,
					reg : /^\S{0,10}$/,
				},
				trademarkId : {
					required : true
				},
				cityCode : {
					required : true
				},
			},
			messages : {
				goodsName : {
					required : "商品名称不能为空",
					reg : "商品名称不可超出50",
				},
				describe : {
					required : "商品描述不能为空",
						reg : "商品描述不可超出100",
				},
				fashionId : {
					required : "流行元素不能为空",
				},
				productionPlace : {
					required : "产地不能为空",
				},
				goodsgenre : {
					required : "商品分类不能为空",
				},
				stylegenre : {
					required : "风格分类不能为空",
				},
				quality : {
					required : "材质不能为空",
					reg : "材质不可超出10",
				},
				season : {
					required : "季节不能为空",
				},
				price : {
					required : "批货价不能为空",
				},
				marketYear : {
					required : "上市年份不能为空",
				},
				marketPrice : {
					required : "市场价不能为空",
				},
				factoryDate : {
					required : "出场日期不能为空",
				},
				model : {
					required : "版型不能为空",
					reg : "版型不可超出10",
				},
				trademarkId : {
					required : "品牌不能为空",
				},
				cityCode : {
					required : "城市不能为空",
				},
			}
		});
	}
	
	function showPicker(el){
		CityPicker.show({
			target:el,
			level:2,
			url:[
				"/address/provinceList",
				"/address//cityList"
			]
		});
// 		$("#provinceValue").val(CityPicker.provinceCode);
// 		$("#cityValue").val(CityPicker.cityCode);
		
	}
 /* 	$("#img").change = function() {
 		$("#userdefinedFile").val($("#img").val());
	  }  */
</script>

</head>
<body>
	<!-- 头部]] -->
	<jsp:include page="../common/top.jsp" />
	<!-- 内容[[ -->
	<main id="page-wrapper">
		<!-- 左侧导航[[ -->
		<jsp:include page="../common/left.jsp" />
		<!-- 左侧导航]] -->
		<!--右侧内容[[-->
		<section id="content-wrapper">
			<div class="widget-box">
				<div class="widget-title">
<!-- 					<span class="icon"><i class="fa fa-table"></i></span> -->
					<h5>${title}</h5>
				</div>
				<div>
					<form action="" id="form" onsubmit="return false;" enctype="application/x-www-form-urlencoded">
						<!-- Stack the columns on mobile by making one full-width and the other half-width -->
						<!-- 隐藏域商品id -->
						<input type="hidden" name="id" value="${goods.id }"/>
						<div class="labelContent goods-title">
							<label for="goodsName">商品名称:</label>
							<input type="text" name="goodsName" value="${goods.goodsName }" />
						</div>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
						<div class="labelContent goods-title">
							<label for="describe">商品描述:</label>
							<input type="text" name="describe" value="${goods.describe }" style="height:60px"/>
						</div>
						<div style="overflow:hidden">
							<div style="float:left">
								<div class="labelContent">
								<label for="describe">流行元素:</label><select  name="fashionId">
									<option value="">请选择</option>
									<c:forEach items="${fashionList }" var="fashion">
										<option ${fashion.id eq goods.fashionId?'selected':''} value="${fashion.id }">${fashion.name }</option>
									</c:forEach>
								</select>
								</div>
								<div class="labelContent">
									<label for="ProductionPlace">省/市:</label>
									<div style="float:left">
										<input id="district" readonly type="text" value="${goods.province }/${goods.city}" onclick="showPicker(this)">
										<input type="hidden" id="city" name="ProductionPlace" value="" />	
									</div>
								</div>
								<div style="clear:both"></div>
								<div class="labelContent" style="margin-top:30px">
									<label for="goodsgenre">商品分类:</label><select name="goodsgenre">
										<option selected value="">请选择</option>
										<c:forEach items="${goodsgenreList }" var="goodsgenre">
											<option ${goodsgenre.id eq goods.goodsgenre?'selected':''} value="${goodsgenre.id }">${goodsgenre.name }</option>
										</c:forEach>
									</select>
								</div>
								<div class="labelContent">
									<label for="goodsgenre">风格分类</label><select name="stylegenre">
										<option value="">请选择</option>
										<c:forEach items="${stylegenreList }" var="stylegenre">
											<option ${stylegenre.id eq goods.stylegenre?'selected':''} value="${stylegenre.id }">${stylegenre.name }</option>
										</c:forEach>
									</select>
								</div>
								<div class="labelContent">
									<label for="goodsgenre">材&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;质</label><input
										type="text" name="quality" value="${goods.quality }"/>
								</div>
								<div class="labelContent">
									<label for="goodsgenre">季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;节</label><select name="season">
										<option  value="">请选择</option>
										<option  ${goods.season eq 1 ?'selected':''}  value="1">春季</option>
										<option  ${goods.season eq 2 ?'selected':''} value="2">夏季</option>
										<option  ${goods.season eq 3 ?'selected':''} value="3">秋季</option>
										<option  ${goods.season eq 4 ?'selected':''} value="4">冬季</option>
									</select>
								</div>
							</div>
							<div class="content-wrapper"  style="float:left">
								<div class="labelContent">
									<label for="goodsgenre">批货价:</label><input type="text" name="price" 
									value="${goods.price }" />
								</div>
								<div class="labelContent">
									<label for="goodsgenre">市场价:</label><input type="text" name="marketPrice"
									 value="${goods.marketPrice }" />
								</div>
								<div class="labelContent">
									<label for="goodsgenre">上市年份:</label><input type="text" name="marketYear" 
									onclick="WdatePicker({dateFmt: 'yyyy'});" value="${goods.marketYear }" />
								</div>
								<div class="labelContent">
									<label for="goodsgenre">出厂日期:</label><input type="text" 
									name="factoryDate" onclick="WdatePicker();" value="${goods.factoryDate }" />
								</div>
								<div class="labelContent">
									<label for="goodsgenre">版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型:</label><input type="text" name="model" 
									value="${goods.model }" />
								</div>
								<div class="labelContent">
									<label for="goodsgenre">品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌:</label><select name="trademarkId" >
										<option value="">请选择</option>
										<c:forEach items="${trademarkList }" var="trademark">
											<option ${goods.trademarkId eq trademark.id ?'selected':''}  value="${trademark.id }">${trademark.name }</option>
										</c:forEach>
									</select>
								</div> 
							</div>
						
						</div>
					</form>
					<div>
						<div id="specification">
							<!-- 添加 -->
							<c:if test="${goods eq null }">
								<c:set var="title" value="商品添加"/>
								<div class="goods-attr">
									<div class="specification">
										<input type="hidden" id="id" name="id" value="${specification.id }"/>
										<span>尺&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</span>
										<select name="size" id="size" >
											<option value="">请选择</option>
											<option value="S">S</option>
											<option value="M">M</option>
											<option value="L">L</option>
											<option value="XL">XL</option>
										</select>
										<span>颜&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色</span>
										<input type="text" name="color" id="color" /> 
										<span>件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数</span>
										<input type="text" id="quantity" name="quantity" maxlength="5" />
									</div>
								</div>
								<div style="margin:30px 0 30px 20px;">
									<a class="btn btn-primary" id="add">添加</a>
								</div>
								<div style="overflow:hidden;margin:30px 4px;" id="imgDiv">
								
								</div>
								<div class="file">
							
								    <div class="userdefined-file">
								        <input type="text" name="userdefinedFile" id="userdefinedFile" >
								        <button type="button">图片上传</button>
								    </div>
								    <input type="file" id="img" style="display:inline-block" name="imgUrl"/>
							    　</div>
								
							</c:if>
							<!-- 修改 -->
							<c:if test="${goods ne null }">
								<c:set var="title" value="商品修改"/>
								<div class="goods-attr">
									<c:forEach items="${goodsSpecificationList }" var="specification">
										<div class="specification">
											<input type="hidden" id="id" name="id" value="${specification.id }"/>
											<span>尺&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</span>
											<select name="size" id="size" >
												<c:if test="${specification.size eq 'S'}"><option value="S">S</option></c:if>
												<c:if test="${specification.size eq 'M'}"><option value="M">M</option></c:if>
												<c:if test="${specification.size eq 'L'}"><option value="L">L</option></c:if>
												<c:if test="${specification.size eq 'XL'}"><option value="XL">XL</option></c:if>
											</select>
											<span>颜&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色</span>
											<input type="text" name="color" id="color" value="${specification.color }" readonly="readonly"/> 
											<span>件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数</span>
											<input type="text" id="quantity" name="quantity" value="${specification.quantity }" maxlength="5" />
										</div>
									</c:forEach>
								</div>
								
								<div style="margin:30px 0 30px 20px;">
									<a class="btn btn-primary" id="add">添加</a>
								</div>
								
								<div style="overflow:hidden;margin:30px 4px;"  id="imgDiv">
									<c:forEach items="${imgList }" var="img">
										<img src="${img.imgUrl }" class="goods-images" 
										id=${img.id } onclick="deleteImg(this);" />
									</c:forEach>
								</div>
								<div class="file">
								    <div class="userdefined-file" style="float:right">
								        <input type="text" name="userdefinedFile" id="userdefinedFile">
								        <button type="button">图片上传</button>
								    </div>
							    	<input type="file" id="img" style="display:inline-block" name="imgUrl"/>
							    </div>
							</c:if>		
							 <div style="margin:30px 0 30px 456px">
								<a class="btn btn-primary" id="submit">提交</a> 
								<a class="btn btn-danger" href="javascript:history.go(-1);" style="background-color:#337ab7;color:#fff">取消</a>
							</div>		
						</div>	
					</div>	
				</div>
			</div>
		</section>
	</main>
</body>
</html>