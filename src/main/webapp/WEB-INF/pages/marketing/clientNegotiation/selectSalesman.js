$(function()
{
	var $selectSalesmanWindow = $('div#selectSalesmanWindow');
	var $selSalesmanGrid = $("table#selSalesmanDatagrid");
	var $selectSalesmanBtn = $('a#selectSalesmanBtn');
	var $salesmanSearchbox = $('input#salesmanNameSearchbox');
	var $salesmanIdInput = $('input#salesmanIdInput');
	
	$selectSalesmanBtn.linkbutton({'onClick': selectSalesman});
	
	function selectSalesman()
	{
		var selRs = $selSalesmanGrid.datagrid('getSelections');
		if(selRs.length == 0)
		{
			$.messager.alert("提示", "请选择要分配的业务员！");
			return;
		}
		$salesmanSearchbox.searchbox("setValue", selRs[0].alias);
		$salesmanIdInput.val(selRs[0].id); 
		$selectSalesmanWindow.window("close"); 
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
		url: "marketing/clientMgr/getAllSalesman",
	});
});