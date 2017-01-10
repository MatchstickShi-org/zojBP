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
			{field:'orderId', title:'单号'},
			{field:'clientName', title:'客户', width: 5},
			{field:'projectAddr', title:'装修地址', width: 5},
			{field:'infoer', title:'信息员', width: 5},
			{field:'designer', title:'设计师', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}},
			{field:'salesman', title:'业务员', width: 5},
			{field:'orderStatus', title:'状态', width: 5, formatter: function(val, row, index)
				{
					switch (val)
					{
						case 10:
							return '正跟踪';
							break;
						case 12:
							return '已放弃';
							break;
						case 30:
						case 32:
							return '在谈单审核中';
							break;
						case 34:
							return '在谈单-设计师跟踪中';
							break;
						case 60:
						case 62:
							return '不准单审核中';
							break;
						case 64:
							return '不准单';
							break;
						case 14:
							return '已打回';
							break;
						case 90:
							return '已签单';
							break;
						case 0:
							return '死单';
							break;
						default:
							return '未知';
							break;
					}
				}
			},
			{field:'remitDate', title:'打款日期', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}},
			{field:'cost', title:'金额', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}},
			{field:'remark', title:'备注', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}}
		]],
		pagination: true,
		singleSelect: true,
		url: 'costMgr/infoCostMgr/getAllInfoCosts',
		onSelect: function(index, row)
		{
			if(row.cost != null)
				$showAddInfoCostWindowBtn.linkbutton('disable');
			else
			{
				if(row.orderStatus == 12 || row.orderStatus >= 34)
					$showAddInfoCostWindowBtn.linkbutton('enable');
				else
					$showAddInfoCostWindowBtn.linkbutton('disable');
			}
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