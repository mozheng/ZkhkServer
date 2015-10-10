<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String key = request.getParameter("keywords");
String url= "main.jsp?keywords="+key;
%>
<jsp:include page="<%= url%>" />