<%@page import="com.sirius.baidu.ueditor.ActionEnter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	try {
		request.setCharacterEncoding("utf-8");
		response.setHeader("Content-Type", "text/html");
		String rootPath = application.getRealPath("/");
		out.write(new ActionEnter(request, rootPath).exec());
	} catch (Exception e) {
		e.printStackTrace();
	}
%>