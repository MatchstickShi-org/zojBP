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
			<label style="vertical-align: middle;">名称：</label>
			<input class="easyui-textbox" id="infoerMgr.nameInput"/>
			<label style="vertical-align: middle;">电话：</label>
			<input class="easyui-textbox" id="infoerMgr.telInput"/>
			<label style="vertical-align: middle;">信息员等级：</label>
			<input type="checkbox" value="0" name="infoerMgr.level" checked="checked"/>全部
			<input type="checkbox" value="1" name="infoerMgr.level"/>金牌
			<input type="checkbox" value="2" name="infoerMgr.level"/>银牌
			<input type="checkbox" value="3" name="infoerMgr.level"/>铜牌
			<input type="checkbox" value="4" name="infoerMgr.level"/>铁牌
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryUserBtn">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddUserWindowBtn">新增</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="userMgrTab">
			<div title="详情" border="false" style="padding: 2px;">
				<form id="editUserForm" action="sysMgr/userMgr/editUser" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<table style="width: 100%; min-width: 500px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>名称：</label></td>
							<td style="min-width: 200px;"><input name="name" class="easyui-textbox" required="required"/></td>
							<td align="right" style="vertical-align: top; min-width: 80px;" rowspan="7"><label>工作单位：</label></td>
							<td style="min-width: 200px;"><input name="org" class="easyui-textbox" required="required"/></td>
							<td align="right" style="vertical-align: top; min-width: 80px;" rowspan="7"><label>地址：</label></td>
							<td style="min-width: 200px;"><input name="address" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>联系电话：</label></td>
							<td style="min-width: 200px;"><input name="tel" class="easyui-textbox" required="required"/></td>
							<td align="right"><label>业务员：</label></td>
							<td style="min-width: 200px;"><input name="salesmanName" class="easyui-textbox" required="required"/></td>
							<td align="right"><label>性质：</label></td>
							<td style="min-width: 200px;">
								<label><input type="radio" name="nature" value="1" checked="checked">中介</label>
								<label><input type="radio" name="nature" value="2" >售楼</label>
							</td>
						</tr>
						<tr>
							<td align="right"><label>联系电话2：</label></td>
							<td style="min-width: 200px;"><input name="tel2" class="easyui-textbox"/></td>
							<td align="right"><label>信息员等级：</label></td>
							<td style="min-width: 200px;"><input name="level" class="easyui-textbox"/></td>
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
<script type="text/javascript" src="pages/marketing/infoerMgr/index.js"></script>
</body>
</html>