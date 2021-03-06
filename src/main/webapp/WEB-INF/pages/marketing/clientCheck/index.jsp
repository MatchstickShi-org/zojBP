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
		<table id="orderCheckDatagrid" border="false"></table>
		<div id="orderCheckDatagridToolbar">
			<label style="vertical-align: middle;">名称：</label>
			<input style="width:120px;" class="easyui-textbox" id="orderApprove-nameInput"/>
			<label style="vertical-align: middle;">单号：</label>
			<input style="width:120px;" class="easyui-textbox" id="orderApprove-idInput"/>
			<label style="vertical-align: middle;">电话：</label>
			<input style="width:120px;" class="easyui-textbox" id="orderApprove-telInput"/>
			<label style="vertical-align: middle;">信息员名称：</label>
			<input style="width:120px;" class="easyui-textbox" id="orderApprove-infoerNameInput"/>
			<label>下属业务员：</label>
			<select style="width: 120px;" class="easyui-combobox" id="orderApprove-salesmanCombobox" data-options="textField: 'alias'">
				<option value="" style="color: gray;">-- 全部 --</option>
				<c:forEach items="${requestScope.underling}" var="underling">
					<option value="${underling.id}">${underling.alias}</option>
				</c:forEach>
			</select>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryCheckOrderBtn">查询</a>
			<br>
			<label><input type="checkbox" value="1" name="orderApprove-isKey" />重点客户</label>
			<label><input type="radio" value="0" name="orderApprove-filterInput" checked="checked"/>全部客户</label>
			<label><input type="radio" value="1" name="orderApprove-filterInput" />我的客户</label>
			<label style="vertical-align: middle;">状态筛选：</label>
			<label><input type="checkbox" value="22" name="orderStatusInput" />打回申请</label>
			<label><input type="checkbox" value="30" name="orderStatusInput" />在谈单申请</label>
			<label><input type="checkbox" value="62" name="orderStatusInput"/>不准单申请</label>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showPermitOrderWindowBtn">批准</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="showRejectOrderWindowBtn">驳回</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="clientMgrTab">
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
		</div>
	</div>
</div>
<div id="permitOrderWindow"></div>
<div id="rejectOrderWindow"></div>
<script type="text/javascript">
var _session_loginUserId = ${sessionScope.loginUser.id};
var _session_loginUserRole = ${sessionScope.loginUser.role};
</script>
<script type="text/javascript" src="pages/marketing/clientCheck/index.js"></script>
</body>
</html>