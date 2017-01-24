$(function()
{
	var $marketingCountDatagrid = $('table#marketingCountDatagrid');
	var $salesmanNameTextbox = $('#order\\.salesmanNameInput');
	var $queryOrderBtn = $('a#queryOrderBtn');
	
	function init()
	{
		$marketingCountDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#marketingCountDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'todayInfoerVisitCount', title:'今日信息员回访数', width: 5},
				{field:'todayOrderVisitCount', title:'今日在谈单回访数', width: 5},
				{field:'todayInfoerAddCount', title:'今日信息员录入数', width: 5},
				{field:'todayClientAddCount', title:'今日客户录入数', width: 5},
				{field:'tracingInfoerCount', title:'正跟踪信息员总数', width: 5},
				{field:'contactingClientCount', title:'联系中的客户总数', width: 5},
				{field:'talkingOrderCount', title:'在谈单总数', width: 5},
				{field:'dealOrderCount', title:'已签单总数', width: 3},
				{field:'monthTalkingOrderCount', title:'本月提单总数', width: 3}
			]],
			border: false,
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'marketing/countMgr/getTodayMarketingCout',
			onSelect: function(idx, row)
			{
				if(row.status ==0 && row.notVisitDays > 1)
				{
					$permitVisitBtn.linkbutton('enable');
				}
				else
				{
					$permitVisitBtn.linkbutton('disable');
				}
			}
		});
		
		$queryOrderBtn.linkbutton
		({
			'onClick': function()
			{
				$marketingCountDatagrid.datagrid('loading');
				$.ajax
				({
					url: 'marketing/countMgr/getTodayMarketingCout',
					data:
					{
						salesmanName: $salesmanNameTextbox.textbox('getValue')
					},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$marketingCountDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$marketingCountDatagrid.datagrid('loaded');
					}
				});
			}
		});
	}

	init();
});