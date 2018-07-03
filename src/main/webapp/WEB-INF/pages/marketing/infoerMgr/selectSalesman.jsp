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
		<table id="selSalesmanDatagrid" style="height: 330px;"></table>
	</td></tr> 
	<tr><td style="text-align: center;">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" id="selectSalesmanBtn">选择</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('div#businessTransferWindow').window('close');">取消</a>
	</td></tr>
</table>
<script type="text/javascript" src="pages/marketing/infoerMgr/selectSalesman.js"></script>
</body>
</html>