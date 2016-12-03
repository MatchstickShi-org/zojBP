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
<title>中奥建业务管理平台</title>
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
			<div id="topDiv"><div id="logoDiv"></div><h1>中奥建业务管理平台</h1></div>
			<div id="topDiv-bg2"></div>
			<div id="topDiv-loginUser">
				欢迎你，${sessionScope.loginUser.alias}
				<a id="logoutBtn" href="logout">[注销]</a>
			</div>
		</div>
		<div data-options="region:'west', split:true" title="菜单" style="width: 200px;">
			<div id="westMenuAccordion"></div>
		</div>
		<div id="idxCenterDiv" data-options="region:'center', title:'欢迎使用'"></div>
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
			return '<span style="margin-left: 10px;">' + value + '</span>';
		},
		onSelect: function(idx, row)
		{
			var opts = $(this).datalist('options');
			var href = row[opts.valueField];
			if(href)
			{
				try
				{
					$idxCenterDiv.panel('setTitle', row[opts.textField]).panel('refresh', href);
				} catch (e)
				{
					$.messager.alert('提示', '加载页面失败，请稍后再试。');
				}
			}
		}
	});
	$westMenuAccordion.accordion('select', 0);
	
	/* datalist
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
					$idxCenterDiv.panel('setTitle', row[opts.textField]).panel('refresh', href);
				} catch (e)
				{
					$.messager.alert('提示', '加载页面失败，请稍后再试。');
				}
			}
			$(this).data('lastSelIdx', idx);
		}
	}); */
});
</script>
</html>