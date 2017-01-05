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
					if($clientMgrTab.tabs("getSelected").panel("options").title == "信息费")
						$infoCostGrid.datagrid('reload');
					$showAddInfoCostWindow.window('close');
				}  
			}
		});
	}});
});