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
		<table id="groupDatagrid" border="false"></table>
		<div id="groupDatagridToolbar">
			<label style="vertical-align: middle;">部门：</label>
			<label style="vertical-align: middle;"><input type="radio" name="type" checked="checked" value="-1">全部</label>
			<label style="vertical-align: middle;"><input type="radio" name="type" value="0">商务部</label>
			<label style="vertical-align: middle;"><input type="radio" name="type" value="1">主案部</label>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryGroupBtn">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddGroupWindowBtn">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeGroupsBtn">删除</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="userGrpMgrTab">
			<div title="详情" border="false" style="padding: 2px;">
				<form id="editGroupForm" action="sysMgr/userGrpMgr/editGroup" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<input type="hidden" name="leaderId" id="editGroupForm_leaderIdInput">
					<input type="hidden" name="type">
					<table style="width: 100%; min-width: 500px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>组名：</label></td>
							<td style="min-width: 200px;"><input name="name" class="easyui-textbox" data-options="required:true, validType:'length[4, 16]'"/></td>
							<td align="right" style="min-width: 80px;"><label>主管：</label></td>
							<td width="70%"><input id="editGroupForm_leaderSearchbox" name="leaderName" class="easyui-searchbox" editable="false"/></td>
						</tr>
						<tr><td colspan="4">
							<a id="saveGroupBtn" href="javascript:void(0)" iconCls="icon-save" style="width: 60px;">保存</a>
							<a id="refreshGroupBtn" href="javascript:void(0)" iconCls="icon-reload" style="width: 60px;">刷新</a>
						</td></tr>
					</table>
				</form>
			</div>
			<div title="下属" border="false" style="padding: 2px;">
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
<div id="addGroupWindow"></div>
<div id="selectLeaderWindow"></div>
<div id="addUnderlingWindow"></div>
<script type="text/javascript" src="pages/sysMgr/userGrpMgr/index.js"></script>
</body>
</html>