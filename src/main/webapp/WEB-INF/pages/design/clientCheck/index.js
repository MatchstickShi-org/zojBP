$(function()
{
	var $orderCheckDatagrid = $('table#orderCheckDatagrid');
	var $orderCheckNameTextbox = $('#orderApprove-nameInput');
	var $orderCheckIdTextbox = $('#orderApprove-idInput');
	var $orderCheckTelTextbox = $('#orderApprove-telInput');
	var $designerCombobox = $('#orderApprove-designerCombobox');
	var $orderFilterInput = $(':radio[name="orderApprove-filterInput"]');
	var $queryCheckOrderBtn = $('a#queryCheckOrderBtn');
	var $permitOrderWindow = $('div#permitOrderWindow');
	var $rejectOrderWindow = $('div#rejectOrderWindow');
	var $checkDeadOrderWindow = $('div#checkDeadOrderWindow');
	var $selectDesignerWindow = $('div#selectDesignerWindow');
	var $clientMgrTab = $('div#clientMgrTab');
	var $editClientForm = $('form#editOrderForm');
	var $submitUpdateClientFormBtn = $('a#submitUpdateClientFormBtn');
	var $refreshUpdateClientFormBtn = $('a#refreshUpdateClientFormBtn');
	var $orderVisitGrid = $('table#orderVisitGrid');
	var $orderStylistVisitGrid = $('table#orderStylistVisitGrid');
	var $orderApproveGrid = $('table#orderApproveGrid');
	
	showSelectDesignerWindow = function()
	{
		$selectDesignerWindow.window('clear');
		$selectDesignerWindow.window('open').window
		({
			title: '请选择设计师',
		}).window('open').window('refresh', 'design/clientCheckMgr/showDesignerForPermit');
	}
	
	function init()
	{
		$orderCheckDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderCheckDatagridToolbar',
			columns:
			[[
			  	{field: 'ck', checkbox: true},
			  	{field:'id', title:'单号', width: 3},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话'},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'面积'},
				{field:'infoerName', title:'信息员'},
				{field:'salesmanName', title:'业务员'},
				{field:'designerId', hidden: true},
				{field:'designerName', title:'设计师'},
				{field:'salesmanStatus', hidden: true},
				{
					field:'status', title:'状态', width: 4, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 20:
								return '打回审核中';
								break;
							case 32:
								return '在谈单审核中';
								break;
							case 60:
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
			url: 'design/clientCheckMgr/getAllClientCheck',
			queryParams:
			{
				filter: $orderFilterInput.filter(':checked').val(),
				status: function()
				{
					var status =[];
					$('#orderCheckDatagridToolbar :input[name="orderStatusInput"]:checked').each(function()
					{ 
						status.push($(this).val());   
					});
					return status;
				}()
			},
			onSelect: function(idx, row)
			{
				refreshBtn(row);
			},
		});
		
		function refreshBtn(row)
		{
			if(row.designerId == _session_loginUserId || _session_loginUserRole == -1)
			{
				if(row.status == 90 || row.status == 0 || row.status == 64)		//已签单、死单、不准单禁止新增回访记录
				{
					$submitUpdateClientFormBtn.linkbutton('disable').linkbutton('hide');
				}
				else
				{
					$submitUpdateClientFormBtn.linkbutton('enable').linkbutton('show');
				}
			}
			else
			{
				$submitUpdateClientFormBtn.linkbutton('disable').linkbutton('hide');
			}
			$clientMgrTab.tabs('showTool');
			loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
		}
		
		$submitUpdateClientFormBtn.linkbutton({'onClick': submitEditClientForm});
		$refreshUpdateClientFormBtn.linkbutton({'onClick': function()
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
				$('#orderCheckDatagridToolbar :input[name="orderStatusInput"]:checked').each(function()
				{ 
					status.push($(this).val());   
				});
				$orderCheckDatagrid.datagrid('load', 
				{
					clientName: $orderCheckNameTextbox.textbox('getValue'),
					orderId: $orderCheckIdTextbox.textbox('getValue'),
					tel: $orderCheckTelTextbox.textbox('getValue'),
					designerId: $designerCombobox.combobox('getValue'),
					filter: $orderFilterInput.filter(':checked').val(),
					status: status
				});
			}
		});
		
		if($orderVisitGrid.length > 0)
		{
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
					  singleSelect: true,
					  selectOnCheck: false,
					  checkOnSelect: false,
					  onDblClickRow: function(index, row){
						  $.messager.alert('回访内容', row.content);
					  }
			});
			
			$orderVisitGrid.datagrid('options').url = 'design/clientMgr/getOrderVisitByOrder';
		}
		
		$orderStylistVisitGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'回访内容', width: 5},
				{field:'date', title:'回访日期'},
				{field:'comment', title:'批示'}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			onDblClickRow: function(index, row){
				$.messager.alert('回访内容', row.content);
			}
		});
		
		$orderStylistVisitGrid.datagrid('options').url = 'design/clientMgr/getStylistOrderVisitByOrder';
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
			  {field:'operateTime', title:'操作日期'}
			  ]],
			  pagination: true
		});
		
		$orderApproveGrid.datagrid('options').url = 'design/clientCheckMgr/getOrderApproveByOrderId';
		
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
		
		function loadTabData(title, row)
		{
			switch (title)
			{
				case '详情':
					$editClientForm.form('clear').form('load', 'design/clientCheckMgr/getOrderById?orderId=' + row.id);
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
				case '设计师回访记录':
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
		
		$('#showPermitOrderWindowBtn').linkbutton({onClick: showPermitOrderWindow});
		$('#showRejectOrderWindowBtn').linkbutton({onClick: showRejectOrderWindow});
		$('#showCheckDeadOrderWindowBtn').linkbutton({onClick: showCheckDeadOrderWindow});
		
		$permitOrderWindow.window({width: 340});
		$rejectOrderWindow.window({width: 340});
		$checkDeadOrderWindow.window({width: 340});
		$selectDesignerWindow.window({width: 500});
		
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
				content: selIds[0].status ==32 ? permitOrderWindowHtml:permitDisagreeOrderWindowHtml
			}).window('open').window('center');
		}
		
		var permitOrderWindowHtml = 
			'<form id="permitOrderForm" action="design/clientCheckMgr/permitOrder" method="post" style="width: 100%;">' + 
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
			'		<input type="hidden" name="designerId" id="permitOrderForm_designerIdInput" type="hidden" value="1" />' +
			'		<tr>' + 
			'			<td align="right"><label>分配设计师：</label></td>' + 
			'			<td><input id="permitOrderForm_designerNameSearchbox"  required="required" name="desingerName" prompt="请选择设计师" editable="false" class="easyui-searchbox" style="width: 160px;"/></td>' + 
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
			'$permitOrderWindow.find(\'#permitOrderForm_designerNameSearchbox\').val(selRows[0].designerName);' +
			'$permitOrderWindow.find(\'#permitOrderForm_designerIdInput\').val(selRows[0].designerId);' +
			'$permitOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$permitOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'var $designerNameSearchbox = $permitOrderWindow.find("#permitOrderForm_designerNameSearchbox");' +
			'$designerNameSearchbox.searchbox({searcher: function(){showSelectDesignerWindow();}});' + 
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
		var permitDisagreeOrderWindowHtml = 
			'<form id="permitOrderForm" action="design/clientCheckMgr/permitOrder" method="post" style="width: 100%;">' + 
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
				content: rejectOrderWindowHtml
			}).window('open').window('center');
		}
		
		var rejectOrderWindowHtml = 
			'<form id="rejectOrderForm" action="design/clientCheckMgr/rejectOrder" method="post" style="width: 100%;">' + 
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
			'					$orderApproveGrid.datagrid("unselectAll").datagrid(\'loadData\', []);' + 
			'				$rejectOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showCheckDeadOrderWindow()
		{
			var selIds = $orderCheckDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中一个客户。');
				return;
			}
			if(selIds[0].status != 60)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">不准单审核中</span>的客户。');
				return;
			}
			$checkDeadOrderWindow.window('clear');
			$checkDeadOrderWindow.window('open').window
			({
				title: '申请死单',
				content: checkDeadOrderWindowHtml
			}).window('open').window('center');
		}
		
		var checkDeadOrderWindowHtml = 
			'<form id="checkDeadOrderForm" action="design/clientCheckMgr/checkDeadOrder" method="post" style="width: 100%;">' + 
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
			'				<a class="easyui-linkbutton" onclick="submitCheckDeadOrderForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$checkDeadOrderWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $checkDeadOrderWindow = $(\'div#checkDeadOrderWindow\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var $orderCheckDatagrid = $(\'table#orderCheckDatagrid\');' +
			'var $orderApproveGrid = $(\'table#orderApproveGrid\');' +
			'var selRows = $orderCheckDatagrid.datagrid("getSelections");' +
			'$checkDeadOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$checkDeadOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$checkDeadOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$checkDeadOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$checkDeadOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitCheckDeadOrderForm()' + 
			'{' + 
			'	$checkDeadOrderWindow.find(\'form#checkDeadOrderForm\').form(\'submit\',' + 
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
			'				$checkDeadOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
	}

	init();
});