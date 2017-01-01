$(function()
{
	var $userDatagrid = $('table#userDatagrid');
	var $userNameTextbox = $('#userMgr\\.userNameInput');
	var $aliasTextbox = $('#userMgr\\.alias');
	var $queryUserBtn = $('a#queryUserBtn');
	var $addUserWindow = $('div#addUserWindow');
	var $addUnderlingWindow = $('div#addUnderlingWindow');
	var $revertUsersBtn = $('a#revertUsersBtn');
	var $removeUsersBtn = $('a#removeUsersBtn');
	var $userMgrTab = $('div#userMgrTab');
	var $editUserForm = $('form#editUserForm');
	var $submitUpdateUserFormBtn = $('a#submitUpdateUserFormBtn');
	var $refreshUpdateUserFormBtn = $('a#refreshUpdateUserFormBtn');
	var $assignedUnderlingGrid = $('table#assignedUnderlingGrid');
	var $showAddUnderlingWindowBtn = $('a#showAddUnderlingWindowBtn');
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
				{field:'alias', title:'姓名', width: 5},
				{
					field:'role', title:'角色', width: 5, formatter: function(value, row, index)
					{
						switch (value)
						{
							case -1:
								return '超级管理员';
								break;
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
					field:'leaderName', title:'上级', width: 5, formatter: function(value, row, index)
					{
						if(row.role <= 0)
							return '<span style="color: gray">-</span>';
						if(!value)
							return '<span style="color: red;">无</span>';
						return row.leaderId == row.id ? '<span style="color: gray">-</span>' : value;
					}
				},
				{
					field:'groupName', title:'所属组', width: 5, formatter: function(value, row, index)
					{
						if(row.role <= 0)
							return '<span style="color: gray">-</span>';
						if(!value)
							return '<span style="color: red;">无</span>';
						return value;
					}
				},
				{
					field:'status', title:'状态', width: 5, formatter: function(value, row, index)
					{
						return value == 1 ? '在职' : '离职';
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
		
		$assignedUnderlingGrid.datagrid
		({
			idField: 'id',
			toolbar: '#assignedUnderlingGridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'用户名', width: 5},
				{field:'alias', title:'姓名', width: 5},
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
							return '<span style="color: red;">无</span>';
						return value;
					}
				},
				{
					field:'groupName', title:'所属组', width: 5, formatter: function(value, row, index)
					{
						if(!value)
							return '<span style="color: red;">无</span>';
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

		$assignedUnderlingGrid.datagrid('options').url = 'sysMgr/userMgr/getAssignedUnderlingByUser';
		
		$('#showAddUserWindowBtn').linkbutton({onClick: showAddUserWindow});
		$showAddUnderlingWindowBtn.linkbutton({onClick: function()
		{
			var selRows = $userDatagrid.datagrid('getSelections');
			if(selRows.length == 0 || (selRows[0].role != 2 && selRows[0].role != 5))
			{
				$.messager.alert('提示', '请选择要分配下属的主管。');
				return;
			}
			showAddUnderlingWindow(selRows[0].alias, selRows[0].id);
		}});
		$removeUnderlingBtn.linkbutton({onClick: removeUnderlings});
		
		$removeUsersBtn.linkbutton({onClick: removeUsers});
		$revertUsersBtn.linkbutton({onClick: revertUsers});
		
		$addUserWindow.window({width: 500});
		$addUnderlingWindow.window({width: 500});

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
			if(title.startWith('下属') && row.groupId == null)		//判断是否有组
			{
				$.messager.alert('提示', '主管：<span style="color: red;">'
						+ row.alias + '</span>还没有被分配任何用户组，无法分配下属，请先为该主管分配用户组。');
				$userMgrTab.tabs('select', 0);
				return;
			}
			switch (title)
			{
				case '详情':
					$.ajax
					({
						url: 'sysMgr/userMgr/getUserById?userId=' + row.id,
						success: function(data, textStatus, jqXHR)
						{
							if(data.returnCode == 0)
							{
								if(data.id == data.leaderId)
									data.leaderName = '-';
								var comboData = [];
								if(data.role >= -1 && data.role <= 0)	//管理员
									comboData = [{text: '管理员', value: 0}, {text: '超级管理员', value: -1}];
								else if(data.role >= 1 && data.role <= 3)	//市场部
									comboData = [{text: '市场部业务员', value: 1}, {text: '市场部主管', value: 2}, {text: '市场部经理', value: 3}];
								else if(data.role >=4  && data.role <= 6)	//设计部
									comboData = [{text: '设计部设计师', value: 4}, {text: '设计部主管', value: 5}, {text: '设计部经理', value: 6}];
								$('#userEditForm\\.role').combobox('loadData', comboData);
								
								$editUserForm.form('clear').form('load', data);
							}
							else
								$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						}
					});
					break;
				case '下属业务员':
				case '下属设计师':
					$assignedUnderlingGrid.datagrid('unselectAll').datagrid('reload', {userId: row.id});
					break;
			}
		}
		
		function clearTabData(title)
		{
			switch (title)
			{
				case '详情':
					$editUserForm.form('clear');
					break;
				default:
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
		
		function revertUsers()
		{
			var selIds = $userDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>要设为在职的用户。');
				return;
			}
			
			if($.inArray(_session_loginUser.id, selIds) >= 0)
			{
				$.messager.alert('提示', '不设置自己。');
				return;
			}
			
			$.messager.confirm('警告','确定要恢复选中的用户到<span style="color: red;">在职</span>状态吗？',function(r)
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
								$.messager.show({title:'提示',msg:'设置成功。'});
								$userDatagrid.datagrid('reload');
							}
							else
								$.messager.show({title:'提示', msg:'设置失败\n' + data.msg});   
						}
				);
			});
		}
		
		function removeUnderlings()
		{
			var userRow = $userDatagrid.datagrid('getSelections')[0];
			var selIds = $assignedUnderlingGrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>要移除的下属。');
				return;
			}
			$.messager.confirm('警告','确定要将勾选的用户从<span style="color: red;">' + userRow.groupName + '</span>移除吗？',
			function(r)
			{
				if (!r)
					return;
				$.post
				(
					'sysMgr/userMgr/removeUnderlingFromUser',
					{userId: userRow.id, underlingIds : selIds},
					function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$.messager.show({title:'提示',msg:'移除成功。'});
							$userDatagrid.datagrid('reload');
							$assignedUnderlingGrid.datagrid('reload');
						}
						else
							$.messager.show({title:'提示', msg:'移除失败\n' + data.msg});   
					},
					'JSON'
				);
			});
		}
		
		function removeUsers()
		{
			var selIds = $userDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>要设为离职的用户。');
				return;
			}
	
			$.messager.confirm('警告','确定要修改勾选的用户为<span style="color: red;">离职</span>状态吗？',function(r)
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
							$.messager.show({title:'提示',msg:'设置成功。'});
							$userDatagrid.datagrid('reload');
						}
						else
							$.messager.show({title:'提示', msg:'设置失败\n' + data.msg});   
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
		
		function showAddUnderlingWindow(alias, id)
		{
			$addUnderlingWindow.window('clear');
			$addUnderlingWindow.window('open').window
			({
				title: '请勾选需要为 ' + alias + ' 分配的下属'
			}).window('open').window('refresh', 'sysMgr/userMgr/showAssignUnderlingWindow?leaderId=' + id);
		}
		
		var addUserWindowHtml = 
			'<form id="addUserForm" action="sysMgr/userMgr/addUser" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>用户名：</label></td>' + 
			'			<td style="width: 170px;"><input name="name" class="easyui-textbox" data-options="required:true, validType:\'length[4, 16]\'" style="width: 160px;"/></td>' + 
			'			<td align="right"><label>姓名：</label></td>' + 
			'			<td><input name="alias" class="easyui-textbox" data-options="required:true, validType:\'length[2, 16]\'" style="width: 160px;"/></td>' + 
			'		<tr>' + 
			'			<td align="right"><label>密码：</label></td>' + 
			'			<td><input name="pwd" class="easyui-textbox" type="password" data-options="required:true, validType:\'length[6, 24]\'" style="width: 160px;"/></td>' + 
			'			<td align="right"><label>电话：</label></td>' + 
			'			<td style="vertical-align: top"><input name="tel" class="easyui-textbox" required="required" style="width: 160px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>密码确认：</label></td>' + 
			'			<td><input name="confirmPwd" class="easyui-textbox" type="password" data-options="required:true, validType:\'length[6, 24]\'" style="width: 160px;"/></td>' + 
			'			<td align="right"><label>角色：</label></td>' + 
			'			<td style="width: 170px;">' +
			'				<select class="easyui-combobox" name="role" style="width: 160px;" initValue="1">' +
			'					<option value="1" selected="selected">市场部业务员</option>' +
			'					<option value="2">市场部主管</option>' +
			'					<option value="3">市场部经理</option>' +
			'					<option value="4">设计部设计师</option>' +
			'					<option value="5">设计部主管</option>' +
			'					<option value="6">设计部经理</option>' +
			'					<option value="0">管理员</option>' +
			'				</select>' +
			'			</td>' + 
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