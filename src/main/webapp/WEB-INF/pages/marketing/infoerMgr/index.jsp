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
<div class="easyui-layout" data-options="fit:true" style="margin: 2px;">
	<div data-options="region:'center'" style="width: 470px;">
		<table id="infoerDatagrid" border="false"></table>
		<div id="infoerDatagridToolbar">
			<label>名称：</label>
			<input style="width: 100px;" class="easyui-textbox" id="infoerMgr.nameInput"/>
			<label>电话：</label>
			<input style="width: 100px;" class="easyui-textbox" id="infoerMgr.telInput"/>
			<label>信息员等级：</label>
			<label><input type="checkbox" value="0" name="levelInput" checked="checked" style="top: 2px;"/>全部</label>
			<label><input type="checkbox" value="1" name="levelInput" style="top: 2px;"/>金牌</label>
			<label><input type="checkbox" value="2" name="levelInput" style="top: 2px;"/>银牌</label>
			<label><input type="checkbox" value="3" name="levelInput" style="top: 2px;"/>铜牌</label>
			<label><input type="checkbox" value="4" name="levelInput" style="top: 2px;"/>铁牌</label>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryInfoerBtn">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddInfoerWindowBtn">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="showBusinessTransferWindowBtn">业务转移</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="infoerMgrTab">
			<div title="详情" border="false">
				<form id="editInfoerForm" action="marketing/infoerMgr/editInfoer" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<input name="level" type="hidden"/>
					<input name="salesmanId" type="hidden"/>
					<table style="width: 100%; min-width: 500px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>名称：</label></td>
							<td style="min-width: 200px;"><input name="name" class="easyui-textbox" required="required"/></td>
							<td align="right" style="min-width: 80px;"><label>信息员等级：</label></td>
							<td width="70%"><input name="levelDesc" readonly="readonly" class="easyui-textbox"/></td>
						</tr>
						<tr>
							<td align="right" ><label>业务员：</label></td>
							<td><input name="salesmanName" readonly="readonly" class="easyui-textbox" required="required"/></td>
							<td align="right" style="vertical-align: mid; min-width: 80px;"><label>性质：</label></td>
							<td>
								<label><input type="radio" name="nature" value="1" checked="checked">中介</label>
								<label><input type="radio" name="nature" value="2">售楼</label>
							</td>
						</tr>
						<tr>
							<td align="right"><label>工作单位：</label></td>
							<td colspan="3"><input name="org" style="width: 459px;" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>地址：</label></td>
							<td colspan="3"><input name="address" style="width: 459px;" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right" style="min-width: 80px;"><label>联系电话：</label></td>
							<td colspan="3"><input name="telAll" style="width: 459px;" readonly="readonly" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="center" colspan="3">
								<a id="submitUpdateInfoerFormBtn" href="javascript:void(0)" iconCls="icon-save" style="width: 60px;">保存</a>
								<a id="refreshUpdateUserFormBtn" href="javascript:void(0)" iconCls="icon-reload" style="width: 60px;">刷新</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div title="回访记录" border="false">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="infoerVisitGrid"></table>
				    		<div id="infoerVisitGridToolbar">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addInfoerVisitBtn">新增</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div title="在谈单" border="false">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="orderGrid"></table>
						</td>
					</tr>
				</table>
			</div>
			<div title="信息费" border="false">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="infoCostGrid"></table>
						</td>
					</tr>
				</table>
			</div>
			<div title="提成" border="false">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="commissionCostGrid"></table>
						</td>
					</tr>
				</table>
			</div>
			<div title="客户" border="false">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="clientGrid"></table>
				    		<div id="clientGridToolbar">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addClientBtn">新增</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<div id="addInfoerWindow"></div>
<div id="businessTransferWindow"></div>
<div id="addInfoerVisitWindow"></div>
<div id="addClientWindow"></div>
<script type="text/javascript" src="pages/marketing/infoerMgr/index.js"></script>
</body>
</html>