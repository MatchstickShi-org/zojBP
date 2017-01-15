$(function()
{
	var $commissionCostDatagrid = $('table#commissionCostDatagrid');
	var $querycommissionCostBtn = $('a#commissionCostMgr\\.queryBtn');
	
	$commissionCostDatagrid.datagrid
	({
		idField: 'id',
		rownumbers: true,
		toolbar: '#commissionCostDatagridToolbar',
		columns:
		[[
			{field:'id', hidden: true},
			{field:'orderId', title:'单号', width: 5},
			{field:'clientName', title:'客户', width: 5},
			{field:'projectAddr', title:'面积', width: 5},
			{field:'infoer', title:'信息员', width: 5},
			{field:'designer', title:'设计师', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}},
			{field:'salesman', title:'业务员', width: 5},
			{field:'remitDate', title:'打款日期', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}},
			{field:'cost', title:'金额', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}},
			{field:'remark', title:'备注', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}}
		]],
		singleSelect: true,
		pagination: true,
		url: 'costMgr/commissionCostMgr/getAllcommissionCosts'
	});
	
	$querycommissionCostBtn.linkbutton({onClick: function()
	{
		$commissionCostDatagrid.datagrid('load');
	}});
});