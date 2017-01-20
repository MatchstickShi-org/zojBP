$(function()
{
	var $orderDatagrid = $('table#orderDatagrid');
	var $orderNameTextbox = $('#clientNegotiation-nameInput');
	var $orderIdTextbox = $('#clientNegotiation-idInput');
	var $telTextbox = $('#clientNegotiation-telInput');
	var $designerCombobox = $('#clientNegotiation-designerCombobox');
	var $orderFilterInput = $(':radio[name="clientNegotiation-orderFilterInput"]');
	var $queryOrderBtn = $('a#queryOrderBtn');
	var $addClientVisitWindow = $('div#addClientVisitWindow');
	var $addVisitCommentWindow = $('div#addVisitCommentWindow');
	var $addClientWindow = $('div#addClientWindow');
	var $dealOrderWindow = $('div#dealOrderWindow');
	var $deadOrderWindow = $('div#deadOrderWindow');
	var $disagreeOrderWindow = $('div#disagreeOrderWindow');
	var $repulseOrderWindow = $('div#repulseOrderWindow');
	var $selectDesignerWindow = $('div#selectDesignerWindow');
	var $businessTransferWindow = $('div#businessTransferWindow');
	var $applyVisitWindow = $('div#applyVisitWindow');
	var $clientMgrTab = $('div#clientMgrTab');
	var $editClientForm = $('form#editOrderForm');
	var $submitUpdateClientFormBtn = $('a#submitUpdateClientFormBtn');
	var $refreshUpdateClientFormBtn = $('a#refreshUpdateClientFormBtn');
	var $applyVisitBtn = $('a#applyVisitBtn');
	var $orderVisitGrid = $('table#orderVisitGrid');
	var $orderStylistVisitGrid = $('table#orderStylistVisitGrid');
	var $orderApproveGrid = $('table#orderApproveGrid');
	
	function init()
	{
		$orderDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderDatagridToolbar',
			columns:
			[[
			  	{field: 'ck', checkbox: true},
			  	{field:'id', title:'单号', width: 3},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话', width: 5},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'面积', width: 2},
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
				{field:'insertTime', title:'录入日期', width: 5},
				{
					field:'notVisitDays', title:'未回访天数', width: 3, sortable: true,
					formatter: function(val, row, index)
					{
						return val == -100 ? "-" : val;
					},
					styler: function (value, row, index)
					{
						if(value > 1)
							return 'background-color: red; color: white;';
					}
				}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'design/clientMgr/getAllClientNegotiation',
			queryParams:
			{
				filter: $orderFilterInput.filter(':checked').val(),
				status: function()
				{
					var status =[]; 
					$('#orderDatagridToolbar :input[name="clientNegotiation-statusInput"]:checked').each(function()
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
					$('#addOrderVisitBtn').linkbutton('disable').linkbutton('hide');
					$applyVisitBtn.linkbutton('disable').linkbutton('hide');
					$submitUpdateClientFormBtn.linkbutton('disable').linkbutton('hide');
				}
				else
				{
					if(_session_loginUserRole == 6 || _session_loginUserRole == -1)		//主案部经理 || superAdmin
					{
						$('#addOrderVisitBtn').linkbutton('enable').linkbutton('show');
						$applyVisitBtn.linkbutton('disable').linkbutton('hide');
					}
					else
					{
						if(row.notVisitDays > 1 && (row.visitApplyStatus == null || row.visitApplyStatus == 0))
						{
							$('#addOrderVisitBtn').linkbutton('disable').linkbutton('hide');
							$applyVisitBtn.linkbutton('enable').linkbutton('show');	
						}
						else
						{
							$('#addOrderVisitBtn').linkbutton('enable').linkbutton('show');
							$applyVisitBtn.linkbutton('disable').linkbutton('hide');
						}
					}
					$submitUpdateClientFormBtn.linkbutton('enable').linkbutton('show');
				}
			}
			else
			{
				$('#addOrderVisitBtn').linkbutton('disable').linkbutton('hide');
				$applyVisitBtn.linkbutton('disable').linkbutton('hide');
				$submitUpdateClientFormBtn.linkbutton('disable').linkbutton('hide');
			}
			$clientMgrTab.tabs('showTool');
			loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
		}
		
		$('#orderDatagridToolbar :checkbox[name="clientNegotiation-statusInput"]').click(function()
		{
			if($(this).attr('value') == '')		//全选
				$('#orderDatagridToolbar :checkbox[name="clientNegotiation-statusInput"][value!=""]').attr("checked", false);
			else
				$('#orderDatagridToolbar :checkbox[name="clientNegotiation-statusInput"][value=""]').attr("checked", false);
		});
		
		$queryOrderBtn.linkbutton
		({
			'onClick': function()
			{
				var status =[]; 
				$('#orderDatagridToolbar :input[name="clientNegotiation-statusInput"]:checked').each(function()
				{ 
					status.push($(this).val());   
				});
				$orderDatagrid.datagrid('load', 
				{
					clientName: $orderNameTextbox.textbox('getValue'), 
					orderId: $orderIdTextbox.textbox('getValue'), 
					tel: $telTextbox.textbox('getValue'),
					designerId: $designerCombobox.length == 0 ? null : $designerCombobox.combobox('getValue'),
					filter: $orderFilterInput.filter(':checked').val(),
					status: status
				});
			}
		});
		
		$submitUpdateClientFormBtn.linkbutton({'onClick': submitEditClientForm});
		$refreshUpdateClientFormBtn.linkbutton({'onClick': function()
		{
			var selRows = $orderDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		if($orderVisitGrid.length > 0)
		{
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
				{field:'date', title:'回访日期', width: 5},
				{field:'comment', title:'批示', width: 5}
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
			  {field:'operateTime', title:'操作日期', width: 5}
			  ]],
			  pagination: true
		});
		
		$orderApproveGrid.datagrid('options').url = 'design/clientMgr/getOrderApproveByOrderId';
		
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
		
		function loadTabData(title, row)
		{
			switch (title)
			{
				case '详情':
					$editClientForm.form('clear').form('load', 'design/clientMgr/getOrderById?orderId=' + row.id);
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
		
		$('#removeOrderBtn').linkbutton({onClick: removeOrder});
		$('#addVisitCommentBtn').linkbutton({onClick: showAddVisitCommentWindow});
		$('#addOrderVisitBtn').linkbutton({onClick: showAddClientVisitWindow});
		$('#dealOrderWindowBtn').linkbutton({onClick: showDealOrderWindow});
		$('#deadOrderWindowBtn').linkbutton({onClick: showDeadOrderWindow});
		$('#disagreeOrderWindowBtn').linkbutton({onClick: showDisagreeOrderWindow});
		$('#repulseOrderWindowBtn').linkbutton({onClick: showRepulseOrderWindow});
		$('#transferOrderWindowBtn').linkbutton({onClick: showBusinessTransferWindow});
		$applyVisitBtn.linkbutton({onClick: showApplyVisitWindow});
		
		function showBusinessTransferWindow()
		{
			var selIds = $orderDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>需要业务转移的客户。');
				return;
			}
			var selRows = $orderDatagrid.datagrid('getChecked');
			if(selRows.length > 0){
				var flag = false;
				$.each(selRows,function(index,obj){
					if(obj.designerStatus ==1){
						if(obj.status ==90 || obj.status ==0 || obj.status ==64){
							flag = true;
							return;
						}
					}
				});
				if(flag){
					$.messager.alert('提示', '在职设计师的客户状态为<span style="color: red;">已签单、死单、不准单</span>的不能转移。');
					return;
				}
			}
			$businessTransferWindow.window('clear');
			$businessTransferWindow.window('open').window
			({
				title: '请选择设计师',
			}).window('open').window('refresh', 'design/clientMgr/showDesignerForTransfer');
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
					'design/clientMgr/deleteOrderByIds',
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
		
		$addClientVisitWindow.window({width: 350});
		$addVisitCommentWindow.window({width: 350});
		$addClientWindow.window({width: 450});
		$dealOrderWindow.window({width: 340});
		$deadOrderWindow.window({width: 340});
		$disagreeOrderWindow.window({width: 340});
		$repulseOrderWindow.window({width: 340});
		$businessTransferWindow.window({width: 500});
		$applyVisitWindow.window({width: 340});
		
		function showApplyVisitWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要申请回访的客户。');
				return;
			}
			if(selIds[0].notVisitDays < 1)
			{
				$.messager.alert('提示', '只能申请未回访天数大于<span style="color: red;">1</span>天的客户。');
				return;
			}
			$applyVisitWindow.window('clear');
			$applyVisitWindow.window('open').window
			({
				title: '申请回访',
				content: applyVisitWindowHtml
			}).window('open').window('center');
		}
		
		var applyVisitWindowHtml = 
			'<form id="applyVisitForm" action="design/clientMgr/visitApply" method="post" style="width: 100%;">' + 
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
			'			<td><input id="designerName" name="designerName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitApplyVisitForm();" href="javascript:void(0)">提交</a>' + 
			'				<a class="easyui-linkbutton" onclick="$applyVisitWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $applyVisitWindow = $(\'div#applyVisitWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$applyVisitWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$applyVisitWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$applyVisitWindow.find(\'#designerName\').val(selRows[0].designerName);' +
			'$applyVisitWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitApplyVisitForm()' + 
			'{' + 
			'	$applyVisitWindow.find(\'form#applyVisitForm\').form(\'submit\',' + 
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
			'				$orderDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$applyVisitWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showDealOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要签单的客户。');
				return;
			}
			if(selIds[0].status != 34)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">在谈单</span>的客户。');
				return;
			}
			$dealOrderWindow.window('clear');
			$dealOrderWindow.window('open').window
			({
				title: '签单',
				content: dealOrderWindowHtml
			}).window('open').window('center');
		}
		
		var dealOrderWindowHtml = 
			'<form id="dealOrderForm" action="design/clientMgr/dealOrder" method="post" style="width: 100%;">' + 
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
			'			<td align="right"><label>签单金额：</label></td>' + 
			'			<td><input id="dealAmount" name="dealAmount" required="required" class="easyui-textbox" /> ￥</td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitDealOrderForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$dealOrderWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $dealOrderWindow = $(\'div#dealOrderWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderApproveGrid = $(\'table#orderApproveGrid\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$dealOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$dealOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$dealOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$dealOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$dealOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitDealOrderForm()' + 
			'{' + 
			'	$dealOrderWindow.find(\'form#dealOrderForm\').form(\'submit\',' + 
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
			'				$orderDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				if($clientMgrTab.tabs("getSelected").panel("options").title == "审核流程")' + 
			'					$orderApproveGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$dealOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showDeadOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要申请死单的客户。');
				return;
			}
			if(selIds[0].status != 34)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">在谈单</span>的客户。');
				return;
			}
			$deadOrderWindow.window('clear');
			$deadOrderWindow.window('open').window
			({
				title: '申请死单',
				content: deadOrderWindowHtml
			}).window('open').window('center');
		}
		
		var deadOrderWindowHtml = 
			'<form id="deadOrderForm" action="design/clientMgr/deadOrder" method="post" style="width: 100%;">' + 
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
			'				<a class="easyui-linkbutton" onclick="submitDeadOrderForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$deadOrderWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $deadOrderWindow = $(\'div#deadOrderWindow\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderApproveGrid = $(\'table#orderApproveGrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$deadOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$deadOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$deadOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$deadOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$deadOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitDeadOrderForm()' + 
			'{' + 
			'	$deadOrderWindow.find(\'form#deadOrderForm\').form(\'submit\',' + 
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
			'				$orderDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				if($clientMgrTab.tabs("getSelected").panel("options").title == "审核流程")' + 
			'					$orderApproveGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$deadOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		
		function showDisagreeOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要申请不准单的客户。');
				return;
			}
			if(selIds[0].status != 34)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">在谈单</span>的客户。');
				return;
			}
			$disagreeOrderWindow.window('clear');
			$disagreeOrderWindow.window('open').window
			({
				title: '申请不准单',
				content: disagreeOrderWindowHtml
			}).window('open').window('center');
		}
		
		var disagreeOrderWindowHtml = 
			'<form id="disagreeOrderForm" action="design/clientMgr/disagreeOrder" method="post" style="width: 100%;">' + 
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
			'				<a class="easyui-linkbutton" onclick="submitDisagreeOrderForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$disagreeOrderWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $disagreeOrderWindow = $(\'div#disagreeOrderWindow\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderApproveGrid = $(\'table#orderApproveGrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$disagreeOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$disagreeOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$disagreeOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$disagreeOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$disagreeOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitDisagreeOrderForm()' + 
			'{' + 
			'	$disagreeOrderWindow.find(\'form#disagreeOrderForm\').form(\'submit\',' + 
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
			'				$orderDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				if($clientMgrTab.tabs("getSelected").panel("options").title == "审核流程")' + 
			'					$orderApproveGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$disagreeOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		function showRepulseOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要打回正跟踪的客户。');
				return;
			}
			if(selIds[0].status != 34)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">在谈单</span>的客户。');
				return;
			}
			$repulseOrderWindow.window('clear');
			$repulseOrderWindow.window('open').window
			({
				title: '打回正跟踪',
				content: repulseOrderWindowHtml
			}).window('open').window('center');
		}
		
		var repulseOrderWindowHtml = 
			'<form id="repulseOrderForm" action="design/clientMgr/repulseOrder" method="post" style="width: 100%;">' + 
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
			'				<a class="easyui-linkbutton" onclick="submitRepulseOrderForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$repulseOrderWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $repulseOrderWindow = $(\'div#repulseOrderWindow\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderApproveGrid = $(\'table#orderApproveGrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$repulseOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$repulseOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$repulseOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$repulseOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$repulseOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitRepulseOrderForm()' + 
			'{' + 
			'	$repulseOrderWindow.find(\'form#repulseOrderForm\').form(\'submit\',' + 
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
			'				$orderDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				if($clientMgrTab.tabs("getSelected").panel("options").title == "审核流程")' + 
			'					$orderApproveGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$repulseOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddClientVisitWindow()
		{
			var selRows = $orderDatagrid.datagrid('getSelections');
			if(selRows.length == 0)
			{
				$.messager.alert('提示', '请选中一个客户。');
				return;
			}
			if(selRows[0].designerName == null || selRows[0].designerName == '')
			{
				$.messager.alert('提示', '请先分配设计师。');
				return;
			}
			$addClientVisitWindow.window('clear');
			$addClientVisitWindow.window('open').window
			({
				title: '新增回访记录',
				content: addClientVisitWindowHtml
			}).window('open').window('center');
		}
		
		var addClientVisitWindowHtml = 
			'<form id="addClientVisitForm" action="design/clientMgr/addOrderVisit" method="post" style="width: 100%;">' + 
			'	<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'	<input id="visitorId"  name="visitorId" type="hidden" value="" />' + 
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
			'var $orderStylistVisitGrid = $(\'table#orderStylistVisitGrid\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
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
			'				if($clientMgrTab.tabs(\'getSelected\').panel(\'options\').title == "设计师回访记录")' + 
			'					$orderStylistVisitGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$addClientVisitWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddVisitCommentWindow()
		{
			var selRows = $orderStylistVisitGrid.datagrid('getSelections');
			if(selRows.length == 0)
			{
				$.messager.alert('提示', '请选中一条回访记录。');
				return;
			}
			$addVisitCommentWindow.window('clear');
			$addVisitCommentWindow.window('open').window
			({
				title: '批示',
				content: addVisitCommentWindowHtml
			}).window('open').window('center');
		}
		
		var addVisitCommentWindowHtml = 
			'<form id="addVisitCommentForm" action="design/clientMgr/addVisitComment" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>批示内容：</label></td>' + 
			'			<td><input id="comment" name="comment" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="id"  name="id" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddVisitCommentForm();" href="javascript:void(0)" iconCls="icon-ok">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientVisitWindow.window(\'close\');" href="javascript:void(0)" iconCls="icon-cancel">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addVisitCommentWindow = $(\'div#addVisitCommentWindow\');' +
			'var $orderStylistVisitGrid = $(\'table#orderStylistVisitGrid\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var selRows = $orderStylistVisitGrid.datagrid("getSelections");' +
			'$addVisitCommentWindow.find(\'#id\').val(selRows[0].id);' +
			'$addVisitCommentWindow.find(\'#comment\').val(selRows[0].comment);' +
			'function submitAddVisitCommentForm()' + 
			'{' + 
			'	$addVisitCommentWindow.find(\'form#addVisitCommentForm\').form(\'submit\',' + 
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
			'				if($clientMgrTab.tabs(\'getSelected\').panel(\'options\').title == "设计师回访记录")' + 
			'					$orderStylistVisitGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$addVisitCommentWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
	}

	init();
});