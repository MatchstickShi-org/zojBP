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
<title>北京中奥建工程设计有限公司业务管理平台</title>
<link rel="stylesheet" href="css/login.css" type="text/css"/>
<link rel="stylesheet" type="text/css" href="css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="css/themes/icon.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="pages/global/easyui.cfg.js"></script>
</head>
<body>
	<div id="container" align="center">
		<div id="maincontent" align="left">
			<div id="bg_top">
				<div id="icon"></div>
				<div id="title">北京中奥建业务管理平台</div>
			</div>
			<div id="bg_bottom">
				<div id="bg_left">
					<div id="left_content">
						<form id="loginForm" method="post">
							<table>
								<tr>
									<td id="text1" colspan="2">用户登录</td>
								</tr>
								<tr>
									<td height="10px"></td>
								</tr>
								<tr>
									<td id="userNameLabelTd"></td>
									<td class="valueTd"><input type="text" id="loginNameInput" name="name" maxlength="20"/></td>
								</tr>
								<tr>
									<td height="5px"></td>
								</tr>
								<tr>
									<td id="pwdLabelTd"></td>
									<td class="valueTd"><input type="text" name="pwd" maxlength="20" onfocus="this.type='password'" onkeypress="return doLogin(event)" autocomplete="off"/></td>
								</tr>
								<tr>
									<td height="52px" id="button" colspan="2"><div id="loginBtn"></div></td>
								</tr>
							</table>
						</form>
					</div>
				</div>
				<div id="bg_right"></div>
			</div>
		</div>
	</div>
<script type="text/javascript">
function doLogin(e)
{
	var keynum = window.event ? e.keyCode : e.which;
	if(keynum == 13)
		$('input#loginNameInput').submit();
}

$(function()
{
	var $form = $('form#loginForm');
	var $loginBtn = $('div#loginBtn');
	var $loginNameInput = $('input#loginNameInput');
	
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
			$.messager.alert('提示', retMsg.msg, 'warning', function(){$loginNameInput.focus().select();});
		}
	});
	
	$loginBtn.linkbutton({onClick: function(){$form.form('submit');}});
	
	$loginNameInput.focus().select();
});
</script>
</body>
</html>