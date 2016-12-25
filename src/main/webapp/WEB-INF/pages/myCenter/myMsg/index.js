$(function()
{
	var $myMsgGrid = $('table#myMsgGrid');
	var $refreshMyMsgGridBtn = $('a#refreshMyMsgGridBtn');
	
	function init()
	{
		$myMsgGrid.datagrid
		({
			idField: 'id',
			rownumbers: true,
			toolbar: '#myMsgGridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'消息内容', width: 15},
				{field:'targetUser', title:'类型', width: 5, formatter: function(value, row, index)
					{
						return value == null ? '广播' : '系统';
					}
				},
				{field:'sendTime', title:'发送时间', width: 5}
			]],
			pagination: true,
			url: 'myCenter/myMsg/getAllMsgs'
		});
		
		$refreshMyMsgGridBtn.linkbutton({onClick: function()
		{
			$myMsgGrid.datagrid('load');
		}});
	}
	init();
});