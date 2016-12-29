$(function()
{
	var $infoCostDatagrid = $('table#infoCostDatagrid');
	$('a#addInfoCostBtn').linkbutton({'onClick': function()
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
					$infoCostDatagrid.datagrid('reload');
					$showAddInfoCostWindow.window('close');
				}
			}
		});
	}});
});