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
		<div id="orderMgrTab">
			<div title="在谈单查询" border=false>
				<table id="orderDatagrid" border=false></table>
				<div id="orderDatagridToolbar">
					<label style="vertical-align: middle;">名称：</label>
					<input style="width:100px;" class="easyui-textbox" id="clientTrace.nameInput"/>
					<label style="vertical-align: middle;">电话：</label>
					<input style="width:100px;" class="easyui-textbox" id="clientTrace.telInput"/>
					<label style="vertical-align: middle;">信息员名称：</label>
					<input style="width:100px;" class="easyui-textbox" id="clientTrace.infoerNameInput"/>
					<label style="vertical-align: middle;">状态筛选：</label>
					<label><input type="checkbox" value="-1" name="statusInput" checked="checked"/>全部</label>
					<label><input type="checkbox" value="34" name="statusInput"/>在谈单已批准</label>
					<label><input type="checkbox" value="90" name="statusInput"/>已签单</label>
					<label><input type="checkbox" value="0" name="statusInput"/>死单</label>
					<label><input type="checkbox" value="60,62" name="statusInput"/>不准单审核中</label>
					<label><input type="checkbox" value="64" name="statusInput"/>不准单</label>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryOrderBtn">查询</a>
					<!-- <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddOrderWindowBtn">新增</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeOrderBtn">放弃</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="applyOrderBtn">申请在谈单</a> -->
				</div>
			</div>
			<div title="审核" border=false>
				<table id="orderCheckDatagrid" border=false></table>
				<div id="orderCheckDatagridToolbar">
					<label style="vertical-align: middle;">名称：</label>
					<input style="width:100px;" class="easyui-textbox" id="order.nameInput"/>
					<label style="vertical-align: middle;">电话：</label>
					<input style="width:100px;" class="easyui-textbox" id="order.telInput"/>
					<label style="vertical-align: middle;">信息员名称：</label>
					<input style="width:100px;" class="easyui-textbox" id="order.infoerNameInput"/>
					<label style="vertical-align: middle;">状态筛选：</label>
					<label><input type="checkbox" value="30" name="orderStatusInput" />在谈单申请</label>
					<label><input type="checkbox" value="62" name="orderStatusInput"/>不准单申请</label>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryCheckOrderBtn">查询</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showPermitOrderWindowBtn">批准</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="showRejectOrderWindowBtn">驳回</a>
				</div>
			</div>
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
							<td align="right" style="min-width: 80px;"><label>设计师：</label></td>
							<td><input name="designerName" readonly="readonly" class="easyui-textbox"/></td>
							<td align="right" style="min-width: 80px;"><label>录入日期：</label></td>
							<td><input name="insertTime" readonly="readonly" class="easyui-textbox"/></td>
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
							<td align="right"><label>工程地址：</label></td>
							<td colspan="3"><input name="projectAddr" style="width: 459px;" class="easyui-textbox" required="required"/></td>
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
			<div title="业务员回访记录" border=false>
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="orderVisitGrid"></table>
				    		<div id="orderVisitGridToolbar">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addOrderVisitBtn">新增</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div title="设计师回访记录" border=false>
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="orderStylistVisitGrid"></table>
						</td>
					</tr>
				</table>
			</div>
				<div title="信息费" border="false">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="infoCostGrid"></table>
				    		<div id="infoCostGridToolbar">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addInfoCostBtn">新增</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="reloadInfoCostBtn">刷新</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div title="提成" border="false">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="commissionCostGrid"></table>
				    		<div id="commissionCostGridToolbar">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addCommissionCostBtn">新增</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="reloadCommissionCostBtn">刷新</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<div id="addClientVisitWindow"></div>
<div id="addClientWindow"></div>
<div id="permitOrderWindow"></div>
<div id="selectInfoerWindow"></div>
<script type="text/javascript" src="pages/marketing/clientNegotiation/index.js"></script>
</body>
</html>