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
				{field: 'ck', checkbox: true},
				{field:'orderId', title:'单号', width: 3},
				{field:'name', title:'名称', width: 3},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'designerName', title:'设计师', width: 3},
				{
					field:'orderStatus', title:'客户状态', width: 3, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 10:
								return '正跟踪';
								break;
							case 12:
								return '已放弃';
								break;
							case 20:
								return '打回中-主案部经理审核';
								break;
							case 22:
								return '打回中-商务部经理审核';
								break;
							case 30:
								return '在谈单-商务部经理审核中';
								break;
							case 32:
								return '在谈单-主案部经理审核中';
								break;
							case 34:
								return '在谈单';
								break;
							case 60:
								return '不准单-主案部经理审核中';
								break;
							case 62:
								return '不准单-商务部经理审核中';
								break;
							case 64:
								return '不准单';
								break;
							case 14:
								return '已打回';
								break;
							case 90:
								return '已签单';
								break;
							case 0:
								return '死单';
								break;
							default:
								return '无状态';
								break;
						}
					}
				},
				{field:'notVisitDays', title:'未回访天数', width: 3,
					styler: function (value, row, index) {
						if(value > 1)
							return 'background-color:red';
		           }
				},
				{
					field:'status', title:'审核状态', width: 3, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 0:
								return '未审核';
								break;
							case 1:
								return '已审核';
								break;
							default:
								return '无状态';
								break;
						}
					}
				},
				{field:'createTime', title:'申请日期', width: 5}
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
				$applyVisitDatagrid.datagrid('load', 
						{
							orderId: $orderIdTextbox.textbox('getValue'),
							designerName: $designerNameTextbox.textbox('getValue'),
							status:chk_value
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