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
		<table id="orderDatagrid" border=false></table>
		<div id="orderDatagridToolbar">
			<label>名称：</label>
			<input class="easyui-textbox" id="clientTrace.nameInput" style="width: 120px;"/>
			<label>电话：</label>
			<input class="easyui-textbox" id="clientTrace.telInput" style="width: 120px;"/>
			<label>信息员名称：</label>
			<input class="easyui-textbox" id="clientTrace.infoerNameInput" style="width: 120px;"/>
			<label><input type="radio" value="0" name="clientTrace.infoerFilterInput" />全部客户</label>
			<label><input type="radio" value="1" name="clientTrace.infoerFilterInput" checked="checked"/>我的客户</label>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryOrderBtn">查询</a>
			<br>
			<label>状态：</label>
			<label><input type="checkbox" value="" name="statusInput" />全部</label>
			<label><input type="checkbox" value="10" name="statusInput" checked="checked" />正跟踪</label>
			<label><input type="checkbox" value="12" name="statusInput"/>已放弃</label>
			<label><input type="checkbox" value="30" name="statusInput"/>在谈单审核中</label>
			<label><input type="checkbox" value="14" name="statusInput"/>在谈单已打回</label>
 			<!--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddOrderWindowBtn">新增</a> -->
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeOrderBtn" disabled="true">放弃</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="applyOrderBtn" disabled="true">申请在谈单</a>
			<c:if test="${sessionScope.loginUser.role == 3}">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="showBusinessTransferWindowBtn">业务转移</a>
			</c:if>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="clientMgrTab" data-options="tools:'#infoerMgrTab-tools'">
			<div title="详情">
				<form id="editOrderForm" action="marketing/clientMgr/editOrder" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<table style="width: 100%; min-width: 700px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>名称：</label></td>
							<td style="min-width: 200px;"><input name="name" class="easyui-textbox" required="required"/></td>
							<td align="right" style="min-width: 80px;"><label>联系电话：</label></td>
							<td width="70%"><input name="telAll" readonly="readonly" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right" style="min-width: 80px;"><label>信息员：</label></td>
							<td><input name="infoerName" readonly="readonly" class="easyui-textbox"/></td>
							<td align="right" style="min-width: 80px;"><label>业务员：</label></td>
							<td><input name="salesmanName" readonly="readonly" class="easyui-textbox"/></td>
						</tr>
						<tr>
							<td align="right"><label>单位地址：</label></td>
							<td colspan="3"><input name="orgAddr" style="width: 459px;" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>工程名称：</label></td>
							<td colspan="3"><input name="projectName" style="width: 459px;" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>面积：</label></td>
							<td colspan="3"><input name="projectAddr" style="width: 459px;" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="center" colspan="3">
								<a id="submitUpdateClientFormBtn" href="javascript:void(0)" iconCls="icon-save" style="width: 60px;">保存</a>
								<a id="refreshUpdateClientFormBtn" href="javascript:void(0)" iconCls="icon-reload" style="width: 60px;">刷新</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<c:if test="${sessionScope.loginUser.role != 4}">
				<div title="回访记录" selected="true">
		    		<table id="orderVisitGrid" border="false"></table>
				</div>
			</c:if>
			<div title="审核流程">
	    		<table id="orderApproveGrid" border="false"></table>
			</div>
		</div>
		<div id="infoerMgrTab-tools" style="border-top: 0px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addOrderVisitBtn">新增回访记录</a>
		</div>
	</div>
</div>
<div id="addClientVisitWindow"></div>
<div id="addClientWindow"></div>
<div id="applyOrderWindow"></div>
<div id="selectInfoerWindow"></div>
<div id="businessTransferWindow"></div>
<script type="text/javascript">
var _session_loginUserId = ${sessionScope.loginUser.id};
</script>
<script type="text/javascript" src="pages/marketing/clientTrace/index.js"></script>
</body>
</html>