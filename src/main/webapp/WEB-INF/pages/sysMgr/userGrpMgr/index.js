$(function()
{
	var $groupDatagrid = $('table#groupDatagrid');
	var $groupTypeInput = $('#groupDatagridToolbar :radio[name="type"]');
	var $queryGroupBtn = $('a#queryGroupBtn');
	var $addGroupWindow = $('div#addGroupWindow');
	var $selectLeaderWindow = $('div#selectLeaderWindow');
	var $addUnderlingWindow = $('div#addUnderlingWindow');
	var $removeGroupsBtn = $('a#removeGroupsBtn');
	var $userGrpMgrTab = $('div#userGrpMgrTab');
	var $editGroupForm = $('form#editGroupForm');
	var $saveGroupBtn = $('a#saveGroupBtn');
	var $refreshGroupBtn = $('a#refreshGroupBtn');
	var $assignedUnderlingGrid = $('table#assignedUnderlingGrid');
	var $showAddUnderlingWindowBtn = $('a#showAddUnderlingWindowBtn');
	var $removeUnderlingBtn = $('a#removeUnderlingBtn');
	var $leaderSearchbox = $('input#editGroupForm_leaderSearchbox');
	
	initDatagrid();
	initBtn();
	initTabs();
	initWindow();
	initForm();
	
	function initForm()
	{
		$leaderSearchbox.searchbox
		({
			searcher: function()
			{
				var selRs = $groupDatagrid.datagrid('getSelections');
				if(selRs.length > 0)
					showSelectLeaderWindow('edit', selRs[0].id, selRs[0].name);
			}
		});
	}

	function initDatagrid()
	{
		$groupDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#groupDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'组名', width: 5},
				{field:'leaderName', title:'主管', width: 5},
				{
					field:'type', title:'所属部门', width: 5, formatter: function(value, row, index)
					{
						return value == 0 ? '商务部' : '主案部';
					}
				}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'sysMgr/userGrpMgr/getAllGroups',
			onSelect: function(idx, row)
			{
				$userGrpMgrTab.tabs('update', {tab: $userGrpMgrTab.tabs('getTab', 1), 
					options: {title: row.type == 0 ? '下属业务员' : '下属设计师'}});
				loadTabData($userGrpMgrTab.tabs('getSelected').panel('options').title, row);
			}
		});

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
							  return '商务部业务员';
							  break;
						  case 4:
							  return '主案部设计师';
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

		$assignedUnderlingGrid.datagrid('options').url = 'sysMgr/userGrpMgr/getAssignedUnderlingByGroup';
	}
	
	function initBtn()
	{
		$queryGroupBtn.linkbutton
		({
			'onClick': function()
			{
				var data = {};
				var type = $groupTypeInput.filter(':checked').val();
				if(type != '-1')
					data.type = type;
				$groupDatagrid.datagrid('loading');
				$.ajax
				({
					url: 'sysMgr/userGrpMgr/getAllGroups',
					data: data,
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$groupDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$groupDatagrid.datagrid('loaded');
					}
				});
			}
		});

		$saveGroupBtn.linkbutton({'onClick': saveGroup});
		$refreshGroupBtn.linkbutton({'onClick': function()
		{
			var selRows = $groupDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($userGrpMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
		}});
		

		$('#showAddGroupWindowBtn').linkbutton({onClick: showAddGroupWindow});
		$showAddUnderlingWindowBtn.linkbutton({onClick: function()
		{
			var selRows = $groupDatagrid.datagrid('getSelections');
			if(selRows.length == 0)
			{
				$.messager.alert('提示', '请选择要分配下属的用户组。');
				return;
			}
			showAddUnderlingWindow(selRows[0].name, selRows[0].id);
		}});
		$removeUnderlingBtn.linkbutton({onClick: removeUnderlings});
		
		$removeGroupsBtn.linkbutton({onClick: removeGroups});
	}
	
	function initTabs()
	{
		$userGrpMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selRows = $groupDatagrid.datagrid('getSelections');
				
				if(selRows.length == 1)
					loadTabData(title, selRows[0]);
				else
					clearTabData(title);
			}
		});
	}

	function initWindow()
	{
		$addGroupWindow.window({width: 460});
		$addUnderlingWindow.window({width: 500});
		$selectLeaderWindow.window({width: 500});
	}
	
	function loadTabData(title, row)
	{
		switch (title)
		{
			case '详情':
				$.ajax
				({
					url: 'sysMgr/userGrpMgr/getGroupById?groupId=' + row.id,
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$editGroupForm.form('clear').form('load', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
					}
				});
				break;
			case '下属业务员':
			case '下属设计师':
				$assignedUnderlingGrid.datagrid('unselectAll').datagrid('reload', {groupId: row.id});
				break;
		}
	}
	
	function clearTabData(title)
	{
		switch (title)
		{
			case '详情':
				$editGroupForm.form('clear');
				break;
			default:
				$assignedUnderlingGrid.datagrid('loadData', []);
				break;
		}
	}
	
	function saveGroup()
	{
		$editGroupForm.form('submit',
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
					$groupDatagrid.datagrid('clearSelections').datagrid('reload');
			}
		});
	}
	
	function removeUnderlings()
	{
		var grpRow = $groupDatagrid.datagrid('getSelections')[0];
		var selIds = $assignedUnderlingGrid.datagrid('getCheckedRowPkValues');
		if(selIds.length == 0)
		{
			$.messager.alert('提示', '请<span style="color: red;">勾选</span>要移除的下属。');
			return;
		}
		$.messager.confirm('警告','确定要将勾选的用户从<span style="color: red;">' + grpRow.name + '</span>移除吗？',
		function(r)
		{
			if (!r)
				return;
			$.post
			(
				'sysMgr/userGrpMgr/removeUnderlingFromGroup',
				{groupId: grpRow.id, underlingIds : selIds},
				function(data, textStatus, jqXHR)
				{
					if(data.returnCode == 0)
					{
						$.messager.show({title:'提示',msg:'移除成功。'});
						$groupDatagrid.datagrid('clearSelections').datagrid('reload');
						$assignedUnderlingGrid.datagrid('clearSelections').datagrid('reload');
					}
					else
						$.messager.show({title:'提示', msg:'移除失败\n' + data.msg});   
				},
				'JSON'
			);
		});
	}
	
	function removeGroups()
	{
		var selIds = $groupDatagrid.datagrid('getCheckedRowPkValues');
		if(selIds.length == 0)
		{
			$.messager.alert('提示', '请<span style="color: red;">勾选</span>要设为删除的用户组。');
			return;
		}

		$.messager.confirm('警告','确定要删除勾选的用户组吗？',function(r)
		{
			if (!r)
				return;
			$.post
			(
				'sysMgr/userGrpMgr/deleteGroupByIds',
				{delIds : selIds},
				function(data, textStatus, jqXHR)
				{
					if(data.returnCode == 0)
					{
						$groupDatagrid.datagrid('clearSelections').datagrid('reload');
						$userGrpMgrTab.tabs('select', 0);
						clearTabData($userGrpMgrTab.tabs('getSelected').panel('options').title);
					}
					else
						$.messager.show({title:'提示', msg:'删除失败<br>' + data.msg});   
				},
				'JSON'
			);
		});
	}
	
	function showAddUnderlingWindow(groupName, groupId)
	{
		$addUnderlingWindow.window('clear');
		$addUnderlingWindow.window('open').window
		({
			title: '请勾选需要为 ' + groupName + ' 分配的下属'
		}).window('open').window('refresh', 'sysMgr/userGrpMgr/showAssignUnderlingWindow?groupId=' + groupId);
	}
	
	showSelectLeaderWindow = function(from, groupId, dept)
	{
		$selectLeaderWindow.window('clear');
		$selectLeaderWindow.window('open').window
		({
			title: dept ? '请选择要设置为<span style="color: red;">[' + dept + ']主管</span>的用户' : '请选择主管',
		}).window('open').window('refresh', 'sysMgr/userGrpMgr/showSelectLeaderWindow?from=' + from + '&groupId=' + (groupId || ''));
	}
	
	function showAddGroupWindow()
	{
		$addGroupWindow.window('clear');
		$addGroupWindow.window('open').window
		({
			title: '新增用户组',
			content: addGroupWindowHtml
		}).window('open').window('center');
	}
	
	var addGroupWindowHtml = 
		'<form id="addGroupForm" action="sysMgr/userGrpMgr/addGroup" method="post" style="width: 100%;">' + 
		'	<input type="hidden" name="leaderId">' + 
		'	<input type="hidden" name="type">' + 
		'	<table width="100%">' + 
		'		<tr>' + 
		'			<td align="right"><label>组名：</label></td>' + 
		'			<td style="width: 170px;"><input name="name" class="easyui-textbox" data-options="required:true, validType:\'length[4, 16]\'" style="width: 160px;"/></td>' + 
		'			<td align="right"><label>部门：</label></td>' + 
		'			<td><input id="addGroupWin_deptInput" class="easyui-textbox" value="-" readonly="readonly" style="width: 160px;"></td>' + 
		'		<tr>' + 
		'		<tr>' + 
		'			<td align="right"><label>主管：</label></td>' + 
		'			<td><input id="addGroupWin_leaderSearchbox" name="leaderName" prompt="请选择主管" editable="false" class="easyui-searchbox" style="width: 160px;"/></td>' + 
		'		<tr>' + 
		'			<td align="center" colspan="4">' + 
		'				<a class="easyui-linkbutton" onclick="addGroup();" iconCls="icon-ok" href="javascript:void(0)">新增</a>' + 
		'				<a class="easyui-linkbutton" onclick="$addGroupWindow.window(\'close\');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>' + 
		'			</td>' + 
		'		</tr>' +
		'	</table>' + 
		'</form>' +
		'<script type="text/javascript">' + 
		'var $addGroupWindow = $(\'div#addGroupWindow\');' +
		'var $groupDatagrid = $(\'table#groupDatagrid\');' +
		'var $leaderNameSearchbox = $addGroupWindow.find("#addGroupWin_leaderSearchbox");' +
		'$leaderNameSearchbox.searchbox({searcher: function(){showSelectLeaderWindow("add");}});' + 
		'function addGroup()' + 
		'{' + 
		'	$addGroupWindow.find(\'form#addGroupForm\').form(\'submit\',' + 
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
		'				$groupDatagrid.datagrid(\'reload\');' + 
		'				$addGroupWindow.window(\'close\');' + 
		'			}' + 
		'		}' + 
		'	});' + 
		'}' + 
		'</script>';
});