$(function()
{
	var $commissionCostGrid = $('table#commissionCostGrid');
	var $clientMgrTab = $('div#clientMgrTab');
	$('a#addCommissionCostRecordBtn').linkbutton({'onClick': function()
	{
		$showAddCommissionCostWindow.find('form#addCommissionCostForm').form('submit',
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
				{
					if($clientMgrTab.tabs("getSelected").panel("options").title == "提成")
						$commissionCostGrid.datagrid('reload');
					$showAddCommissionCostWindow.window('close');
				}else
					$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});
			}
		});
	}});
});