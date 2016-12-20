$(function()
{
	var $userDatagrid = $('table#userDatagrid');
	var $infoerNameTextbox = $('#infoerMgr\\.nameInput');
	var $telTextbox = $('#infoerMgr\\.telInput');
	var $levelCheckbox = $('input[name="levelInput"]:checked');
	var $queryInfoerBtn = $('a#queryInfoerBtn');
	var $addInfoerWindow = $('div#addInfoerWindow');
	var $addInfoerVisitWindow = $('div#addInfoerVisitWindow');
	var $addClientWindow = $('div#addClientWindow');
	var $revertUsersBtn = $('a#revertUsersBtn');
	var $removeUsersBtn = $('a#removeUsersBtn');
	var $infoerMgrTab = $('div#infoerMgrTab');
	var $editInfoerForm = $('form#editInfoerForm');
	var $submitUpdateInfoerFormBtn = $('a#submitUpdateInfoerFormBtn');
	var $refreshUpdateUserFormBtn = $('a#refreshUpdateUserFormBtn');
	var $infoerVisitGrid = $('table#infoerVisitGrid');
	var $clientGrid = $('table#clientGrid');
	var $removeUnderlingBtn = $('a#removeUnderlingBtn');
	
	function init()
	{
		$userDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#userDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'名称', width: 5},
				{field:'tel', title:'联系电话', width: 5},
				{
					field:'level', title:'等级', width: 5, formatter: function(value, row, index)
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
					field:'nature', title:'性质', width: 5, formatter: function(value, row, index)
					{
						return value == 1 ? '中介' : '售楼';
					}
				},
				{field:'org', title:'工作单位', width: 5},
				{field:'address', title:'地址', width: 5},
				{field:'salesmanName', title:'业务员', width: 5},
				{field:'visitLeftDays', title:'回访剩余天数', width: 5,
					styler: function (value, row, index) {
						if(value < 5)
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
				loadTabData($infoerMgrTab.tabs('getSelected').panel('options').title, row);
			},
			onCheck: updateBtnStatus,
			onUncheck: updateBtnStatus,
			onCheckAll: updateBtnStatus,
			onUncheckAll: updateBtnStatus,
		});
		
		$queryInfoerBtn.linkbutton
		({
			'onClick': function()
			{
				$userDatagrid.datagrid('loading');
				$levelCheckbox.each(function(){ 
					alert($(this).val()); 
					}); 
				$.ajax
				({
					url: 'marketing/infoerMgr/getAllInfoers',
					data: {name: $infoerNameTextbox.textbox('getValue'), tel: $telTextbox.textbox('getValue'),level:$levelCheckbox.val()},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$userDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$userDatagrid.datagrid('loaded');
					}
				});
			}
		});
		
		$infoerMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selRows = $userDatagrid.datagrid('getSelections');
				
				if(selRows.length == 1)
					loadTabData(title, selRows[0]);
				else
					clearTabData(title);
			}
		});
		
		$submitUpdateInfoerFormBtn.linkbutton({'onClick': submitEditInfoerForm});
		$refreshUpdateUserFormBtn.linkbutton({'onClick': function()
		{
			var selRows = $userDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($infoerMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
		}});
		
		/*$notAssignUnderlingGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'用户名', width: 5},
				{field:'alias', title:'昵称', width: 5},
				{
					field:'role', title:'角色', width: 5, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 1:
								return '市场部业务员';
								break;
							case 4:
								return '设计部设计师';
								break;
							default:
								return '未知';
								break;
						}
					}
				},
				{
					field:'leaderName', title:'上级', width: 5, formatter: function(value, row, index)
					{
						if(!value)
							return '<span style="color: gray;">无</span>';
						return value;
					}
				},
				{
					field:'status', title:'状态', width: 5, formatter: function(value, row, index)
					{
						return value == 1 ? '正常' : '禁用';
					}
				}
			]],
			pagination: true
		});*/
		
		$infoerVisitGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'回访内容', width: 5},
				{field:'date', title:'回访日期', width: 5}
			]],
			pagination: true
		});
		
		$clientGrid.datagrid
		({
			idField: 'id',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'name', title:'联系人', width: 5},
				  {field:'tel', title:'联系电话', width: 5},
				  {field:'orgAddr', title:'单位地址', width: 5},
				  {field:'projectName', title:'工程名称', width: 5},
				  {field:'projectAddr', title:'工程地址', width: 5},
				  {field:'infoerName', title:'信息员', width: 5},
				  {field:'salesmanName', title:'业务员', width: 5},
				  {field:'insertTime', title:'录入日期', width: 5}
				  ]],
				  pagination: true
		});

		$infoerVisitGrid.datagrid('options').url = 'marketing/infoerMgr/getInfoerVisitByInfoer';
		$clientGrid.datagrid('options').url = 'marketing/infoerMgr/getClientByInfoer';
		
		function updateBtnStatus()
		{
			var selRows = $userDatagrid.datagrid('getChecked');
			if(selRows.length > 0)
			{
				var revertFlag = true, removeFlag = true;
			}
		}
		
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
				case '客户':
					$clientGrid.datagrid('loadData', []);
					break;
			}
		}
		
		function submitEditInfoerForm()
		{
			$editInfoerForm.form('submit',
			{
				onSubmit: function()
				{
					if(!$(this).form('validate'))
						return false;
					/*if($(this).find('[name="pwd"]').val() != $(this).find('[name="confirmPwd"]').val())
					{
						$.messager.alert('提示', '两次密码输入不一致，请重新输入密码。');
						return false;
					}*/
				},
				success: function(data)
				{
					data = $.fn.form.defaults.success(data);
					if(data.returnCode == 0)
						$userDatagrid.datagrid('reload');
				}
			});
		}
		
		$('#showAddInfoerWindowBtn').linkbutton({onClick: showAddInfoerWindow});
		$('#addInfoerVisitBtn').linkbutton({onClick: showAddInfoerVisitWindow});
		$('#addClientBtn').linkbutton({onClick: showAddClientWindow});
		$removeUsersBtn.linkbutton({onClick: removeUsers});
		$revertUsersBtn.linkbutton({onClick: revertUsers});
		
		$addInfoerWindow.window({width: 500});
		$addInfoerVisitWindow.window({width: 350});
		$addClientWindow.window({width: 450});
		
		function revertUsers()
		{
			var selIds = $userDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>要恢复的用户。');
				return;
			}
			
			if($.inArray(_session_loginUser.id, selIds) >= 0)
			{
				$.messager.alert('提示', '不恢复除自己。');
				return;
			}
			
			$.messager.confirm('警告','确定要恢复选中的用户吗？',function(r)
			{
				if (!r)
					return;
				$.post
				(
						'sysMgr/userMgr/revertUserByIds',
						{revertIds : selIds},
						function(data, textStatus, jqXHR)
						{
							if(data.returnCode == 0)
							{
								$.messager.show({title:'提示',msg:'恢复成功。'});
								$userDatagrid.datagrid('reload');
							}
							else
								$.messager.show({title:'提示', msg:'恢复失败\n' + data.msg});   
						}
				);
			});
		}
		
		function removeUsers()
		{
			var selIds = $userDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>要删除的用户。');
				return;
			}
			
			if($.inArray(_session_loginUser.id, selIds) >= 0)
			{
				$.messager.alert('提示', '不能删除自己。');
				return;
			}
	
			$.messager.confirm('警告','确定要删除选中的用户吗？',function(r)
			{
				if (!r)
					return;
				$.post
				(
					'sysMgr/userMgr/deleteUserByIds',
					{delIds : selIds},
					function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$.messager.show({title:'提示',msg:'删除成功。'});
							$userDatagrid.datagrid('reload');
						}
						else
							$.messager.show({title:'提示', msg:'删除失败\n' + data.msg});   
					},
					'JSON'
				);
			});
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
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>名称：</label></td>' + 
			'			<td><input name="name" class="easyui-textbox" required="required" style="width: 230px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>电话：</label></td>' + 
			'			<td style="vertical-align: top"><input name="tel" class="easyui-textbox" required="required" style="width: 230px;"/> ＋</td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>性质：</label></td>' + 
			'			<td><label><input type="radio" name="nature" value="1" checked="checked">中介</label>' + 
			'				<label><input type="radio" name="nature" value="2">售楼</label>' + 
			'			</td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工作单位：</label></td>' + 
			'			<td><input name="org" class="easyui-textbox" style="width: 230px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>地址：</label></td>' + 
			'			<td><input name="address" class="easyui-textbox" style="width: 230px;"/></td>' + 
			'		</tr>' + 
			'		<input name="level" type="hidden" value="4" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitaddInfoerForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addInfoerWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addInfoerWindow = $(\'div#addInfoerWindow\');' +
			'var $userDatagrid = $(\'table#userDatagrid\');' +
			'function submitaddInfoerForm()' + 
			'{' + 
			'	$addInfoerWindow.find(\'form#addInfoerForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'			if($(this).find(\'[name="pwd"]\').val() != $(this).find(\'[name="confirmPwd"]\').val())' + 
			'			{' + 
			'				$.messager.alert(\'提示\', \'两次密码输入不一致，请重新输入密码。\');' + 
			'				return false;' + 
			'			}' + 
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$userDatagrid.datagrid(\'reload\');' + 
			'				$addInfoerWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddInfoerVisitWindow()
		{
			$addInfoerVisitWindow.window('clear');
			$addInfoerVisitWindow.window('open').window
			({
				title: '新增回访记录',
				content: addInfoerVisitWindowHtml
			}).window('open').window('center');
		}
		
		var addInfoerVisitWindowHtml = 
			'<form id="addInfoerVisitForm" action="marketing/infoerMgr/addInfoerVisit" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>回访内容：</label></td>' + 
			'			<td><textarea name="content" required="required" style="width: 230px;height:150px;"></textarea></td>' + 
			'		</tr>' + 
			'		<input id="infoerId"  name="infoerId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitaddInfoerVisitForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addInfoerVisitWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addInfoerVisitWindow = $(\'div#addInfoerVisitWindow\');' +
			'var $userDatagrid = $(\'table#userDatagrid\');' +
			'var $infoerVisitGrid = $("table#infoerVisitGrid");' +
			'var selRows = $userDatagrid.datagrid("getSelections");' +
			'$addInfoerVisitWindow.find(\'#infoerId\').val(selRows[0].id);' +
			'function submitaddInfoerVisitForm()' + 
			'{' + 
			'	$addInfoerVisitWindow.find(\'form#addInfoerVisitForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$infoerVisitGrid.datagrid(\'reload\');' + 
			'				$addInfoerVisitWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddClientWindow()
		{
			$addClientWindow.window('clear');
			$addClientWindow.window('open').window
			({
				title: '新增客户',
				content: addClientWindowHtml
			}).window('open').window('center');
		}
		
		var addClientWindowHtml = 
			'<form id="addClientForm" action="marketing/infoerMgr/addClient" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>联系人：</label></td>' + 
			'			<td><input name="name" class="easyui-textbox" required="required" style="width: 150px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话：</label></td>' + 
			'			<td><input name="tel"  onblur="checkValue(this)" class="easyui-textbox" required="required" style="width: 150px;"/> ＋<label></label></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>所属信息员：</label></td>' + 
			'			<td><input id="infoerName" name="infoerName" readonly="readonly" class="easyui-textbox" style="width: 150px;"/></td>' + 
			'		</tr>' + 
			'		<input id="infoerId"  name="infoerId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="right"><label>单位地址：</label></td>' + 
			'			<td><input name="orgAddr" class="easyui-textbox" style="width: 250px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程名称：</label></td>' + 
			'			<td><input name="projectName" class="easyui-textbox" style="width: 250px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程地址：</label></td>' + 
			'			<td><input name="projectAddr" class="easyui-textbox" style="width: 250px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitaddClientForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addClientWindow = $(\'div#addClientWindow\');' +
			'var $userDatagrid = $(\'table#userDatagrid\');' +
			'var $clientGrid = $("table#clientGrid");' +
			'var selRows = $userDatagrid.datagrid("getSelections");' +
			'$addClientWindow.find(\'#infoerId\').val(selRows[0].id);' +
			'$addClientWindow.find(\'#infoerName\').val(selRows[0].name);' +
			'function submitaddClientForm()' + 
			'{' + 
			'	$addClientWindow.find(\'form#addClientForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$clientGrid.datagrid(\'reload\');' + 
			'				$addClientWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'function checkValue(obj)' + 
			'{' + 
			'	alert(obj.value);' + 
			'}' + 
			'</script>';
	}

	init();
});