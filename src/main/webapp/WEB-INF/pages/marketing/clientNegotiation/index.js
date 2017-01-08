$(function()
{
	var $showNewOrderWindowBtn = $('a#showNewOrderWindowBtn');
	var $orderDatagrid = $('table#orderDatagrid');
	var $orderCheckDatagrid = $('table#orderCheckDatagrid');
	var $infoerNameTextbox = $('#clientTrace\\.infoerNameInput');
	var $orderNameTextbox = $('#clientTrace\\.nameInput');
	var $telTextbox = $('#clientTrace\\.telInput');
	var $orderCheckInfoerNameTextbox = $('#order\\.infoerNameInput');
	var $orderCheckNameTextbox = $('#order\\.nameInput');
	var $orderCheckTelTextbox = $('#order\\.telInput');
	var $queryOrderBtn = $('a#queryOrderBtn');
	var $queryCheckOrderBtn = $('a#queryCheckOrderBtn');
	var $addInfoCostBtn = $('a#clientNegotiationMgr-addInfoCostBtn');
	var $addCommissionCostBtn = $('a#clientNegotiationMgr-addCommissionCostBtn');
	var $reloadInfoCostBtn = $('a#reloadInfoCostBtn');
	var $showAddInfoCostWindow = $('div#showAddInfoCostWindow');
	var $showAddCommissionCostWindow = $('div#showAddCommissionCostWindow');
	var $addClientVisitWindow = $('div#addClientVisitWindow');
	var $addClientWindow = $('div#addClientWindow');
	var $permitOrderWindow = $('div#permitOrderWindow');
	var $rejectOrderWindow = $('div#rejectOrderWindow');
	var $selectInfoerWindow = $('div#selectInfoerWindow');
	var $clientMgrTab = $('div#clientMgrTab');
	var $orderCheckMgrTab = $('div#orderCheckMgrTab');
	var $editClientForm = $('form#editOrderForm');
	var $submitUpdateClientFormBtn = $('a#submitUpdateClientFormBtn');
	var $refreshUpdateClientFormBtn = $('a#refreshUpdateClientFormBtn');
	var $orderVisitGrid = $('table#orderVisitGrid');
	var $orderStylistVisitGrid = $('table#orderStylistVisitGrid');
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
	
	initGrid();
	initTab();
	initWindow();
	initBtn();
	
	function initWindow()
	{
		$addClientVisitWindow.window({width: 350});
		$addClientWindow.window({width: 450});
		$permitOrderWindow.window({width: 340});
		$rejectOrderWindow.window({width: 340});
		$selectInfoerWindow.window({width: 350});
		$showAddInfoCostWindow.window({width: 500});
		$showAddCommissionCostWindow.window({width: 500});
	}
	
	function initGrid()
	{
		$orderDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话', width: 5},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'工程地址', width: 8},
				{field:'infoerName', title:'信息员', width: 3},
				{field:'salesmanId', hidden: true},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'salesmanStatus', hidden: true},
				{field:'designerId', hidden: true},
				{field:'designerName', title:'设计师', width: 3},
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
							case 30:
							case 32:
								return '在谈单审核中';
								break;
							case 34:
								return '在谈单已批准';
								break;
							case 60:
							case 62:
								return '不准单审核中';
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
					field:'notVisitDays', title:'未回访天数', width: 2,
					styler: function (value, row, index){if(value > 5) return 'background-color:red';}
				}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			onSelect: function(idx, row)
			{
				if(row.salesmanId != _session_loginUserId)
					$('#clientNegotiationMgr-addOrderVisitBtn').linkbutton('disable').linkbutton('hide');
				else
					$('#clientNegotiationMgr-addOrderVisitBtn').linkbutton('enable').linkbutton('show');

				if(_session_loginUserRole == 3)
				{
					$showNewOrderWindowBtn.linkbutton('enable').linkbutton('show');
					$addInfoCostBtn.linkbutton('enable').linkbutton('show');
					$('#showPermitOrderWindowBtn').linkbutton('enable').linkbutton('show');
					$('#showRejectOrderWindowBtn').linkbutton('enable').linkbutton('show');
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
					$('#showPermitOrderWindowBtn').linkbutton('disable').linkbutton('hide');
					$('#showRejectOrderWindowBtn').linkbutton('disable').linkbutton('hide');
				}
				
				$clientMgrTab.tabs('showTool');
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
			},
		});
		$orderDatagrid.datagrid('options').url = 'marketing/clientMgr/getAllClientNegotiation';
		
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
		
		$orderCheckDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderCheckDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话', width: 5},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'工程地址', width: 8},
				{field:'infoerName', title:'信息员', width: 3},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'designerName', title:'设计师', width: 3},
				{field:'salesmanStatus', hidden: true},
				{
					field:'status', title:'状态', width: 4, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 30:
								return '在谈单审核中';
								break;
							case 62:
								return '不准单审核中';
								break;
							default:
								return '无状态';
								break;
						}
					}
				},
				{field:'insertTime', title:'录入日期', width: 5}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			onSelect: function(idx, row)
			{
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
			},
		});
		
		$orderVisitGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'回访内容', width: 5},
				{field:'date', title:'回访日期', width: 5}
			]],
			pagination: true,
			onDblClickRow: function(index, row){
				$.messager.alert('回访内容', row.content);
			}
		});

		if($orderCheckDatagrid.length > 0)
			$orderCheckDatagrid.datagrid('options').url = 'marketing/clientMgr/getAllClientCheck';
		$orderVisitGrid.datagrid('options').url = 'marketing/clientMgr/getOrderVisitByOrder';
		$infoCostGrid.datagrid('options').url = 'marketing/clientMgr/getInfoCostByOrder';
		$commissionCostGrid.datagrid('options').url = 'marketing/clientMgr/getCommissionCostByOrder';
		
		$orderStylistVisitGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
			  {field:'id', hidden: true},
			  {field:'content', title:'回访内容', width: 5},
			  {field:'date', title:'回访日期', width: 5}
			]],
			pagination: true,
			onDblClickRow: function(index, row){
				$.messager.alert('回访内容', row.content);
			}
		});
		
		$orderStylistVisitGrid.datagrid('options').url = 'marketing/clientMgr/getStylistOrderVisitByOrder';
		
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
		$('#orderDatagridToolbar :checkbox').click(function()
		{
			if($(this).attr('value') == '')		//全选
				$('#orderDatagridToolbar :checkbox[value!=""]').attr("checked", false);
			else
				$('#orderDatagridToolbar :checkbox[value=""]').attr("checked", false);
		});
		
		$showNewOrderWindowBtn.linkbutton({});
		
		$queryOrderBtn.linkbutton
		({
			'onClick': function()
			{
				$orderDatagrid.datagrid('loading');
				var chk_value =[]; 
				$('#orderDatagridToolbar :input[name="statusInput"]:checked').each(function()
				{ 
					chk_value.push($(this).val());  
				}); 
				
				$.ajax
				({
					url: 'marketing/clientMgr/getAllClientNegotiation',
					data: {name: $orderNameTextbox.textbox('getValue'), tel: $telTextbox.textbox('getValue'),infoerName: $infoerNameTextbox.textbox('getValue'), status:chk_value},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$orderDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$orderDatagrid.datagrid('loaded');
					}
				});
			}
		});
		
		if(_session_loginUserRole == 3)
			$showNewOrderWindowBtn.linkbutton('enable').linkbutton('show');
		else
			$showNewOrderWindowBtn.linkbutton('disable').linkbutton('hide');
		$submitUpdateClientFormBtn.linkbutton({'onClick': submitEditClientForm});
		$refreshUpdateClientFormBtn.linkbutton({'onClick': function()
		{
			var selTab = $orderCheckMgrTab.tabs('getSelected');
			var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
			var selRows = null;
			if(index == 0)
				selRows = $orderDatagrid.datagrid('getSelections');
			else
				selRows = $orderCheckDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		$reloadInfoCostBtn.linkbutton({'onClick': function()
		{
			var selTab = $orderCheckMgrTab.tabs('getSelected');
			var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
			var selRows = null;
			if(index == 0)
				selRows = $orderDatagrid.datagrid('getSelections');
			else
				selRows = $orderCheckDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		$queryCheckOrderBtn.linkbutton
		({
			'onClick': function()
			{
				$orderCheckDatagrid.datagrid('loading');
				var chk_value =[]; 
				$('#orderCheckDatagridToolbar :input[name="orderStatusInput"]:checked').each(function(){ 
					chk_value.push($(this).val());  
				}); 
				$.ajax
				({
					url: 'marketing/clientMgr/getAllClientCheck',
					data: {name: $orderCheckNameTextbox.textbox('getValue'), tel: $orderCheckTelTextbox.textbox('getValue'),infoerName: $orderCheckInfoerNameTextbox.textbox('getValue'),status:chk_value},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$orderCheckDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$orderCheckDatagrid.datagrid('loaded');
					}
				});
			}
		});
		
		$addInfoCostBtn.linkbutton({onClick: function()
		{
			var selTab = $orderCheckMgrTab.tabs('getSelected');
			var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
			var orderIds = null;
			if(index == 0)
				orderIds = $orderDatagrid.datagrid('getSelectRowPkValues');
			else
				orderIds = $orderCheckDatagrid.datagrid('getSelectRowPkValues');
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
			var selTab = $orderCheckMgrTab.tabs('getSelected');
			var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
			var orderIds = null;
			if(index == 0)
				orderIds = $orderDatagrid.datagrid('getSelectRowPkValues');
			else
				orderIds = $orderCheckDatagrid.datagrid('getSelectRowPkValues');
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
			var selTab = $orderCheckMgrTab.tabs('getSelected');
			var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
			var orderIds = null;
			if(index == 0)
				orderIds = $orderDatagrid.datagrid('getSelectRowPkValues');
			else
				orderIds = $orderCheckDatagrid.datagrid('getSelectRowPkValues');
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

		$('#showAddOrderWindowBtn').linkbutton({onClick: showAddClientWindow});
		$('#removeOrderBtn').linkbutton({onClick: removeOrder});
		$('#clientNegotiationMgr-addOrderVisitBtn').linkbutton({onClick: showAddClientVisitWindow});
		$('#showPermitOrderWindowBtn').linkbutton({onClick: showPermitOrderWindow});
		$('#showRejectOrderWindowBtn').linkbutton({onClick: showRejectOrderWindow});
	}
	
	function initTab()
	{
		$orderCheckMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selRows = null;
				if(title == '在谈单查询')
					$orderDatagrid.datagrid('unselectAll').datagrid('reload');
				else
					$orderCheckDatagrid.datagrid('unselectAll').datagrid('reload');
				$editClientForm.form('clear');
				$orderVisitGrid.datagrid('loadData', []);
				$orderStylistVisitGrid.datagrid('loadData', []);
				$orderApproveGrid.datagrid('loadData', []);
				$infoCostGrid.datagrid('loadData', []);
				$commissionCostGrid.datagrid('loadData', []);
			}
		});

		$clientMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selTab = $orderCheckMgrTab.tabs('getSelected');
				var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
				var selRows = null;
				if(index == 0)
					selRows = $orderDatagrid.datagrid('getSelections');
				else
					selRows = $orderCheckDatagrid.datagrid('getSelections');
				if(selRows.length == 1)
					loadTabData(title, selRows[0]);
				else
					clearTabData(title);
			}
		});
		$clientMgrTab.tabs('hideTool');
		
		$orderCheckMgrTab.tabs({});
		if(_session_loginUserRole != 3)
			$orderCheckMgrTab.tabs('hideHeader');
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
				$orderStylistVisitGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
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
				$orderStylistVisitGrid.datagrid('loadData', []);
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
		var selTab = $orderCheckMgrTab.tabs('getSelected');
		var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
		var selRows = null;
		if(index == 0)
			selRows = $orderDatagrid.datagrid('getSelections');
		else
			selRows = $orderCheckDatagrid.datagrid('getSelections');
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
				if(data.returnCode == 0){
					if(index == 0)
						$orderDatagrid.datagrid('reload');
					else
						$orderCheckDatagrid.datagrid('reload');
				}
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
	
	function showPermitOrderWindow()
	{
		var selIds = $orderCheckDatagrid.datagrid('getSelections');
		if(selIds.length == 0)
		{
			$.messager.alert('提示', '请选中要批准的客户。');
			return;
		}
		$permitOrderWindow.window('clear');
		$permitOrderWindow.window('open').window
		({
			title: '批准',
			content: getPermitOrderWindowHtml()
		}).window('open').window('center');
		
		function getPermitOrderWindowHtml()
		{
			return '<form id="permitOrderForm" action="marketing/clientMgr/permitOrder" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>客户名称：</label></td>' + 
			'			<td><input id="clientName" name="name" readonly="readonly" class="easyui-textbox" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话：</label></td>' + 
			'			<td><input id="telAll" name="telAll" readonly="readonly" class="easyui-textbox" style="width: 230px;" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitPermitOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$permitOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $permitOrderWindow = $(\'div#permitOrderWindow\');' +
			'var $orderCheckDatagrid = $(\'table#orderCheckDatagrid\');' +
			'var $orderApproveGrid = $(\'table#orderApproveGrid\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var selRows = $orderCheckDatagrid.datagrid("getSelections");' +
			'$permitOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$permitOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$permitOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$permitOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$permitOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitPermitOrderForm()' + 
			'{' + 
			'	$permitOrderWindow.find(\'form#permitOrderForm\').form(\'submit\',' + 
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
			'				$orderCheckDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				if($clientMgrTab.tabs("getSelected").panel("options").title == "审核流程")' + 
			'					$orderApproveGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$permitOrderWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		}
	}

	function showRejectOrderWindow()
	{
		var selIds = $orderCheckDatagrid.datagrid('getSelections');
		if(selIds.length == 0)
		{
			$.messager.alert('提示', '请选中要驳回的客户。');
			return;
		}
		$rejectOrderWindow.window('clear');
		$rejectOrderWindow.window('open').window
		({
			title: '驳回',
			content: getRejectOrderWindowHtml()
		}).window('open').window('center');
		
		function getRejectOrderWindowHtml()
		{
			return '<form id="rejectOrderForm" action="marketing/clientMgr/rejectOrder" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>客户名称：</label></td>' + 
			'			<td><input id="clientName" name="name" readonly="readonly" class="easyui-textbox" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话：</label></td>' + 
			'			<td><input id="telAll" name="telAll" readonly="readonly" class="easyui-textbox" style="width: 230px;" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitRejectOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$rejectOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $rejectOrderWindow = $(\'div#rejectOrderWindow\');' +
			'var $orderCheckDatagrid = $(\'table#orderCheckDatagrid\');' +
			'var $orderApproveGrid = $(\'table#orderApproveGrid\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var selRows = $orderCheckDatagrid.datagrid("getSelections");' +
			'$rejectOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$rejectOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$rejectOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$rejectOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$rejectOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitRejectOrderForm()' + 
			'{' + 
			'	$rejectOrderWindow.find(\'form#rejectOrderForm\').form(\'submit\',' + 
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
			'				$orderCheckDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				if($clientMgrTab.tabs("getSelected").panel("options").title == "审核流程")' + 
			'					$orderApproveGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$rejectOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		}
	}


	function showAddClientVisitWindow()
	{
		var selTab = $orderCheckMgrTab.tabs('getSelected');
		var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
		var selRows = null;
		if(index == 0)
			selRows = $orderDatagrid.datagrid('getSelections');
		else
			selRows = $orderCheckDatagrid.datagrid('getSelections');
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
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>回访内容：</label></td>' + 
			'			<td><input name="content" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddClientVisitForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientVisitWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addClientVisitWindow = $(\'div#addClientVisitWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderCheckDatagrid = $(\'table#orderCheckDatagrid\');' +
			'var $orderVisitGrid = $(\'table#orderVisitGrid\');' +
			'var $orderCheckMgrTab = $(\'div#orderCheckMgrTab\');' +
			'var selTab = $orderCheckMgrTab.tabs("getSelected");' +
			'var index = $orderCheckMgrTab.tabs("getTabIndex",selTab);' +
			'var selRows = null;' +
			'if(index == 0)' +
			'	selRows = $orderDatagrid.datagrid("getSelections");' +
			'else' +
			'	selRows = $orderCheckDatagrid.datagrid("getSelections");' +
			'$addClientVisitWindow.find(\'#orderId\').val(selRows[0].id);' +
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
			'				if(index == 0)' + 
			'					$orderDatagrid.datagrid(\'reload\');' + 
			'				else' + 
			'					$orderCheckDatagrid.datagrid("reload");' + 
			'				$addClientVisitWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		}
	}
	
	function showAddClientWindow()
	{
		$addClientWindow.window('clear');
		$addClientWindow.window('open').window
		({
			title: '新增客户',
			content: getAddClientWindowHtml()
		}).window('open').window('center');
		
		function getAddClientWindowHtml()
		{
			return '<form id="addClientForm" action="marketing/infoerMgr/addClient" method="post" style="width: 100%;">' + 
			'	<table width="100%" id="clientTab" >' + 
			'		<tr>' + 
			'			<td style="width: 120px;" align="right"><label>联系人：</label></td>' + 
			'			<td style="width: 180px;"><input name="name" class="easyui-textbox" required="required" style="width: 150px;"/></td>' + 
			'			<td></td>' + 
			'		</tr>' + 
			'		<tr id="clientTelTr">' + 
			'			<td align="right"><label>联系电话：</label></td>' + 
			'			<td><input name="tel1" id="tel1" onblur="checkClientTelValue(this);" required="required" style="width: 150px;"/><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addClientTelBtn"></a></td>' + 
			'			<td><font id="errorclienttel1" color="red"></font></td>' + 
			'		</tr>' + 
			'		<input id="clientTelCount" type="hidden" value="1" />' +
			'		<input type="hidden" name="infoerId" id="addClientForm_infoerIdInput" type="hidden" value="1" />' +
			'		<tr>' + 
			'			<td align="right"><label>所属信息员：</label></td>' + 
			'			<td><input id="addClientForm_infoerSearchbox" name="infoerName" prompt="请选择信息员" editable="false" class="easyui-searchbox" style="width: 160px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>单位地址：</label></td>' + 
			'			<td><input name="orgAddr" class="easyui-textbox" style="width: 200px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程名称：</label></td>' + 
			'			<td><input name="projectName" class="easyui-textbox" style="width: 200px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程地址：</label></td>' + 
			'			<td><input name="projectAddr" class="easyui-textbox" style="width: 200px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="3">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddClientForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addClientWindow = $(\'div#addClientWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'var $infoerNameSearchbox = $addClientWindow.find("#addClientForm_infoerSearchbox");' +
			'$infoerNameSearchbox.searchbox({searcher: function(){showSelectInfoerWindow();}});' + 
			'function submitAddClientForm()' + 
			'{' + 
			'	$addClientWindow.find(\'form#addClientForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'			if($(\'#tel1\').val() == ""){' + 
			'      			$(\'#errorclienttel1\').html("联系电话未填！"); '+
			'				return false;' + 
			'			}' + 
			'			var reg = /^1[0-9]{10}$/;'+
			'			if(!(reg.test($(\'#tel1\').val()))){'+
			'				$(\'#errorclienttel1\').html("无效的手机号码！");'+
			'				$(\'#tel1\').val().focus(); '+
			'				return false; '+
			'			}'+
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$orderDatagrid.datagrid(\'reload\');' + 
			'				$addClientWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'function checkClientTelValue(obj)' + 
			'{' + 
			'	var errorId = obj.name;'+
			'	errorId = errorId.charAt(errorId.length - 1);'+
			'	if(obj.value.length==0) '+
			' 	{ '+
			'      $(\'#errorclienttel\'+errorId+\'\').html("联系电话未填！"); '+
			'      return; '+
			'   } '+
			'	var reg = /^1[0-9]{10}$/;'+
			'	if(!(reg.test(obj.value))){'+
			'		$(\'#errorclienttel\'+errorId+\'\').html("无效的手机号码！");'+
			'		obj.focus(); '+
			'		return; '+
			'	}else{'+
			'		$(\'#errorclienttel\'+errorId+\'\').html("");'+
			'	}'+
			'	if(errorId > 1) '+
			' 	{ '+
			'		if($(\'#tel1\').val() == obj.value){' + 
			'      		$(\'#errorclienttel\'+errorId+\'\').html("联系电话重复！"); '+
			'      		return; '+
			'   	} '+
			'   } '+
			'	$.ajax'+
			'	({'+
			'		url: \'marketing/infoerMgr/checkClientTel\','+
			'		data: {tel:obj.value},'+
			'		success: function(data, textStatus, jqXHR)'+
			'		{'+
			'			if(data.returnCode != 0){'+
			'				$(\'#errorclienttel\'+errorId+\'\').html(data.msg); '+  
			'				obj.focus(); '+  
			'			}else{ '+  
			'				$(\'#errorclienttel\'+errorId+\'\').html(""); '+  
			'			} '+  
			'		}'+
			'	});'+
			'} ' + 
			'function removeTelAdd(obj)' +
			'{' +
			'	$.messager.confirm(\'确认\',\'您确认想要删除此联系电话吗？\',function(r){' +  
			'   	if (r){ ' +
			' 			var count = $(\'#clientTelCount\').val();' + 
			' 			count = parseInt(count)-1;' + 
			' 			$(\'#clientTelCount\').val(count);' +
			'			obj.parent().parent().remove();' +
			'    	}  ' +
			'	}); ' +
			'} ' +
			'$(\'#addClientTelBtn\').click(function()'+
			'{' + 
			' 	var count = $(\'#clientTelCount\').val();' + 
			' 	count = parseInt(count)+1;' + 
			' 	if(count <6){' + 
			' 		$(\'#clientTelCount\').val(count);' + 
			'		var appendHtml =\'<tr>'+
			'		<td align="right"><label>联系电话：</label></td>'+
			'		<td><input name="tel\'+count+\'" onblur="checkClientTelValue(this);" class="easyui-textbox" required="required" style="width: 150px;"/><a href="javascript:void(0)" onclick="removeTelAdd($(this));" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeClientTelBtn">删除</a></td>' +
			'		<td><font style="text-align:top" id="errorclienttel\'+count+\'" color="red"></font></td>' +
			'		</tr>\';' + 
			'		$(\'#clientTelTr\').after(appendHtml);' +   
			'	}else{' +   
			'		$.messager.alert(\'提示\', \'联系电话最多只能添加5个！\');' +   
			'	}' +   
			'});' + 
			'</script>';
		}
	}
});