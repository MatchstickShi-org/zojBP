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
<table id="brodcastMsgDatagrid" border="false"></table>
<div id="brodcastMsgDatagridToolbar">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddBroadcastMsgWindowBtn" style="float: left;">新增广播消息</a>
	<div class="datagrid-btn-separator" style="float: left;"></div>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="refreshBroadcastMsgGridBtn">刷新</a>
</div>
<script type="text/javascript" src="pages/sysMgr/msgBroadcast/index.js"></script>
</body>
</html>