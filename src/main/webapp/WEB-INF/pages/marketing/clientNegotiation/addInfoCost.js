$(function()
{
	var $infoCostGrid = $('table#infoCostGrid');
	$('a#addInfoCostRecordBtn').linkbutton({'onClick': function()
	{
		$showAddInfoCostWindow.find('form#addInfoCostForm').form('submit',
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
					$infoCostGrid.datagrid('reload');
					$showAddInfoCostWindow.window('close');
				}else
					$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});  
			}
		});
	}});
});