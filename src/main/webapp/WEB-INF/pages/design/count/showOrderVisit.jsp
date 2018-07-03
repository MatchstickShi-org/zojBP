<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<table style="width: 100%;">
	<tr><td height="300">
		<table id="orderVisitDatagrid" style="height: 330px;"></table>
	</td></tr> 
</table>
<script type="text/javascript" src="pages/design/count/showOrderVisit.js"></script>
</body>
</html>