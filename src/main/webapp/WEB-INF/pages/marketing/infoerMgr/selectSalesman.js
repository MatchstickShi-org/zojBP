$(function()
{
	var $infoerDatagrid = $('table#infoerDatagrid');
	var $businessTransferWindow = $('div#businessTransferWindow');
	var $selSalesmanGrid = $("table#selSalesmanDatagrid");
	var $selectSalesmanBtn = $('a#selectSalesmanBtn');
	
	$selectSalesmanBtn.linkbutton({'onClick': selectSalesman});
	
	function selectSalesman()
	{
		var selIds = $infoerDatagrid.datagrid('getCheckedRowPkValues');
		var selRs = $selSalesmanGrid.datagrid('getSelections');
		if(selRs.length == 0)
		{
			$.messager.alert("提示", "请选中一个业务员。");
			return;
		}
		$.ajax
		({
			url: 'marketing/infoerMgr/transferInfoer',
			data: {infoerIds:selIds, salesmanId:selRs[0].id },
			success: function(data, textStatus, jqXHR)
			{
				if(data.returnCode == 0)
				{
					$.messager.show({title:'提示',msg:'操作成功。'});
					$infoerDatagrid.datagrid('clearChecked').datagrid('reload');
					$businessTransferWindow.window("close"); 
				}
				else
					$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
			}
		});
	}
	
	$selSalesmanGrid.datagrid
	({
		singleSelect: true,
		idField: "id",
		columns:
		[[
			{field: "id", hidden: true},
			{field: "ck", checkbox: true},
			{field:'name', title:'用户名', width: 5},
			{field:'alias', title:'姓名', width: 5},
			{
				field:'leaderName', title:'上级', width: 5, formatter: function(value, row, index)
				{
					if(row.role == 0)
						return '<span style="color: gray">-</span>';
					if(!value)
						return '<span style="color: red;">无</span>';
					return row.leaderId == row.id ? '<span style="color: gray">-</span>' : value;
				}
			},
			{
				field:'groupName', title:'所属组', width: 5, formatter: function(value, row, index)
				{
					if(row.role == 0)
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
		url: "marketing/infoerMgr/getAllSalesman",
	});
});