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
				{field:'todayOrderVisitCount', title:'在谈单回访数', width: 5},
				{field:'talkingOrderCount', title:'在谈单总数', width: 5},
				{field:'dealOrderCount', title:'已签单总数', width: 5},
				{field:'deadOrderCount', title:'死单总数', width: 5},
//				{field:'monthDealAmount', title:'本月签单总额', width: 5},
				{field:'totalDealAmount', title:'累计签单总额', width: 5}
			]],
			border: false,
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'design/countMgr/getHistoryDesignCout'
		});
		
		$queryOrderBtn.linkbutton
		({
			'onClick': function()
			{
				var endDate = $endDateTextbox.datebox('getValue');
				var startDate = $startDateTextbox.datebox('getValue');
				var nowDate = formatterDate(new Date());
				if(Date.parse(endDate) < Date.parse(startDate)){
	    			$.messager.alert('提示','查询日期存在错误，请检查输入的查询日期！');
	    			return;
				}
				if(Date.parse(startDate) >= Date.parse(nowDate) || Date.parse(endDate) >= Date.parse(nowDate)){
	    			$.messager.alert('提示','只能查询 '+nowDate+' 之前的数据！');
	    			return;
				}
				$designCountDatagrid.datagrid('loading');
				$.ajax
				({
					url: 'design/countMgr/getHistoryDesignCout',
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
	}
	init();
});