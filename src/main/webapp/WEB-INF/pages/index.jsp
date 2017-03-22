<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中奥建业务管理平台</title>
<link rel="stylesheet" type="text/css" href="css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/header.css">
<link rel="stylesheet" type="text/css" href="css/global.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="pages/global/easyui.cfg.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
<style type="text/css">
#broadcastMsgDiv span
{
	display:block;/*这个属性是必须的*/
	font-size:14px;
	line-height:16px;
	cursor: pointer;
	float: left;
	margin-left: 50px;
}

#broadcastMsgDiv
{
	left: 320px;
	position: absolute;
	top: 57px;
	right: 0px;
	text-align: left;
	color: red;
	height:16px;
	overflow:hidden;
}
</style>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north', border: false" style="height: 50px; overflow: hidden;">
			<div id="topDiv"><div id="logoDiv"></div><h1>中奥建业务管理平台</h1></div>
			<div id="topDiv-bg2"></div>
			<div id="topDiv-loginUser" style="font-size: 14px;">
				欢迎你，${sessionScope.loginUser.alias} (${sessionScope.loginUser.roleName})
				<a id="logoutBtn" href="logout">[注销]</a>
			</div>
		</div>
		<div data-options="region:'west', split:true" title="菜单" style="width: 200px;">
			<div id="westMenuAccordion"></div>
		</div>
		<div id="idxCenterDiv" data-options="region:'center', title:'欢迎使用'"></div>
	</div>
	<marquee id="broadcastMsgDiv" scrollamount="2">
		<c:forEach items="${sessionScope.broadcastMsgs}" var="m">
			<span id="${m.id}">${m.content}</span>
		</c:forEach>
	</marquee>
</body>
<script type="text/javascript">
/*禁用后退*/
window.history.forward();
window.onbeforeunload=function(){};
/** 屏蔽退格键 */
document.onkeydown=banBackSpace;
function banBackSpace(e)
{
	var ev = e || window.event;//获取event对象    
	var obj = ev.target || ev.srcElement;//获取事件源    
	var t = obj.type || obj.getAttribute('type');//获取事件源类型   
	
	//获取作为判断条件的事件类型 
	var vReadOnly = obj.getAttribute('readonly'); 
	var vEnabled = obj.getAttribute('enabled'); 
	//处理null值情况 
	vReadOnly = (vReadOnly == null) ? false : vReadOnly; 
	vEnabled = (vEnabled == null) ? true : vEnabled; 
	
	   //当敲Backspace键时，事件源类型为密码或单行、多行文本的， 
	   //并且readonly属性为true或enabled属性为false的，则退格键失效 
	var flag1 = (ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea")
			&& (vReadOnly==true || vEnabled!=true)) ? true : false;
	
	   //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效 
	var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") ? true : false;         

	if(flag2)
		return false; 
	  
	if(flag1)
		return false;    
}

var _session_loginUser =
{
	id : ${loginUser.id},
	isAdmin: false
};
if(${loginUser.role == 0})
	_session_loginUser.isAdmin = true;

$(function()
{
	var $broadcastMsgDiv = $('#broadcastMsgDiv');
		
	function getLastMsg()
	{
		$.ajax
		({
			url: 'getLastMsg',
			success: function(data, textStatus, jqXHR)
			{
				if(data.returnCode == 0)
				{
					if(data.newestMsg.targetUser == null)
					{
						var $lastSpan = $broadcastMsgDiv.find('span:last');
						var $tmpSpan = $('<span id="' + data.newestMsg.id + '">' + data.newestMsg.content + '</span>');
						if($lastSpan.length == 0)		//之前没有消息
							$broadcastMsgDiv.append($tmpSpan);
						else
							$lastSpan.after($tmpSpan);
					}
					else
					{
						$.messager.show({title: '你有新的消息', msg: data.newestMsg.content, timeout: 0, style:
						{
		                    right:'',
		                    top:document.body.scrollTop+document.documentElement.scrollTop,
		                    bottom:''
		                }});
					}
					getLastMsg();
				}
			},
			timeout: 0,
			error: function(xhr, status, error)
			{
				setTimeout(getLastMsg, 30000);		//30秒后重新尝试获取信息。
			}
		});
	}
	getLastMsg();
	
	var req_userMenus = ${requestScope.loginUserMenus};
	var $westMenuAccordion = $('div#westMenuAccordion');
	var $idxCenterDiv = $('div#idxCenterDiv');
	
	$idxCenterDiv.panel('options').onLoadError = function(jqXHR, textStatus, errorThrown)
	{
		$(this).html(jqXHR.responseText);
	};
	
	$westMenuAccordion.accordion({border: false});
	
	for(var i in req_userMenus)
	{
		var parentMenu = req_userMenus[i];
		$westMenuAccordion.accordion('add', 
		{
			title: parentMenu.name,
			content: function()
			{
				if(parentMenu.childMenus.length >0)
				{
					var cmDataListHtml = '<ul class="westMenu_datalist">';
					for(var j in parentMenu.childMenus)
					{
						var cm = parentMenu.childMenus[j];
						cmDataListHtml += '<li value="' + cm.url + '">' + cm.name + '</li>';
					}
					cmDataListHtml += '</ul>'
					return cmDataListHtml;
				}
				return '';
			}()
		});
	}
	$westMenuAccordion.find('ul.westMenu_datalist').datalist
	({
		border: false,
		textField: 'name',
		valueField: 'url',
		textFormatter: function (value, row, index)
		{
			return '<span style="margin-left: 20px;">' + value + '</span>';
		},
		onSelect: function(idx, row)
		{
			var opts = $(this).datalist('options');
			var href = row[opts.valueField];
			if(href)
			{
				try
				{
					$('.window-body').window('destroy');
					$idxCenterDiv.panel('clear').panel('setTitle', row[opts.textField]).panel('refresh', href);
				}
				catch (e)
				{
					$.messager.alert('提示', '加载页面失败，请稍后再试。');
				}
			}
		}
	});
	$westMenuAccordion.accordion('select', 0);
});
</script>
</html>