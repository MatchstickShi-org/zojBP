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
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeUsersBtn">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="revertUsersBtn">恢复</a>
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
							<td style="min-width: 200px;"><input name="name" class="easyui-textbox" required="required"/></td>
							<td align="right" style="vertical-align: top; min-width: 80px;" rowspan="7"><label>角色：</label></td>
							<td rowspan="5" style="width: 70%;">
								<label><input type="radio" name="role" value="1" checked="checked">市场部业务员</label><br>
								<label><input type="radio" name="role" value="2">市场部主管</label><br>
								<label><input type="radio" name="role" value="3">市场部经理</label><br>
								<label><input type="radio" name="role" value="4">设计部设计师</label><br>
								<label><input type="radio" name="role" value="5">设计部主管</label><br>
								<label><input type="radio" name="role" value="6">设计部经理</label><br>
								<label><input type="radio" name="role" value="0">管理员</label>
							</td>
						</tr>
						<tr>
							<td align="right"><label>姓名：</label></td>
							<td><input name="alias" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>电话：</label></td>
							<td><input name="tel" class="easyui-textbox"/></td>
						</tr>
						<tr>
							<td align="right"><label>新密码：</label></td>
							<td><span class="easyui-tooltip" content="不填写表示不修改密码" position="right"><input name="pwd" class="easyui-textbox" type="password"/></span></td>
						</tr>
						<tr>
							<td align="right"><label>密码确认：</label></td>
							<td><input name="confirmPwd" class="easyui-textbox" type="password"/></td>
						</tr>
						<tr><td colspan="4">
							<a id="submitUpdateUserFormBtn" href="javascript:void(0)" style="width: 60px;">保存</a>
							<a id="refreshUpdateUserFormBtn" href="javascript:void(0)" style="width: 60px;">刷新</a>
						</td></tr>
					</table>
				</form>
			</div>
			<div title="下属业务员" border="false" style="padding: 2px;" disabled="true">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="assignedUnderlingGrid" title="下属"></table>
						</td>
						<td style="width: 40px; text-align: center;">
					    	<a id="assignUnderlingBtn" href="javascript:void(0)">&lt;&lt;</a>
							<a id="removeUnderlingBtn" href="javascript:void(0)">&gt;&gt;</a>
						</td>
						<td width="50%">
				    		<table id="notAssignUnderlingGrid" title="可分配人员"></table>
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