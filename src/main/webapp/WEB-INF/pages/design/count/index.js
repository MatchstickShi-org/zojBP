$(function()
{
	var $designCountDatagrid = $('table#designCountDatagrid');
	var $designerNameTextbox = $('#order\\.designerNameInput');
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
		$designCountDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#designCountDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'designerName', title:'设计师', width: 3},
				{field:'todayOrderVisitCount', title:'区间内在谈单回访数', width: 5},
				{field:'talkingOrderCount', title:'在谈单总数', width: 5},
				{field:'dealOrderCount', title:'已签单总数', width: 5},
				{field:'deadOrderCount', title:'死单总数', width: 5},
				{field:'monthDealAmount', title:'本月签单总额', width: 5},
				{field:'totalDealAmount', title:'累计签单总额', width: 5}
			]],
			border: false,
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'design/countMgr/getTodayDesignCout'
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
				$designCountDatagrid.datagrid('loading');
				$.ajax
				({
					url: 'design/countMgr/getTodayDesignCout',
					data:
					{
						designerName: $designerNameTextbox.textbox('getValue'),
						startDate: $startDateTextbox.datebox('getValue'),
						endDate: $endDateTextbox.datebox('getValue')
					},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$designCountDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$designCountDatagrid.datagrid('loaded');
					}
				});
			}
		});
		
		$startDateTextbox.val(formatterDate(new Date()));
		$endDateTextbox.val(formatterDate(new Date()));
	}
	init();
});