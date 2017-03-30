$(function()
{
	var $showNewOrderWindowBtn = $('a#showNewOrderWindowBtn');
	var $orderDatagrid = $('table#orderDatagrid');
	var $infoerNameTextbox = $('#clientNegotiation-infoerNameInput');
	var $orderNameTextbox = $('#clientNegotiation-nameInput');
	var $orderIdTextbox = $('#clientNegotiation-idInput');
	var $telTextbox = $('#clientNegotiation-telInput');
	var $salesmanCombobox = $('#clientNegotiation-salesmanCombobox');
	var $queryOrderBtn = $('a#queryOrderBtn');
	var $addInfoCostBtn = $('a#clientNegotiationMgr-addInfoCostBtn');
	var $addCommissionCostBtn = $('a#clientNegotiationMgr-addCommissionCostBtn');
	var $reloadInfoCostBtn = $('a#reloadInfoCostBtn');
	var $showAddInfoCostWindow = $('div#showAddInfoCostWindow');
	var $showAddNewOrderWindow = $('div#showAddNewOrderWindow');
	var $showAddCommissionCostWindow = $('div#showAddCommissionCostWindow');
	var $addClientVisitWindow = $('div#addClientVisitWindow');
	var $selectInfoerWindow = $('div#selectInfoerWindow');
	var $clientMgrTab = $('div#clientMgrTab');
	var $editClientForm = $('form#editOrderForm');
	var $submitUpdateClientFormBtn = $('a#submitUpdateClientFormBtn');
	var $refreshUpdateClientFormBtn = $('a#refreshUpdateClientFormBtn');
	var $orderVisitGrid = $('table#orderVisitGrid');
	var $designerVisitGrid = $('table#designerVisitGrid');
	var $orderApproveGrid = $('table#orderApproveGrid');
	var $infoCostGrid = $('table#infoCostGrid');
	var $commissionCostGrid = $('table#commissionCostGrid');
	
	showSelectInfoerWindow = function()
	{
		$selectInfoerWindow.window('clear');
		$selectInfoerWindow.window('open').window
		({
			title: '请选择信息员',
		}).window('open').window('refresh', 'marketing/clientMgr/showSelectInfoerWindow');
	}
	
	initBtn();
	initGrid();
	initTab();
	initWindow();
	
	function initWindow()
	{
		$addClientVisitWindow.window({width: 350});
		$selectInfoerWindow.window({width: 350});
		$showAddInfoCostWindow.window({width: 500});
		$showAddCommissionCostWindow.window({width: 500});
		$showAddNewOrderWindow.window({width: 500});
	}
	
	function initGrid()
	{
		$orderDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderDatagridToolbar',
			columns:
			[[
			  	{field: 'ck', checkbox: true},
			  	{field:'isKey', hidden: true},
				{field:'id', title:'单号', width: 3},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话'},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'面积'},
				{field:'infoerName', title:'信息员'},
				{field:'salesmanId', hidden: true},
				{field:'salesmanName', title:'业务员'},
				{field:'salesmanStatus', hidden: true},
				{field:'designerId', hidden: true},
				{field:'designerName', title:'设计师'},
				{field:'designerStatus', hidden: true},
				{
					field:'status', title:'状态', width: 4, formatter: function(value, row, index)
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
								return '未知状态';
								break;
						}
					}
				},
				{field:'insertTime', title:'录入日期', width: 5},
				{
					field:'notVisitDays', title:'未回访天数', width: 2, sortable: true, 
					formatter: function(val, row, index)
					{
						return val == -100 ? "-" : val;
					},
					styler: function (value, row, index){if(value > 5) return 'background-color: red; color: white;';}
				}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			rowStyler: function(index, row)
			{
				if(row.isKey == 1)
					return 'color: red;';
			},
			onSelect: function(idx, row)
			{
				refreshBtn(row);
			},
			url: 'marketing/clientMgr/getAllClientNegotiation',
			queryParams:
			{
				filter: $(':radio[name="clientNegotiation-orderFilterInput"]:checked').val(),
				isKey: $(':checkbox[name="clientNegotiation-isKey"]:checked').val(),
				status: function()
				{
					var status =[]; 
					$('#orderDatagridToolbar :input[name="statusInput"]:checked').each(function()
					{
						status.push($(this).val());  
					});
					return status;
				}()
			}
		});
		
		function refreshBtn(row)
		{
			if(row.status == 0 || row.status == 90 || row.status == 64)		//死单、已签单、不准单商务部经理可以新生成客户，不允许再回访
			{
				if(_session_loginUserRole == 3 || _session_loginUserRole == -1)
					$showNewOrderWindowBtn.linkbutton('enable').linkbutton('show');
				$('#clientNegotiationMgr-addOrderVisitBtn').linkbutton('disable').linkbutton('hide');
			}
			else
			{
				if(row.salesmanId == _session_loginUserId || _session_loginUserRole == -1){
					$submitUpdateClientFormBtn.linkbutton('enable');
					$('#clientNegotiationMgr-addOrderVisitBtn').linkbutton('enable').linkbutton('show');
				}
				else{
					$submitUpdateClientFormBtn.linkbutton('disable');					
					$('#clientNegotiationMgr-addOrderVisitBtn').linkbutton('disable').linkbutton('hide');
				}
			}

			if(_session_loginUserRole == 3 || _session_loginUserRole == -1)
			{
				if(row.status == 34 || row.status == 90 || row.status == 64)		//在谈单、已签单、不准单才能打信息费
					$addInfoCostBtn.linkbutton('enable').linkbutton('show');
				else
					$addInfoCostBtn.linkbutton('disable').linkbutton('hide');
				if(row.status == 90)
					$addCommissionCostBtn.linkbutton('enable').linkbutton('show');
				else
					$addCommissionCostBtn.linkbutton('disable').linkbutton('hide');
			}
			else
			{
				$showNewOrderWindowBtn.linkbutton('disable').linkbutton('hide');
				$addInfoCostBtn.linkbutton('disable').linkbutton('hide');
				$addCommissionCostBtn.linkbutton('disable').linkbutton('hide');
			}
			
			$clientMgrTab.tabs('showTool');
			loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
		}
		
		$infoCostGrid.datagrid
		({
			idField: 'id',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'orderId', title:'单号', width: 3},
				  {field:'projectName', title:'项目名称', width: 6},
				  {field:'infoerName', title:'信息员', width: 3},
				  {field:'salesmanName', title:'业务员', width: 3},
				  {field:'designerName', title:'设计师', width: 3},
				  {field:'amount', title:'金额', width: 3, formatter: function(value, row, index)
						{
					  		return '￥' + value;
						}
				  },
				  {field:'date', title:'打款日期', width: 3},
				  {field:'remark', title:'备注', width: 8}
				  ]],
				  pagination: true
		});
		
		$commissionCostGrid.datagrid
		({
			idField: 'id',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'orderId', title:'单号', width: 3},
				  {field:'projectName', title:'项目名称', width: 6},
				  {field:'infoerName', title:'信息员', width: 3},
				  {field:'salesmanName', title:'业务员', width: 3},
				  {field:'designerName', title:'设计师', width: 3},
				  {field:'amount', title:'金额', width: 3, formatter: function(value, row, index)
					  {
					  return '￥' + value;
					  }
				  },
				  {field:'date', title:'打款日期', width: 3},
				  {field:'remark', title:'备注', width: 8}
				  ]],
				  pagination: true
		});
		
		$orderVisitGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'回访内容', width: 5},
				{field:'date', title:'回访日期'}
			]],
			pagination: true,
			onDblClickRow: function(index, row){
				$.messager.alert('回访内容', row.content);
			}
		});

		$orderVisitGrid.datagrid('options').url = 'marketing/clientMgr/getOrderVisitByOrder';
		$infoCostGrid.datagrid('options').url = 'marketing/clientMgr/getInfoCostByOrder';
		$commissionCostGrid.datagrid('options').url = 'marketing/clientMgr/getCommissionCostByOrder';
		
		if($designerVisitGrid.length > 0)
		{
			$designerVisitGrid.datagrid
			({
				idField: 'id',
				columns:
				[[
				  {field:'id', hidden: true},
				  {field:'content', title:'回访内容', width: 5},
				  {field:'date', title:'回访日期'}
				]],
				pagination: true,
				onDblClickRow: function(index, row){
					$.messager.alert('回访内容', row.content);
				}
			});
			
			$designerVisitGrid.datagrid('options').url = 'marketing/clientMgr/getStylistOrderVisitByOrder';
		}
		
		$orderApproveGrid.datagrid
		({
			idField: 'id',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'claimerName', title:'申请人', width: 2},
				  {field:'approverName', title:'审核人', width: 2},
				  {
						field:'operate', title:'操作', width: 1, formatter: function(value, row, index)
						{
							switch (value)
							{
								case 0:
									return '驳回';
									break;
								case 1:
									return '批准';
									break;
								case 2:
									return '申请';
									break;
								case 3:
									return '打回';
									break;
								default:
									return '无操作';
									break;
							}
						}
					},
				  {
						field:'status', title:'状态', width: 4, formatter: function(value, row, index)
						{
							switch (value)
							{
								case 10:
									return '正跟踪';
									break;
								case 12:
									return '已放弃';
									break;
								case 14:
									return '在谈单-设计师已打回';
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
									return '在谈单-设计师跟踪中';
									break;
								case 90:
									return '已签单';
									break;
								case 0:
									return '死单';
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
								default:
									return '无状态';
									break;
							}
						}
					},
			  {field:'remark', title:'备注', width: 6},
			  {field:'operateTime', title:'操作日期', width: 5}
			  ]],
			  pagination: true
		});
		
		$orderApproveGrid.datagrid('options').url = 'marketing/clientMgr/getOrderApproveByOrderId';
	}

	function initBtn()
	{
		$('#orderDatagridToolbar :checkbox[name="statusInput"]').click(function()
		{
			if($(this).attr('value') == '')		//全选
				$('#orderDatagridToolbar :checkbox[name="statusInput"][value!=""]').attr("checked", false);
			else
				$('#orderDatagridToolbar :checkbox[name="statusInput"][value=""]').attr("checked", false);
		});
		
		$showNewOrderWindowBtn.linkbutton
		({
			'onClick': function()
			{
				var orderIds = $orderDatagrid.datagrid('getSelections');
				if(orderIds.length == 0)
				{
					$.messager.alert('提示', '请选择要新生成的客户。');
					return;
				}
				if(orderIds[0].status != 90 && orderIds[0].status != 0 && orderIds[0].status != 64){
					$.messager.alert('提示', '只能新生成状态为<span style="color: red;">已签单、死单、不准单</span>的客户。');
					return;
				}
				$showAddNewOrderWindow.window('clear');
				$showAddNewOrderWindow.window('open').window
				({
					title: '新生成客户'
				}).window('open').window('refresh', 'marketing/clientMgr/showAddNewOrderWindow?orderId='+ orderIds[0].id);
			}
		});
		
		$queryOrderBtn.linkbutton
		({
			'onClick': function()
			{
				var status =[];
				$('#orderDatagridToolbar :input[name="statusInput"]:checked').each(function()
				{
					status.push($(this).val());  
				}); 
				$orderDatagrid.datagrid('load', 
				{
					name: $orderNameTextbox.textbox('getValue'), 
					orderId: $orderIdTextbox.textbox('getValue'), 
					tel: $telTextbox.textbox('getValue'),
					infoerName: $infoerNameTextbox.textbox('getValue'),
					salesmanId: $salesmanCombobox.length == 0 ? null : $salesmanCombobox.combo('getValue'),
					filter: $(':radio[name="clientNegotiation-orderFilterInput"]:checked').val(),
					isKey: $(':checkbox[name="clientNegotiation-isKey"]:checked').val(),
					status: status
				});
			}
		});
		
		if(_session_loginUserRole == 3 || _session_loginUserRole == -1)
			$showNewOrderWindowBtn.linkbutton('enable').linkbutton('show');
		else
			$showNewOrderWindowBtn.linkbutton('disable').linkbutton('hide');
		$submitUpdateClientFormBtn.linkbutton({'onClick': submitEditClientForm});
		$refreshUpdateClientFormBtn.linkbutton({'onClick': function()
		{
			var selRows = $orderDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		$reloadInfoCostBtn.linkbutton({'onClick': function()
		{
			var selRows = $orderDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		$addInfoCostBtn.linkbutton({onClick: function()
		{
			var orderIds = $orderDatagrid.datagrid('getSelectRowPkValues');
			if(orderIds.length == 0)
			{
				$.messager.alert('提示', '请选择要打款的客户。');
				return;
			}
			$showAddInfoCostWindow.window('clear');
			$showAddInfoCostWindow.window('open').window
			({
				title: '新增信息费打款记录'
			}).window('open').window('refresh', 'marketing/clientMgr/showAddInfoCostWindow?orderId=' + orderIds[0]);
		}});
		
		$addCommissionCostBtn.linkbutton({onClick: function()
		{
			var orderIds = $orderDatagrid.datagrid('getSelectRowPkValues');
			if(orderIds.length == 0)
			{
				$.messager.alert('提示', '请选择要打款的客户。');
				return;
			}
			$showAddCommissionCostWindow.window('clear');
			$showAddCommissionCostWindow.window('open').window
			({
				title: '新增提成打款记录'
			}).window('open').window('refresh', 'marketing/clientMgr/showAddCommissionCostWindow?orderId=' + orderIds[0]);
		}});
		
		$addCommissionCostBtn.linkbutton({onClick: function()
		{
			var orderIds = $orderDatagrid.datagrid('getSelectRowPkValues');
			if(orderIds.length == 0)
			{
				$.messager.alert('提示', '请选择要打款的客户。');
				return;
			}
			$showAddCommissionCostWindow.window('clear');
			$showAddCommissionCostWindow.window('open').window
			({
				title: '新增提成打款记录'
			}).window('open').window('refresh', 'marketing/clientMgr/showAddCommissionCostWindow?orderId=' + orderIds[0]);
		}});

		$('#removeOrderBtn').linkbutton({onClick: removeOrder});
		$('#clientNegotiationMgr-addOrderVisitBtn').linkbutton({onClick: showAddClientVisitWindow});
	}
	
	function initTab()
	{
		$clientMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selRows = $orderDatagrid.datagrid('getSelections');
				if(selRows.length == 1)
					loadTabData(title, selRows[0]);
				else
					clearTabData(title);
			}
		});
		$clientMgrTab.tabs('hideTool');
	}
	
	function loadTabData(title, row)
	{
		switch (title)
		{
			case '详情':
				$editClientForm.form('clear').form('load', 'marketing/clientMgr/getOrderById?orderId=' + row.id);
				break;
			case '业务员回访记录':
				$orderVisitGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
				break;
			case '设计师回访记录':
				$designerVisitGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
				break;
			case '审核流程':
				$orderApproveGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
				break;
			case '信息费':
				$infoCostGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
				break;
			case '提成':
				$commissionCostGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
				break;
		}
	}

	function clearTabData(title)
	{
		switch (title)
		{
			case '详情':
				$editClientForm.form('clear');
				break;
			case '业务员回访记录':
				$orderVisitGrid.datagrid('loadData', []);
				break;
			case '设计师回访记录':
				$designerVisitGrid.datagrid('loadData', []);
				break;
			case '审核流程':
				$orderApproveGrid.datagrid('loadData', []);
				break;
			case '信息费':
				$infoCostGrid.datagrid('loadData', []);
				break;
			case '提成':
				$commissionCostGrid.datagrid('loadData', []);
				break;
		}
	}

	function submitEditClientForm()
	{
		var selRows = $orderDatagrid.datagrid('getSelections');
		if(selRows.length == 0)
		{
			$.messager.alert('提示', '请选中一个客户。');
			return;
		}
		$editClientForm.form('submit',
		{
			onSubmit: function()
			{
				if(!$(this).form('validate'))
					return false;
			},
			success: function(data)
			{
				data = $.fn.form.defaults.success(data);
				if(data.returnCode == 0)
					$orderDatagrid.datagrid('reload');
				else
					$.messager.show({title: '提示', msg: data.msg});
			}
		});
	}

	function removeOrder()
	{
		var selIds = $orderDatagrid.datagrid('getCheckedRowPkValues');
		if(selIds.length == 0)
		{
			$.messager.alert('提示', '请<span style="color: red;">勾选</span>要放弃的客户。');
			return;
		}
		var selRows = $orderDatagrid.datagrid('getChecked');
		if(selRows.length > 0){
			var flag = false;
			$.each(selRows,function(index,obj){
				if(obj.status !=10){
					flag = true;
					return;
				}
			});
			if(flag){
				$.messager.alert('提示', '只能放弃状态为<span style="color: red;">正跟踪</span>的客户。');
				return;
			}
		}
		$.messager.confirm('警告','确定要放弃勾选的客户吗？',function(r)
		{
			if (!r)
				return;
			$.post
			(
				'marketing/clientMgr/deleteOrderByIds',
				{delIds : selIds},
				function(data, textStatus, jqXHR)
				{
					if(data.returnCode == 0)
					{
						$.messager.show({title:'提示',msg:'操作成功。'});
						$orderDatagrid.datagrid('reload');
					}
					else
						$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
				}
			);
		});
	}

	function showAddClientVisitWindow()
	{
		var selRows = $orderDatagrid.datagrid('getSelections');
		if(selRows.length == 0)
		{
			$.messager.alert('提示', '请选中一个客户。');
			return;
		}
		$addClientVisitWindow.window('clear');
		$addClientVisitWindow.window('open').window
		({
			title: '新增回访记录',
			content: getAddClientVisitWindowHtml()
		}).window('open').window('center');
		
		function getAddClientVisitWindowHtml()
		{
			return '<form id="addClientVisitForm" action="marketing/clientMgr/addOrderVisit" method="post" style="width: 100%;">' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<input id="visitorId"  name="visitorId" type="hidden" value="" />' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>回访内容：</label></td>' + 
			'			<td><input name="content" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddClientVisitForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientVisitWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addClientVisitWindow = $(\'div#addClientVisitWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderVisitGrid = $(\'table#orderVisitGrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$addClientVisitWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$addClientVisitWindow.find(\'#visitorId\').val(selRows[0].salesmanId);' +
			'function submitAddClientVisitForm()' + 
			'{' + 
			'	$addClientVisitWindow.find(\'form#addClientVisitForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$orderVisitGrid.datagrid("unselectAll").datagrid(\'reload\', {orderId: selRows[0].id});' + 
			'				$orderDatagrid.datagrid(\'reload\');' + 
			'				$addClientVisitWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		}
	}
});