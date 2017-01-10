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
<form id="addNewOrderForm" action="marketing/clientMgr/addNewOrder" method="post" style="width: 100%;">
	<input type="hidden" name="infoerId" value="${order.infoerId}">
<%-- 	<input type="hidden" name="designerId" value="${order.designerId}"> --%>
	<input type="hidden" id="salesmanIdInput" name="salesmanId" value="${order.salesmanId}">
	<table style="width: 100%">
	<tr>
		<td style="min-width: 70px;" align="right"><label>联系人：</label></td>
		<td><input name="name" value="${order.name}" readonly="readonly" class="easyui-textbox" required="required" style="width: 140px;"/></td>
		<td style="min-width: 75px;" align="right"><label>所属业务员：</label></td>
		<td><input id="salesmanNameSearchbox" name="salesmanName" required="required" prompt="请选择业务员" editable="false" class="easyui-searchbox" value="${order.salesmanName}" style="width: 136px;"/></td>
	</tr>
	<tr>
		<td align="right"><label>联系电话1：</label></td>
		<td><input name="tel1" id="tel1" value="${order.tel1}" readonly="readonly" required="required" class="easyui-textbox" style="width: 140px;"/></td>
		<td align="right"><label>联系电话2：</label></td>
		<td><input name="tel2" id="tel2" value="${order.tel2}" readonly="readonly" class="easyui-textbox" style="width: 140px;"/></td>
	</tr>
	<tr>
		<td align="right"><label>联系电话3：</label></td>
		<td><input name="tel3" id="tel3" value="${order.tel3}" readonly="readonly" class="easyui-textbox"  style="width: 140px;"/></td>
		<td align="right"><label>联系电话4：</label></td>
		<td><input name="tel4" id="tel4" value="${order.tel4}" readonly="readonly" class="easyui-textbox"  style="width: 140px;"/></td>
	</tr>
	<tr>
		<td align="right"><label>联系电话5：</label></td>
		<td><input name="tel5" id="tel5" value="${order.tel5}" readonly="readonly" class="easyui-textbox"  style="width: 140px;"/></td>
		<td align="right"><label>所属信息员：</label></td>
		<td><input name="infoerName" id="infoerName" value="${order.infoerName}" readonly="readonly" class="easyui-textbox"  style="width: 140px;"/></td>
<!-- 				<td colspan="2"><font id="errorclienttel" color="red"></font></td> -->
	</tr>
	<tr>
		<td align="right"><label>单位地址：</label></td>
		<td colspan="3"><input name="orgAddr" value="${order.orgAddr}" class="easyui-textbox" style="width: 398px;"/></td>
	</tr>
	<tr>
		<td align="right"><label>工程名称：</label></td>
		<td colspan="3"><input name="projectName" value="${order.projectName}" class="easyui-textbox" style="width: 398px;"/></td>
	</tr>
	<tr>
		<td align="right"><label>工程地址：</label></td>
		<td colspan="3"><input name="projectAddr" value="${order.projectAddr}" class="easyui-textbox" style="width: 398px;"/></td>
	</tr>
		<tr>
			<td align="center" colspan="4">
				<a class="easyui-linkbutton" id="addNewOrderRecordBtn" iconCls="icon-ok" href="javascript:void(0)">新增</a>
				<a class="easyui-linkbutton" onclick="$showAddNewOrderWindow.window('close');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>
			</td>
		</tr>
	</table>
</form>
<div id="selectSalesmanWindow"></div>
<script type="text/javascript">
var $showAddNewOrderWindow = $('div#showAddNewOrderWindow');
var _req_errorMsg = '${requestScope.errorMsg}';
if(_req_errorMsg != '')
{
	$.messager.alert('警告', _req_errorMsg, 'WARN');
	$showAddNewOrderWindow.window('close');
}
</script>
<c:if test="${requestScope.errorMsg == null}">
	<script type="text/javascript" src="pages/marketing/clientNegotiation/addNewOrder.js"></script>
</c:if>
</body>
</html>