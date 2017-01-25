$(function()
{
	var $designCountDatagrid = $('table#designCountDatagrid');
	var $designerNameTextbox = $('#order\\.designerNameInput');
	var $designerIdInput = $('#order\\.designerIdInput');
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
		$showOrderVisitWindow.window({width: 800});
		$designCountDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#designCountDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'designerId', hidden: true},
				{field:'designerName', title:'设计师', width: 3},
				{field:'todayOrderVisitCount', title:'今日在谈单回访数', width: 5,
					formatter: function(val, row, index)
					{
						return "<a href='javacript:void(0);'>" + val + "</a>";
					}
				},
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
			url: 'design/countMgr/getTodayDesignCout',
			onClickCell: function(index,field,value){
				if(field == 'todayOrderVisitCount' && value > 0){
					var selRows = $designCountDatagrid.datagrid('getData').rows[index];
					$designerIdInput.val(selRows.designerId);
					$showOrderVisitWindow.window('clear');
					$showOrderVisitWindow.window('open').window
					({
						title: selRows.designerName+"-"+formatterDate(new Date())+'-在谈单回访记录'
					}).window('open').window('refresh', 'design/countMgr/toShowOrderVisitView');
				}
			}
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