<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<title>商品详情</title>
<jsp:include page="../common/importJs.jsp" />
<jsp:include page="../common/importCss.jsp" />
</head>
<body>
	<div id="content">
		<input type="hidden" name="id" value="${goods.id }" />
		<h5>基本信息</h5>
		<div class="labelContent label_name">
			<label for="goodsName">商品名称  ：</label> 
			<input type="text" name="goodsName" value="${goods.goodsName }" readonly="readonly" />
		</div>
		<div class="labelContent label_name">
			<label for="describe">商品描述  ：</label> <input class="label-w"
				type="text" name="describe" value="${goods.describe }"
				readonly="readonly" />
		</div>
		<h5>商品属性</h5>
		<div>
			<div class="goods-attrs">
				<div class="labelContent label_name">
					<label>品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌 ：</label>
					<input value="${trademark.name }" readonly="readonly" />
				</div>
				<div class="labelContent label_name">
					<label>版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型 ：</label>
					<input type="text" name="model" value="${goods.model }" readonly="readonly" />
				</div>
				<div class="labelContent label_name">
					<label>流行元素  ：</label>
					<input type="text" value="${fashion.name }" readonly="readonly" />
				</div>
				<div class="labelContent label_name">
					<label>商品分类  ：</label>
					<input type="text" value="${goodsgenre.name }" readonly="readonly" />
				</div>
				<div class="labelContent label_name">
					<label>风格分类  ：</label>
					<input type="text" value="${stylegenre.name }" readonly="readonly" />
				</div>
				<div class="labelContent label_name">
					<label>材&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;质  ：</label>
					<input type="text" name="quality" value="${goods.quality }" readonly="readonly" />
				</div>
			</div>
			<div class="goods-attrs">
				<div class="labelContent label_name">
					<label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;节  ：</label>
					<input readonly value="${goods.season eq 1 ?'春':''}${goods.season eq 2 ?'夏':''}${goods.season eq 3 ?'秋':''}${goods.season eq 4 ?'冬':''}" />
				</div>
				<div class="labelContent label_name">
					<label>省 &nbsp;/ &nbsp;市  ：</label> 
					<input type="text" value="${province.provinceName }/${city.cityName }" readonly="readonly" />
	<!-- 								<label>市 </label>  -->
	<!-- 								<input type="text" value="${city.cityName }" readonly="readonly" /> -->
				</div>
				<div class="labelContent label_name">
					<label>批货价  ：</label>
					<input type="text" name="price" value="${goods.price }" readonly="readonly" />
				</div>
				<div class="labelContent label_name">
					<label>市场价  ：</label>
					<input type="text" name="marketPrice" value="${goods.marketPrice }" readonly="readonly" />
				</div>
				<div class="labelContent label_name">
					<label>上市年份  ：</label>
					<input type="text" value="${goods.marketYear }" readonly="readonly" />
				</div>
				<div class="labelContent label_name">
					<label>出厂日期  ：</label>
					<input type="text" name="factoryDate" value="${goods.factoryDate }" readonly="readonly" />
				</div>
			</div>
		</div>
		<h5>商品规格</h5>
		<c:forEach items="${goodsSpecificationList }" var="specification">
			<div class="specification">
				<input type="hidden" id="id" name="id" value="${specification.id }" />
				<div class="labelContent goods-specification">
					<label>尺&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</label>
					<c:if test="${specification.size eq 'S'}">
						<input type="text" name="color" id="color" value="S" readonly="readonly" /> 
					</c:if>
					<c:if test="${specification.size eq 'M'}">
						<input type="text" name="color" id="color" value="M" readonly="readonly" /> 
					</c:if>
					<c:if test="${specification.size eq 'L'}">
						<input type="text" name="color" id="color" value="L" readonly="readonly" /> 
					</c:if>
					<c:if test="${specification.size eq 'XL'}">
						<input type="text" name="color" id="color" value="XL" readonly="readonly" /> 
					</c:if>
					<label>颜&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色</label>
					<input type="text" name="color" id="color" value="${specification.color }" readonly="readonly" /> 
					<label>件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数</label>
					<input type="text" id="quantity" name="quantity" value="${specification.quantity }" readonly="readonly" />
				</div>
				
			</div>
		</c:forEach>
		<h5>商品图片</h5>
		<div id="goods-images">
			<c:forEach items="${imgList }" var="img">
				<div>
					<img src="${img.imgUrl }" width='150px' height='200px' />
				</div>
				
			</c:forEach>	
		</div>
		
	</div>
</body>

</html>
