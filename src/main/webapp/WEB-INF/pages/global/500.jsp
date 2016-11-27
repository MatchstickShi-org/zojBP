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
	<h3 style="color: red;">服务器内部错误，请稍后再试。</h3>
	<span>详细信息：${msg}</span>
</body>
<script type="text/javascript">
$(function()
{
	$.messager.alert('警告', '${msg}' || '服务器内部错误，请稍后再试。');
});
</script>
</html>