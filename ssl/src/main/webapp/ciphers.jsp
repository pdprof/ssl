<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.TreeMap"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=request.getAttribute("host")%>:<%=request.getAttribute("port")%>
	Supported Ciphers</title>
</head>
<body>
	<h2><%=request.getAttribute("host")%>:<%=request.getAttribute("port")%></h2>
	<hr>
	<form action="socket" method="get">
		<table>
			<tr>
				<td align="right">host:</td>
				<td align="left"><input type="text" name="host" size="50"></input></td>
			</tr>
			<tr>
				<td align="right">port:</td>
				<td align="left"><input type="text" name="port" size="8"></input></td>
			</tr>
		</table>
		<button name="action" value="get">Check Ciphers</button>
	</form>
	<hr>
	<table>
		<%
		TreeMap<String, Boolean> map = (TreeMap<String, Boolean>) request.getAttribute("ciphers");
		for (String cipher : map.keySet()) {
		%>
		<tr>
			<td><%=cipher%></td>
			<td><%=map.get(cipher)%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>