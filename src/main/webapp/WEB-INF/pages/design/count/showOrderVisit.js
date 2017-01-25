$(function()
{
	var $orderVisitGrid = $('table#orderVisitDatagrid');
	var $designerIdInput = $('#order\\.designerIdInput');
	
	$orderVisitGrid.datagrid
	({
		idField: 'id',
		columns:
		[[
			{field:'id', hidden: true},
			{field:'name', title:'客户名称', width: 3},
			{field:'content', title:'回访内容', width: 6},
			{field:'date', title:'回访日期', width: 4},
			{field:'comment', title:'批示', width: 4}
		]],
		pagination: true,
		url:'design/countMgr/getTodayTalkingOrderVisitByUserId?designerId='+$designerIdInput.val(),
		onDblClickRow: function(index, row){
			$.messager.alert('回访内容', row.content);
		}
	});
});