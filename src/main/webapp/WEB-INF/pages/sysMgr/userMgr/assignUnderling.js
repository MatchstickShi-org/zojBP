$(function()
{
	var $userDatagrid = $('table#userDatagrid');
	var $assignedUnderlingGrid = $('table#assignedUnderlingGrid');
	var $notAssignUnderlingGrid = $('table#notAssignUnderlingGrid');
	var $addUnderlingWindow = $('div#addUnderlingWindow');
	var $assignUnderlingBtn = $('a#assignUnderlingBtn');
	var $cancelAssignUnderlingBtn = $('a#cancelAssignUnderlingBtn');
	
	function init()
	{
		$notAssignUnderlingGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
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
					field:'groupName', title:'所属小组', width: 5, formatter: function(value, row, index)
					{
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
			url: 'sysMgr/userMgr/getNotAssignUnderlingByUser',
			queryParams: {userId: _param_leaderId}
		});
		
		$assignUnderlingBtn.linkbutton
		({
			'onClick': function()
			{
				var selIds = $notAssignUnderlingGrid.datagrid('getSelectRowPkValues');
				if(selIds.length == 0)
				{
					$.messager.alert('提示', '请选择要分配的下属。');
					return;
				}
				$.ajax
				({
					url: 'sysMgr/userMgr/addUnderlingToUser',
					data: {userId: _param_leaderId, underlingIds: selIds},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$assignedUnderlingGrid.datagrid('reload');
							$userDatagrid.datagrid('reload');
							$addUnderlingWindow.window('close');
						}
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});
					}
				});
			}
		});
		
		$cancelAssignUnderlingBtn.linkbutton({'onClick': function(){$addUnderlingWindow.window('close');}});
	}
	init();
});