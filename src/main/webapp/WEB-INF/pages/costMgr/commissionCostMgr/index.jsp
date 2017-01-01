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
<table id="commissionCostDatagrid" border="false"></table>
<div id="commissionCostDatagridToolbar">
	<label style="vertical-align: middle;">客户：</label>
	<input class="easyui-textbox" id="commissionCostMgr.clientName"/>
	<label style="vertical-align: middle;">单号：</label>
	<input class="easyui-textbox" id="commissionCostMgr.orderId"/>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="commissionCostMgr.queryBtn">查询</a>
</div>
<script type="text/javascript" src="pages/costMgr/commissionCostMgr/index.js"></script>
</body>
</html>