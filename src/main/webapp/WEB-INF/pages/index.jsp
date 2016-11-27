<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>车辆配件管理系统</title>
<link rel="stylesheet" type="text/css" href="css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/header.css">
<link rel="stylesheet" type="text/css" href="css/swiper-3.3.1.min.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="pages/global/easyui.cfg.js"></script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north', border: false" style="height: 50px; overflow: hidden;">
			<div id="topDiv"><div id="logoDiv"></div><h1>展云车型配件综合管理平台</h1></div>
			<div id="topDiv-bg2"></div>
			<div id="topDiv-loginUser">
				欢迎你，${sessionScope.loginUser.alias}
				<a id="changePwdBtn" href="javascript:void(0)">[修改密码]</a><a id="logoutBtn" href="logout">[注销]</a>
			</div>
		</div>
		<div data-options="region:'west', split:true" title="菜单" style="width: 200px;">
			<div id="westMenuDatalist"></div>
		</div>
		<div id="idxCenterDiv" data-options="region:'center', title:'欢迎使用'"></div>
	</div>
	<div id="changePwdWindow" title="修改密码" style="width: 270px;">
		<form id="changePwdForm" action="userMgr/changePwd" method="post" style="width: 100%;">
			<input type="hidden" name="id" value="${loginUser.id}">
			<table style="width: 100%">
				<tr>
					<td align="right"><label>原密码：</label></td>
					<td><input name="originalPwd" class="easyui-textbox" type="password" required="required"/></td>
				</tr>
				<tr>
					<td align="right"><label>新密码：</label></td>
					<td><input name="newPwd" class="easyui-textbox" type="password" required="required"/></td>
				</tr>
				<tr>
					<td align="right"><label>确认新密码：</label></td>
					<td><input name="confirmNewPwd" class="easyui-textbox" type="password" required="required"/></td>
				</tr>
				<tr>
					<td align="center" colspan="2">
						<a class="easyui-linkbutton" id="submitChangePwdBtn" href="javascript:void(0)">保存</a>
						<a class="easyui-linkbutton" onclick="$('div#changePwdWindow').window('close');" href="javascript:void(0)">取消</a>
					</td> 
				</tr>
			</table>
		</form>
	</div>
</body>
<script type="text/javascript">
var _session_loginUser =
{
	id : ${loginUser.id},
	isAdmin: false
};
if(${loginUser.role == '0'})
	_session_loginUser.isAdmin = true;

var _productClassifyJson =
{
	A: '刹车片',
	B: '雨刮器',
	C: '机油'
};

$(function()
{
	var req_userMenus = ${requestScope.loginUserMenus};
	var $westMenuDatalist = $('div#westMenuDatalist');
	var $idxCenterDiv = $('div#idxCenterDiv');
	var $changePwdBtn = $('a#changePwdBtn');
	var $changePwdWindow = $('div#changePwdWindow');
	var $submitChangePwdBtn = $('a#submitChangePwdBtn');
	
	$idxCenterDiv.panel('options').onLoadError = function(jqXHR, textStatus, errorThrown)
	{
		$(this).html(jqXHR.responseText);
	};
	
	$westMenuDatalist.datalist
	({
		lines: true,
		fit: true,
		striped: true,
		border: false,
		textField: 'name',
		valueField: 'url',
		data: req_userMenus,
		rowStyler: function(){return "cursor: pointer"},
		onSelect: function(idx, row)
		{
			if(idx == $(this).data('lastSelIdx'))
				return;
			var opts = $(this).datalist('options');
			var href = row[opts.valueField];
			if(href)
			{
				try
				{
					$('.window-body:not(#changePwdWindow)').window('destroy');
					$idxCenterDiv.panel('setTitle', row[opts.textField]).panel('refresh', href);
				} catch (e)
				{
					$.messager.alert('提示', '加载页面失败，请稍后再试。');
				}
			}
			$(this).data('lastSelIdx', idx);
		}
	});
	
	$changePwdWindow.window({});
	$changePwdBtn.click(function()
	{
		$changePwdWindow.window('open').window('center');
	});
	
	$submitChangePwdBtn.click(function()
	{
		$changePwdWindow.find('form#changePwdForm').form('submit',
		{
			onSubmit: function()
			{
				if(!$(this).form('validate'))
					return false;
				if($(this).find('[name="newPwd"]').val() != $(this).find('[name="confirmNewPwd"]').val())
				{
					$.messager.alert('提示', '两次密码输入不一致，请重新输入密码。');
					return false;
				}
			},
			success: function(data)
			{
				data = $.fn.form.defaults.success(data);
				if(data.returnCode == 0)
					$changePwdWindow.window('close');
			}
		});
	});
});
</script>
</html>