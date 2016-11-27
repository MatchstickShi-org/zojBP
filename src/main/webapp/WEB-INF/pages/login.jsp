<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>车辆配件管理系统</title>
<link rel="stylesheet" type="text/css" href="css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/header.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="pages/global/easyui.cfg.js"></script>
</head>
<body style="margin: 0px;">
	<div id="topDiv"><div id="logoDiv" style="height: 48px;"></div><h1>展云车型配件综合管理平台</h1></div>
	<div id="topDiv-bg2" style="height: 50px;"></div>
	<div class="easyui-window" title="登录" style="width: 300px;" 
		data-options="closable: false, modal:false, collapsible: false, minimizable: false, maximizable: false, resizable: false">
		<form id="loginForm" style="width: 100%" method="post">
			<table style="width: 100%">
				<tr>
					<td style="text-align: right;"><label>用户名：</label></td>
					<td><input id="loginNameInput" name="name" class="easyui-textbox" data-options="required:true" style="width:200px"></td>
				</tr>
				<tr>
					<td style="text-align: right;"><label>密码：</label></td>
					<td><input name="pwd" class="easyui-textbox" type="password" data-options="required:true" style="width:200px"></td>
				</tr>
				<tr><td colspan="2" style="text-align: center;">
					<a id="loginBtn" href="javascript:void(0)">登录</a>
					<a id="resetFormBtn" href="javascript:void(0)">重置</a>
				</td></tr>
				<tr><td colspan="2" style="text-align: center;">
					<div class="tips-warn-div">
						请使用非IE内核浏览器登录本系统，如chrome、Firefox等，IE系列浏览器本系统暂不支持。
					</div>
				</tr>
			</table>
		</form>
	</div>
<script type="text/javascript">
$(function()
{
	var $form = $('form#loginForm');
	var $loginBtn = $('a#loginBtn');
	var $resetBtn = $('a#resetFormBtn');
	var $loginNameInput = $('#loginNameInput');
	
	$form.form
	({
		url: 'login',
		onSubmit: function()
		{
			if(!$(this).form('validate'))
				return false;
		},
		success: function(data)
		{
			var retMsg = $.parseJSON(data);
			if(retMsg.returnCode == 0)
			{
				window.location.href = "toIndexView";
				return;
			}
			$.messager.alert('提示', retMsg.msg, 'warning', function(){$loginNameInput.textbox('textbox').focus().select();});
		}
	});
	
	$loginBtn.linkbutton({onClick: function(){$form.form('submit');}});
	
	$resetBtn.linkbutton({onClick: function(){$form.form('reset');}});
	
	$loginNameInput.textbox('textbox').focus().select();
});
</script>
</body>
</html>