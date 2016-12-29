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
<form id="addInfoCostForm" action="costMgr/infoCostMgr/addInfoCost" method="post" style="width: 100%;">
	<input type="hidden" name="clientId" value="${infoCost.clientId}">
	<input type="hidden" name="infoerId" value="${infoCost.infoerId}">
	<input type="hidden" name="designerId" value="${infoCost.designerId}">
	<input type="hidden" name="salesmanId" value="${infoCost.salesmanId}">
	<table style="width: 100%">
		<tr>
			<td align="right"><label>单号：</label></td>
			<td style="width: 170px;"><input name="orderId" value="${infoCost.orderId}" class="easyui-textbox" data-options="required:true, validType:'length[4, 16]'" readonly="readonly" style="width: 160px;"/></td>
			<td align="right"><label>客户：</label></td>
			<td><input name="clientName" class="easyui-textbox" value="${infoCost.clientName}" readonly="readonly" style="width: 160px;"></td>
		</tr>
		<tr>
			<td align="right"><label>信息员：</label></td>
			<td><input name="infoer" value="${infoCost.infoer}" readonly="readonly" class="easyui-textbox" style="width: 160px;"/></td>
			<td align="right"><label>设计师：</label></td>
			<td><input name="designer" value="${infoCost.designer}" readonly="readonly" class="easyui-textbox" style="width: 160px;"/></td>
		</tr>
		<tr>
			<td align="right"><label>业务员：</label></td>
			<td><input name="salesman" value="${infoCost.salesman}" readonly="readonly" class="easyui-textbox" style="width: 160px;"/></td>
			<td align="right"><label>装修地址：</label></td>
			<td><input name="projectAddr" value="${infoCost.projectAddr}" readonly="readonly" class="easyui-textbox" style="width: 160px;"/></td>
		</tr>
		<tr>
			<td align="right"><label>打款金额：</label></td>
			<td><input name="cost" value="500.00" class="easyui-numberbox" style="width: 160px;" data-options="min: 0, precision: 2, prefix: '￥'"/></td>
			<td align="right"><label>备注：</label></td>
			<td><input name="remark" class="easyui-textbox" style="width: 160px;"/></td>
		</tr>
		<tr>
			<td align="center" colspan="4">
				<a class="easyui-linkbutton" id="addInfoCostBtn" iconCls="icon-ok" href="javascript:void(0)">新增</a>
				<a class="easyui-linkbutton" onclick="$showAddInfoCostWindow.window('close');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
var $showAddInfoCostWindow = $('div#showAddInfoCostWindow');
var _req_errorMsg = '${requestScope.errorMsg}';
if(_req_errorMsg != '')
{
	$.messager.alert('警告', _req_errorMsg, 'WARN');
	$showAddInfoCostWindow.window('close');
}
</script>
<c:if test="${requestScope.errorMsg == null}">
	<script type="text/javascript" src="pages/costMgr/infoCostMgr/addInfoCost.js"></script>
</c:if>
</body>
</html>