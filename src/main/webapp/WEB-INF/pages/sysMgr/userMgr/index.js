$(function()
{
	var $userDatagrid = $('table#userDatagrid');
	var $userNameTextbox = $('#userMgr\\.userNameInput');
	var $aliasTextbox = $('#userMgr\\.alias');
	var $queryUserBtn = $('a#queryUserBtn');
	var $addUserWindow = $('div#addUserWindow');
	var $revertUsersBtn = $('a#revertUsersBtn');
	var $removeUsersBtn = $('a#removeUsersBtn');
	var $userMgrTab = $('div#userMgrTab');
	var $editUserForm = $('form#editUserForm');
	var $submitUpdateUserFormBtn = $('a#submitUpdateUserFormBtn');
	var $refreshUpdateUserFormBtn = $('a#refreshUpdateUserFormBtn');
	var $notAssignUnderlingGrid = $('table#notAssignUnderlingGrid');
	var $assignedUnderlingGrid = $('table#assignedUnderlingGrid');
	var $assignUnderlingBtn = $('a#assignUnderlingBtn');
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
				{field:'name', title:'用户名', width: 5},
				{field:'alias', title:'昵称', width: 5},
				{
					field:'role', title:'角色', width: 5, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 0:
								return '管理员';
								break;
							case 1:
								return '市场部业务员';
								break;
							case 2:
								return '市场部主管';
								break;
							case 3:
								return '市场部经理';
								break;
							case 4:
								return '设计部设计师';
								break;
							case 5:
								return '设计部主管';
								break;
							case 2:
								return '设计部部经理';
								break;
							default:
								return '未知';
								break;
						}
					}
				},
				{
					field:'status', title:'状态', width: 5, formatter: function(value, row, index)
					{
						return value == 1 ? '正常' : '禁用';
					}
				}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'sysMgr/userMgr/getAllUsers',
			onSelect: function(idx, row)
			{
				if(row.role == 2 || row.role == 5)
				{
					$userMgrTab.tabs('enableTab', 1)
					$userMgrTab.tabs('update', {tab: $userMgrTab.tabs('getTab', 1), options: {title: row.role == 2 ? '下属业务员' : '下属设计师'}});
				}
				else
					$userMgrTab.tabs('select', 0).tabs('disableTab', 1)
				loadTabData($userMgrTab.tabs('getSelected').panel('options').title, row);
			},
			onCheck: updateBtnStatus,
			onUncheck: updateBtnStatus,
			onCheckAll: updateBtnStatus,
			onUncheckAll: updateBtnStatus,
		});
		
		$queryUserBtn.linkbutton
		({
			'onClick': function()
			{
				$userDatagrid.datagrid('loading');
				$.ajax
				({
					url: 'sysMgr/userMgr/getAllUsers',
					data: {userName: $userNameTextbox.textbox('getValue'), alias: $aliasTextbox.textbox('getValue')},
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
		
		$userMgrTab.tabs
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
		
		$submitUpdateUserFormBtn.linkbutton({'onClick': submitEditUserForm});
		$refreshUpdateUserFormBtn.linkbutton({'onClick': function()
		{
			var selRows = $userDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($userMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
		}});
		
		$notAssignUnderlingGrid.datagrid
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
		});
		
		$assignedUnderlingGrid.datagrid
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
				{field:'leaderName', title:'上级', width: 5},
				{
				  field:'status', title:'状态', width: 5, formatter: function(value, row, index)
				  {
					  return value == 1 ? '正常' : '禁用';
				  }
				}
			]],
			pagination: true
		});

		$assignedUnderlingGrid.datagrid('options').url = 'sysMgr/userMgr/getAssignedUnderlingByUser';
		$notAssignUnderlingGrid.datagrid('options').url = 'sysMgr/userMgr/getNotAssignUnderlingByUser';
		
		$assignUnderlingBtn.linkbutton
		({
			onClick: function()
			{
				var userIds = $userDatagrid.datagrid('getSelectRowPkValues');
				if(userIds.length == 0)
				{
					$.messager.alert('提示', '请选择要分配下属的用户。');
					return;
				}
				var underlingIds = $notAssignUnderlingGrid.datagrid('getSelectRowPkValues');
				if(underlingIds.length == 0)
				{
					$.messager.alert('提示', '请勾选要分配的下属用户。');
					return;
				}
				
				$.ajax
				({
					url: 'sysMgr/userMgr/addUnderlingToUser',
					dataType: 'JSON',
					data: {userId: userIds[0], underlingIds: underlingIds},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$notAssignUnderlingGrid.datagrid('unselectAll').datagrid('reload');
							$assignedUnderlingGrid.datagrid('unselectAll').datagrid('reload');
						}
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
					}
				});
			}
		});
			
		$removeUnderlingBtn.linkbutton
		({
			onClick: function()
			{
				var userIds = $userDatagrid.datagrid('getSelectRowPkValues');
				if(userIds.length == 0)
				{
					$.messager.alert('提示', '请选择要移除下属的用户。');
					return;
				}
				var underlingIds = $assignedUnderlingGrid.datagrid('getSelectRowPkValues');
				if(underlingIds.length == 0)
				{
					$.messager.alert('提示', '请勾选要移除的下属用户。');
					return;
				}
				$.ajax
				({
					url: 'sysMgr/userMgr/removeUnderlingFromUser',
					dataType: 'JSON',
					data: {userId: userIds[0], underlingIds: underlingIds},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$notAssignUnderlingGrid.datagrid('unselectAll').datagrid('reload');
							$assignedUnderlingGrid.datagrid('unselectAll').datagrid('reload');
						}
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
					}
				});
			}
		});

		function updateBtnStatus()
		{
			var selRows = $userDatagrid.datagrid('getChecked');
			if(selRows.length > 0)
			{
				var revertFlag = true, removeFlag = true;
				for(var i in selRows)
				{
					if(selRows[i].status == 1)
					{
						$revertUsersBtn.linkbutton('disable');
						revertFlag = false;
					}
					else
					{
						$removeUsersBtn.linkbutton('disable');
						removeFlag = false;
					}
				}
				if(revertFlag)
					$revertUsersBtn.linkbutton('enable');
				if(removeFlag)
					$removeUsersBtn.linkbutton('enable');
				return;
			}
			$revertUsersBtn.linkbutton('enable');
			$removeUsersBtn.linkbutton('enable');
		}
		
		function loadTabData(title, row)
		{
			switch (title)
			{
				case '详情':
					$editUserForm.form('clear').form('load', 'sysMgr/userMgr/getUserById?userId=' + row.id);
					break;
				case '下属业务员':
				case '下属设计师':
					$assignedUnderlingGrid.datagrid('unselectAll').datagrid('reload', {userId: row.id});
					$notAssignUnderlingGrid.datagrid('unselectAll').datagrid('reload', {userId: row.id});
					break;
			}
		}
		
		function clearTabData(title)
		{
			switch (title)
			{
				case '基本信息':
					$editUserForm.form('clear');
					break;
				case '产品权限信息':
					$notAssignUnderlingGrid.datagrid('loadData', []);
					$assignedUnderlingGrid.datagrid('loadData', []);
					break;
			}
		}
		
		function submitEditUserForm()
		{
			$editUserForm.form('submit',
			{
				onSubmit: function()
				{
					if(!$(this).form('validate'))
						return false;
					if($(this).find('[name="pwd"]').val() != $(this).find('[name="confirmPwd"]').val())
					{
						$.messager.alert('提示', '两次密码输入不一致，请重新输入密码。');
						return false;
					}
				},
				success: function(data)
				{
					data = $.fn.form.defaults.success(data);
					if(data.returnCode == 0)
						$userDatagrid.datagrid('reload');
				}
			});
		}
		
		$('#showAddUserWindowBtn').linkbutton({onClick: showAddUserWindow});
		$removeUsersBtn.linkbutton({onClick: removeUsers});
		$revertUsersBtn.linkbutton({onClick: revertUsers});
		
		$addUserWindow.window({width: 500});
		
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
		
		function showAddUserWindow()
		{
			$addUserWindow.window('clear');
			$addUserWindow.window('open').window
			({
				title: '新增用户',
				content: addUserWindowHtml
			}).window('open').window('center');
		}
		
		var addUserWindowHtml = 
			'<form id="addUserForm" action="sysMgr/userMgr/addUser" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>用户名：</label></td>' + 
			'			<td style="width: 240px;"><input name="name" class="easyui-textbox" required="required" style="width: 230px;"/></td>' + 
			'			<td align="right" style="vertical-align: top" rowspan="5"><label>角色：</label></td>' + 
			'			<td rowspan="5" style="width: 110px;">' + 
			'				<label><input type="radio" name="role" value="1" checked="checked">市场部业务员</label>' + 
			'				<br>' + 
			'				<label><input type="radio" name="role" value="2">市场部主管</label>' + 
			'				<br>' + 
			'				<label><input type="radio" name="role" value="3">市场部经理</label>' + 
			'				<br>' + 
			'				<label><input type="radio" name="role" value="4">设计部设计师</label>' + 
			'				<br>' + 
			'				<label><input type="radio" name="role" value="5">设计部主管</label>' + 
			'				<br>' + 
			'				<label><input type="radio" name="role" value="6">设计部经理</label>' + 
			'				<br>' + 
			'				<label><input type="radio" name="role" value="0">管理员</label>' + 
			'			</td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>姓名：</label></td>' + 
			'			<td><input name="alias" class="easyui-textbox" required="required" style="width: 230px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>电话：</label></td>' + 
			'			<td style="vertical-align: top"><input name="tel" class="easyui-textbox" required="required" style="width: 230px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>密码：</label></td>' + 
			'			<td><input name="pwd" class="easyui-textbox" type="password" required="required" style="width: 230px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>密码确认：</label></td>' + 
			'			<td><input name="confirmPwd" class="easyui-textbox" type="password" required="required" style="width: 230px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddUserForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addUserWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addUserWindow = $(\'div#addUserWindow\');' +
			'var $userDatagrid = $(\'table#userDatagrid\');' +
			'function submitAddUserForm()' + 
			'{' + 
			'	$addUserWindow.find(\'form#addUserForm\').form(\'submit\',' + 
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
			'				$addUserWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
	}

	init();
});