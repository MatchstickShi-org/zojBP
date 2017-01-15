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
<form id="addCommissionCostForm" action="costMgr/commissionCostMgr/addCommissionCost" method="post" style="width: 100%;">
	<input type="hidden" name="infoerId" value="${order.infoerId}">
	<input type="hidden" name="designerId" value="${order.designerId}">
	<input type="hidden" name="salesmanId" value="${order.salesmanId}">
	<table style="width: 100%">
		<tr>
			<td align="right"><label>单号：</label></td>
			<td style="width: 170px;"><input name="orderId" value="${order.id}" class="easyui-textbox" data-options="required:true, validType:'length[4, 16]'" readonly="readonly" style="width: 160px;"/></td>
			<td align="right"><label>客户：</label></td>
			<td><input name="clientName" class="easyui-textbox" value="${order.name}" readonly="readonly" style="width: 160px;"></td>
		</tr>
		<tr>
			<td align="right"><label>信息员：</label></td>
			<td><input name="infoer" value="${order.infoerName}" readonly="readonly" class="easyui-textbox" style="width: 160px;"/></td>
			<td align="right"><label>设计师：</label></td>
			<td><input name="designer" value="${order.designerName}" readonly="readonly" class="easyui-textbox" style="width: 160px;"/></td>
		</tr>
		<tr>
			<td align="right"><label>业务员：</label></td>
			<td><input name="salesman" value="${order.salesmanName}" readonly="readonly" class="easyui-textbox" style="width: 160px;"/></td>
			<td align="right"><label>面积：</label></td>
			<td><input name="projectAddr" value="${order.orgAddr}" readonly="readonly" class="easyui-textbox" style="width: 160px;"/></td>
		</tr>
		<tr>
			<td align="right"><label>打款金额：</label></td>
			<td><input name="cost" value="500.00" required="required" class="easyui-numberbox" style="width: 160px;" data-options="min: 0, precision: 2, prefix: '￥'"/></td>
			<td align="right"><label>备注：</label></td>
			<td><input name="remark" class="easyui-textbox" style="width: 160px;"/></td>
		</tr>
		<tr>
			<td align="center" colspan="4">
				<a class="easyui-linkbutton" id="addCommissionCostRecordBtn" iconCls="icon-ok" href="javascript:void(0)">新增</a>
				<a class="easyui-linkbutton" onclick="$showAddCommissionCostWindow.window('close');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
var $showAddCommissionCostWindow = $('div#showAddCommissionCostWindow');
var _req_errorMsg = '${requestScope.errorMsg}';
if(_req_errorMsg != '')
{
	$.messager.alert('警告', _req_errorMsg, 'WARN');
	$showAddCommissionCostWindow.window('close');
}
</script>
<c:if test="${requestScope.errorMsg == null}">
	<script type="text/javascript" src="pages/marketing/clientNegotiation/addCommissionCost.js"></script>
</c:if>
</body>
</html>