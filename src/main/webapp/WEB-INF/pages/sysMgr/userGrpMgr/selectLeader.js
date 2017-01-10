$(function()
{
	var $addGroupWindow = $('div#addGroupWindow');
	var $selLeaderWindow = $('div#selectLeaderWindow');
	var $groupDatagrid = $('table#groupDatagrid');
	var $selLeaderGrid = $("table#selLeaderDatagrid");
	var selGroupId = $groupDatagrid.datagrid("getSelectRowPkValues")[0];
	var $leaderSearchbox = $('input#editGroupForm_leaderSearchbox');
	var $leaderIdInput = $('input#editGroupForm_leaderIdInput');
	var $selectLeaderBtn = $('a#selectLeaderBtn');
	
	$selectLeaderBtn.linkbutton({'onClick': selectLeader});
	
	function selectLeader()
	{
		var selRs = $selLeaderGrid.datagrid('getSelections');
		if(selRs.length == 0)
		{
			$.messager.alert("提示", "请选择要设置为主管的用户");
			return;
		}
		if(_param_from == 'add')
		{
			$addGroupWindow.find("[searchboxname='leaderName']").searchbox("setValue", selRs[0].alias); 
			$addGroupWindow.find(":hidden[name='leaderId']").val(selRs[0].id); 
			$addGroupWindow.find("#addGroupWin_deptInput").textbox("setValue", selRs[0].role == 2 ? "商务部" : "主案部"); 
			$addGroupWindow.find(":hidden[name='type']").val(selRs[0].role == 2 ? "0" : "1"); 
		}
		else
		{
			$leaderSearchbox.searchbox("setValue", selRs[0].alias);
			$leaderIdInput.val(selRs[0].id); 
		}
		$selLeaderWindow.window("close"); 
	}
	
	$selLeaderGrid.datagrid
	({
		singleSelect: true,
		idField: "id",
		columns:
		[[
			{field: "id", hidden: true},
			{field: "ck", checkbox: true},
			{field: "alias", title:"姓名", width: 5},
			{
				field: "role", title: "部门", width: 5, formatter: function(value, row, index)
				{
					return value == 2 ? "商务部" : "主案部";
				}
			},
			{
				field: "groupName", title:"所属小组", width: 5, formatter: function(value, row, index)
				{
					if(!value)
						return '<span style="color: red;">无</span>';
					return value;
				}
			}
		]],
		pagination: true,
		url: "sysMgr/userGrpMgr/getCanAssignLeadersByGroup",
		queryParams: {groupId: selGroupId}
	});
});