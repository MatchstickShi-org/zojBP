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
	<form id="editUserForm" action="myCenter/myInfo/updateUser" method="post" style="width: 100%;">
		<input type="hidden" name="id" value="${userInfo.id}">
		<input type="hidden" name="role" value="${userInfo.role}">
		<table style="width: 100%; min-width: 500px;">
			<tr>
				<td align="right" style="min-width: 80px;"><label>用户名：</label></td>
				<td style="min-width: 200px;"><input name="name" class="easyui-textbox" data-options="required:true, validType:'length[4, 16]'" value="${userInfo.name}"/></td>
				<td align="right" style="min-width: 80px;"><label>姓名：</label></td>
				<td width="70%"><input name="alias" class="easyui-textbox" data-options="required:true, validType:'length[2, 16]'" value="${userInfo.alias}"/></td>
			</tr>
			<tr>
				<td align="right"><label>电话：</label></td>
				<td><input name="tel" class="easyui-textbox" data-options="required:true, validType:'length[11, 11]'" value="${userInfo.tel}"/></td>
				<td align="right"><label>上级：</label></td>
				<td><input name="leaderName" class="easyui-textbox" readonly="readonly" value="${userInfo.leaderName}"/></td>
			</tr>
			<tr>
				<td align="right"><label>新密码：</label></td>
				<td><span class="easyui-tooltip" content="不填写表示不修改密码" position="right"><input id="pwd" name="pwd" class="easyui-textbox" type="password" data-options="validType:'length[6, 24]'"/></span></td>
				<td align="right"><label>所属组：</label></td>
				<td><input name="groupName" class="easyui-textbox" readonly="readonly" value="${userInfo.groupName}"/></td>
			</tr>
			<tr>
				<td align="right"><label>密码确认：</label></td>
				<td><input name="confirmPwd" class="easyui-textbox" type="password" data-options="validType:'length[6, 24]'"/></td>
				<td align="right" style="vertical-align: top;"><label>角色：</label></td>
				<td><input name="roleName" class="easyui-textbox" readonly="readonly"/></td>
			</tr>
			<tr><td colspan="4">
				<a id="submitUpdateUserFormBtn" href="javascript:void(0)">保存</a>
				<a id="refreshUpdateUserFormBtn" href="javascript:void(0)">刷新</a>
			</td></tr>
		</table>
	</form>
<script type="text/javascript">
var role = ${userInfo.role};
$('form#editUserForm').find('[name=roleName]').val(function()
{
	switch (role)
	{
		case 0:
			return '管理员';
			break;
		case 1:
			return '商务部业务员';
			break;
		case 2:
			return '商务部主管';
			break;
		case 3:
			return '商务部经理';
			break;
		case 4:
			return '主案部设计师';
			break;
		case 5:
			return '主案部主管';
			break;
		case 2:
			return '主案部部经理';
			break;
		default:
			return '未知';
			break;
	}
}());
</script>
<script type="text/javascript" src="pages/myCenter/myInfo/index.js"></script>
</body>
</html>