$(function()
{
	var $addClientWindow = $('div#addClientWindow');
	var $selInfoerWindow = $('div#selectInfoerWindow');
	var $selInfoerGrid = $("table#selInfoerDatagrid");
	var $infoerSearchbox = $('input#addClientForm_infoerSearchbox');
	var $infoerIdInput = $('input#addClientForm_infoerIdInput');
	var $selectInfoerBtn = $('a#selectInfoerBtn');
	
	$selectInfoerBtn.linkbutton({'onClick': selectInfoer});
	
	function selectInfoer()
	{
		var selRs = $selInfoerGrid.datagrid('getSelections');
		if(selRs.length == 0)
		{
			$.messager.alert("提示", "请选择要添加的客户的信息员");
			return;
		}
		$infoerSearchbox.searchbox("setValue", selRs[0].name);
		$infoerIdInput.val(selRs[0].id); 
		$selInfoerWindow.window("close"); 
	}
	
	$selInfoerGrid.datagrid
	({
		singleSelect: true,
		idField: "id",
		columns:
		[[
			{field: "id", hidden: true},
			{field: "ck", checkbox: true},
			{field: "name", title:"姓名", width: 5},
		]],
		pagination: true,
		url: "marketing/clientMgr/findInfoerBySalesmanId",
	});
});