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
		<table id="orderDatagrid"></table>
		<div id="orderDatagridToolbar">
			<label style="vertical-align: middle;">名称：</label>
			<input class="easyui-textbox" id="clientTrace.nameInput"/>
			<label style="vertical-align: middle;">电话：</label>
			<input class="easyui-textbox" id="clientTrace.telInput"/>
			<label style="vertical-align: middle;">信息员名称：</label>
			<input class="easyui-textbox" id="clientTrace.infoerNameInput"/>
			<label style="vertical-align: middle;">状态筛选：</label>
			<input type="checkbox" value="-1" name="statusInput" checked="checked"/>全部
			<input type="checkbox" value="34" name="statusInput"/>在谈单已批准
			<input type="checkbox" value="90" name="statusInput"/>已签单
			<input type="checkbox" value="0" name="statusInput"/>死单
			<input type="checkbox" value="60,62" name="statusInput"/>不准单审核中
			<input type="checkbox" value="64" name="statusInput"/>不准单
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryOrderBtn">查询</a>
			<!-- <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddOrderWindowBtn">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeOrderBtn">放弃</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="applyOrderBtn">申请在谈单</a> -->
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="clientMgrTab">
			<div title="详情" style="padding: 2px;">
				<form id="editOrderForm" action="marketing/clientMgr/editOrder" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<table style="width: 100%; min-width: 700px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>名称：</label></td>
							<td style="min-width: 100px;"><input name="name" style="min-width: 280px;"  class="easyui-textbox" required="required"/></td>
							<td align="right" style="min-width: 80px;"><label>联系电话：</label></td>
							<td style="min-width: 280px;"><input name="telAll"  style="min-width: 280px;" readonly="readonly" class="easyui-textbox" required="required"/></td>
							<td align="right" style="vertical-align: mid; min-width: 80px;"><label>单位地址：</label></td>
							<td style="min-width: 280px;"><input style="min-width: 280px;" name="orgAddr" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>工程名称：</label></td>
							<td><input name="projectName" style="min-width: 280px;" class="easyui-textbox" required="required"/></td>
							<td align="right" style="min-width: 80px;"><label>工程地址：</label></td>
							<td><input name="projectAddr" style="min-width: 280px;" class="easyui-textbox" required="required"/></td>
							<td align="right" ><label>录入日期：</label></td>
							<td><input name="insertTime" style="min-width: 280px;" readonly="readonly" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right" style="min-width: 80px;"><label>信息员：</label></td>
							<td><input name="infoerName" style="min-width: 280px;" readonly="readonly" class="easyui-textbox"/></td>
							<td align="right" style="min-width: 80px;"><label>业务员：</label></td>
							<td><input name="salesmanName" style="min-width: 280px;" readonly="readonly" class="easyui-textbox"/></td>
							<td align="right" style="min-width: 80px;"><label>设计师：</label></td>
							<td><input name="stylistName" style="min-width: 280px;" readonly="readonly" class="easyui-textbox"/></td>
						</tr>
						<tr>
							<td align="center" colspan="6">
								<a id="submitUpdateClientFormBtn" href="javascript:void(0)" style="width: 60px;">保存</a>
								<a id="refreshUpdateClientFormBtn" href="javascript:void(0)" style="width: 60px;">刷新</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div title="业务员回访记录" style="padding: 2px;">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="orderVisitGrid" title="回访记录"></table>
				    		<div id="orderVisitGridToolbar">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addOrderVisitBtn">新增</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div title="设计师回访记录" style="padding: 2px;">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="orderStylistVisitGrid" title="回访记录"></table>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<div id="addClientVisitWindow"></div>
<div id="addClientWindow"></div>
<div id="applyOrderWindow"></div>
<div id="selectInfoerWindow"></div>
<script type="text/javascript" src="pages/marketing/clientNegotiation/index.js"></script>
</body>
</html>