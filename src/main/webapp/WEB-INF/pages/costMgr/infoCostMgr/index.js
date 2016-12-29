$(function()
{
	var $infoCostDatagrid = $('table#infoCostDatagrid');
	var $queryInfoCostBtn = $('a#infoCostMgr\\.queryBtn');
	
	function init()
	{
		$infoCostDatagrid.datagrid
		({
			idField: 'id',
			rownumbers: true,
			toolbar: '#infoCostDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
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
			pagination: true,
			url: 'costMgr/infoCostMgr/getAllInfoCosts'
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
		
		$('#showAddInfoCostWindowBtn').linkbutton({onClick: function()
		{
			
		}});
	}

	init();
});