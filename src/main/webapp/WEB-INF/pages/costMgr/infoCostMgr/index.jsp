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
<table id="infoCostDatagrid" border="false"></table>
<div id="infoCostDatagridToolbar">
	<label style="vertical-align: middle;">客户：</label>
	<input class="easyui-textbox" id="infoCostMgr.clientName"/>
	<label style="vertical-align: middle;">单号：</label>
	<input class="easyui-textbox" id="infoCostMgr.orderId"/>
	<label style="vertical-align: middle;">状态：</label>
	<label style="vertical-align: middle;"><input type="radio" name="infoCostMgr.status" checked="checked" value="">全部</label>
	<label style="vertical-align: middle;"><input type="radio" name="infoCostMgr.status" value="1">已打款</label>
	<label style="vertical-align: middle;"><input type="radio" name="infoCostMgr.status" value="0">未打款</label>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="infoCostMgr.queryBtn">查询</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddInfoCostWindowBtn">新增打款记录</a>
</div>
<div id="showAddInfoCostWindow"></div>
<script type="text/javascript" src="pages/costMgr/infoCostMgr/index.js"></script>
</body>
</html>