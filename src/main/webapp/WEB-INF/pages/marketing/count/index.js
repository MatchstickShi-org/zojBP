$(function()
{
	var $marketingCountDatagrid = $('table#marketingCountDatagrid');
	var $salesmanNameTextbox = $('#order\\.salesmanNameInput');
	var $salesmanIdInput = $('#order\\.salesmanIdInput');
	var $showOrderVisitWindow = $('div#showOrderVisitWindow');
	var $queryOrderBtn = $('a#queryOrderBtn');
	
	//得到当前日期
	formatterDate = function(date) {
		var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
		var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"+ (date.getMonth() + 1);
		return date.getFullYear() + '-' + month + '-' + day;
	};
	
	function init()
	{
		$showOrderVisitWindow.window({width: 700});
		$marketingCountDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#marketingCountDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'salesmanId', hidden: true},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'todayInfoerVisitCount', title:'今日信息员回访数', width: 5},
				{field:'todayOrderVisitCount', title:'今日在谈单回访数', width: 5,
					formatter: function(val, row, index)
					{
						return "<a href='javacript:void(0);'>" + val + "</a>";
					}
				},
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
			onClickCell: function(index,field,value){
				if(field == 'todayOrderVisitCount' && value > 0){
					var selRows = $marketingCountDatagrid.datagrid('getData').rows[index];
					$salesmanIdInput.val(selRows.salesmanId);
					$showOrderVisitWindow.window('clear');
					$showOrderVisitWindow.window('open').window
					({
						title: selRows.salesmanName+"-"+formatterDate(new Date())+'-在谈单回访记录'
					}).window('open').window('refresh', 'marketing/countMgr/toShowOrderVisitView');
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