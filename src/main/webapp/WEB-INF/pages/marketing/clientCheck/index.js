$(function()
{
	var $orderCheckDatagrid = $('table#orderCheckDatagrid');
	var $orderCheckInfoerNameTextbox = $('#order\\.infoerNameInput');
	var $orderCheckNameTextbox = $('#order\\.nameInput');
	var $orderCheckTelTextbox = $('#order\\.telInput');
	var $queryCheckOrderBtn = $('a#queryCheckOrderBtn');
	var $reloadInfoCostBtn = $('a#reloadInfoCostBtn');
	var $addClientVisitWindow = $('div#addClientVisitWindow');
	var $permitOrderWindow = $('div#permitOrderWindow');
	var $rejectOrderWindow = $('div#rejectOrderWindow');
	var $clientMgrTab = $('div#clientMgrTab');
	var $editClientForm = $('form#editOrderForm');
	var $submitUpdateClientFormBtn = $('a#submitUpdateClientFormBtn');
	var $refreshUpdateClientFormBtn = $('a#refreshUpdateClientFormBtn');
	var $orderVisitGrid = $('table#orderVisitGrid');
	var $designerVisitGrid = $('table#designerVisitGrid');
	var $orderApproveGrid = $('table#orderApproveGrid');
	
	initGrid();
	initTab();
	initWindow();
	initBtn();
	
	function initWindow()
	{
		$permitOrderWindow.window({width: 340});
		$rejectOrderWindow.window({width: 340});
	}
	
	function initGrid()
	{
		function refreshBtn(row)
		{
			if(_session_loginUserRole == 3 || _session_loginUserRole == -1)
			{
				$('#showPermitOrderWindowBtn').linkbutton('enable').linkbutton('show');
				$('#showRejectOrderWindowBtn').linkbutton('enable').linkbutton('show');
			}
			else
			{
				$('#showPermitOrderWindowBtn').linkbutton('disable').linkbutton('hide');
				$('#showRejectOrderWindowBtn').linkbutton('disable').linkbutton('hide');
			}
			
			$clientMgrTab.tabs('showTool');
			loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
		}
		
		$orderCheckDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderCheckDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'isKey', hidden: true},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话', width: 5},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'面积', width: 8},
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
								return '未知状态';
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
			rowStyler: function(index, row)
			{
				if(row.isKey == 1)
					return 'color: red;';
			},
			url: 'marketing/clientCheckMgr/getAllClientCheck',
			onSelect: function(idx, row)
			{
				refreshBtn(row);
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

		$orderVisitGrid.datagrid('options').url = 'marketing/clientCheckMgr/getOrderVisitByOrder';
		
		if($designerVisitGrid.length > 0)
		{
			$designerVisitGrid.datagrid
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
			
			$designerVisitGrid.datagrid('options').url = 'marketing/clientCheckMgr/getStylistOrderVisitByOrder';
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
		
		$orderApproveGrid.datagrid('options').url = 'marketing/clientCheckMgr/getOrderApproveByOrderId';
	}

	function initBtn()
	{
		$submitUpdateClientFormBtn.linkbutton({'onClick': submitEditClientForm});
		$refreshUpdateClientFormBtn.linkbutton({'onClick': function()
		{
			var selRows = $orderCheckDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		$reloadInfoCostBtn.linkbutton({'onClick': function()
		{
			var selRows = $orderCheckDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		$queryCheckOrderBtn.linkbutton
		({
			'onClick': function()
			{
				var status =[]; 
				$('#orderCheckDatagridToolbar :input[name="orderStatusInput"]:checked').each(function(){ 
					status.push($(this).val());  
				}); 
				$orderCheckDatagrid.datagrid('load', 
				{
					name: $orderCheckNameTextbox.textbox('getValue'),
					tel: $orderCheckTelTextbox.textbox('getValue'),
					infoerName: $orderCheckInfoerNameTextbox.textbox('getValue'),
					filter: $(':radio[name="clientNegotiation\.orderApproveFilterInput"]:checked').val(),
					status: status
				});
			}
		});
		
		$('#showPermitOrderWindowBtn').linkbutton({onClick: showPermitOrderWindow});
		$('#showRejectOrderWindowBtn').linkbutton({onClick: showRejectOrderWindow});
	}
	
	function initTab()
	{
		$clientMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selRows = $orderCheckDatagrid.datagrid('getSelections');
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
				$editClientForm.form('clear').form('load', 'marketing/clientCheckMgr/getOrderById?orderId=' + row.id);
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
		}
	}

	function submitEditClientForm()
	{
		var selRows = $orderCheckDatagrid.datagrid('getSelections');
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
					$orderCheckDatagrid.datagrid('reload');
				else
					$.messager.show({title: '提示', msg: data.msg});
			}
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
			return '<form id="permitOrderForm" action="marketing/clientCheckMgr/permitOrder" method="post" style="width: 100%;">' + 
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
			'				<a class="easyui-linkbutton" onclick="submitPermitOrderForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$permitOrderWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
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
			return '<form id="rejectOrderForm" action="marketing/clientCheckMgr/rejectOrder" method="post" style="width: 100%;">' + 
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
			'				<a class="easyui-linkbutton" onclick="submitRejectOrderForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$rejectOrderWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
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
});