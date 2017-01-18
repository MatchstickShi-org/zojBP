$(function()
{
	var $designCountDatagrid = $('table#designCountDatagrid');
	var $designerNameTextbox = $('#order\\.designerNameInput');
	var $queryOrderBtn = $('a#queryOrderBtn');
	
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
				{field:'todayOrderVisitCount', title:'今日在谈单回访数', width: 5},
				{field:'talkingOrderCount', title:'当前在谈单数量', width: 5},
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
				$designCountDatagrid.datagrid('loading');
				$.ajax
				({
					url: 'design/countMgr/getTodayDesignCout',
					data:
					{
						designerName: $designerNameTextbox.textbox('getValue')
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