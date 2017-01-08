$(function()
{
	var $infoerDatagrid = $('table#infoerDatagrid');
	var $infoerNameTextbox = $('#infoerMgr\\.nameInput');
	var $telTextbox = $('#infoerMgr\\.telInput');
	var $queryInfoerBtn = $('a#queryInfoerBtn');
	var $addInfoerWindow = $('div#addInfoerWindow');
	var $businessTransferWindow = $('div#businessTransferWindow');
	var $addInfoerVisitWindow = $('div#addInfoerVisitWindow');
	var $addClientWindow = $('div#addClientWindow');
	var $revertUsersBtn = $('a#revertUsersBtn');
	var $removeUsersBtn = $('a#removeUsersBtn');
	var $infoerMgrTab = $('div#infoerMgrTab');
	var $editInfoerForm = $('form#editInfoerForm');
	var $submitUpdateInfoerFormBtn = $('a#submitUpdateInfoerFormBtn');
	var $refreshUpdateUserFormBtn = $('a#refreshUpdateUserFormBtn');
	var $infoerVisitGrid = $('table#infoerVisitGrid');
	var $orderGrid = $('table#orderGrid');
	var $infoCostGrid = $('table#infoCostGrid');
	var $commissionCostGrid = $('table#commissionCostGrid');
	var $clientGrid = $('table#clientGrid');
	
	function init()
	{
		$infoerDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#infoerDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话', width:5},
				{
					field:'level', title:'等级', width: 3, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 1:
								return '金牌';
								break;
							case 2:
								return '银牌';
								break;
							case 3:
								return '铜牌';
								break;
							case 4:
								return '铁牌';
								break;
							default:
								return '未评级';
								break;
						}
					}
				},
				{
					field:'nature', title:'性质', width: 3, formatter: function(value, row, index)
					{
						return value == 1 ? '中介' : '售楼';
					}
				},
				{field:'org', title:'工作单位', width: 8},
				{field:'address', title:'地址', width: 8},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'leftVisitDays', title:'未回访天数', width: 5,
					styler: function (value, row, index) {
						if(value > 5)
							return 'background-color:red';
		           }}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'marketing/infoerMgr/getAllInfoers',
			onSelect: function(idx, row)
			{
				if(row.salesmanId != _session_loginUserId)
				{
					$('#addInfoerVisitBtn').linkbutton('disable').linkbutton('hide');
					$('#addClientBtn').linkbutton('disable').linkbutton('hide');
					$infoerMgrTab.tabs('hideTool');
					$submitUpdateInfoerFormBtn.linkbutton('disable').linkbutton('hide');
				}
				else
				{
					$('#addInfoerVisitBtn').linkbutton('enable').linkbutton('show');
					$('#addClientBtn').linkbutton('enable').linkbutton('show');
					$infoerMgrTab.tabs('showTool');
					$submitUpdateInfoerFormBtn.linkbutton('enable').linkbutton('show');
				}
				loadTabData($infoerMgrTab.tabs('getSelected').panel('options').title, row);
			}
		});
		
		$('#infoerDatagridToolbar :checkbox').click(function()
		{
			if($(this).attr('value') == '')		//全选
				$('#infoerDatagridToolbar :checkbox[value!=""]').attr("checked", false);
			else
				$('#infoerDatagridToolbar :checkbox[value=""]').attr("checked", false);
		});
		
		$queryInfoerBtn.linkbutton
		({
			'onClick': function()
			{
				$infoerDatagrid.datagrid('loading');
				var chkLevels = []; 
				$('#infoerDatagridToolbar :checkbox[name="levelInput"]:checked').each(function()
				{ 
					chkLevels.push($(this).val()); 
				});
				$.ajax
				({
					url: 'marketing/infoerMgr/getAllInfoers',
					data: {name: $infoerNameTextbox.textbox('getValue'), tel: $telTextbox.textbox('getValue'), level: chkLevels},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$infoerDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$infoerDatagrid.datagrid('loaded');
					}
				});
			}
		});
		
		$infoerMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selRows = $infoerDatagrid.datagrid('getSelections');
				
				if(selRows.length == 1)
					loadTabData(title, selRows[0]);
				else
					clearTabData(title);
			}
		});
		
		$infoerMgrTab.tabs('hideTool');
		
		$submitUpdateInfoerFormBtn.linkbutton({'onClick': submitEditInfoerForm});
		$refreshUpdateUserFormBtn.linkbutton({'onClick': function()
		{
			var selRows = $infoerDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($infoerMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选择信息员。');
		}});
		
		$infoerVisitGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'回访内容', width: 5},
				{field:'date', title:'回访日期', width: 5}
			]],
			pagination: true,
			onDblClickRow: function(index, row){
				$.messager.alert('回访内容', row.content);
			}
		});
		
		$orderGrid.datagrid
		({
			idField: 'id',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'name', title:'客户', width: 3},
				  {field:'projectName', title:'工程名称', width: 6},
				  {field:'projectAddr', title:'工程地址', width: 6},
				  {field:'infoerName', title:'信息员', width: 3},
				  {field:'salesmanName', title:'业务员', width: 3},
				  {field:'designerName', title:'设计师', width: 3},
				  {field:'insertTime', title:'生成日期', width: 5},
				  {field:'status', title:'状态', width: 5, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 14:
								return '在谈单-设计师已打回';
								break;
							case 30:
								return '在谈单-市场部经理审核中';
								break;
							case 32:
								return '在谈单-设计部经理审核中';
								break;
							case 34:
								return '在谈单-设计师跟踪中';
								break;
							default:
								return '未知';
								break;
						}
					}
				  }
				  ]],
				  pagination: true
		});

		$infoCostGrid.datagrid
		({
			idField: 'id',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'orderId', title:'单号', width: 3},
				  {field:'projectName', title:'项目名称', width: 6},
				  {field:'infoerName', title:'信息员', width: 3},
				  {field:'salesmanName', title:'业务员', width: 3},
				  {field:'designerName', title:'设计师', width: 3},
				  {field:'amount', title:'金额', width: 3, formatter: function(value, row, index)
						{
					  		return '￥' + value;
						}
				  },
				  {field:'date', title:'打款日期', width: 3},
				  {field:'remark', title:'备注', width: 8}
				  ]],
				  pagination: true
		});
		
		$commissionCostGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'orderId', title:'单号', width: 3},
				{field:'projectName', title:'项目名称', width: 6},
				{field:'infoerName', title:'信息员', width: 3},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'designerName', title:'设计师', width: 3},
				{field:'amount', title:'金额', width: 3, formatter: function(value, row, index)
					{
						return '￥' + value;
					}
				},
				{field:'date', title:'打款日期', width: 3},
				{field:'remark', title:'备注', width: 8}
			]],
				  pagination: true
		});
		
		$clientGrid.datagrid
		({
			idField: 'id',
			//toolbar: '#clientGridToolbar',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'name', title:'联系人', width: 3},
				  {field:'telAll', title:'联系电话', width: 6},
				  {field:'orgAddr', title:'单位地址', width: 8},
				  {field:'projectName', title:'工程名称', width: 5},
				  {field:'projectAddr', title:'工程地址', width: 8},
				  {field:'infoerName', title:'信息员', width: 3},
				  {field:'salesmanName', title:'业务员', width: 3},
				  {field:'insertTime', title:'录入日期', width: 5}
				  ]],
				  pagination: true
		});

		$infoerVisitGrid.datagrid('options').url = 'marketing/infoerMgr/getInfoerVisitByInfoer';
		$orderGrid.datagrid('options').url = 'marketing/infoerMgr/getOrderByInfoer';
		$infoCostGrid.datagrid('options').url = 'marketing/infoerMgr/getInfoCostByInfoer';
		$commissionCostGrid.datagrid('options').url = 'marketing/infoerMgr/getCommissionCostByInfoer';
		$clientGrid.datagrid('options').url = 'marketing/infoerMgr/getClientByInfoer';
		
		function loadTabData(title, row)
		{
			switch (title)
			{
				case '详情':
					$editInfoerForm.form('clear').form('load', 'marketing/infoerMgr/getInfoerById?infoerId=' + row.id);
					break;
				case '回访记录':
					$infoerVisitGrid.datagrid('unselectAll').datagrid('reload', {infoerId: row.id});
					break;
				case '在谈单':
					$orderGrid.datagrid('unselectAll').datagrid('reload', {infoerId: row.id});
					break;
				case '信息费':
					$infoCostGrid.datagrid('unselectAll').datagrid('reload', {infoerId: row.id});
					break;
				case '提成':
					$commissionCostGrid.datagrid('unselectAll').datagrid('reload', {infoerId: row.id});
					break;
				case '客户':
					$clientGrid.datagrid('unselectAll').datagrid('reload', {infoerId: row.id});
					break;
			}
		}
		
		function clearTabData(title)
		{
			switch (title)
			{
				case '详情':
					$editInfoerForm.form('clear');
					break;
				case '回访记录':
					$infoerVisitGrid.datagrid('loadData', []);
					break;
				case '在谈单':
					$orderGrid.datagrid('loadData', []);
					break;
				case '信息费':
					$infoCostGrid.datagrid('loadData', []);
					break;
				case '提成':
					$commissionCostGrid.datagrid('loadData', []);
					break;
				case '客户':
					$clientGrid.datagrid('loadData', []);
					break;
			}
		}
		
		function submitEditInfoerForm()
		{
			var selIds = $infoerDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选择要修改的信息员。');
				return;
			}
			$editInfoerForm.form('submit',
			{
				onSubmit: function()
				{
					if(!$(this).form('validate'))
						return false;
				},
				success: function(data)
				{
					data = $.fn.form.defaults.success(data);
					if(data.returnCode == 0)
						$infoerDatagrid.datagrid('reload');
					else
						$.messager.show({title: '提示', msg: data.msg});
				}
			});
		}
		
		$('#showAddInfoerWindowBtn').linkbutton({onClick: showAddInfoerWindow});
		$('#showBusinessTransferWindowBtn').linkbutton({onClick: showBusinessTransferWindow});
		$('#addInfoerVisitBtn').linkbutton({onClick: showAddInfoerVisitWindow});
		$('#addClientBtn').linkbutton({onClick: showAddClientWindow});
		
		$addInfoerWindow.window({width: 500});
		$businessTransferWindow.window({width: 500});
		$addInfoerVisitWindow.window({width: 322});
		$addClientWindow.window({width: 500});
		
		function showBusinessTransferWindow()
		{
			var selIds = $infoerDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>需要业务转移的信息员。');
				return;
			}
			$businessTransferWindow.window('clear');
			$businessTransferWindow.window('open').window
			({
				title: '请选择业务员',
			}).window('open').window('refresh', 'marketing/infoerMgr/showAllSalesman');
		}
		
		function showAddInfoerWindow()
		{
			$addInfoerWindow.window('clear');
			$addInfoerWindow.window('open').window
			({
				title: '新增信息员',
				content: addInfoerWindowHtml
			}).window('open').window('center');
		}
		
		var addInfoerWindowHtml = 
			'<form id="addInfoerForm" action="marketing/infoerMgr/addInfoer" method="post" style="width: 100%;">' + 
			'	<table width="100%" id="infoerTable">' + 
			'		<tr>' + 
			'			<td style="min-width: 70px;" align="right"><label>名称：</label></td>' + 
			'			<td><input name="name" style="width:150px;" class="easyui-textbox" required="required" /></td>' + 
			'			<td style="min-width: 40px;" align="right"><label>性质：</label></td>' + 
			'			<td style="width: 150px;"><label><input type="radio" name="nature" value="1" checked="checked">中介</label>' + 
			'				<label><input type="radio" name="nature" value="2">售楼</label>' + 
			'			</td>' +
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>电话1：</label></td>' + 
			'			<td><input name="tel1" id="tel1" required="required" style="width: 150px;"/></td>' +
			'			<td align="right"><label>电话2：</label></td>' + 
			'			<td><input name="tel2" id="tel2" style="width: 150px;"/></td>' +
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>电话3：</label></td>' + 
			'			<td><input name="tel3" id="tel3" style="width: 150px;"/></td>' +
			'			<td align="right"><label>电话4：</label></td>' + 
			'			<td><input name="tel4" id="tel4" style="width: 150px;"/></td>' +
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>电话5：</label></td>' + 
			'			<td><input name="tel5" id="tel5" style="width: 150px;"/></td>' +
			'			<td colspan="2"><font id="errortel" color="red" ></font></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工作单位：</label></td>' + 
			'			<td colspan="3"><input name="org" class="easyui-textbox" style="width: 380px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>地址：</label></td>' + 
			'			<td colspan="3"><input name="address" class="easyui-textbox" style="width: 380px;"/></td>' + 
			'		</tr>' + 
			'		<input name="level" type="hidden" value="4" />' + 
			'		<input id="infoerTelCount" type="hidden" value="1" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitaddInfoerForm();" iconCls="icon-ok" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addInfoerWindow.window(\'close\');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addInfoerWindow = $(\'div#addInfoerWindow\');' +
			'var $infoerDatagrid = $(\'table#infoerDatagrid\');' +
			'var $tel1Input = $(\'table#infoerTable input#tel1\');' +
			'$tel1Input.textbox({});' +
			'$tel1Input.textbox("textbox").bind("blur", function(){checkTelValue($tel1Input.get(0));});' +
			'var $tel2Input = $(\'table#infoerTable input#tel2\');' +
			'$tel2Input.textbox({});' +
			'$tel2Input.textbox("textbox").bind("blur", function(){checkTelValue($tel2Input.get(0));});' +
			'var $tel3Input = $(\'table#infoerTable input#tel3\');' +
			'$tel3Input.textbox({});' +
			'$tel3Input.textbox("textbox").bind("blur", function(){checkTelValue($tel3Input.get(0));});' +
			'var $tel4Input = $(\'table#infoerTable input#tel4\');' +
			'$tel4Input.textbox({});' +
			'$tel4Input.textbox("textbox").bind("blur", function(){checkTelValue($tel4Input.get(0));});' +
			'var $tel5Input = $(\'table#infoerTable input#tel5\');' +
			'$tel5Input.textbox({});' +
			'$tel5Input.textbox("textbox").bind("blur", function(){checkTelValue($tel5Input.get(0));});' +
			'function submitaddInfoerForm()' + 
			'{' + 
			'	var errortel = $(\'#errortel\');'+
			'	$addInfoerWindow.find(\'form#addInfoerForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'			if(errortel.html().length > 0){' + 
			'				return false;' + 
			'			}'+
			'			var flag = true;'+
			'			for(var i=1;i<6;i++){' +
			'				flag = checkTelValue($(\'table#infoerTable input#tel\'+i+\'\').get(0));'+
			'				if(!flag)'+
			'					return false;'+
			'			}'+
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$infoerDatagrid.datagrid(\'reload\');' + 
			'				$addInfoerWindow.window(\'close\');' + 
			'			}else' + 
			'				errortel.html(data.msg);' + 
			'		}' + 
			'	});' + 
			'}' + 
			'function checkTelValue(obj)' + 
			'{' + 
			'	var errorId = obj.id;'+
			'	errorId = errorId.charAt(errorId.length - 1);'+
			'	if(obj.value.length==0) '+
		    ' 	{ '+
		    '		if(errorId ==1) '+
		    ' 		{ '+
		    '      		$(\'#errortel\').html("联系电话未填！"); '+
		    '      		return false; '+
		    '      	}'+
		    '   }else{'+
			'		var reg = /^1[0-9]{10}$/;'+
			'		if(!(reg.test(obj.value))){'+
			'			$(\'#errortel\').html("无效的手机号码！");'+
			'			return false; '+
			'		}else{'+
			'			$(\'#errortel\').html("");'+
			'		}'+
			'		if(errorId > 1) '+
			' 		{'+
			'			if($(\'#tel1\').val() == obj.value){' + 
			'      			$(\'#errortel\').html("联系电话重复！");'+
			'      			return false;'+
			'   		}'+
			'   	}'+
			'	}'+
			'	if(obj.value.length >0){'+
			'		$.ajax'+
			'		({'+
			'			url: \'marketing/infoerMgr/checkInfoerTel\','+
			'			data: {tel:obj.value},'+
			'			success: function(data, textStatus, jqXHR)'+
			'			{'+
			'				if(data.returnCode != 0){'+
			'					$(\'#errortel\').html(data.msg); '+  
			'					return false; '+  
			'				}else{ '+  
			'					$(\'#errortel\').html("");'+  
			'				}'+  
			'			}'+
			'		});'+
			'	}'+
			'   return true;'+
			'} ' + 
			'</script>';
		
		function showAddInfoerVisitWindow()
		{
			var selIds = $infoerDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert("提示", "请选择要回访的信息员。");
				return;
			}
			$addInfoerVisitWindow.window('clear');
			$addInfoerVisitWindow.window('open').window
			({
				title: '新增回访记录',
				content: addInfoerVisitWindowHtml
			}).window('open').window('center');
		}
		
		var addInfoerVisitWindowHtml = 
			'<form id="addInfoerVisitForm" action="marketing/infoerMgr/addInfoerVisit" method="post" style="width: 100%;">' + 
			'	<input id="infoerId" name="infoerId" type="hidden" value="" />' + 
			'	<input id="salesmanId" name="salesmanId" type="hidden" value="" />' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>回访内容：</label></td>' + 
			'			<td><input name="content" required="required" class="easyui-textbox" multiline="true" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitaddInfoerVisitForm();" iconCls="icon-ok" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addInfoerVisitWindow.window(\'close\');"  iconCls="icon-cancel" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addInfoerVisitWindow = $(\'div#addInfoerVisitWindow\');' +
			'var $infoerDatagrid = $(\'table#infoerDatagrid\');' +
			'var $infoerVisitGrid = $("table#infoerVisitGrid");' +
			'var $infoerMgrTab = $("div#infoerMgrTab");' +
			'var selRows = $infoerDatagrid.datagrid("getSelections");' +
			'$addInfoerVisitWindow.find(\'#infoerId\').val(selRows[0].id);' +
			'$addInfoerVisitWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'function submitaddInfoerVisitForm()' + 
			'{' + 
			'	$addInfoerVisitWindow.find(\'form#addInfoerVisitForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			var selInfoerIds = $infoerDatagrid.datagrid("getSelectRowPkValues");' + 
			'			if(selInfoerIds.length == 0)' + 
			'			{' + 
			'				$.messager.alert("提示", "请选择要回访的信息员。");' + 
			'				return false;' + 
			'			}' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$infoerDatagrid.datagrid(\'reload\');' + 
			'				if($infoerMgrTab.tabs("getSelected").panel("options").title == "回访记录")' + 
			'					$infoerVisitGrid.datagrid(\'reload\');' + 
			'				$addInfoerVisitWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddClientWindow()
		{
			var selIds = $infoerDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选择要添加客户信息员。');
				return;
			}
			$addClientWindow.window('clear');
			$addClientWindow.window('open').window
			({
				title: '新增客户',
				content: addClientWindowHtml
			}).window('open').window('center');
		}
		
		var addClientWindowHtml = 
			'<form id="addClientForm" action="marketing/infoerMgr/addClient" method="post" style="width: 100%;">' + 
			'	<input id="clientTelCount" type="hidden" value="1" />' + 
			'	<input id="infoerId"  name="infoerId" type="hidden" value="" />' + 
			'	<table width="100%" id="clientTab" >' + 
			'		<tr>' + 
			'			<td style="min-width: 70px;" align="right"><label>联系人：</label></td>' + 
			'			<td><input name="name" class="easyui-textbox" required="required" style="width: 140px;"/></td>' + 
			'			<td style="min-width: 75px;" align="right"><label>所属信息员：</label></td>' + 
			'			<td><input id="infoerName" name="infoerName" readonly="readonly" class="easyui-textbox" style="width: 136px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话1：</label></td>' + 
			'			<td><input name="tel1" id="tel1" required="required" style="width: 140px;"/></td>' + 
			'			<td align="right"><label>联系电话2：</label></td>' + 
			'			<td><input name="tel2" id="tel2" style="width: 140px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话3：</label></td>' + 
			'			<td><input name="tel3" id="tel3" style="width: 140px;"/></td>' + 
			'			<td align="right"><label>联系电话4：</label></td>' + 
			'			<td><input name="tel4" id="tel4" style="width: 140px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话5：</label></td>' + 
			'			<td><input name="tel5" id="tel5" style="width: 140px;"/></td>' + 
			'			<td colspan="2"><font id="errorclienttel" color="red"></font></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>单位地址：</label></td>' + 
			'			<td colspan="3"><input name="orgAddr" class="easyui-textbox" style="width: 398px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程名称：</label></td>' + 
			'			<td colspan="3"><input name="projectName" class="easyui-textbox" style="width: 398px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程地址：</label></td>' + 
			'			<td colspan="3"><input name="projectAddr" class="easyui-textbox" style="width: 398px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitaddClientForm();" iconCls="icon-ok" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientWindow.window(\'close\');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addClientWindow = $(\'div#addClientWindow\');' +
			'var $infoerDatagrid = $(\'table#infoerDatagrid\');' +
			'var $clientGrid = $("table#clientGrid");' +
			'var $infoerMgrTab = $("div#infoerMgrTab");' +
			'var selRows = $infoerDatagrid.datagrid("getSelections");' +
			'$addClientWindow.find(\'#infoerId\').val(selRows[0].id);' +
			'$addClientWindow.find(\'#infoerName\').val(selRows[0].name);' +
			'var $tel1Input = $(\'table#clientTab input#tel1\');' +
			'$tel1Input.textbox({});' +
			'$tel1Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel1Input.get(0));});' +
			'var $tel2Input = $(\'table#clientTab input#tel2\');' +
			'$tel2Input.textbox({});' +
			'$tel2Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel2Input.get(0));});' +
			'var $tel3Input = $(\'table#clientTab input#tel3\');' +
			'$tel3Input.textbox({});' +
			'$tel3Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel3Input.get(0));});' +
			'var $tel4Input = $(\'table#clientTab input#tel4\');' +
			'$tel4Input.textbox({});' +
			'$tel4Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel4Input.get(0));});' +
			'var $tel5Input = $(\'table#clientTab input#tel5\');' +
			'$tel5Input.textbox({});' +
			'$tel5Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel5Input.get(0));});' +
			'function submitaddClientForm()' + 
			'{' + 
			'	var errortel = $(\'#errorclienttel\');'+
			'	$addClientWindow.find(\'form#addClientForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'			if(errortel.html().length > 0){' + 
			'				return false;' + 
			'			}'+
			'			var flag = true;'+
			'			for(var i=1;i<6;i++){' +
			'				flag = checkClientTelValue($(\'table#clientTab input#tel\'+i+\'\').get(0));'+
			'				if(!flag)'+
			'					return false;'+
			'			}'+
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$infoerDatagrid.datagrid(\'reload\');' + 
			'				if($infoerMgrTab.tabs("getSelected").panel("options").title == "客户")' + 
			'					$clientGrid.datagrid(\'reload\');' + 
			'				$addClientWindow.window(\'close\');' + 
			'			}else' + 
			'				errortel.html(data.msg);' + 
			'		}' + 
			'	});' + 
			'}' + 
			'function checkClientTelValue(obj)' + 
			'{' + 
			'	var errorId = obj.id;'+
			'	errorId = errorId.charAt(errorId.length - 1);'+
			'	if(obj.value.length==0) '+
		    ' 	{ '+
		    '		if(errorId ==1) '+
		    ' 		{ '+
		    '      		$(\'#errorclienttel\').html("联系电话未填！"); '+
		    '      		return false; '+
		    '      	}'+
		    '   }else{ '+
			'		var reg = /^1[0-9]{10}$/;'+
			'		if(!(reg.test(obj.value))){'+
			'			$(\'#errorclienttel\').html("无效的手机号码！");'+
			'			return false; '+
			'		}else{'+
			'			$(\'#errorclienttel\').html("");'+
			'		}'+
			'		if(errorId > 1) '+
			' 		{'+
			'			if($(\'#tel1\').val() == obj.value){' + 
			'      			$(\'#errorclienttel\').html("联系电话重复！"); '+
			'      			return false; '+
			'   		} '+
			'   	}'+
			'	}'+
			'	if(obj.value.length >0){'+
			'		$.ajax'+
			'		({'+
			'			url: \'marketing/infoerMgr/checkClientTel\','+
			'			data: {tel:obj.value},'+
			'			success: function(data, textStatus, jqXHR)'+
			'			{'+
			'				if(data.returnCode != 0){'+
			'					$(\'#errorclienttel\').html(data.msg); '+  
			'					return false; '+  
			'				}else{ '+  
			'					$(\'#errorclienttel\').html(""); '+  
			'				} '+  
			'			}'+
			'		});'+
			'	}'+
			'   return true;'+
			'} ' + 
			'</script>';
	}
	
	init();
});