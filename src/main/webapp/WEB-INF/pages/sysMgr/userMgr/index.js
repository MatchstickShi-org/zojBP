$(function()
{
	var $userDatagrid = $('table#userDatagrid');
	var $userNameTextbox = $('#userMgr\\.userNameInput');
	var $aliasTextbox = $('#userMgr\\.alias');
	var $queryUserBtn = $('a#queryUserBtn');
	var $addUserWindow = $('div#addUserWindow');
	var $userMgrTab = $('div#userMgrTab');
	var $editUserForm = $('form#editUserForm');
	var $submitUpdateUserFormBtn = $('a#submitUpdateUserFormBtn');
	var $refreshUpdateUserFormBtn = $('a#refreshUpdateUserFormBtn');
	var $notAssignProductBrandTree = $('ul#notAssignProductBrandTree');
	var $assignedProductBrandTree = $('ul#assignedProductBrandTree');
	var $addUserBrandBtn = $('a#addUserBrandBtn');
	var $removeUserBrandBtn = $('a#removeUserBrandBtn');
	
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
				loadTabData($userMgrTab.tabs('getSelected').panel('options').title, row);
			}
		});
		
		$queryUserBtn.linkbutton
		({
			'onClick': function()
			{
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
		
		$notAssignProductBrandTree.tree
		({
			checkbox: true,
			onlyLeafCheck: true,
			onBeforeCheck: function(node, checked)
			{
				if(checked && !node.attributes.isLeaf)
					return false;
			}
		});
		
		$assignedProductBrandTree.tree
		({
			checkbox: true,
			onlyLeafCheck: true,
			onBeforeCheck: function(node, checked)
			{
				if(checked && !node.attributes.isLeaf)
					return false;
			}
		});
		
		$addUserBrandBtn.linkbutton
		({
			onClick: function()
			{
				var userIds = $userDatagrid.datagrid('getSelectRowPkValues');
				if(userIds.length == 0)
				{
					$.messager.alert('提示', '请选择要赋予产品权限的用户。');
					return;
				}
				var chkNodes = $notAssignProductBrandTree.tree('getChecked');
				if(chkNodes.length == 0)
				{
					$.messager.alert('提示', '请勾选要分配给选择用户的产品品牌。');
					return;
				}
				
				var brandIds = [];
				for(var i in chkNodes)
				{
					if(!chkNodes[i].attributes.isLeaf)		//非品牌
					{
						$.messager.alert('提示', '请勾选要分配给选择用户的产品品牌。');
						return;
					}
					brandIds.push(chkNodes[i].id);
				}
				$.ajax
				({
					url: 'sysMgr/userMgr/addBrandsToUser',
					dataType: 'JSON',
					data: {userId: userIds[0], brandIds: brandIds},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$notAssignProductBrandTree.tree('loadData', data.notAssignProductBrandTree);
							$assignedProductBrandTree.tree('loadData', data.assignedProductBrandTree);
						}
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
					}
				});
			}
		});
			
		$removeUserBrandBtn.linkbutton
		({
			onClick: function()
			{
				var userIds = $userDatagrid.datagrid('getSelectRowPkValues');
				if(userIds.length == 0)
				{
					$.messager.alert('提示', '请选择要从移除产品权限的用户。');
					return;
				}
				var chkNodes = $assignedProductBrandTree.tree('getChecked');
				if(chkNodes.length == 0)
				{
					$.messager.alert('提示', '请勾选要从选择用户权限中移除的产品品牌。');
					return;
				}
				var brandIds = [];
				for(var i in chkNodes)
				{
					if(!chkNodes[i].attributes.isLeaf)		//非品牌
					{
						$.messager.alert('提示', '请勾选要从选择用户权限中移除的产品品牌。');
						return;
					}
					brandIds.push(chkNodes[i].id);
				}
				$.ajax
				({
					url: 'sysMgr/userMgr/removeBrandsFromUser',
					dataType: 'JSON',
					data: {userId: userIds[0], brandIds: brandIds},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$notAssignProductBrandTree.tree('loadData', data.notAssignProductBrandTree);
							$assignedProductBrandTree.tree('loadData', data.assignedProductBrandTree);
						}
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
					}
				});
			}
		});
		
		function loadTabData(title, row)
		{
			switch (title)
			{
				case '基本信息':
					$editUserForm.form('load', 'sysMgr/userMgr/getUserById?userId=' + row.id);
					break;
				case '产品权限信息':
					if(row.role != 1)
					{
						$addUserBrandBtn.linkbutton('disable');
						$removeUserBrandBtn.linkbutton('disable');
					}
					else
					{
						$addUserBrandBtn.linkbutton('enable');
						$removeUserBrandBtn.linkbutton('enable');
					}
					$.ajax
					({
						url: 'sysMgr/userMgr/getBrandTreeByUser',
						dataType: 'JSON',
						data: {userId: row.id},
						success: function(data, textStatus, jqXHR)
						{
							if(data.returnCode == 0)
							{
								$notAssignProductBrandTree.tree('loadData', data.notAssignProductBrandTree);
								$assignedProductBrandTree.tree('loadData', data.assignedProductBrandTree);
							}
							else
								$.messager.show({title:'提示', msg:'获取用户权限信息失败\n' + data.msg});   
						}
					});
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
					$notAssignProductBrandTree.tree('loadData', []);
					$assignedProductBrandTree.tree('loadData', []);
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
		$('#removeUsersBtn').linkbutton({onClick: removeUsers});
		
		$addUserWindow.window({width: 500});
		
		function removeUsers()
		{
			var selIds = $userDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选择要删除的用户。');
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
			'			<td><input name="name" class="easyui-textbox" required="required" style="width: 230px;"/></td>' + 
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