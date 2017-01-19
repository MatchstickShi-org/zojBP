<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<div data-options="region:'center'" style="width: 470px;">
		<table id="marketingCountDatagrid" border=false></table>
		<div id="marketingCountDatagridToolbar">
			<label>业务员名称：</label>
			<input class="easyui-textbox" id="order.salesmanNameInput" style="width: 120px;"/>
			<label>开始日期：</label>
			<input id="order.startDateInput"  type= "text" class= "easyui-datebox"/> 
			<label>截止日期：</label>
			<input id="order.endDateInput"  type= "text" class= "easyui-datebox" /> 
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryOrderBtn">查询</a>
		</div>
	</div>
</div>
<div id="addClientVisitWindow"></div>
<script type="text/javascript">
var _session_loginUserId = ${sessionScope.loginUser.id};
</script>
<script type="text/javascript" src="pages/marketing/count/index.js"></script>
</body>
</html>