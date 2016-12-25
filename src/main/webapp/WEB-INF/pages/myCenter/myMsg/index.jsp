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
<table id="myMsgGrid" border="false"></table>
<div id="myMsgGridToolbar">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="refreshMyMsgGridBtn">刷新</a>
</div>
<script type="text/javascript" src="pages/myCenter/myMsg/index.js"></script>
</body>
</html>