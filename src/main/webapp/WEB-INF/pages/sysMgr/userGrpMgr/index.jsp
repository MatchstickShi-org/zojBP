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
		<table id="userDatagrid" border="false"></table>
		<div id="userDatagridToolbar">
			<label style="vertical-align: middle;">用户名：</label>
			<input class="easyui-textbox" id="userMgr.userNameInput"/>
			<label style="vertical-align: middle;">昵称：</label>
			<input class="easyui-textbox" id="userMgr.alias"/>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryUserBtn">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddUserWindowBtn">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeUsersBtn">设为离职</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="revertUsersBtn">设为在职</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="userMgrTab">
			<div title="详情" border="false" style="padding: 2px;">
				<form id="editUserForm" action="sysMgr/userMgr/editUser" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<table style="width: 100%; min-width: 500px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>用户名：</label></td>
							<td style="min-width: 200px;"><input name="name" class="easyui-textbox" data-options="required:true, validType:'length[4, 16]'"/></td>
							<td align="right"><label>姓名：</label></td>
							<td width="70%"><input name="alias" class="easyui-textbox" data-options="required:true, validType:'length[2, 16]'"/></td>
						</tr>
						<tr>
							<td align="right" style="min-width: 80px;"><label>电话：</label></td>
							<td><input name="tel" class="easyui-textbox" data-options="required:true, validType:'length[11, 11]'"/></td>
							<td align="right"><label>上级：</label></td>
							<td><input name="leaderName" class="easyui-textbox" readonly="readonly"/></td>
						</tr>
						<tr>
							<td align="right"><label>新密码：</label></td>
							<td><span class="easyui-tooltip" content="不填写表示不修改密码" position="right"><input id="pwd" name="pwd" class="easyui-textbox" type="password" data-options="validType:'length[6, 24]'"/></span></td>
							<td align="right"><label>所属组：</label></td>
							<td><input name="groupName" class="easyui-textbox" readonly="readonly"/></td>
						</tr>
						<tr>
							<td align="right"><label>密码确认：</label></td>
							<td><input name="confirmPwd" class="easyui-textbox" type="password" data-options="validType:'length[6, 24]'"/></td>
							<td align="right" style="vertical-align: top;"><label>角色：</label></td>
							<td>
								<select class="easyui-combobox" name="role" style="width: 173px;" id="userEditForm.role"></select>
							</td>
						</tr>
						<tr><td colspan="4">
							<a id="submitUpdateUserFormBtn" href="javascript:void(0)" style="width: 60px;">保存</a>
							<a id="refreshUpdateUserFormBtn" href="javascript:void(0)" style="width: 60px;">刷新</a>
						</td></tr>
					</table>
				</form>
			</div>
			<div title="下属业务员" border="false" style="padding: 2px;" disabled="true">
	    		<table id="assignedUnderlingGrid"></table>
	    		<div id="assignedUnderlingGridToolbar">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddUnderlingWindowBtn" style="float: left;">分配下属</a>
					<div class="datagrid-btn-separator" style="float: left;"></div>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeUnderlingBtn">移除下属</a>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="addUserWindow"></div>
<div id="addUnderlingWindow"></div>
<script type="text/javascript" src="pages/sysMgr/userMgr/index.js"></script>
</body>
</html>