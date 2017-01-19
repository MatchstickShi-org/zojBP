$(function()
{
	var $marketingCountDatagrid = $('table#marketingCountDatagrid');
	var $salesmanNameTextbox = $('#order\\.salesmanNameInput');
	var $startDateTextbox = $('#order\\.startDateInput');
	var $endDateTextbox = $('#order\\.endDateInput');
	var $queryOrderBtn = $('a#queryOrderBtn');
	
	//得到当前日期
	formatterDate = function(date) {
		var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
		var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"+ (date.getMonth() + 1);
		return date.getFullYear() + '-' + month + '-' + day;
	};
	
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
				{field:'todayInfoerVisitCount', title:'区间内信息员回访数', width: 5},
				{field:'todayOrderVisitCount', title:'区间内在谈单回访数', width: 5},
				{field:'todayInfoerAddCount', title:'区间内信息员录入数', width: 5},
				{field:'todayClientAddCount', title:'区间内客户录入数', width: 5},
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
				var endDate = $endDateTextbox.datebox('getValue');
				var startDate = $startDateTextbox.datebox('getValue');
				if(Date.parse(endDate) < Date.parse(startDate)){
	    			$.messager.alert('提示','请选择正确的查询日期！');
	    			return;
				}
				$marketingCountDatagrid.datagrid('loading');
				$.ajax
				({
					url: 'marketing/countMgr/getTodayMarketingCout',
					data:
					{
						salesmanName: $salesmanNameTextbox.textbox('getValue'),
						startDate: $startDateTextbox.datebox('getValue'),
						endDate: $endDateTextbox.datebox('getValue')
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
		
		$startDateTextbox.val(formatterDate(new Date()));
		$endDateTextbox.val(formatterDate(new Date()));
	}

	init();
});