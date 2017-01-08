$(function()
{
	var $businessTransferWindow = $('div#businessTransferWindow');
	var $selDesignerGrid = $("table#selDesignerDatagrid");
	var $orderDatagrid = $('table#orderDatagrid');
	var $selectDesignerTransferBtn = $('a#selectDesignerTransferBtn');
	
	$selectDesignerTransferBtn.linkbutton({'onClick': selectDesignerTransfer});
	
	function selectDesignerTransfer()
	{
		var selIds = $orderDatagrid.datagrid('getCheckedRowPkValues');
		var selRs = $selDesignerGrid.datagrid('getSelections');
		if(selRs.length == 0)
		{
			$.messager.alert("提示", "请选中一个设计师！");
			return;
		}
		$.ajax
		({
			url: 'design/clientMgr/transferOrder',
			data: {orderIds:selIds, designerId:selRs[0].id },
			success: function(data, textStatus, jqXHR)
			{
				if(data.returnCode == 0)
				{
					$.messager.show({title:'提示',msg:'操作成功。'});
					$orderDatagrid.datagrid('reload');
					$businessTransferWindow.window("close"); 
				}
				else
					$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
			}
		});
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