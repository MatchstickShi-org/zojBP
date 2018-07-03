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
<div class="easyui-layout" data-options="fit:true" style="margin: 2px;">
	<div data-options="region:'center'">
		<table id="commissionMgr-orderGrid" border="false"></table>
		<div id="commissionMgr-orderGridToolbar">
			<label style="vertical-align: middle;">客户：</label>
			<input class="easyui-textbox" id="commissionCostMgr-clientName"/>
			<label style="vertical-align: middle;">单号：</label>
			<input class="easyui-numberbox" id="commissionCostMgr-orderId" data-options="precision:0"/>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="commissionMgr-queryBtn">查询</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 330px;">
		<div id="commissionMgr-tab" data-options="tools:'#commissionMgr-tabTools'">
			<div title="提成" border="false">
				<table id="commissionMgr-costGrid" border="false"></table>
			</div>
		</div>
		<div id="commissionMgr-tabTools" style="border-top: 0px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="commissionMgr-addCostBtn">新增提成记录</a>
		</div>
	</div>
</div>
<div id="commissionMgr-addCostWindow"></div>
<script type="text/javascript" src="pages/costMgr/commissionCostMgr/index.js"></script>
</body>
</html>