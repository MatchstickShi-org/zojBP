$(function()
{
	var $costGrid = $('table#commissionMgr-costGrid');
	$('a#commissionMgr-addBtn').linkbutton({'onClick': function()
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
					$costGrid.datagrid('reload');
					$showAddCommissionCostWindow.window('close');
				}
				else
					$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});
			}
		});
	}});
});