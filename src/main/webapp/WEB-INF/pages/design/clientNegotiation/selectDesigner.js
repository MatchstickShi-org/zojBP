$(function()
{
	var $selDesignerWindow = $('div#selectDesignerWindow');
	var $selDesignerGrid = $("table#selDesignerDatagrid");
	var $designerSearchbox = $('input#permitOrderForm_designerNameSearchbox');
	var $designerIdInput = $('input#permitOrderForm_designerIdInput');
	var $selectDesignerBtn = $('a#selectDesignerBtn');
	
	$selectDesignerBtn.linkbutton({'onClick': selectDesigner});
	
	function selectDesigner()
	{
		var selRs = $selDesignerGrid.datagrid('getSelections');
		if(selRs.length == 0)
		{
			$.messager.alert("提示", "请选择要分配的设计师！");
			return;
		}
		$designerSearchbox.searchbox("setValue", selRs[0].name);
		$designerIdInput.val(selRs[0].id); 
		$selDesignerWindow.window("close"); 
	}
	
	$selDesignerGrid.datagrid
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
		url: "design/clientMgr/getAllDesigner",
	});
});