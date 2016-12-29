$(function()
{
	var $infoCostDatagrid = $('table#infoCostDatagrid');
	var $queryInfoCostBtn = $('a#infoCostMgr\\.queryBtn');
	var $showAddInfoCostWindow = $('div#showAddInfoCostWindow');
	var $showAddInfoCostWindowBtn = $('a#showAddInfoCostWindowBtn');
	
	initWindow();
	
	$infoCostDatagrid.datagrid
	({
		idField: 'orderId',
		rownumbers: true,
		toolbar: '#infoCostDatagridToolbar',
		columns:
		[[
			{field:'orderId', title:'单号', width: 5},
			{field:'clientName', title:'客户', width: 5},
			{field:'projectAddr', title:'装修地址', width: 5},
			{field:'infoer', title:'信息员', width: 5},
			{field:'designer', title:'设计师', width: 5, formatter: function(val, row, index)
				{
					return val == null ? '-' : val;
				}
			},
			{field:'salesman', title:'业务员', width: 5},
			{field:'remitDate', title:'打款日期', width: 5, formatter: function(val, row, index)
				{
					return val == null ? '-' : val;
				}
			},
			{field:'cost', title:'金额', width: 5, formatter: function(val, row, index)
				{
					return val == null ? '-' : val;
				}
			},
			{field:'remark', title:'备注', width: 5, formatter: function(val, row, index)
					{
					return val == null ? '-' : val;
				}
			}
		]],
		singleSelect: true,
		pagination: true,
		url: 'costMgr/infoCostMgr/getAllInfoCosts',
		onSelect: function(index, row)
		{
			$showAddInfoCostWindowBtn.linkbutton(row.cost != null ? 'disable' : 'enable');
		}
	});
	
	$queryInfoCostBtn.linkbutton({onClick: function()
	{
		$infoCostDatagrid.datagrid('load', 
		{
			status: $(':radio[name="infoCostMgr\\.status"]:checked').val(),
			clientName: $(':text#infoCostMgr\\.clientName').val(),
			orderId: $(':text#infoCostMgr\\.orderId').val()
		});
	}});
	
	function initWindow()
	{
		$showAddInfoCostWindow.window({width: 500});
		$showAddInfoCostWindowBtn.linkbutton({onClick: function()
		{
			var orderIds = $infoCostDatagrid.datagrid('getSelectRowPkValues');
			if(orderIds.length == 0)
			{
				$.messager.alert('提示', '请选择要打款的客户。');
				return;
			}
			$showAddInfoCostWindow.window('clear');
			$showAddInfoCostWindow.window('open').window
			({
				title: '新增信息费打款记录'
			}).window('open').window('refresh', 'costMgr/infoCostMgr/showAddInfoCostWindow?orderId=' + orderIds[0]);
		}});
	}
});