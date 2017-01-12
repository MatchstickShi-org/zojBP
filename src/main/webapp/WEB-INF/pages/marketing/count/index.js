$(function()
{
	var $applyVisitDatagrid = $('table#applyVisitDatagrid');
	var $designerNameTextbox = $('#order\\.designerNameInput');
	var $orderIdTextbox = $('#order\\.idInput');
	var $queryOrderBtn = $('a#queryOrderBtn');
	var $permitVisitBtn = $('a#permitVisitBtn');
	
	function init()
	{
		$applyVisitDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#applyVisitDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'designerName', title:'设计师', width: 3},
				{field:'talkingVistiAmount', title:'在谈单回访数', width: 3},
				{field:'talkingAmount', title:'在谈单数', width: 3},
				{field:'dealTotal', title:'已签单数', width: 3},
				{field:'deaTotal', title:'死单总数', width: 3}
			]],
			border: false,
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'design/clientMgr/getAllDesignerVisitApply',
			onSelect: function(idx, row)
			{
				if(row.status ==0 && row.notVisitDays > 1)
				{
					$permitVisitBtn.linkbutton('enable');
				}
				else
				{
					$permitVisitBtn.linkbutton('disable');
				}
			}
		});
		
		$('#applyVisitDatagridToolbar :checkbox').click(function()
		{
			if($(this).attr('value') == '')		//全选
				$('#applyVisitDatagridToolbar :checkbox[value!=""]').attr("checked", false);
			else
				$('#applyVisitDatagridToolbar :checkbox[value=""]').attr("checked", false);
		});
		
		$queryOrderBtn.linkbutton
		({
			'onClick': function()
			{
				$applyVisitDatagrid.datagrid('loading');
				var chk_value =[]; 
				$('#applyVisitDatagridToolbar :input[name="statusInput"]:checked').each(function(){ 
					chk_value.push($(this).val());  
				}); 
				$.ajax
				({
					url: 'design/clientMgr/getAllDesignerVisitApply',
					data:
					{
						orderId: $orderIdTextbox.textbox('getValue'),
						designerName: $designerNameTextbox.textbox('getValue'),
						status:chk_value
					},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$applyVisitDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$applyVisitDatagrid.datagrid('loaded');
					}
				});
			}
		});
		
		$permitVisitBtn.linkbutton({onClick: permitVisit});
		
		function permitVisit()
		{
			var selIds = $applyVisitDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要同意回访的客户！');
				return;
			}
			$.messager.confirm('警告','确定同意回访该客户吗？',function(r)
			{
				if (!r)
					return;
				$.post
				(
					'design/clientMgr/checkVisitApply',
					{id: selIds[0].id},
					function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$.messager.show({title:'提示',msg:'操作成功。'});
							$applyVisitDatagrid.datagrid('reload');
						}
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
					}
				);
			});
		}
	}

	init();
});