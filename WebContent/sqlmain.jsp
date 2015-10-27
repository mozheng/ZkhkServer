<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="java.sql.Connection, java.sql.DriverManager,java.sql.Statement" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
 <%
	Class.forName("org.sqlite.JDBC");
	Connection connection = DriverManager.getConnection("jdbc:sqlite://c:/jkserver.sqlite");
	Statement statement = connection.createStatement();
  	String key = request.getParameter("keywords");
  	String Sql;
  	//statement.executeUpdate("DROP table dev;");
	//Sql = "create table dev(mcode varchar(50),hp int,sp int,pulse int,time varchar(30),sendtime varchar(30));";
	//statement.executeUpdate(Sql);
  	if(key ==null)
  		key ="";
 	Sql = "select * from dev where mcode like '%" +key + "%'; ";
	//System.out.println(Sql);
	ResultSet rSet =  statement.executeQuery(Sql);
  %>
<table bgcolor = "#9999dd" border="1" width="400">
<tr>
<td>机器码</td>
<td>高压</td>
<td>低压</td>
<td>脉搏</td>
<td>测量时间</td>
<td>接受时间</td>
</tr>
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
	out.println("<td>");	
	out.println(rSet.getInt(3));	
	out.println("</td>");	
	out.println("<td>");	
	out.println(rSet.getInt(4));	
	out.println("</td>");	
	out.println("<td>");	
	out.println(rSet.getString(5));	
	out.println("</td>");	
	out.println("<td>");	
	out.println(rSet.getString(6));	
	out.println("</td>");	
	
	out.println("</tr>");	
}
%>
</table>

</body>
</html>