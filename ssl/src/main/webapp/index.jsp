<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<body>
	<form action="httpsclient" method="get">
		<table>
			<tr>
				<td align="right">host:</td>
				<td align="left"><input type="text" name="host" size="50"></input></td>
			</tr>
			<tr>
				<td align="right">port:</td>
				<td align="left"><input type="text" name="port" size="8"></input></td>
			</tr>
			<tr>
				<td align="right">trust manager:</td>
				<td align="left"><select name="tm">
						<option value="default">default</option>
						<option value="custom">custom</option>
				</select></td>
			</tr>
		</table>
		<button name="action" value="get">Access to above HTTPS host:port</button>
	</form>
	<hr />
	<a href="socket">Check SSL port ciphers</a>
</body>
</html>
