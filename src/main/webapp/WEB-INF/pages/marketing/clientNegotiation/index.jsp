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
		<table id="orderDatagrid" border="false"></table>
		<div id="orderDatagridToolbar">
			<label style="vertical-align: middle;">名称：</label>
			<input class="easyui-textbox" id="clientNegotiation-nameInput" style="width: 120px;"/>
			<label style="vertical-align: middle;">电话：</label>
			<input class="easyui-textbox" id="clientNegotiation-telInput" style="width: 120px;"/>
			<label style="vertical-align: middle;">信息员：</label>
			<input class="easyui-textbox" id="clientNegotiation-infoerNameInput" style="width: 120px;"/>
			<c:if test="${sessionScope.loginUser.role != 1}">
				<label>下属业务员：</label>
				<select style="width: 120px;" class="easyui-combobox" id="clientNegotiation-salesmanCombobox" data-options="textField: 'alias'">
					<option value="">-- 全部 --</option>
					<c:forEach items="${requestScope.underling}" var="underling">
						<option value="${underling.id}">${underling.alias}</option>
					</c:forEach>
				</select>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryOrderBtn">查询</a>
				<br>
				<label><input type="radio" value="0" name="clientNegotiation-orderFilterInput" />全部客户</label>
				<label><input type="radio" value="1" name="clientNegotiation-orderFilterInput" checked="checked"/>我的客户</label>
			</c:if>
			<c:if test="${sessionScope.loginUser.role == 1}">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryOrderBtn">查询</a>
				<br>
			</c:if>
			<label><input type="checkbox" value="1" name="clientNegotiation-isKey" />重点客户</label>
			<label style="vertical-align: middle;">状态筛选：</label>
			<label><input type="checkbox" value="" name="statusInput" />全部</label>
			<label><input type="checkbox" value="34" name="statusInput" checked="checked" />在谈单</label>
			<label><input type="checkbox" value="90" name="statusInput"/>已签单</label>
			<label><input type="checkbox" value="0" name="statusInput"/>死单</label>
			<label><input type="checkbox" value="60" name="statusInput"/>不准单-主案部经理审核中</label>
			<label><input type="checkbox" value="62" name="statusInput"/>不准单-商务部经理审核中</label>
			<label><input type="checkbox" value="64" name="statusInput"/>不准单</label>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showNewOrderWindowBtn" disabled="true">新生成客户</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="clientMgrTab" data-options="tools:'#clientNegotiationMgrTab-tools'">
			<div title="详情" border=false>
				<form id="editOrderForm" action="marketing/clientMgr/editOrder" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<table style="width: 100%; min-width: 700px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>名称：</label></td>
							<td style="min-width: 200px;"><input name="name" class="easyui-textbox" required="required" style="width: 154px;"/></td>
							<td align="right" style="min-width: 80px;"><label>联系电话：</label></td>
							<td width="70%"><input name="telAll" readonly="readonly" class="easyui-textbox" required="required" style="width: 154px;"/></td>
						</tr>
						<tr>
							<td align="right" style="min-width: 80px;"><label>信息员：</label></td>
							<td><input name="infoerName" readonly="readonly" class="easyui-textbox" style="width: 154px;"/></td>
							<td align="right" style="min-width: 80px;"><label>业务员：</label></td>
							<td><input name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 154px;"/></td>
						</tr>
						<tr>
							<td align="right" style="min-width: 80px;"><label>设计师：</label></td>
							<td><input name="designerName" readonly="readonly" class="easyui-textbox" style="width: 154px;"/></td>
							<td align="right" style="min-width: 80px;"><label>录入日期：</label></td>
							<td><input name="insertTime" readonly="readonly" class="easyui-textbox" style="width: 154px;"/></td>
						</tr>
						<tr>
							<td align="right"><label>面积：</label></td>
							<td><input name="projectAddr" style="width: 154px;" class="easyui-textbox" required="required"/></td>
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
							<td align="center" colspan="3">
								<a id="submitUpdateClientFormBtn" href="javascript:void(0)"  iconCls="icon-save" style="width: 60px;">保存</a>
								<a id="refreshUpdateClientFormBtn" href="javascript:void(0)" iconCls="icon-reload" style="width: 60px;">刷新</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<c:if test="${sessionScope.loginUser.role != 4}">
				<div title="业务员回访记录" selected="true">
		    		<table id="orderVisitGrid" border="false"></table>
				</div>
			</c:if>
			<c:if test="${sessionScope.loginUser.role != 1}">
				<div title="设计师回访记录">
		    		<table id="designerVisitGrid" border="false"></table>
				</div>
			</c:if>
			<div title="审核流程">
	    		<table id="orderApproveGrid" border="false"></table>
			</div>
			<div title="信息费">
	    		<table id="infoCostGrid" border="false"></table>
			</div>
			<div title="提成">
	    		<table id="commissionCostGrid" border="false"></table>
			</div>
		</div>
   		<div id="clientNegotiationMgrTab-tools" style="border-top: 0px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="clientNegotiationMgr-addOrderVisitBtn">新增回访记录</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="clientNegotiationMgr-addInfoCostBtn">新增信息费</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="clientNegotiationMgr-addCommissionCostBtn">新增提成</a>
		</div>
	</div>
</div>
<div id="addClientVisitWindow"></div>
<div id="selectInfoerWindow"></div>
<div id="showAddInfoCostWindow"></div>
<div id="showAddCommissionCostWindow"></div>
<div id="showAddNewOrderWindow"></div>
<script type="text/javascript">
var _session_loginUserId = ${sessionScope.loginUser.id};
var _session_loginUserRole = ${sessionScope.loginUser.role};
</script>
<script type="text/javascript" src="pages/marketing/clientNegotiation/index.js"></script>
</body>
</html>