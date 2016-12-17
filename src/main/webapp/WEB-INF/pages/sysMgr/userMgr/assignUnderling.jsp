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
<table style="width: 100%;">
	<tr height="300"><td>
		<table id="notAssignUnderlingGrid" style="width: 100%; height: 100%;"></table>
	</td></tr>
	<tr><td style="text-align: center;">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" id="assignUnderlingBtn">分配</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" id="cancelAssignUnderlingBtn">取消</a>
	</td></tr>
</table>
<script type="text/javascript">
var _request_isLeader = ${requestScope.isLeader};
var _param_leaderId = ${param.leaderId};
if(_request_isLeader == false)
{
	$.messager.alert('警告', '要为其分配下属的用户不是主管，无法分配。');
	$addUnderlingWindow.window('close');
}
</script>
<c:if test="${requestScope.isLeader == true}">
	<script type="text/javascript" src="pages/sysMgr/userMgr/assignUnderling.js"></script>
</c:if>
</body>
</html>