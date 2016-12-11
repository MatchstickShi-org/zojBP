$(function()
{
	var $brodcastMsgDatagrid = $('table#brodcastMsgDatagrid');
	var $refreshBroadcastMsgGridBtn = $('a#refreshBroadcastMsgGridBtn');
	
	function init()
	{
		$brodcastMsgDatagrid.datagrid
		({
			idField: 'id',
			rownumbers: true,
			toolbar: '#brodcastMsgDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'内容', width: 15},
				{field:'sendTime', title:'广播时间', width: 5}
			]],
			pagination: true,
			url: 'sysMgr/msgBroadcast/getAllBroadcastMsgs'
		});
		
		$refreshBroadcastMsgGridBtn.linkbutton({onClick: function()
		{
			$brodcastMsgDatagrid.datagrid('load');
		}});
		
		$('#showAddBroadcastMsgWindowBtn').linkbutton({onClick: function()
		{
			$.messager.prompt('请输入要广播的消息', '消息内容：', function(msg)
			{
				if(!msg || msg.length == 0)
					return;
				
				if(msg.length > 127)
				{
					$.messager.alert('提示', '消息内容超长，请重新输入。', 'info');
					return;
				}
				
				$.ajax
				({
					url: 'sysMgr/msgBroadcast/addBroadcastMsg',
					data: {context: msg},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$brodcastMsgDatagrid.datagrid('reload');
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
					}
				});
			});
		}});
	}

	init();
});