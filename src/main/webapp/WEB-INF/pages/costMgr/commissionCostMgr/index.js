$(function()
{
	var $orderGrid = $('table#commissionMgr-orderGrid');
	var $queryOrderBtn = $('a#commissionMgr-queryBtn');
	var $costGrid = $('table#commissionMgr-costGrid');
	var $tab = $('div#commissionMgr-tab');
	var $addCostBtn = $('a#commissionMgr-addCostBtn');
	var $addCostWindow = $('div#commissionMgr-addCostWindow');

	initTab();
	initGrid();
	initWindow();
	initBtn();
	
	function initWindow()
	{
		$addCostWindow.window({width: 500});
	}
	
	function initGrid()
	{
		$orderGrid.datagrid
		({
			idField: 'id',
			toolbar: '#commissionMgr-orderGridToolbar',
			columns:
			[[
				{field:'id', title: '单号'},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话'},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'面积'},
				{field:'infoerName', title:'信息员'},
				{field:'salesmanId', hidden: true},
				{field:'salesmanName', title:'业务员'},
				{field:'salesmanStatus', hidden: true},
				{field:'designerId', hidden: true},
				{field:'designerName', title:'设计师'},
				{field:'designerStatus', hidden: true},
				{
					field:'status', title:'状态', formatter: function(value, row, index)
					{
						switch (value)
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
								return '在谈单';
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
								return '未知状态';
								break;
						}
					}
				}
			]],
			singleSelect: true,
			pagination: true,
			url: 'costMgr/commissionCostMgr/getDealOrders',
			onSelect: function(idx, row)
			{
				loadTabData(row);
			}
		});
		
		$costGrid.datagrid
		({
			idField: 'id',
			rownumbers: true,
			toolbar: '#commissionCostDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'orderId', title:'单号', width: 5},
				{field:'clientName', title:'客户', width: 5},
				{field:'projectAddr', title:'面积'},
				{field:'infoer', title:'信息员'},
				{field:'designer', title:'设计师', formatter: function(val, row, index){return val == null ? '-' : val;}},
				{field:'salesman', title:'业务员'},
				{field:'remitDate', title:'打款日期', formatter: function(val, row, index){return val == null ? '-' : val;}},
				{field:'cost', title:'金额', align: 'right', formatter: function(val, row, index){return val == null ? '-' : '¥' + val;}},
				{field:'remark', title:'备注', width: 5, formatter: function(val, row, index){return val == null ? '-' : val;}}
			]],
			singleSelect: true,
			pagination: true
		});

		$costGrid.datagrid('options').url = 'costMgr/commissionCostMgr/getCommissionCostsByOrder';
	}
	
	function loadTabData(row)
	{
		$costGrid.datagrid('load', 
		{
			orderId : row.id
		});
	}
	
	function initTab()
	{
		$tab.tabs({border: false});
	}
	
	function initBtn()
	{
		$queryOrderBtn.linkbutton({onClick: function()
		{
			$orderGrid.datagrid('load', 
			{
				orderId : $('#commissionCostMgr-orderId').numberbox('getValue'),
				clientName : $('#commissionCostMgr-clientName').textbox('getValue')
			});
		}});
		
		$addCostBtn.linkbutton({onClick: function()
		{
			var orderIds = $orderGrid.datagrid('getSelectRowPkValues');
			if(orderIds.length == 0)
			{
				$.messager.alert('提示', '请选择要打款的客户。');
				return;
			}
			$addCostWindow.window('clear');
			$addCostWindow.window('open').window
			({
				title: '新增提成打款记录'
			}).window('open').window('refresh', 'costMgr/commissionCostMgr/showAddCostWindow?orderId=' + orderIds[0]);
		}});
	}
});