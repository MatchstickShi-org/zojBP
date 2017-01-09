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
		<table id="applyVisitDatagrid" border=false></table>
		<div id="applyVisitDatagridToolbar">
			<label>单号：</label>
			<input class="easyui-textbox" id="order.idInput" style="width: 120px;"/>
			<label>设计师名称：</label>
			<input class="easyui-textbox" id="order.designerNameInput" style="width: 120px;"/>
			<label>状态：</label>
			<label><input type="checkbox" value="" name="statusInput" />全部</label>
			<label><input type="checkbox" value="0" name="statusInput" checked="checked" />未审核</label>
			<label><input type="checkbox" value="1" name="statusInput"/>已审核</label>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryOrderBtn">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="permitVisitBtn" disabled="true">同意回访</a>
		</div>
	</div>
</div>
<div id="addClientVisitWindow"></div>
<script type="text/javascript">
var _session_loginUserId = ${sessionScope.loginUser.id};
</script>
<script type="text/javascript" src="pages/design/applyVisit/index.js"></script>
</body>
</html>