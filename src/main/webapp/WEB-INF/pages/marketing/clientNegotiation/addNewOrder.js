$(function()
{
	var $orderDatagrid = $('table#orderDatagrid');
	var $showAddNewOrderWindow = $('div#showAddNewOrderWindow');
	var $selectSalesmanWindow = $('div#selectSalesmanWindow');
	var $salesmanNameSearchbox = $showAddNewOrderWindow.find("#salesmanNameSearchbox");
	$salesmanNameSearchbox.searchbox({searcher: function(){showSelectSalesmanWindow();}});
	showSelectSalesmanWindow = function()
	{
		$selectSalesmanWindow.window('clear');
		$selectSalesmanWindow.window('open').window
		({
			title: '请选择业务员',
		}).window('open').window('refresh', 'marketing/clientMgr/showSelectSalesmanWindow');
	}
	$selectSalesmanWindow.window({width: 500});
	$('a#addNewOrderRecordBtn').linkbutton({'onClick': function()
	{
		$showAddNewOrderWindow.find('form#addNewOrderForm').form('submit',
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
					$.messager.show({title:'提示', msg:'操作成功！'});
					$showAddNewOrderWindow.window('close');
				}else
					$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});
			}
		});
	}});
});