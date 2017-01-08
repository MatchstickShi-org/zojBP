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
			{field: "name", title:"用户名", width: 5},
			{field: "alias", title:"姓名", width: 5},
			{
				field:'role', title:'角色', width: 5, formatter: function(value, row, index)
				{
					switch (value)
					{
						case -1:
							return '超级管理员';
							break;
						case 0:
							return '管理员';
							break;
						case 1:
							return '商务部业务员';
							break;
						case 2:
							return '商务部主管';
							break;
						case 3:
							return '商务部经理';
							break;
						case 4:
							return '主案部设计师';
							break;
						case 5:
							return '主案部主管';
							break;
						case 6:
							return '主案部经理';
							break;
						default:
							return '未知';
							break;
					}
				}
			},
			{
				field:'leaderName', title:'上级', width: 5, formatter: function(value, row, index)
				{
					if(row.role == 0)
						return '<span style="color: gray">-</span>';
					if(!value)
						return '<span style="color: red;">无</span>';
					return row.leaderId == row.id ? '<span style="color: gray">-</span>' : value;
				}
			},
			{
				field:'groupName', title:'所属组', width: 5, formatter: function(value, row, index)
				{
					if(row.role == 0)
						return '<span style="color: gray">-</span>';
					if(!value)
						return '<span style="color: red;">无</span>';
					return value;
				}
			}
		]],
		pagination: true,
		url: "design/clientMgr/getAllDesigner",
	});
});