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
		<table id="userDatagrid" border="false" title="用户一览"></table>
		<div id="userDatagridToolbar">
			<label style="padding-top: 14px;">用户名：<input class="easyui-textbox" name="userName"/></label>
			<label style="padding-top: 14px;">昵称：<input class="easyui-textbox" name="alias"/></label>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryUserBtn">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddUserWindowBtn">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeUsersBtn">禁用</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="assignSubordinate">分配下属</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="userMgrTab">
			<div title="基本信息" border="false" style="padding: 2px;">
				<form id="editUserForm" action="userMgr/editUser" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<table style="width: 100%;">
						<tr>
							<td align="right"><label>用户名：</label></td>
							<td><input name="name" class="easyui-textbox" required="required"/></td>
							<td align="right"><label>昵称：</label></td>
							<td><input name="alias" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>密码：</label></td>
							<td><input name="pwd" class="easyui-textbox" type="password" required="required"/></td>
							<td align="right"><label>密码确认：</label></td>
							<td><input name="confirmPwd" class="easyui-textbox" type="password" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>角色：</label></td>
							<td colspan="3">
								<label><input type="radio" name="role" value="1" checked="checked">产品维护员</label>
								<label><input type="radio" name="role" value="2">车型维护员</label>
								<label><input type="radio" name="role" value="0">管理员</label>
							</td>
						</tr>
						<tr><td colspan="4">
							<a id="submitUpdateUserFormBtn" href="javascript:void(0)">保存</a>
							<a id="refreshUpdateUserFormBtn" href="javascript:void(0)">刷新</a>
						</td></tr>
					</table>
				</form>
			</div>
			<div title="产品权限信息" border="false" style="padding: 2px;">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
					    	<div class="easyui-panel" title="已分配产品" data-options="fit:true">
					    		<ul id="assignedProductBrandTree"></ul>
					    	</div>
						</td>
						<td style="width: 40px; text-align: center;">
					    	<a id="addUserBrandBtn" href="javascript:void(0)">&lt;&lt;</a>
							<a id="removeUserBrandBtn" href="javascript:void(0)">&gt;&gt;</a>
						</td>
						<td width="50%">
					    	<div class="easyui-panel" title="未分配产品" data-options="fit:true">
					    		<ul id="notAssignProductBrandTree"></ul>	
					    	</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<div id="addUserWindow"></div>
<script type="text/javascript" src="pages/sysMgr/userMgr/index.js"></script>
</body>
</html>