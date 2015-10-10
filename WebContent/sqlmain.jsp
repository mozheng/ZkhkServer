<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="java.sql.Connection, java.sql.DriverManager,java.sql.Statement" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
 <%
	Class.forName("org.sqlite.JDBC");
	Connection connection = DriverManager.getConnection("jdbc:sqlite:JkServer.sqlite");
	Statement statement = connection.createStatement();
  	String key = request.getParameter("keywords");
  	String Sql;
  	if(key ==null)
  		key ="";
 	Sql = "select * from dev where mcode like '%" +key + "%'; ";
	//System.out.println(Sql);
	ResultSet rSet =  statement.executeQuery(Sql);
  %>
<table bgcolor = "#9999dd" border="1" width="400">
<%
while(rSet.next())
{
	out.println("<tr>");
	
	out.println("<td>");	
	out.println(rSet.getString(1));	
	out.println("</td>");	
	
	out.println("<td>");	
	out.println(rSet.getInt(2));	
	out.println("</td>");	
	
	out.println("</tr>");	
}
%>
</table>

</body>
</html>