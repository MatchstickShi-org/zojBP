<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>车辆配件管理系统</title>
</head>
<body>
	<h3 style="color: red;">对不起，你访问的资源不存在。</h3>
</body>
<script type="text/javascript">
$(function()
{
	$.messager.alert('警告', '对不起，你访问的资源不存在。');
});
</script>
</html>